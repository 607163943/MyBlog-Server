package com.my.blog.server.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.my.blog.pojo.dto.ArticlePageQueryDTO;
import com.my.blog.pojo.dto.user.UserArticlePageQueryDTO;
import com.my.blog.pojo.po.Article;
import com.my.blog.pojo.vo.admin.AdminArticlePageQueryVO;
import com.my.blog.pojo.vo.admin.RatioChartData;
import com.my.blog.pojo.vo.admin.TrendChartData;
import com.my.blog.pojo.vo.user.UserArticlePageQueryVO;

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

    /**
     * 分页查询文章
     * @param page 分页参数
     * @param articlePageQueryDTO 查询条件
     * @param articleIds 文章id集合
     * @return 文章分页结果
     */
    Page<AdminArticlePageQueryVO> pageQuery(Page<AdminArticlePageQueryVO> page, ArticlePageQueryDTO articlePageQueryDTO, List<Long> articleIds);

    /**
     * 分页查询用户文章
     * @param page 分页参数
     * @param userArticlePageQueryDTO 查询条件
     * @param articleIds 文章id集合
     * @return 用户文章分页结果
     */
    Page<UserArticlePageQueryVO> userPageQuery(Page<UserArticlePageQueryVO> page, UserArticlePageQueryDTO userArticlePageQueryDTO, List<Long> articleIds);
}
