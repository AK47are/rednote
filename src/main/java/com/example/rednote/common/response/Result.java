package com.example.rednote.common.response;

import com.example.rednote.common.enumeration.ResultCode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result<T> {
    private Integer code;
    private String msg;
    private T data;

    public static <T> Result<T> success(T data) {
        return build(ResultCode.SUCCESS, data);
    }

    public static <T> Result<T> success() {
        return build(ResultCode.SUCCESS, null);
    }

    public static <T> Result<T> fail(String msg) {
        Result<T> result = new Result<>();
        result.setCode(ResultCode.FAIL.getCode());
        result.setMsg(msg);
        return result;
    }

    private static <T> Result<T> build(ResultCode code, T data) {
        Result<T> r = new Result<>();
        r.code = code.getCode();
        r.msg = code.getMsg();
        r.data = data;
        return r;
    }
}
