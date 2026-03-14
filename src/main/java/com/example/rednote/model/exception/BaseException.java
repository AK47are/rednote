package com.example.rednote.model.exception;

// 将业务异常和其它异常区分开
public class BaseException extends RuntimeException {

    public BaseException() {
    }

    public BaseException(String msg) {
        super(msg);
    }

}
