package com.my.blog.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ExceptionEnums {

    ADMIN_USER_NOT_EXIST(1001, "用户不存在"),
    ADMIN_USER_PASSWORD_ERROR(1002, "密码错误"),
    ADMIN_USER_CAPTCHA_CODE_ERROR(1003, "验证码错误"),
    ADMIN_USER_CAPTCHA_EXPIRED(1004, "验证码已过期"),
    ADMIN_USER_LOGIN_TIMEOUT(1005, "登录超时"),
    ADMIN_USER_NOT_LOGIN(1006, "用户未登录"),
    ADMIN_USER_NOT_MATCH(1007, "用户未匹配"),

    ADMIN_TAG_EXIST(2001, "标签已存在"),

    ADMIN_CATEGORY_EXIST(3001, "分类已存在"),
    ADMIN_CATEGORY_COVER_NOT_EXIST(3002, "分类封面不存在"),
    ADMIN_CATEGORY_NOT_EMPTY(3003, "分类下有文章，不能删除"),

    ADMIN_UPLOAD_MD5_CREATE_ERROR(4001, "创建文件MD5值生成失败"),

    ADMIN_ARTICLE_EXIST(5001, "文章已存在"),
    ADMIN_ARTICLE_COVER_NOT_EXIST(5002, "文章封面不存在"),
    ADMIN_ARTICLE_STATUS_ERROR(5003, "文章状态错误"),
    ADMIN_ARTICLE_PUBLISH_CANT_UPDATE(5004, "不能修改发布中的文章"),
    ADMIN_ARTICLE_BELONG_CATEGORY_DISABLE(5005, "文章所属分类已禁用"),
    ADMIN_ARTICLE_TAG_DISABLE(5006, "该标签不存在或已禁用"),

    USER_DATA_EXCEPTION(6001, "系统数据异常"),

    USER_ARTICLE_NOT_EXIST(7001, "文章不存在"),
    USER_ARTICLE_BELONG_CATEGORY_DISABLE(7002, "文章所属分类已禁用");

    private final Integer code;
    private final String msg;
}
