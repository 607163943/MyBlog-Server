package com.my.blog.server.service.admin.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.my.blog.common.constants.*;
import com.my.blog.common.enums.ExceptionEnums;
import com.my.blog.common.exception.admin.AdminArticleException;
import com.my.blog.common.result.PageResult;
import com.my.blog.common.utils.PageQueryUtils;
import com.my.blog.pojo.dto.ArticlePageQueryDTO;
import com.my.blog.pojo.dto.admin.AdminArticleDTO;
import com.my.blog.pojo.po.*;
import com.my.blog.pojo.vo.admin.*;
import com.my.blog.server.mapper.ArticleMapper;
import com.my.blog.server.service.admin.*;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements IArticleService {
    private final PageQueryUtils pageQueryUtils;

    private final IUploadFileRefService uploadFileRefService;

    private final IArticleTagService articleTagService;

    private final ICategoryService categoryService;

    private final ITagService tagService;

    // 本类代理
    @Lazy
    @Resource
    private IArticleService articleService;


    /**
     * 分页查询文章
     *
     * @param articlePageQueryDTO 查询条件
     * @return 分页结果
     */
    @Override
    public PageResult<AdminArticlePageQueryVO> pageQuery(ArticlePageQueryDTO articlePageQueryDTO) {
        // 创建分页对象
        Page<AdminArticlePageQueryVO> page = pageQueryUtils.createPage(articlePageQueryDTO, AdminArticlePageQueryVO.class);

        // 获取标签对应文章id
        List<ArticleTag> articleTags = null;
        List<Long> articleIds = null;
        // 空标签就不查
        if (articlePageQueryDTO.getTagId() != null) {
            // 判断该标签是否存在或禁用
            Tag tag = tagService.getById(articlePageQueryDTO.getTagId());
            if (tag == null) {
                throw new AdminArticleException(ExceptionEnums.ADMIN_ARTICLE_TAG_DISABLE);
            }

            articleTags = articleTagService.lambdaQuery()
                    .eq(ArticleTag::getTagId, articlePageQueryDTO.getTagId())
                    .list();

            articleIds = articleTags.stream().map(ArticleTag::getArticleId).collect(Collectors.toList());
        }

        // 文章标签关联数据为空但标签查询条件不为空也返回空集合
        if (CollUtil.isEmpty(articleIds) && articlePageQueryDTO.getTagId() != null) {
            return PageResult.<AdminArticlePageQueryVO>builder()
                    .pageSize(page.getPages())
                    .pageNum(page.getCurrent())
                    .total(0L)
                    .result(Collections.emptyList())
                    .build();
        }

        // 查询
        page = baseMapper.pageQuery(page, articlePageQueryDTO, articleIds);

        return PageResult.<AdminArticlePageQueryVO>builder()
                .pageSize(page.getSize())
                .pageNum(page.getCurrent())
                .total(page.getTotal())
                .result(page.getRecords())
                .build();
    }

    /**
     * 根据id查询文章
     *
     * @param id 文章id
     * @return 文章数据
     */
    @Override
    public AdminArticleVO getById(Long id) {
        Article article = super.getById(id);
        List<ArticleTag> articleTags = articleTagService.lambdaQuery()
                .eq(ArticleTag::getArticleId, id)
                .list();
        List<Long> tagIds = articleTags.stream().map(ArticleTag::getTagId).collect(Collectors.toList());
        AdminArticleVO adminArticleVO = BeanUtil.copyProperties(article, AdminArticleVO.class);
        adminArticleVO.setTagIds(tagIds);
        return adminArticleVO;
    }

    /**
     * 添加分类
     *
     * @param adminArticleDTO 分类数据
     */
    @Transactional
    @Override
    public void addArticle(AdminArticleDTO adminArticleDTO) {
        // 检测是否存在同名分类
        Long count = lambdaQuery()
                .eq(Article::getCategoryId, adminArticleDTO.getCategoryId())
                .eq(Article::getTitle, adminArticleDTO.getTitle())
                .count();
        if (count > 0) {
            throw new AdminArticleException(ExceptionEnums.ADMIN_ARTICLE_EXIST);
        }

        // 保存文章
        Article article = BeanUtil.copyProperties(adminArticleDTO, Article.class);
        // 发布文章
        if (article.getStatus().equals(ArticleStatus.PUBLISH)) {
            // 检测所属分类是否禁用
            if (article.getCategoryId() != null) {
                Category category = categoryService.getById(article.getCategoryId());
                if (category.getStatus().equals(CategoryStatus.DISABLE)) {
                    throw new AdminArticleException(ExceptionEnums.ADMIN_ARTICLE_BELONG_CATEGORY_DISABLE);
                }
            }
            // 首次发布，设置发布时间
            article.setPublishTime(LocalDateTime.now());
        }

        // 保证事务正常回滚
        articleService.save(article);

        // 保存文章标签关联数据
        List<ArticleTag> articleTags = new ArrayList<>(adminArticleDTO.getTagIds().size());
        for (Long tagId : adminArticleDTO.getTagIds()) {
            ArticleTag articleTag = ArticleTag.builder()
                    .articleId(article.getId())
                    .tagId(tagId)
                    .build();
            articleTags.add(articleTag);
        }
        articleTagService.saveBatch(articleTags);

        // 标记引用文件状态为已使用，同时更新业务标记
        if (adminArticleDTO.getUploadFileRefId() != null) {
            UploadFileRef uploadFileRef = uploadFileRefService.lambdaQuery()
                    .eq(UploadFileRef::getId, adminArticleDTO.getUploadFileRefId())
                    .one();

            if (uploadFileRef == null) {
                throw new AdminArticleException(ExceptionEnums.ADMIN_ARTICLE_COVER_NOT_EXIST);
            }

            // 更新为使用
            uploadFileRef.setStatus(UploadFileRefStatus.USE);
            // 补充业务数据
            uploadFileRef.setBizType(BizTypeConstant.ARTICLE_COVER);
            uploadFileRef.setBizId(article.getId());

            uploadFileRefService.updateById(uploadFileRef);
        }
    }

    /**
     * 修改分类
     *
     * @param adminArticleDTO 分类数据
     */
    @Transactional
    @Override
    public void updateArticle(AdminArticleDTO adminArticleDTO) {
        // 检测修改后是否存在同名文章
        Long count = lambdaQuery()
                .eq(Article::getCategoryId, adminArticleDTO.getCategoryId())
                .eq(Article::getTitle, adminArticleDTO.getTitle())
                .notIn(Article::getId, adminArticleDTO.getId())
                .count();
        if (count > 0) {
            throw new AdminArticleException(ExceptionEnums.ADMIN_ARTICLE_EXIST);
        }

        Article article = BeanUtil.copyProperties(adminArticleDTO, Article.class);

        Article oldArticle = super.getById(article.getId());
        // 查看该文章是否处于发布状态
        if (oldArticle.getStatus().equals(ArticleStatus.PUBLISH)) {
            throw new AdminArticleException(ExceptionEnums.ADMIN_ARTICLE_PUBLISH_CANT_UPDATE);
        }

        // 修改文章为发布时
        if (article.getStatus().equals(ArticleStatus.PUBLISH)) {
            // 检查所属分类是否禁用
            if (article.getCategoryId() != null) {
                Category category = categoryService.getById(article.getCategoryId());
                if (category.getStatus().equals(CategoryStatus.DISABLE)) {
                    throw new AdminArticleException(ExceptionEnums.ADMIN_ARTICLE_BELONG_CATEGORY_DISABLE);
                }
            }

            // 查看修改前文章是否为首次发布，如果是则设置发布时间
            if (oldArticle.getPublishTime() == null) {
                article.setPublishTime(LocalDateTime.now());
            }
        }

        // 保证事务正常回滚
        articleService.updateById(article);

        // 标记引用文件状态为已使用，同时更新业务标记，旧文件标记为未使用
        if (adminArticleDTO.getUploadFileRefId() != null) {
            UploadFileRef uploadFileRef = uploadFileRefService.lambdaQuery()
                    .eq(UploadFileRef::getBizId, adminArticleDTO.getId())
                    .one();
            // 旧文件标记为未使用
            if (uploadFileRef != null) {
                uploadFileRef.setStatus(UploadFileRefStatus.NOT_USE);
                uploadFileRefService.updateById(uploadFileRef);
            }

            // 标记引用文件状态为已使用，同时更新业务标记
            uploadFileRef = uploadFileRefService.lambdaQuery()
                    .eq(UploadFileRef::getId, adminArticleDTO.getUploadFileRefId())
                    .one();

            if (uploadFileRef == null) {
                throw new AdminArticleException(ExceptionEnums.ADMIN_ARTICLE_COVER_NOT_EXIST);
            }

            // 更新为使用
            uploadFileRef.setStatus(UploadFileRefStatus.USE);
            // 补充业务数据
            uploadFileRef.setBizType(BizTypeConstant.ARTICLE_COVER);
            uploadFileRef.setBizId(article.getId());

            uploadFileRefService.updateById(uploadFileRef);
        }
    }

    /**
     * 修改分类状态
     *
     * @param id     文章id
     * @param status 文章状态
     */
    @Override
    public void updateStatus(Long id, Integer status) {
        List<Integer> statusList = Arrays.asList(0, 1, 2);
        if (!statusList.contains(status)) {
            throw new AdminArticleException(ExceptionEnums.ADMIN_ARTICLE_STATUS_ERROR);
        }

        Article article = super.getById(id);

        // 发布文章
        if (status.equals(ArticleStatus.PUBLISH)) {
            // 检查所属分类是否禁用
            Category category = categoryService.getById(article.getCategoryId());
            if (category != null && category.getStatus().equals(CategoryStatus.DISABLE)) {
                throw new AdminArticleException(ExceptionEnums.ADMIN_ARTICLE_BELONG_CATEGORY_DISABLE);
            }

            // 首次发布设置发布时间
            if (article.getPublishTime() == null) {
                article.setPublishTime(LocalDateTime.now());
            }
        }

        article.setStatus(status);
        updateById(article);
    }

    /**
     * 预览文章
     *
     * @param id 文章id
     * @return 文章数据
     */
    @Override
    public AdminArticlePreviewVO preview(Long id) {
        // 查询文章
        Article article = super.getById(id);
        AdminArticlePreviewVO adminArticlePreviewVO = BeanUtil.copyProperties(article, AdminArticlePreviewVO.class);
        // 标签数据补充
        List<ArticleTag> articleTags = articleTagService.lambdaQuery()
                .eq(ArticleTag::getArticleId, article.getId())
                .list();
        List<Long> tagIds = articleTags.stream().map(ArticleTag::getTagId).collect(Collectors.toList());
        List<Tag> tagList = tagService.listByIds(tagIds);
        List<AdminTagVO> adminTagVOS = BeanUtil.copyToList(tagList, AdminTagVO.class);
        adminArticlePreviewVO.setTags(adminTagVOS);

        return adminArticlePreviewVO;
    }

    /**
     * 获取7天新增文章趋势
     *
     * @return 7天文章新增趋势
     */
    @Override
    public List<TrendChartData> trend7Day() {
        List<TrendChartData> trendChartDataList = baseMapper.trend7Day();
        // 循环中获取7天数据，条件是 是否在当前时间之前，故往后添加一天，保证数据完整
        LocalDate now = LocalDate.now().plusDays(1);
        LocalDate start = now.minusDays(7);
        List<TrendChartData> allTrendChartDataList = new ArrayList<>(7);
        while (start.isBefore(now)) {
            TrendChartData chartData = new TrendChartData(0L, start);
            for (TrendChartData trendChartData : trendChartDataList) {
                if (trendChartData.getDate().equals(start)) {
                    chartData.setCount(trendChartData.getCount());
                    break;
                }
            }
            start = start.plusDays(1);

            allTrendChartDataList.add(chartData);
        }
        return allTrendChartDataList;
    }

    /**
     * 分类文章Top10
     *
     * @return 分类文章
     */
    @Override
    public List<RatioChartData> categoryArticleRatio() {
        return baseMapper.categoryArticleRatio();
    }

    /**
     * 删除文章
     *
     * @param id 文章id
     */
    @Transactional
    @Override
    public void deleteById(Long id) {
        // 删除文章标签引用
        articleTagService.lambdaUpdate()
                .eq(ArticleTag::getArticleId, id)
                .remove();

        // 删除文章封面和正文图片引用
        uploadFileRefService.lambdaUpdate()
                .in(UploadFileRef::getBizType,
                        Arrays.asList(BizTypeConstant.ARTICLE_COVER, BizTypeConstant.ARTICLE_TEXT_IMAGE))
                .eq(UploadFileRef::getBizId, id)
                .remove();

        // 保证事务正常回滚
        articleService.removeById(id);
    }

    /**
     * 批量删除文章
     *
     * @param ids 文章id集合
     */
    @Transactional
    @Override
    public void deleteByIds(List<Long> ids) {
        // 空集合不处理
        if (ids.isEmpty()) {
            return;
        }
        // 删除文章标签引用
        articleTagService.lambdaUpdate()
                .in(ArticleTag::getArticleId, ids)
                .remove();

        // 删除文章封面和正文图片引用
        uploadFileRefService.lambdaUpdate()
                .in(UploadFileRef::getBizType,
                        Arrays.asList(BizTypeConstant.ARTICLE_COVER, BizTypeConstant.ARTICLE_TEXT_IMAGE))
                .in(UploadFileRef::getBizId, ids)
                .remove();

        articleService.removeBatchByIds(ids);
    }

    /**
     * 根据待删除标签id集合统计受影响的文章数量
     *
     * @param tagId 标签id
     * @return 影响文章数量
     */
    @Override
    public Long countAffectedByTagId(Long tagId) {
        return articleTagService.lambdaQuery()
                .eq(ArticleTag::getTagId, tagId)
                .count();
    }

    /**
     * 根据待删除标签id集合统计受影响的文章数量
     *
     * @param tagIds 标签id集合
     * @return 影响文章数量
     */
    @Override
    public Long countAffectedByTagIds(List<Long> tagIds) {
        Long countAffected = 0L;
        if (CollUtil.isNotEmpty(tagIds)) {
            countAffected = articleTagService.lambdaQuery()
                    .in(ArticleTag::getTagId, tagIds)
                    .count();
        }
        return countAffected;
    }
}
