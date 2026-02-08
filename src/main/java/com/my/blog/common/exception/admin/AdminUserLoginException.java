package com.my.blog.common.exception.admin;

import com.my.blog.common.enums.ExceptionEnums;

public class AdminUserLoginException extends AdminUserException{
    public AdminUserLoginException(ExceptionEnums exceptionEnums) {
        super(exceptionEnums);
    }
}
