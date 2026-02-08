package com.my.blog.pojo.vo.admin;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@ApiModel("管理端首页图表卡片VO")
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminHomeChartCardVO {
    @ApiModelProperty("7天新增文章趋势数据")
    private List<TrendChartData> trendData;
    @ApiModelProperty("各分类文章数量占比Top10数据")
    private List<RatioChartData> ratioData;
}
