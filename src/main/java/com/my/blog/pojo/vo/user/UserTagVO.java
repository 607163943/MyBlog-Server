package com.my.blog.pojo.vo.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@ApiModel("标签VO")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserTagVO {
    @ApiModelProperty("id")
    private Long id;
    @ApiModelProperty("标签名称")
    private String name;
}
