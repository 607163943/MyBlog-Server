package com.my.blog.common.exception.admin;

import com.my.blog.common.enums.ExceptionEnums;
import com.my.blog.common.exception.MyBlogException;

public class AdminCategoryException extends MyBlogException {
    public AdminCategoryException(ExceptionEnums exceptionEnums) {
        super(exceptionEnums);
    }
}
