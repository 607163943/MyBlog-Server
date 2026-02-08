package com.my.blog.common.exception.user;

import com.my.blog.common.enums.ExceptionEnums;
import com.my.blog.common.exception.MyBlogException;

public class UserCategoryException extends MyBlogException {
    public UserCategoryException(ExceptionEnums exceptionEnums) {
        super(exceptionEnums);
    }
}
