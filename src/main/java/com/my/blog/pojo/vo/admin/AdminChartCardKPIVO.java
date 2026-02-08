package com.my.blog.pojo.vo.admin;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@ApiModel("图表数据KPI")
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminChartCardKPIVO {
    @ApiModelProperty("最近新增文章数")
    private Long addArticleCount;
    @ApiModelProperty("最近发布文章数")
    private Long publishArticleCount;
    @ApiModelProperty("文章总数")
    private Long totalArticleCount;
    @ApiModelProperty("分类总数")
    private Long categoryCount;
    @ApiModelProperty("标签总数")
    private Long tagCount;
}
