package com.example.rednote.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.example.rednote.config.interceptor.JwtInterceptor;

import lombok.AllArgsConstructor;

@Configuration
@AllArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final JwtInterceptor jwtInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtInterceptor)
                .addPathPatterns("/users/**", "/posts/**", "/files/**", "/topics/**", "/comments/**", "/follow/**")
                .excludePathPatterns("/users/register", "/users/login");
    }
}
