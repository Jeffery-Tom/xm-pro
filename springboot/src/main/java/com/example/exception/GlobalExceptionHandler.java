package com.example.exception;

import com.example.common.Result;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
//全局异常类
@ControllerAdvice("com.example.controller")//捕获controller里的错误
public class GlobalExceptionHandler {

    /*
    Exception.class 是所有异常的父类
    意味着：任何异常（空指针、数组越界、数据库连接失败等）都会被这个方法捕获
     */
    @ExceptionHandler(Exception.class)
    @ResponseBody//让方法返回的结果自动转换成 JSON（而不是跳转页面）
    public Result error(Exception e) {//参数 e 就是被捕获到的异常对象，里面包含了错误信息、堆栈轨迹等
        e.printStackTrace();
        return Result.error();
    }
    @ExceptionHandler(CustomException.class)//专门处理CustomException这个自定义异常
    @ResponseBody//捕捉到错误返回JSON
    public Result error(CustomException  e) {
        return Result.error(e.getCode(),e.getMsg());
    }
}
