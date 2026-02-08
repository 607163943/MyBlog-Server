package com.my.blog.common.exception.user;

import com.my.blog.common.enums.ExceptionEnums;
import com.my.blog.common.exception.MyBlogException;

public class UserUserException extends MyBlogException {
    public UserUserException(ExceptionEnums exceptionEnums) {
        super(exceptionEnums);
    }
}
