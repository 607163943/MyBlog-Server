package com.my.blog.common.exception;

import com.my.blog.common.enums.ExceptionEnums;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class MyBlogException extends RuntimeException {
    private ExceptionEnums exceptionEnums;
}
