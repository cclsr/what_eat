package com.cclsr.eat.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLIntegrityConstraintViolationException;

/**
 * 全局异常处理
 * 拦截指定注解的controller异常
 */
@ControllerAdvice(annotations = {RestController.class, Controller.class})
@ResponseBody
@Slf4j
public class GlobalExceptionHandler {
    /**
     * 异常处理 ExceptionHandler 里面放指定的异常
     * @return
     */
    @ExceptionHandler({SQLIntegrityConstraintViolationException.class})
    public R<String> exceptionHandler(SQLIntegrityConstraintViolationException ex){
        log.info("异常信息{}",ex.getMessage());
        if (ex.getMessage().contains("Duplicate entry")){
            String[] split = ex.getMessage().split(" ");
            return R.error(split[2] + "账号存在");
        }
        return R.error("未知错误");
    }

    /**
     * 自定义的异常
     * @return
     */
    @ExceptionHandler({CustomException.class})
    public R<String> exceptionHandler(CustomException ex){
        log.info("异常信息{}",ex.getMessage());
        return R.error(ex.getMessage());
    }
}
