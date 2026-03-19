package com.example.rednote.controller;

import com.example.rednote.common.response.Result;
import com.example.rednote.model.vo.TopicVO;
import com.example.rednote.service.TopicService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/topics")
@AllArgsConstructor
@Tag(name = "主题相关接口")
public class TopicController {
    private final TopicService topicService;

    @Operation(summary = "获取所有主题")
    @GetMapping("/list")
    public Result<?> list() {
        List<TopicVO> topicVOS = topicService.list();
        return Result.success(topicVOS);
    }
}
