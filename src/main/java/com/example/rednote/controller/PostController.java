package com.example.rednote.controller;

import com.example.rednote.model.dto.PostDTO;
import com.example.rednote.model.vo.PostWithUserVO;
import org.springframework.web.bind.annotation.*;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.rednote.common.response.Result;
import com.example.rednote.model.po.PostPO;
import com.example.rednote.service.PostService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;

import java.util.List;

@RestController
@RequestMapping("/posts")
@Tag(name = "帖子接口", description = "帖子相关接口")
@AllArgsConstructor
public class PostController {
    private final PostService postService;

    @GetMapping
    @Operation(summary = "获取全部帖子", description = "返回所有贴子，支持分页")
    public Result<Page<PostPO>> list(@RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "-1") int size) {
        return Result.success(postService.page(new Page<>(page, size)));
    }
    @GetMapping("/list/{topicId}")
    @Operation(summary = "获取帖子列表", description = "适用于主页的返回所有贴子,带用户头像、用户名")
    public Result<List<PostWithUserVO>> listWithUserInfoByTopicId(
            @PathVariable Integer topicId) {
        List<PostWithUserVO> postResults = postService.listWithUserInfo(topicId);
        return Result.success(postResults);
    }

    @GetMapping("/list/user/{userId}")
    @Operation(summary = "获取博主帖子", description = "适用于主页的返回所有贴子,带用户头像、用户名")
    public Result<List<PostWithUserVO>> listWithUserInfoByUserId(
            @PathVariable Integer userId) {
        List<PostWithUserVO> postResults = postService.listWithUserInfoByUserId(userId);
        return Result.success(postResults);
    }

    @GetMapping("/{postId}")
    @Operation(summary = "获取帖子详情", description = "根据帖子ID返回详细信息")
    public Result<PostPO> getPost(@PathVariable Integer postId) {
        return Result.success(postService.getById(postId));
    }

    @DeleteMapping("/{postId}")
    @Operation(summary = "删除帖子", description = "根据帖子ID删除指定帖子")
    public Result<?> delete(@PathVariable Integer postId) {
        postService.removeById(postId);
        return Result.success();
    }

    @Operation(summary = "添加帖子")
    @PostMapping("/insert")
    public Result addPost(@RequestBody PostDTO postDTO){
        postService.addPost(postDTO);
        return Result.success();
    }
}
