package com.my.blog.server.service.admin;

import com.my.blog.pojo.vo.admin.AdminHomeChartCardVO;
import com.my.blog.pojo.vo.admin.AdminHomeDataCardVO;

public interface IHomeService {
    /**
     * 获取首页数据
     * @return 管理端首页数据统计卡片VO
     */
    AdminHomeDataCardVO dataCardCount();

    /**
     * 获取首页图表数据
     * @return 管理端首页图表数据
     */
    AdminHomeChartCardVO chartCardCount();
}
