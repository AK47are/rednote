package com.example.rednote.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.rednote.common.utils.MinioUtils;
import com.example.rednote.mapper.PostTopicMapper;
import com.example.rednote.mapper.UserMapper;
import com.example.rednote.model.po.PostTopicPO;
import com.example.rednote.model.po.UserPO;
import com.example.rednote.model.vo.PostWithUserVO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.rednote.mapper.PostMapper;
import com.example.rednote.model.po.PostPO;
import com.example.rednote.service.PostService;
import lombok.AllArgsConstructor;
import java.util.List;

@Service
@AllArgsConstructor
public class PostServiceImpl extends ServiceImpl<PostMapper, PostPO> implements PostService {

    private final PostTopicMapper postTopicMapper;
    private final UserMapper userMapper;
    private final PostMapper postMapper;
    private final MinioUtils minioUtils;

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
}
