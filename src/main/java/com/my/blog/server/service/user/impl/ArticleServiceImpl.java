package com.my.blog.server.service.user.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.my.blog.common.constants.ArticleStatus;
import com.my.blog.common.constants.CategoryStatus;
import com.my.blog.common.constants.TagStatus;
import com.my.blog.common.enums.ExceptionEnums;
import com.my.blog.common.exception.admin.AdminArticleException;
import com.my.blog.common.exception.user.UserArticleException;
import com.my.blog.common.result.PageResult;
import com.my.blog.common.utils.PageQueryUtils;
import com.my.blog.pojo.dto.user.UserArticlePageQueryDTO;
import com.my.blog.pojo.po.Article;
import com.my.blog.pojo.po.ArticleTag;
import com.my.blog.pojo.po.Category;
import com.my.blog.pojo.po.Tag;
import com.my.blog.pojo.vo.admin.AdminTagPageQueryVO;
import com.my.blog.pojo.vo.user.UserArticlePageQueryVO;
import com.my.blog.pojo.vo.user.UserArticleViewVO;
import com.my.blog.pojo.vo.user.UserTagVO;
import com.my.blog.server.mapper.ArticleMapper;
import com.my.blog.server.service.user.IArticleService;
import com.my.blog.server.service.user.IArticleTagService;
import com.my.blog.server.service.user.ICategoryService;
import com.my.blog.server.service.user.ITagService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service("user-article-service")
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements IArticleService {

    @Resource(name = "user-article-tag-service")
    private IArticleTagService articleTagService;

    @Resource(name = "user-category-service")
    private ICategoryService categoryService;

    @Resource(name = "user-tag-service")
    private ITagService tagService;

    @Resource
    private PageQueryUtils pageQueryUtils;

    /**
     * 分页查询文章
     *
     * @param userArticlePageQueryDTO 查询条件
     * @return 分页结果
     */
    @Override
    public PageResult<UserArticlePageQueryVO> pageQuery(UserArticlePageQueryDTO userArticlePageQueryDTO) {
        // 参数校验和初始化
        pageQueryUtils.checkAndInitPageQuery(userArticlePageQueryDTO);

        // 构建分页条件
        Page<Article> page = new Page<>();
        page.setCurrent(userArticlePageQueryDTO.getPageNum());
        page.setSize(userArticlePageQueryDTO.getPageSize());

        // 获取标签对应文章id
        List<ArticleTag> articleTags = null;
        List<Long> articleIds = null;
        // 空标签就不查
        if (userArticlePageQueryDTO.getTagId() != null) {
            // 判断该标签是否存在或禁用
            Tag tag = tagService.getById(userArticlePageQueryDTO.getTagId());
            if (tag == null || tag.getStatus().equals(TagStatus.DISABLE)) {
                throw new AdminArticleException(ExceptionEnums.ADMIN_ARTICLE_TAG_DISABLE);
            }

            articleTags = articleTagService.lambdaQuery()
                    .eq(ArticleTag::getTagId, userArticlePageQueryDTO.getTagId())
                    .list();

            articleIds = articleTags.stream().map(ArticleTag::getArticleId).collect(Collectors.toList());
        }

        // 查询
        page = lambdaQuery()
                .select(Article::getId, Article::getTitle, Article::getSummary,
                        Article::getStatus, Article::getCategoryId,
                        Article::getPublishTime)
                // 关键字暂时针对文章标题进行模糊匹配
                // TODO:后续需要改成标题+摘要模糊匹配，添加ES后改为ES查询
                .like(StrUtil.isNotBlank(userArticlePageQueryDTO.getKeyword()),
                        Article::getTitle,
                        userArticlePageQueryDTO.getKeyword())
                // 只查询发布文章
                .eq(Article::getStatus, ArticleStatus.PUBLISH)
                .eq(userArticlePageQueryDTO.getCategoryId() != null,
                        Article::getCategoryId,
                        userArticlePageQueryDTO.getCategoryId())
                .in(CollUtil.isNotEmpty(articleIds),
                        Article::getId,
                        articleIds)
                .page(page);

        // 查询为空直接返回空集合
        if (page.getTotal() == 0) {
            return PageResult.<UserArticlePageQueryVO>builder()
                    .pageSize(page.getPages())
                    .pageNum(page.getCurrent())
                    .total(page.getTotal())
                    .result(Collections.emptyList())
                    .build();
        }

        // 文章标签关联数据为空但标签查询条件不为空也返回空集合
        if (CollUtil.isEmpty(articleIds) && userArticlePageQueryDTO.getTagId() != null) {
            return PageResult.<UserArticlePageQueryVO>builder()
                    .pageSize(page.getPages())
                    .pageNum(page.getCurrent())
                    .total(page.getTotal())
                    .result(Collections.emptyList())
                    .build();
        }

        // 常规查询
        // 构建VO数据
        List<Article> articleList = page.getRecords();
        List<UserArticlePageQueryVO> articlePageQueryVOS = new ArrayList<>(articleList.size());

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
            UserArticlePageQueryVO articlePageQueryVO = BeanUtil.copyProperties(article, UserArticlePageQueryVO.class);
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

        return PageResult.<UserArticlePageQueryVO>builder()
                .pageSize(page.getSize())
                .pageNum(page.getCurrent())
                .total(page.getTotal())
                .result(articlePageQueryVOS)
                .build();
    }

    /**
     * 最新发布文章Top5
     *
     * @return 最新发布文章Top5
     */
    @Override
    public List<UserArticlePageQueryVO> newArticleTop5() {
        Page<Article> page = Page.of(1, 5);
        page = lambdaQuery()
                .eq(Article::getStatus, ArticleStatus.PUBLISH)
                .orderByDesc(Article::getPublishTime)
                .page(page);
        List<Article> articleList = page.getRecords();

        return BeanUtil.copyToList(articleList, UserArticlePageQueryVO.class);
    }

    /**
     * 文章详情
     *
     * @param articleId 文章id
     * @return 文章详情
     */
    @Override
    public UserArticleViewVO articleView(Long articleId) {
        // 查询文章
        Article article = getById(articleId);
        if (article == null) {
            throw new UserArticleException(ExceptionEnums.USER_ARTICLE_NOT_EXIST);
        }
        UserArticleViewVO userArticleViewVO = BeanUtil.copyProperties(article, UserArticleViewVO.class);

        // 分类补全
        if (article.getCategoryId() != null) {
            Category category = categoryService.getById(article.getCategoryId());
            if (category != null) {
                // 检测所属分类是否禁用
                if (category.getStatus().equals(CategoryStatus.DISABLE)) {
                    throw new UserArticleException(ExceptionEnums.USER_ARTICLE_BELONG_CATEGORY_DISABLE);
                }

                userArticleViewVO.setCategoryId(category.getId());
                userArticleViewVO.setCategoryName(category.getName());
            }
        }

        // 标签补全
        List<ArticleTag> articleTags = articleTagService.lambdaQuery()
                .eq(ArticleTag::getArticleId, article.getId())
                .list();
        List<Long> tagIds = articleTags.stream().map(ArticleTag::getTagId).collect(Collectors.toList());

        if (CollUtil.isNotEmpty(tagIds)) {
            List<Tag> tagList = tagService.lambdaQuery()
                    // 文章残留禁用标签时不应导致用户端对整个文章的查询报错，直接过滤即可
                    .eq(Tag::getStatus, TagStatus.ENABLE)
                    .in(Tag::getId, tagIds)
                    .list();
            List<UserTagVO> userTagVOS = BeanUtil.copyToList(tagList, UserTagVO.class);
            userArticleViewVO.setTags(userTagVOS);
        }

        return userArticleViewVO;
    }
}
