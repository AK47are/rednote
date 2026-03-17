package com.example.rednote.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.rednote.common.response.Result;
import com.example.rednote.model.po.PostDetailsPO;
import com.example.rednote.service.PostDetailsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/posts/details")
@Tag(name = "帖子详情接口", description = "帖子详情相关接口")
@AllArgsConstructor
public class PostDetailsController {
    private final PostDetailsService postDetailsService;

    @GetMapping("/{postId}")
    @Operation(summary = "获取帖子详情", description = "根据帖子ID返回详细信息")
    public Result<PostDetailsPO> getPost(@PathVariable Integer postId) {
        LambdaQueryWrapper<PostDetailsPO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(PostDetailsPO::getPostId, postId);
        return Result.success(postDetailsService.getOne(wrapper));
    }
}
