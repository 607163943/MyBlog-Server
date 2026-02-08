package com.my.blog.pojo.vo.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@ApiModel("用户统计图表数据")
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserUserCountChartCardVO {
    @ApiModelProperty("发布中文章数量")
    private Long articleCount;
    @ApiModelProperty("分类数量")
    private Long categoryCount;
    @ApiModelProperty("标签数量")
    private Long tagCount;
}
