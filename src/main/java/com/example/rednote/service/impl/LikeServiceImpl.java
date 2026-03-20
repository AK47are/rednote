package com.example.rednote.service.impl;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.rednote.common.utils.ThreadLocalUtils;
import com.example.rednote.mapper.LikeMapper;
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

    @Override
    public Boolean isLike(Integer postId) {
        Integer userId = Integer.parseInt(ThreadLocalUtils.get("userId"));

        LambdaQueryWrapper<LikePO> wrapper = Wrappers.lambdaQuery(LikePO.class)
                .eq(LikePO::getPostId,postId)
                .eq(LikePO::getUserId,userId);
        return Objects.nonNull(likeMapper.selectOne(wrapper));
    }

}
