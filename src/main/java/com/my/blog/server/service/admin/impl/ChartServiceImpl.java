package com.my.blog.server.service.admin.impl;

import com.my.blog.common.constants.ArticleStatus;
import com.my.blog.pojo.vo.admin.*;
import com.my.blog.server.service.admin.IArticleService;
import com.my.blog.server.service.admin.ICategoryService;
import com.my.blog.server.service.admin.IChartService;
import com.my.blog.server.service.admin.ITagService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class ChartServiceImpl implements IChartService {
    @Resource
    private IArticleService articleService;
    @Resource
    private ICategoryService categoryService;
    @Resource
    private ITagService tagService;

    /**
     * 获取卡片数据
     *
     * @return 卡片数据
     */
    @Override
    public AdminChartCardKPIVO chartCardKPI() {
        // 最近新增文章数
        Long addArticleCount = articleService.countArticle7Day(null);
        // 最近发布文章数
        Long publishArticleCount = articleService.countArticle7Day(ArticleStatus.PUBLISH);
        // 文章总数
        long totalArticleCount = articleService.count();
        // 分类总数
        long categoryCount = categoryService.count();
        // 标签总数
        long tagCount = tagService.count();

        return AdminChartCardKPIVO.builder()
                .addArticleCount(addArticleCount)
                .publishArticleCount(publishArticleCount)
                .totalArticleCount(totalArticleCount)
                .categoryCount(categoryCount)
                .tagCount(tagCount)
                .build();
    }

    /**
     * 获取趋势数据
     *
     * @return 趋势数据
     */
    @Override
    public AdminChartTrendCardVO chartCardTrend() {
        // 新增文章趋势
        List<TrendChartData> trendChartData = articleService.trend7Day();
        // 文章状态占比
        List<RatioChartData> articleStatusRatio = articleService.countGroupByStatus();

        return AdminChartTrendCardVO.builder()
                .addArticleTrendData(trendChartData)
                .articleStatusRatioData(articleStatusRatio)
                .build();
    }

    /**
     * 获取日历数据
     *
     * @return 日历数据
     */
    @Override
    public List<List<Object>> chartCardCalendar() {
        return articleService.countThisYearAddArticleActive();
    }
}
