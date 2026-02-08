package com.my.blog.pojo.vo.admin;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@ApiModel("图表趋势数据VO")
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminChartTrendCardVO {
    @ApiModelProperty("7天新增文章趋势数据")
    private List<TrendChartData> addArticleTrendData;
    @ApiModelProperty("各状态文章数量占比数据")
    private List<RatioChartData> articleStatusRatioData;
}
