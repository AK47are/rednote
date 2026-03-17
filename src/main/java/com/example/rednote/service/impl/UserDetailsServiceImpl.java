package com.example.rednote.service.impl;

import org.springframework.stereotype.Service;

import com.example.rednote.mapper.UserDetailsMapper;
import com.example.rednote.model.po.UserDetailsPO;
import com.example.rednote.service.UserDetailsService;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserDetailsMapper userDetailsMapper;

    @Override
    public UserDetailsPO getById(Integer userId) {
        return userDetailsMapper.selectById(userId);
    }
}
