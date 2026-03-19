package com.example.rednote.controller;

import com.example.rednote.common.response.Result;
import com.example.rednote.model.dto.CommentDTO;
import com.example.rednote.model.vo.CommentVO;
import com.example.rednote.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/comments")
@AllArgsConstructor
@Tag(name = "评论接口")
public class CommentController {

    private final CommentService commentService;

    @Operation(summary = "查询帖子评论", description = "根据帖子ID查询该帖子下的所有评论")
    @GetMapping("/list/{postId}")
    public Result<?> listByPostId(@PathVariable Integer postId) {
        List<CommentVO> commentVOS = commentService.listByPostId(postId);
        return Result.success(commentVOS);
    }

    @Operation(summary = "添加评论", description = "用户对帖子进行评论")
    @PostMapping("/insert")
    public Result<?> addComment(@RequestBody CommentDTO commentDTO) {
        commentService.insert(commentDTO);
        return Result.success();
    }

    @Operation(summary = "删除评论", description = "根据评论ID删除评论")
    @DeleteMapping("/{commentId}")
    public Result<?> delete(@PathVariable Integer commentId) {
        commentService.removeById(commentId);
        return Result.success();
    }
}
