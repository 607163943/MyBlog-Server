package com.my.blog.common.exception.admin;

import com.my.blog.common.enums.ExceptionEnums;
import com.my.blog.common.exception.MyBlogException;

public class AdminArticleException extends MyBlogException {
    public AdminArticleException(ExceptionEnums exceptionEnums) {
        super(exceptionEnums);
    }
}
