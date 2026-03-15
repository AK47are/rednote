package com.example.rednote.config.handler;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.rednote.common.response.Result;
import com.example.rednote.model.exception.BaseException;

import cn.hutool.http.HttpStatus;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BaseException.class)
    public ResponseEntity<Result<?>> handle(BaseException e) {
        return ResponseEntity.status(HttpStatus.HTTP_INTERNAL_ERROR).body(Result.fail(e.getMessage()));
    }
}
