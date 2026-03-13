package com.example.rednote.service.impl;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.example.rednote.mapper.UserMapper;
import com.example.rednote.model.dto.UserDTO;
import com.example.rednote.model.po.UserPO;
import com.example.rednote.model.vo.UserVO;
import com.example.rednote.service.UserService;

import cn.hutool.crypto.digest.BCrypt;
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

    @Override
    public Integer register(UserDTO userDTO) {
        userDTO.setUserId(null);
        userDTO.setPassword(BCrypt.hashpw(userDTO.getPassword()));
        UserPO userPO = new UserPO();
        BeanUtils.copyProperties(userDTO, userPO);
        return userMapper.insert(userPO);
    }
}
