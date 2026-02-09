package com.my.blog.server.controller.admin;

import cn.hutool.core.bean.BeanUtil;
import com.my.blog.common.result.PageResult;
import com.my.blog.common.result.Result;
import com.my.blog.pojo.dto.ArticlePageQueryDTO;
import com.my.blog.pojo.dto.admin.AdminArticleDTO;
import com.my.blog.pojo.po.Article;
import com.my.blog.pojo.vo.admin.AdminArticlePageQueryVO;
import com.my.blog.pojo.vo.admin.AdminArticlePreviewVO;
import com.my.blog.pojo.vo.admin.AdminArticleVO;
import com.my.blog.server.config.valid.UpdateValidGroup;
import com.my.blog.server.service.admin.IArticleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.groups.Default;
import java.util.List;

@Api(tags = "文章管理")
@RestController("admin-article-controller")
@RequestMapping("/admin/article")
public class ArticleController {
    @Resource
    private IArticleService articleService;

    @ApiOperation("分页查询文章")
    @GetMapping
    public Result<PageResult<AdminArticlePageQueryVO>> pageQuery(ArticlePageQueryDTO articlePageQueryDTO) {
        PageResult<AdminArticlePageQueryVO> pageResult = articleService.pageQuery(articlePageQueryDTO);
        return Result.success(pageResult);
    }

    @ApiOperation("查询所有文章")
    @GetMapping("/all")
    public Result<List<AdminArticleVO>> categoryAll() {
        List<Article> articleList = articleService.list();
        List<AdminArticleVO> adminCategoryVOS = BeanUtil.copyToList(articleList, AdminArticleVO.class);
        return Result.success(adminCategoryVOS);
    }

    @ApiOperation("根据待删除标签id统计受影响的文章数量")
    @GetMapping("/count-affected/{tagId}")
    public Result<Long> countAffectedByTagId(@PathVariable Long tagId) {
        Long countAffected = articleService.countAffectedByTagId(tagId);
        return Result.success(countAffected);
    }

    @ApiOperation("根据待删除标签id集合统计受影响的文章数量")
    @GetMapping("/count-affected")
    public Result<Long> countAffectedByTagIds(@RequestParam List<Long> tagIds) {
        Long countAffected = articleService.countAffectedByTagIds(tagIds);
        return Result.success(countAffected);
    }

    @ApiOperation("根据id预览文章")
    @GetMapping("/preview/{id}")
    public Result<AdminArticlePreviewVO> preview(@PathVariable Long id) {
        AdminArticlePreviewVO adminArticlePreviewVO = articleService.preview(id);
        return Result.success(adminArticlePreviewVO);
    }

    @ApiOperation("添加文章")
    @PostMapping
    public Result<Object> addArticle(@Validated @RequestBody AdminArticleDTO adminArticleDTO) {
        articleService.addArticle(adminArticleDTO);
        return Result.success();
    }

    @ApiOperation("修改文章")
    @PutMapping
    public Result<Object> updateArticle(@Validated({Default.class, UpdateValidGroup.class}) @RequestBody AdminArticleDTO adminArticleDTO) {
        articleService.updateArticle(adminArticleDTO);
        return Result.success();
    }

    @ApiOperation("根据id修改文章状态")
    @PatchMapping("/{id}/status/{status}")
    public Result<Object> updateStatus(@PathVariable Long id, @PathVariable Integer status) {
        articleService.updateStatus(id, status);
        return Result.success();
    }

    @ApiOperation("根据id查询文章")
    @GetMapping("/{id}")
    public Result<AdminArticleVO> getById(@PathVariable Long id) {
        AdminArticleVO adminArticleVO = articleService.getById(id);
        return Result.success(adminArticleVO);
    }

    @ApiOperation("根据id删除文章")
    @DeleteMapping("/{id}")
    public Result<Object> deleteById(@PathVariable Long id) {
        articleService.deleteById(id);
        return Result.success();
    }

    @ApiOperation("批量删除文章")
    @DeleteMapping
    public Result<Object> deleteByIds(@RequestParam List<Long> ids) {
        articleService.deleteByIds(ids);
        return Result.success();
    }
}
