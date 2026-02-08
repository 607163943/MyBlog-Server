package com.my.blog.common.result;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@ApiModel("统一返回结果")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result<T> {
    @ApiModelProperty("业务状态码")
    private Integer code;
    @ApiModelProperty("业务数据")
    private T data;
    @ApiModelProperty("业务信息")
    private String msg;

    public static <T> Result<T> of(Integer code, T data, String msg) {
        return new Result<>(code, data, msg);
    }

    public static <T> Result<T> success(T data) {
        return of(200, data, "success");
    }

    public static <T> Result<T> success() {
        return of(200, null, "success");
    }

    public static <T> Result<T> error(Integer code, String msg) {
        return of(code, null, msg);
    }

    public static <T> Result<T> error(Integer code) {
        return of(code, null, "error");
    }
}
