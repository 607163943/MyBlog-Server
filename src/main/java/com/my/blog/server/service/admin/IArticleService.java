package com.my.blog.server.service.admin;

import com.baomidou.mybatisplus.extension.service.IService;
import com.my.blog.common.result.PageResult;
import com.my.blog.pojo.dto.ArticlePageQueryDTO;
import com.my.blog.pojo.dto.admin.AdminArticleDTO;
import com.my.blog.pojo.po.Article;
import com.my.blog.pojo.vo.admin.*;

import java.util.List;

public interface IArticleService extends IService<Article> {

    /**
     * 分页查询文章
     * @param articlePageQueryDTO 查询条件
     * @return 分页结果
     */
    PageResult<AdminArticlePageQueryVO> pageQuery(ArticlePageQueryDTO articlePageQueryDTO);

    /**
     * 根据id查询文章
     * @param id 文章id
     * @return 文章数据
     */
    AdminArticleVO getById(Long id);

    /**
     * 新增文章
      * @param adminArticleDTO 分类数据
     */
    void addArticle(AdminArticleDTO adminArticleDTO);

    /**
     * 修改文章
     * @param adminArticleDTO 分类数据
     */
    void updateArticle(AdminArticleDTO adminArticleDTO);

    /**
     * 修改文章状态
     * @param id 文章id
     * @param status 文章状态
     */
    void updateStatus(Long id,Integer status);

    /**
     * 预览文章
     * @param id 文章id
     * @return 文章预览数据
     */
    AdminArticlePreviewVO preview(Long id);

    /**
     * 获取7天新增文章趋势
     * @return 7天文章新增趋势
     */
    List<TrendChartData> trend7Day();

    /**
     * 获取文章分类Top10
     * @return 文章分类
     */
    List<RatioChartData> categoryArticleRatio();

    Long countArticle7Day(Integer status);

    /**
     * 获取文章状态占比
     * @return 文章状态占比
     */
    List<RatioChartData> countGroupByStatus();

    /**
     * 获取文章活跃数据
     * @return 文章活跃数据
     */
    List<List<Object>> countThisYearAddArticleActive();

    /**
     * 删除文章
     * @param id 文章id
     */
    void deleteById(Long id);

    /**
     * 批量删除文章
     * @param ids 文章id集合
     */
    void deleteByIds(List<Long> ids);
}
