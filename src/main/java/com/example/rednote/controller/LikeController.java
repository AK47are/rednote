package com.example.rednote.controller;
import com.example.rednote.common.response.Result;
import com.example.rednote.service.LikeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/likes")
@AllArgsConstructor
@Tag(name = "点赞管理",description = "点赞管理接口")
public class LikeController {
    private final LikeService likeService;

    @Operation(summary = "判断是否已赞",description = "根据postId判断是否已赞")
    @GetMapping("/status/{postId}")
    public Result<Boolean> isLike(@PathVariable Integer postId){
        return Result.success(likeService.isLike(postId));
    }

    @Operation(summary = "点赞/取消点赞",description = "根据postId点赞/取消点赞")
    @PostMapping("/{postId}")
    public Result<Boolean> toogleLiked(@PathVariable Integer postId){
        return Result.success(likeService.toogleLike(postId));
    }

}
