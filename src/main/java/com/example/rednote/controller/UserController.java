package com.example.rednote.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.toolkit.BeanUtils;
import com.example.rednote.common.response.Result;
import com.example.rednote.model.vo.UserVO;
import com.example.rednote.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/users")
@Tag(name = "用户接口", description = "用户相关接口")
public class UserController {

    private final UserService userService;

    @GetMapping("/{userId}")
    @Operation(summary = "获取用户详情", description = "根据用户ID返回详细信息")
    public Result<UserVO> getById(@PathVariable Integer userId) {
        return Result.success(userService.getById(userId));
    }
}
