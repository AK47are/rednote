package com.example.rednote.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.rednote.common.response.Result;
import com.example.rednote.model.po.PostPO;
import com.example.rednote.service.PostService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("/posts")
@Tag(name = "帖子接口", description = "帖子相关接口")
public class PostController {
    private final PostService postService;

    @GetMapping
    @Operation(summary = "获取全部帖子", description = "返回所有贴子，支持分页")
    public Result<Page<PostPO>> list(@RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "-1") int size) {
        return Result.success(postService.page(new Page<>(page, size)));
    }
}
