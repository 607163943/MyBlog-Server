package com.my.blog.pojo.vo.admin;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@ApiModel("管理端首页图表数据")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RatioChartData {
    @ApiModelProperty("名称")
    private String name;
    @ApiModelProperty("数量")
    private Long value;
}
