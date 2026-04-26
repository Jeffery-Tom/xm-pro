package com.example.exception;

import com.example.common.Result;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
//全局异常类
@ControllerAdvice("com.example.controller")//捕获controller里的错误
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    @ResponseBody//捕捉到错误返回JSON
    public Result error(Exception e) {
        e.printStackTrace();
        return Result.error();
    }
    @ExceptionHandler(CustomException.class)
    @ResponseBody//捕捉到错误返回JSON
    public Result error(CustomException  e) {
        return Result.error(e.getCode(),e.getMsg());
    }
}
