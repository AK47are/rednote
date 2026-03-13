package com.example.rednote.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {

    @Schema(description = "用户id")
    private Integer userId;
    @Schema(description = "用户名")
    private String username;
    @Schema(description = "用户密码")
    private String password;
    @Schema(description = "头像 URL")
    private String avatar;
}
