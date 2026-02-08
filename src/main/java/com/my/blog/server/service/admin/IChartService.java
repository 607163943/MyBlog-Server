package com.my.blog.server.service.admin;

import com.my.blog.pojo.vo.admin.AdminChartCardKPIVO;
import com.my.blog.pojo.vo.admin.AdminChartTrendCardVO;

import java.util.List;

public interface IChartService {
    /**
     * 获取首页图表数据
     * @return 管理端首页图表数据
     */
    AdminChartCardKPIVO chartCardKPI();

    /**
     * 获取首页图表趋势数据
     * @return 管理端首页图表趋势数据
     */
    AdminChartTrendCardVO chartCardTrend();

    /**
     * 获取首页图表日历数据
     * @return 管理端首页图表日历数据
     */
    List<List<Object>> chartCardCalendar();
}
