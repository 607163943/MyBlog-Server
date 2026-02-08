package com.my.blog.pojo.dto.admin;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@ApiModel("用户密码数据")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserPasswordDTO {
    @ApiModelProperty("旧密码")
    @NotBlank(message = "旧密码不能为空")
    private String oldPassword;

    @ApiModelProperty("新密码")
    @NotBlank(message = "新密码不能为空")
    private String newPassword;

    @ApiModelProperty("验证码")
    @NotBlank(message = "验证码不能为空")
    private String captchaCode;

    @ApiModelProperty("验证码key")
    @NotBlank(message = "验证码key不能为空")
    private String captchaKey;
}
