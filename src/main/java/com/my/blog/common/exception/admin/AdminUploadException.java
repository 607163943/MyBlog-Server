package com.my.blog.common.exception.admin;

import com.my.blog.common.enums.ExceptionEnums;
import com.my.blog.common.exception.MyBlogException;

public class AdminUploadException extends MyBlogException {
    public AdminUploadException(ExceptionEnums exceptionEnums) {
        super(exceptionEnums);
    }
}
