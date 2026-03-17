package com.example.rednote.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.rednote.common.response.Result;
import com.example.rednote.service.FileService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("/files")
@Tag(name = "文件接口", description = "文件相关接口")
public class FileController {

    private final FileService fileService;

    @PostMapping("/update")
    @Operation(summary = "文件上传", description = "上传帖子的图片/视频，返回上传的 URL")
    public Result<?> uploadFile(@RequestParam("file") MultipartFile file) throws Exception {
        String fileUrl = fileService.uploadFile(file);
        return Result.success(fileUrl);
    }
}
