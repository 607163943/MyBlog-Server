package com.my.blog.server.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.my.blog.pojo.po.Article;
import com.my.blog.pojo.vo.admin.RatioChartData;
import com.my.blog.pojo.vo.admin.TrendChartData;

import java.util.List;

public interface ArticleMapper extends BaseMapper<Article> {
    /**
     * 获取7天新增文章趋势
     * @return 7天新增文章趋势
     */
    List<TrendChartData> trend7Day();

    /**
     * 获取文章分类占比
     * @return 文章分类占比
     */
    List<RatioChartData> categoryArticleRatio();
}
