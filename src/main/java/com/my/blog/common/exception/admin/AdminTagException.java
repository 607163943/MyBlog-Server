package com.my.blog.common.exception.admin;

import com.my.blog.common.enums.ExceptionEnums;
import com.my.blog.common.exception.MyBlogException;

public class AdminTagException extends MyBlogException {
    public AdminTagException(ExceptionEnums exceptionEnums) {
        super(exceptionEnums);
    }
}
