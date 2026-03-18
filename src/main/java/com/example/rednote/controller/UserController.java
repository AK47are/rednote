package com.example.rednote.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.rednote.common.response.Result;
import com.example.rednote.model.dto.LoginDTO;
import com.example.rednote.model.dto.UserDTO;
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
    @Operation(summary = "获取用户信息", description = "根据用户ID返回其基础信息")
    public Result<UserVO> getById(@PathVariable Integer userId) {
        return Result.success(userService.getById(userId));
    }

    @PostMapping("/register")
    @Operation(summary = "用户注册", description = "创建新用户账户")
    public Result<?> register(@RequestBody UserDTO userDto) {
        if (userService.register(userDto) == 1) {
            return Result.success();
        } else {
            return Result.fail("注册失败");
        }
    }

    @PostMapping("/login")
    @Operation(summary = "用户登录", description = "登录用户账号")
    public Result<String> login(@RequestBody LoginDTO loginDTO) {
        return Result.success(userService.login(loginDTO));
    }

    @GetMapping("/current")
    @Operation(summary = "获取当前登录用户", description = "返回当前登录用户的简略信息")
    public Result<UserVO> getCurrentUser() {
        return Result.success(userService.getCurrentUser());
    }
}
