package com.example.rednote.controller;
import com.example.rednote.common.response.Result;
import com.example.rednote.service.PostImageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/post/images")
@Tag(name = "帖子图片接口", description = "帖子图片相关接口")
@AllArgsConstructor
public class PostImageController {
    private final PostImageService postImageService;

    @Operation(summary = "根据帖子图片列表", description = "根据帖子ID查询帖子图片列表")
    @GetMapping("/{postId}")
    public Result<List<String>> list(@PathVariable Integer postId){
        return Result.success(postImageService.list(postId));
    }
}