package com.my.blog.common.exception.user;

import com.my.blog.common.enums.ExceptionEnums;
import com.my.blog.common.exception.MyBlogException;

public class UserArticleException extends MyBlogException {
    public UserArticleException(ExceptionEnums exceptionEnums) {
        super(exceptionEnums);
    }
}
