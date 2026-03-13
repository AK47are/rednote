package com.example.rednote.service.impl;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.rednote.common.utils.JwtUtils;
import com.example.rednote.mapper.UserMapper;
import com.example.rednote.model.dto.LoginDTO;
import com.example.rednote.model.dto.UserDTO;
import com.example.rednote.model.exception.LoginFailedException;
import com.example.rednote.model.po.UserPO;
import com.example.rednote.model.vo.UserVO;
import com.example.rednote.service.UserService;

import cn.hutool.crypto.digest.BCrypt;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;
    private final JwtUtils jwtUtils;

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

    @Override
    public String login(LoginDTO loginDTO) {
        QueryWrapper<UserPO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", loginDTO.getUsername());
        UserPO userPO = userMapper.selectOne(queryWrapper);
        if (BCrypt.checkpw(loginDTO.getPassward(), userPO.getPassword())) {
            return jwtUtils.generateToken(Integer.toString(userPO.getUserId()), userPO.getUsername());
        } else {
            throw new LoginFailedException("用户名或密码错误");
        }
    }
}
