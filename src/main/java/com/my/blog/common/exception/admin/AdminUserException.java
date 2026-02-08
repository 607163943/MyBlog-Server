package com.my.blog.common.exception.admin;

import com.my.blog.common.enums.ExceptionEnums;
import com.my.blog.common.exception.MyBlogException;

public class AdminUserException extends MyBlogException {
    public AdminUserException(ExceptionEnums exceptionEnums) {
        super(exceptionEnums);
    }
}
