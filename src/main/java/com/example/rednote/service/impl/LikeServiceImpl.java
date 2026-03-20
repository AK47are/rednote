package com.example.rednote.service.impl;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.rednote.common.utils.ThreadLocalUtils;
import com.example.rednote.mapper.LikeMapper;
import com.example.rednote.mapper.PostDetailsMapper;
import com.example.rednote.mapper.UserDetailsMapper;
import com.example.rednote.model.po.LikePO;
import com.example.rednote.service.LikeService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@Transactional
@AllArgsConstructor
@Slf4j
public class LikeServiceImpl implements LikeService {
    private final LikeMapper likeMapper;
    private final UserDetailsMapper userDetailsMapper;
    private final PostDetailsMapper postDetailsMapper;

    @Override
    public Boolean isLike(Integer postId) {
        Integer userId = Integer.parseInt(ThreadLocalUtils.get("userId"));

        LambdaQueryWrapper<LikePO> wrapper = Wrappers.lambdaQuery(LikePO.class)
                .eq(LikePO::getPostId,postId)
                .eq(LikePO::getUserId,userId);
        return Objects.nonNull(likeMapper.selectOne(wrapper));
    }

    @Override
    @Transactional
    public Boolean toogleLike(Integer postId) {
        Integer userId = Integer.parseInt(ThreadLocalUtils.get("userId"));

        if (isLike(postId)){
            LambdaQueryWrapper<LikePO> wrapper = Wrappers.lambdaQuery(LikePO.class)
                    .eq(LikePO::getPostId, postId)
                    .eq(LikePO::getUserId, userId);
            userDetailsMapper.updateLikeReceiveCount(userId, -1);
            postDetailsMapper.updateLikeCount(postId, -1);
            likeMapper.delete(wrapper);
            return false;
        }else{
            userDetailsMapper.updateLikeReceiveCount(userId, 1);
            postDetailsMapper.updateLikeCount(postId, 1);
            LikePO likePO = new LikePO();
            likePO.setPostId(postId);
            likePO.setUserId(userId);
            likeMapper.insert(likePO);
            return true;
        }
    }


}
