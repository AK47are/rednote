package com.example.rednote.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.example.rednote.common.utils.MinioUtils;
import com.example.rednote.common.utils.ThreadLocalUtils;
import com.example.rednote.mapper.CommentMapper;
import com.example.rednote.mapper.PostMapper;
import com.example.rednote.mapper.UserMapper;
import com.example.rednote.model.dto.CommentDTO;
import com.example.rednote.model.exception.PostQueryFailedExceptioin;
import com.example.rednote.model.po.CommentPO;
import com.example.rednote.model.po.UserPO;
import com.example.rednote.model.vo.CommentWithUserVO;
import com.example.rednote.service.CommentService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentMapper commentMapper;
    private final PostMapper postMapper;
    private final UserMapper userMapper;
    private final MinioUtils minioUtils;

    @Override
    public List<CommentWithUserVO> listByPostId(Integer postId) {
        List<CommentPO> commentPOS = commentMapper.selectList(
                Wrappers.<CommentPO>lambdaQuery().eq(CommentPO::getPostId, postId));
        if (commentPOS.isEmpty()) {
            return List.of();
        }
        List<CommentWithUserVO> commentResultVOS = BeanUtil.copyToList(commentPOS, CommentWithUserVO.class);
        // @TODO 优化查询，N+1问题
        commentResultVOS.forEach(commentResultVO -> {
            UserPO userPO = userMapper.selectById(commentResultVO.getUserId());
            commentResultVO.setUserName(userPO.getUsername());
            commentResultVO.setUserAvatar(minioUtils.getPublicUrl(userPO.getAvatar()));
        });
        return commentResultVOS;
    }

    @Override
    public void insert(CommentDTO commentDTO) {
        Integer userId = Integer.parseInt(ThreadLocalUtils.get("userId"));
        if (postMapper.selectById(commentDTO.getPostId()) == null) {
            throw new PostQueryFailedExceptioin("帖子不存在");
        }
        CommentPO commentPO = BeanUtil.copyProperties(commentDTO, CommentPO.class);
        commentPO.setUserId(userId);
        commentMapper.insert(commentPO);
    }

    @Override
    public void removeById(Integer id) {
        commentMapper.deleteById(id);
    }
}
