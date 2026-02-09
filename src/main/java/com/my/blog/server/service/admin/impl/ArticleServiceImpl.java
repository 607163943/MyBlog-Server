package com.my.blog.server.service.admin.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
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
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements IArticleService {
    @Resource
    private PageQueryUtils pageQueryUtils;

    @Resource
    private IUploadFileRefService uploadFileRefService;

    @Resource
    private IArticleTagService articleTagService;
    @Resource
    private ICategoryService categoryService;
    @Resource
    private ITagService tagService;

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
        // 参数校验和初始化
        pageQueryUtils.checkAndInitPageQuery(articlePageQueryDTO);

        // 构建分页条件
        Page<Article> page = new Page<>();
        page.setCurrent(articlePageQueryDTO.getPageNum());
        page.setSize(articlePageQueryDTO.getPageSize());

        // 获取标签对应文章id
        List<ArticleTag> articleTags = null;
        List<Long> articleIds = null;
        // 空标签就不查
        if (articlePageQueryDTO.getTagId() != null) {
            // 判断该标签是否存在或禁用
            Tag tag = tagService.getById(articlePageQueryDTO.getTagId());
            if (tag == null || tag.getStatus().equals(TagStatus.DISABLE)) {
                throw new AdminArticleException(ExceptionEnums.ADMIN_ARTICLE_TAG_DISABLE);
            }

            articleTags = articleTagService.lambdaQuery()
                    .eq(ArticleTag::getTagId, articlePageQueryDTO.getTagId())
                    .list();

            articleIds = articleTags.stream().map(ArticleTag::getArticleId).collect(Collectors.toList());
        }

        // 查询
        page = lambdaQuery()
                .select(Article::getId, Article::getTitle,
                        Article::getStatus, Article::getCategoryId,
                        Article::getUpdateTime)
                .like(StrUtil.isNotBlank(articlePageQueryDTO.getTitle()),
                        Article::getTitle,
                        articlePageQueryDTO.getTitle())
                .eq(articlePageQueryDTO.getStatus() != null,
                        Article::getStatus,
                        articlePageQueryDTO.getStatus())
                .eq(articlePageQueryDTO.getCategoryId() != null,
                        Article::getCategoryId,
                        articlePageQueryDTO.getCategoryId())
                .in(CollUtil.isNotEmpty(articleIds),
                        Article::getId,
                        articleIds)
                .page(page);

        // 查询为空直接返回空集合
        if (page.getTotal() == 0) {
            return PageResult.<AdminArticlePageQueryVO>builder()
                    .pageSize(page.getPages())
                    .pageNum(page.getCurrent())
                    .total(page.getTotal())
                    .result(Collections.emptyList())
                    .build();
        }

        // 文章标签关联数据为空但标签查询条件不为空也返回空集合
        if (CollUtil.isEmpty(articleIds) && articlePageQueryDTO.getTagId() != null) {
            return PageResult.<AdminArticlePageQueryVO>builder()
                    .pageSize(page.getPages())
                    .pageNum(page.getCurrent())
                    .total(page.getTotal())
                    .result(Collections.emptyList())
                    .build();
        }

        // 常规查询
        // 构建VO数据
        List<Article> articleList = page.getRecords();
        List<AdminArticlePageQueryVO> articlePageQueryVOS = new ArrayList<>(articleList.size());

        // 获取查询出的文章相关分类数据
        // 获取分类id集合
        List<Long> categoryIds = articleList.stream()
                .map(Article::getCategoryId)
                .filter(categoryId -> categoryId != null)
                .collect(Collectors.toList());

        List<Category> categories = new ArrayList<>(categoryIds.size());
        if (CollUtil.isNotEmpty(categoryIds)) {
            categories = categoryService.lambdaQuery()
                    .in(CollUtil.isNotEmpty(categoryIds), Category::getId, categoryIds)
                    .list();
        }
        // 构建分类和文章id映射
        Map<Long, Category> categoryMap = categories.stream().collect(Collectors.toMap(Category::getId, category -> category));

        // 获取查询出的文章相关标签数据
        // 获取查询出的文章id集合
        List<Long> articleIdList = articleList.stream().map(Article::getId).collect(Collectors.toList());
        List<ArticleTag> articleTagList = articleTagService.lambdaQuery()
                .in(ArticleTag::getArticleId, articleIdList)
                .list();
        // 构建文章id和文章标签关联映射
        Map<Long, List<ArticleTag>> longListMap = articleTagList.stream().collect(Collectors.groupingBy(ArticleTag::getArticleId));

        // 查询文章标签关系数据中相关标签
        // 获取文章标签关联id
        List<Long> tagIds = articleTagList.stream().map(ArticleTag::getTagId).collect(Collectors.toList());
        List<Tag> tags = new ArrayList<>(tagIds.size());
        if (CollUtil.isNotEmpty(tagIds)) {
            tags = tagService
                    .lambdaQuery()
                    .in(Tag::getId, tagIds)
                    .list();
        }
        Map<Long, Tag> tagMap = tags.stream().collect(Collectors.toMap(Tag::getId, tag -> tag));

        // 补充分类和标签数据
        for (Article article : articleList) {
            AdminArticlePageQueryVO articlePageQueryVO = BeanUtil.copyProperties(article, AdminArticlePageQueryVO.class);
            // 补充分类数据
            Category category = categoryMap.get(articlePageQueryVO.getCategoryId());
            if (category != null) {
                articlePageQueryVO.setCategoryName(category.getName());
            }

            // 补充标签数据
            List<ArticleTag> tempArticleTagList = longListMap.get(article.getId());
            if (tempArticleTagList != null) {
                List<AdminTagPageQueryVO> adminTagPageQueryVOS = new ArrayList<>(tempArticleTagList.size());
                for (ArticleTag articleTag : tempArticleTagList) {
                    Tag tag = tagMap.get(articleTag.getTagId());
                    if (tag != null) {
                        AdminTagPageQueryVO adminTagPageQueryVO = BeanUtil.copyProperties(tag, AdminTagPageQueryVO.class);
                        adminTagPageQueryVOS.add(adminTagPageQueryVO);
                    }
                }
                articlePageQueryVO.setTags(adminTagPageQueryVOS);
            }

            articlePageQueryVOS.add(articlePageQueryVO);
        }

        return PageResult.<AdminArticlePageQueryVO>builder()
                .pageSize(page.getSize())
                .pageNum(page.getCurrent())
                .total(page.getTotal())
                .result(articlePageQueryVOS)
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
}
