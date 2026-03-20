package com.example.rednote.service.impl;

import java.util.Objects;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.rednote.common.utils.JwtUtils;
import com.example.rednote.common.utils.MinioUtils;
import com.example.rednote.common.utils.ThreadLocalUtils;
import com.example.rednote.mapper.UserDetailsMapper;
import com.example.rednote.mapper.UserMapper;
import com.example.rednote.model.dto.UserAuthDTO;
import com.example.rednote.model.dto.UserUpdateDTO;
import com.example.rednote.model.exception.LoginFailedException;
import com.example.rednote.model.exception.RegisterFailedException;
import com.example.rednote.model.po.UserDetailsPO;
import com.example.rednote.model.po.UserPO;
import com.example.rednote.model.vo.UserVO;
import com.example.rednote.service.UserService;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.BCrypt;
import io.minio.errors.MinioException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;
    private final UserDetailsMapper userDetailsMapper;
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
    @Transactional
    public void register(UserAuthDTO userAuthDTO) {
        // 检查 username 是否已经存在
        QueryWrapper<UserPO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", userAuthDTO.getUsername());

        if (Objects.nonNull(userMapper.selectOne(queryWrapper))) {
            throw new RegisterFailedException("用户名已经存在");
        }

        // 插入到 user 表
        UserPO userPO = new UserPO();
        userPO.setUsername(userAuthDTO.getUsername());
        userPO.setPassword(BCrypt.hashpw(userAuthDTO.getPassword()));
        userMapper.insert(userPO);

        // 插入到 user_details 表
        log.debug("获得 userId: {}", userPO.getUserId());
        UserDetailsPO details = new UserDetailsPO();
        details.setUserId(userPO.getUserId());
        userDetailsMapper.insert(details);
    }

    @Override
    public String login(UserAuthDTO userAuthDTO) {
        QueryWrapper<UserPO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", userAuthDTO.getUsername());
        UserPO userPO = userMapper.selectOne(queryWrapper);
        if (Objects.nonNull(userPO) && BCrypt.checkpw(userAuthDTO.getPassword(), userPO.getPassword())) {
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

    @Override
    @Transactional
    public void updateUser(UserUpdateDTO userUpdateDTO) {
        userUpdateDTO.setAvatar(minioUtils.getObjectName(userUpdateDTO.getAvatar()));
        Integer userId = Integer.parseInt(ThreadLocalUtils.get("userId"));

        // user 表更新
        UserPO userPO = new UserPO();
        userPO.setUserId(userId);
        BeanUtil.copyProperties(userUpdateDTO, userPO);
        userMapper.updateById(userPO);

        // user_details 表更新
        UserDetailsPO userDetailsPO = new UserDetailsPO();
        userDetailsPO.setUserId(userId);
        BeanUtil.copyProperties(userUpdateDTO, userDetailsPO);
        userDetailsMapper.updateById(userDetailsPO);
    }
}
