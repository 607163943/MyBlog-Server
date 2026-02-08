package com.my.blog.server.handler;


import com.my.blog.common.enums.ExceptionEnums;
import com.my.blog.common.exception.MyBlogException;
import com.my.blog.common.exception.admin.*;
import com.my.blog.common.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 系统异常处理
     *
     * @param e 系统异常
     * @return 错误响应
     */
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public Result<Object> exceptionHandler(Exception e) {
        log.error("服务器错误：{}", e.getMessage());
        e.printStackTrace();
        return Result.error(500, "系统错误，请联系管理员");
    }

    /**
     * 管理端用户异常处理
     *
     * @param e 管理端用户异常
     * @return 错误响应
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(AdminUserException.class)
    public Result<Object> adminUserExceptionHandler(AdminUserException e) {
        ExceptionEnums exceptionEnums = e.getExceptionEnums();
        log.warn("管理端用户模块异常：code:{} msg:{}", exceptionEnums.getCode(), exceptionEnums.getMsg());
        return Result.error(exceptionEnums.getCode(), exceptionEnums.getMsg());
    }

    /**
     * 管理端用户登录异常处理
     * @param e 管理端用户登录异常
     * @return 错误响应
     */
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(AdminUserLoginException.class)
    public Result<Object> adminUserLoginExceptionHandler(AdminUserLoginException e) {
        ExceptionEnums exceptionEnums = e.getExceptionEnums();
        log.warn("管理端用户认证异常：code:{} msg:{}", exceptionEnums.getCode(), exceptionEnums.getMsg());
        return Result.error(exceptionEnums.getCode(), exceptionEnums.getMsg());
    }

    /**
     * 管理端标签异常处理
     * @param e 管理端标签异常
     * @return 错误响应
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(AdminTagException.class)
    public Result<Object> adminTagExceptionHandler(AdminTagException e) {
        ExceptionEnums exceptionEnums = e.getExceptionEnums();
        log.warn("管理端标签模块异常：code:{} msg:{}", exceptionEnums.getCode(), exceptionEnums.getMsg());
        return Result.error(exceptionEnums.getCode(), exceptionEnums.getMsg());
    }

    /**
     * 管理端分类异常处理
     * @param e 管理端分类异常
     * @return 错误响应
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(AdminCategoryException.class)
    public Result<Object> adminCategoryExceptionHandler(AdminCategoryException e) {
        ExceptionEnums exceptionEnums = e.getExceptionEnums();
        log.warn("管理端分类模块异常：code:{} msg:{}", exceptionEnums.getCode(), exceptionEnums.getMsg());
        return Result.error(exceptionEnums.getCode(), exceptionEnums.getMsg());
    }

    /**
     * 管理端上传异常处理
     * @param e 管理端上传异常
     * @return 错误响应
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(AdminUploadException.class)
    public Result<Object> adminUploadExceptionHandler(AdminUploadException e) {
        ExceptionEnums exceptionEnums = e.getExceptionEnums();
        log.warn("管理端上传模块异常：code:{} msg:{}", exceptionEnums.getCode(), exceptionEnums.getMsg());
        return Result.error(exceptionEnums.getCode(), exceptionEnums.getMsg());
    }

    /**
     * 参数校验异常处理
     * @param e 参数校验异常
     * @return 错误响应
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BindException.class)
    public Result<Object> bindExceptionHandler(BindException e) {
        // 获取校验信息
        BindingResult bindingResult = e.getBindingResult();
        FieldError fieldError = bindingResult.getFieldError();
        String defaultMessage = fieldError == null ? "参数错误" : fieldError.getDefaultMessage();

        log.warn("参数校验异常：{}", defaultMessage);
        return Result.error(400, defaultMessage);
    }

    /**
     * 未知业务异常处理
     *
     * @param e 未知业务异常
     * @return 错误响应
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MyBlogException.class)
    public Result<Object> myBlogExceptionHandler(MyBlogException e) {
        ExceptionEnums exceptionEnums = e.getExceptionEnums();
        log.warn("未知业务模块异常：code:{} msg:{}", exceptionEnums.getCode(), exceptionEnums.getMsg());
        return Result.error(exceptionEnums.getCode(), exceptionEnums.getMsg());
    }
}
