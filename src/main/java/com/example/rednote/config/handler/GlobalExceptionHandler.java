package com.example.rednote.config.handler;

import java.sql.SQLIntegrityConstraintViolationException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.rednote.common.response.Result;
import com.example.rednote.model.exception.BaseException;

import cn.hutool.http.HttpStatus;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BaseException.class)
    public ResponseEntity<Result<?>> handle(BaseException e) {
        log.error("异常信息：{}", e.getMessage());
        return ResponseEntity.status(HttpStatus.HTTP_INTERNAL_ERROR).body(Result.fail(e.getMessage()));
    }

    // 单独处理用户名重复的异常
    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public ResponseEntity<Result<?>> exceptionHandler(SQLIntegrityConstraintViolationException e) {
        String message = e.getMessage();
        if (message.contains("Duplicate entry")) {
            String username = message.split(" ")[2];
            log.error("异常信息：{} 已存在", e.getMessage());
            return ResponseEntity.status(HttpStatus.HTTP_CONFLICT).body(Result.fail(username + " 已存在"));
        } else {
            log.error("异常信息：{}", e.getMessage());
            return ResponseEntity.status(HttpStatus.HTTP_CONFLICT).body(Result.fail(e.getMessage()));
        }
    }
}
