package com.example.rednote.service.impl;

import java.util.List;
import java.util.Objects;
import com.example.rednote.common.utils.MinioUtils;
import com.example.rednote.model.vo.UserVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.example.rednote.common.utils.ThreadLocalUtils;
import com.example.rednote.mapper.FollowMapper;
import com.example.rednote.mapper.UserDetailsMapper;
import com.example.rednote.model.exception.FollowFailedException;
import com.example.rednote.model.po.FollowPO;
import com.example.rednote.service.FollowService;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class FollowServiceImpl implements FollowService {

    private final FollowMapper followMapper;
    private final UserDetailsMapper userDetailsMapper;
    private final MinioUtils minioUtils;

    @Override
    @Transactional
    public void toogleFollow(Integer userId) {
        Integer currId = Integer.parseInt(ThreadLocalUtils.get("userId"));

        if (Objects.equals(currId, userId)) {
            throw new FollowFailedException("你不能关注自己!");
        }

        if (isFollow(userId)) {
            LambdaQueryWrapper<FollowPO> wrapper = Wrappers.lambdaQuery(FollowPO.class)
                    .eq(FollowPO::getFollowerId, currId)
                    .eq(FollowPO::getFollowingId, userId);
            userDetailsMapper.updateFansCount(userId, -1);
            userDetailsMapper.updateFollowCount(currId, -1);
            followMapper.delete(wrapper);
        } else {
            userDetailsMapper.updateFansCount(userId, 1);
            userDetailsMapper.updateFollowCount(currId, 1);
            FollowPO follow = new FollowPO();
            follow.setFollowerId(currId);
            follow.setFollowingId(userId);
            followMapper.insert(follow);
        }
    }

    @Override
    public Boolean isFollow(Integer userId) {
        Integer currId = Integer.parseInt(ThreadLocalUtils.get("userId"));
        LambdaQueryWrapper<FollowPO> wrapper = Wrappers.lambdaQuery(FollowPO.class)
                .eq(FollowPO::getFollowerId, currId)
                .eq(FollowPO::getFollowingId, userId);
        return Objects.nonNull(followMapper.selectOne(wrapper));
    }

    @Override
    public List<UserVO> listFollowing() {
        Integer currId = Integer.parseInt(ThreadLocalUtils.get("userId"));
        List<UserVO> userVOS = followMapper.selectByUserId(currId);
        userVOS.forEach(userVO -> {
            userVO.setAvatar(minioUtils.getPublicUrl(userVO.getAvatar()));
        });
        return userVOS;
    }
}
