package com.example.rednote.service.impl;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.example.rednote.mapper.UserMapper;
import com.example.rednote.model.vo.UserVO;
import com.example.rednote.service.UserService;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;

    @Override
    public UserVO getById(Integer userId) {
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(userMapper.selectById(userId), userVO);
        return userVO;
    }

}
