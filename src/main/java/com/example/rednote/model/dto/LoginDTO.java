package com.example.rednote.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;
import lombok.Data;

@Data
public class LoginDTO {

    @Schema(description = "用户名", requiredMode = RequiredMode.REQUIRED)
    String username;
    @Schema(description = "密码", requiredMode = RequiredMode.REQUIRED)
    String password;
}
