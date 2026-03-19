package com.example.rednote.service.impl;

import java.util.Objects;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.rednote.common.utils.JwtUtils;
import com.example.rednote.common.utils.MinioUtils;
import com.example.rednote.common.utils.ThreadLocalUtils;
import com.example.rednote.mapper.UserMapper;
import com.example.rednote.model.dto.LoginDTO;
import com.example.rednote.model.dto.UserDTO;
import com.example.rednote.model.exception.LoginFailedException;
import com.example.rednote.model.exception.RegisterFailedException;
import com.example.rednote.model.po.UserPO;
import com.example.rednote.model.vo.UserVO;
import com.example.rednote.service.UserService;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.crypto.digest.BCrypt;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;
    private final JwtUtils jwtUtils;
    private final MinioUtils minioUtils;

    @Override
    public UserVO getById(Integer userId) {
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(userMapper.selectById(userId), userVO);
        userVO.setAvatar(minioUtils.getPublicUrl(userVO.getAvatar()));
        return userVO;
    }

    @Override
    public void register(UserDTO userDTO) {
        // 检查 username 是否已经存在
        QueryWrapper<UserPO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", userDTO.getUsername());

        if (Objects.nonNull(userMapper.selectOne(queryWrapper))) {
            throw new RegisterFailedException("用户名已经存在");
        }

        // 插入到 user 表
        UserPO userPO = new UserPO();
        userPO.setUsername(userDTO.getUsername());
        userPO.setPassword(BCrypt.hashpw(userDTO.getPassword()));
        userMapper.insert(userPO);
    }

    @Override
    public String login(LoginDTO loginDTO) {
        QueryWrapper<UserPO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", loginDTO.getUsername());
        UserPO userPO = userMapper.selectOne(queryWrapper);
        if (Objects.nonNull(userPO) && BCrypt.checkpw(loginDTO.getPassword(), userPO.getPassword())) {
            return jwtUtils.generateToken(Integer.toString(userPO.getUserId()), userPO.getUsername());
        } else {
            throw new LoginFailedException("用户名或密码错误");
        }
    }

    @Override
    public UserVO getCurrentUser() {
        String userIdStr = ThreadLocalUtils.get("userId");
        Integer currentUserId = Integer.parseInt(userIdStr);
        UserPO userPO = userMapper.selectById(currentUserId);
        UserVO userVO = new UserVO();
        BeanUtil.copyProperties(userPO, userVO);
        userVO.setAvatar(minioUtils.getPublicUrl(userVO.getAvatar()));
        return userVO;
    }
}
