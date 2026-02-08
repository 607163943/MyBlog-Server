package com.my.blog.pojo.dto.admin;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@ApiModel("用户信息")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoDTO {
    @ApiModelProperty("用户id")
    private Long id;
    @ApiModelProperty("用户名")
    @NotBlank(message = "用户名不能为空")
    private String username;
    @ApiModelProperty("昵称")
    private String nickname;
}
