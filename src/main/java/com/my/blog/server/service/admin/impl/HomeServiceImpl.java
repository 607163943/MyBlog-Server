package com.my.blog.server.service.admin.impl;

import com.my.blog.common.constants.ArticleStatus;
import com.my.blog.pojo.po.Article;
import com.my.blog.pojo.vo.admin.AdminHomeChartCardVO;
import com.my.blog.pojo.vo.admin.AdminHomeDataCardVO;
import com.my.blog.pojo.vo.admin.RatioChartData;
import com.my.blog.pojo.vo.admin.TrendChartData;
import com.my.blog.server.service.admin.IArticleService;
import com.my.blog.server.service.admin.ICategoryService;
import com.my.blog.server.service.admin.IHomeService;
import com.my.blog.server.service.admin.ITagService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class HomeServiceImpl implements IHomeService {
    @Resource
    private IArticleService articleService;
    @Resource
    private ICategoryService categoryService;
    @Resource
    private ITagService tagService;
    /**
     * 获取首页数据统计
     * @return 管理端首页数据统计卡片VO
     */
    @Override
    public AdminHomeDataCardVO dataCardCount() {
        // 文章总数
        Long articleCount = articleService.count();
        // 发布文章总数
        Long publishArticleCount = articleService.lambdaQuery()
                .eq(Article::getStatus, ArticleStatus.PUBLISH)
                .count();
        // 草稿文章总数
        Long draftArticleCount = articleService.lambdaQuery()
                .eq(Article::getStatus, ArticleStatus.DRAFT)
                .count();
        Long categoryCount = categoryService.count();
        // 标签总数
        Long tagCount = tagService.count();

        return AdminHomeDataCardVO.builder()
                .articleCount(articleCount)
                .publishArticleCount(publishArticleCount)
                .draftArticleCount(draftArticleCount)
                .categoryCount(categoryCount)
                .tagCount(tagCount)
                .build();
    }

    /**
     * 获取首页图表数据
     * @return 管理端首页图表数据
     */
    @Override
    public AdminHomeChartCardVO chartCardCount() {
        List<TrendChartData> trendData = articleService.trend7Day();
        List<RatioChartData> ratioData=articleService.categoryArticleRatio();

        return AdminHomeChartCardVO.builder()
                .trendData(trendData)
                .ratioData(ratioData)
                .build();
    }
}
