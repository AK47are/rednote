package com.example.rednote.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostDTO {
    private Integer postId;
    @Schema(description = "用户id", requiredMode = RequiredMode.REQUIRED)
    private Integer userId;
    @Schema(description = "帖子标题", requiredMode = RequiredMode.REQUIRED)
    private String title;
    @Schema(description = "帖子内容", requiredMode = RequiredMode.REQUIRED)
    private String content;
}
