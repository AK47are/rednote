package com.example.rednote.service.impl;

import cn.hutool.core.io.FileUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.rednote.common.utils.MinioUtils;
import com.example.rednote.common.utils.ThreadLocalUtils;
import com.example.rednote.mapper.*;
import com.example.rednote.model.dto.PostDTO;
import com.example.rednote.model.po.*;
import com.example.rednote.model.vo.PostWithUserVO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.rednote.service.PostService;
import lombok.AllArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
public class PostServiceImpl extends ServiceImpl<PostMapper, PostPO> implements PostService {

    private final PostTopicMapper postTopicMapper;
    private final UserMapper userMapper;
    private final PostMapper postMapper;
    private final MinioUtils minioUtils;
    private final PostDetailsMapper postDetailsMapper;
    private final PostImageMapper postImageMapper;

    @Override
    public List<PostWithUserVO> listWithUserInfo(Integer topicId) {
        LambdaQueryWrapper<PostPO> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByDesc(PostPO::getCreateTime);
        if(topicId != 0){
            List<PostTopicPO> postTopicPOS = postTopicMapper
                    .selectList(new LambdaQueryWrapper<PostTopicPO>().eq(PostTopicPO::getTopicId, topicId));
            List<Integer> postIds = postTopicPOS.stream().map(PostTopicPO::getPostId).toList();
            wrapper.in(PostPO::getPostId, postIds);
        }
        List<PostPO> postPOS = postMapper.selectList(wrapper);
        // TODO: 优化 N+1 查询问题，数据量一大必崩
        List<PostWithUserVO> postWithUserVOs = postPOS.stream().map(postPO -> {
            UserPO userPO = userMapper.selectById(postPO.getUserId());
            PostWithUserVO postWithUserVO = new PostWithUserVO();
            postPO.setCoverImage(minioUtils.getPublicUrl(postPO.getCoverImage()));
            BeanUtils.copyProperties(postPO, postWithUserVO);
            postWithUserVO.setUserName(userPO.getUsername());
            postWithUserVO.setUserAvatar(minioUtils.getPublicUrl(userPO.getAvatar()));
            return postWithUserVO;
        }).toList();
        return postWithUserVOs;
    }

    @Override
    public List<PostWithUserVO> listWithUserInfoByUserId(Integer userId) {
        List<PostWithUserVO> postWithUserVOS = postMapper.selectPostWithUser(userId);
        postWithUserVOS.forEach(postWithUserVO -> {
            postWithUserVO.setUserAvatar(minioUtils.getPublicUrl(postWithUserVO.getUserAvatar()));
            postWithUserVO.setCoverImage(minioUtils.getPublicUrl(postWithUserVO.getCoverImage()));
        });
        return postWithUserVOS;
    }

    @Transactional
    @Override
    public void addPost(PostDTO postDTO){
        PostPO postPO = new PostPO();
        postDTO.setUserId(ThreadLocalUtils.get("userId"));
        BeanUtils.copyProperties(postDTO, postPO);
        postPO.setCoverImage(minioUtils.getObjectName(postPO.getCoverImage()));
        postMapper.insert(postPO);
        Integer postId = postPO.getPostId();
        PostDetailsPO postDetailsPO = new PostDetailsPO();
        postDetailsPO.setPostId(postId);
        String videoRegex = "^(?:mp4|avi|mov|wmv)$";
        if(postDTO.getFileUrls() == null){
            postDetailsPO.setType(3);
        }else if(postDTO.getFileUrls().length == 1 && videoRegex.matches(postDTO.getFileUrls()[0])){
            postDetailsPO.setType(2);
        }else{
            postDetailsPO.setType(1);
        }
        postDetailsMapper.insert(postDetailsPO);
        // NOTE: N+1 查询问题，数据量一大必崩
        for(String fileUrl : postDTO.getFileUrls()){
            PostImagePO postImagePO = new PostImagePO();
            postImagePO.setPostId(postId);
            postImagePO.setUrl(minioUtils.getObjectName(fileUrl));
            postImageMapper.insert(postImagePO);
        }
        // NOTE: N+1 查询问题，数据量一大必崩
        for (String topicId : postDTO.getTopicIds()){
            PostTopicPO postTopicPO = new PostTopicPO();
            postTopicPO.setPostId(postId);
            postTopicPO.setTopicId(Integer.parseInt(topicId));
            postTopicMapper.insert(postTopicPO);
        }
    }
}
