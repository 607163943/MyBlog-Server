package com.my.blog.pojo.vo.admin;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@ApiModel("日历热力图单项数据")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CalendarChartData {
    @ApiModelProperty("日期")
    private LocalDate date;
    @ApiModelProperty("数量")
    private Long count;
}
