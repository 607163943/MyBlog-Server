package com.my.blog.server.service.user.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.my.blog.common.constants.ArticleStatus;
import com.my.blog.common.constants.CategoryStatus;
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
import java.util.Collections;
import java.util.List;
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
        // 创建分页对象
        Page<UserArticlePageQueryVO> page = pageQueryUtils.createPage(userArticlePageQueryDTO, UserArticlePageQueryVO.class);

        // 获取标签对应文章id
        List<ArticleTag> articleTags = null;
        List<Long> articleIds = null;
        // 空标签就不查
        if (userArticlePageQueryDTO.getTagId() != null) {
            // 判断该标签是否存在或禁用
            Tag tag = tagService.getById(userArticlePageQueryDTO.getTagId());
            if (tag == null) {
                throw new AdminArticleException(ExceptionEnums.ADMIN_ARTICLE_TAG_DISABLE);
            }

            articleTags = articleTagService.lambdaQuery()
                    .eq(ArticleTag::getTagId, userArticlePageQueryDTO.getTagId())
                    .list();

            articleIds = articleTags.stream().map(ArticleTag::getArticleId).collect(Collectors.toList());
        }

        // 文章标签关联数据为空但标签查询条件不为空也返回空集合
        if (CollUtil.isEmpty(articleIds) && userArticlePageQueryDTO.getTagId() != null) {
            return PageResult.<UserArticlePageQueryVO>builder()
                    .pageSize(page.getPages())
                    .pageNum(page.getCurrent())
                    .total(0L)
                    .result(Collections.emptyList())
                    .build();
        }

        // 查询
        page = baseMapper.userPageQuery(page, userArticlePageQueryDTO, articleIds);

        return PageResult.<UserArticlePageQueryVO>builder()
                .pageSize(page.getSize())
                .pageNum(page.getCurrent())
                .total(page.getTotal())
                .result(page.getRecords())
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
                    .in(Tag::getId, tagIds)
                    .list();
            List<UserTagVO> userTagVOS = BeanUtil.copyToList(tagList, UserTagVO.class);
            userArticleViewVO.setTags(userTagVOS);
        }

        return userArticleViewVO;
    }
}
