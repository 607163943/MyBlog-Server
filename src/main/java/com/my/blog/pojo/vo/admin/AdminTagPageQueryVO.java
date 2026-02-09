package com.my.blog.pojo.vo.admin;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@ApiModel("标签分页查询结果")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminTagPageQueryVO {
    @ApiModelProperty("id")
    private Long id;
    @ApiModelProperty("标签名称")
    private String name;
}
