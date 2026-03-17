package com.example.rednote.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/users/details")
@Tag(name = "用户详情接口", description = "用户详情相关接口")
@AllArgsConstructor
public class UserDetailsController {

}
