package com.my.blog.pojo.vo.admin;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@ApiModel("新增文章趋势数据")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TrendChartData {
    @ApiModelProperty("数量")
    private Long count;
    @ApiModelProperty("日期")
    private LocalDate date;
}
