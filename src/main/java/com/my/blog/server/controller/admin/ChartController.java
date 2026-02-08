package com.my.blog.server.controller.admin;

import com.my.blog.common.result.Result;
import com.my.blog.pojo.vo.admin.AdminChartCardKPIVO;
import com.my.blog.pojo.vo.admin.AdminChartTrendCardVO;
import com.my.blog.server.service.admin.IChartService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@Api(tags = "后台图表")
@RequestMapping("/admin/chart")
@RestController("admin-chart-controller")
public class ChartController {
    @Resource
    private IChartService chartService;

    @ApiOperation("图表卡片数据")
    @GetMapping("/card/kpi")
    public Result<AdminChartCardKPIVO> chartCardKPI() {
        AdminChartCardKPIVO adminChartCardKPIVO = chartService.chartCardKPI();
        return Result.success(adminChartCardKPIVO);
    }

    @ApiOperation("图表趋势数据")
    @GetMapping("/card/trend")
    public Result<AdminChartTrendCardVO> chartCardTrend() {
        AdminChartTrendCardVO adminChartTrendCardVO = chartService.chartCardTrend();
        return Result.success(adminChartTrendCardVO);
    }

    @ApiOperation("图表日历数据")
    @GetMapping("/card/calendar")
    public Result<List<List<Object>>> chartCardCalendar() {
        List<List<Object>> calendarChartData = chartService.chartCardCalendar();
        return Result.success(calendarChartData);
    }
}
