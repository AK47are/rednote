package com.example.rednote.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.rednote.common.response.Result;
import com.example.rednote.service.FollowService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("/follow")
@Tag(name = "关注接口", description = "关注相关接口")
public class FollowController {

    private final FollowService followService;

    @PostMapping("/{userId}")
    @Operation(summary = "关注/取关用户", description = "令当前用户关注/取关 userId 用户")
    public Result<?> toogleFollow(@PathVariable Integer userId) {
        followService.toogleFollow(userId);
        return Result.success();
    }

    @GetMapping("/status/{userId}")
    @Operation(summary = "判断是否关注用户", description = "判断当前用户是否关注 userId 用户")
    public Result<Boolean> isFollow(@PathVariable Integer userId) {
        Boolean isFollow = followService.isFollow(userId);
        return Result.success(isFollow);
    }

}
