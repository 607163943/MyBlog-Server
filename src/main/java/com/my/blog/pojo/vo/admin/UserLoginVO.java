package com.my.blog.pojo.vo.admin;


import com.my.blog.pojo.vo.UserInfoVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@ApiModel("用户登录信息")
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserLoginVO {
    @ApiModelProperty("用户登录token")
    private String token;
    @ApiModelProperty("用户信息")
    private UserInfoVO userInfoVO;
}
