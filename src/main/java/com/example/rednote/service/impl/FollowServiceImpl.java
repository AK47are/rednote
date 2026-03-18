package com.example.rednote.service.impl;

import java.util.Objects;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.example.rednote.common.utils.ThreadLocalUtils;
import com.example.rednote.mapper.FollowMapper;
import com.example.rednote.model.po.FollowPO;
import com.example.rednote.service.FollowService;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class FollowServiceImpl implements FollowService {

    private final FollowMapper followMapper;


    @Override
    public Boolean isFollow(Integer userId) {
        Integer currId = Integer.parseInt(ThreadLocalUtils.get("userId"));
        LambdaQueryWrapper<FollowPO> wrapper = Wrappers.lambdaQuery(FollowPO.class)
                .eq(FollowPO::getFollowerId, currId)
                .eq(FollowPO::getFollowingId, userId);
        return Objects.nonNull(followMapper.selectOne(wrapper));
    }

}
