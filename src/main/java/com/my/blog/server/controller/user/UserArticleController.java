package com.my.blog.server.controller.user;

import com.my.blog.common.result.PageResult;
import com.my.blog.common.result.Result;
import com.my.blog.pojo.dto.user.UserArticlePageQueryDTO;
import com.my.blog.pojo.vo.user.UserArticlePageQueryVO;
import com.my.blog.pojo.vo.user.UserArticleViewVO;
import com.my.blog.server.service.user.IArticleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@Api(tags = "用户文章接口")
@RequestMapping("/article")
@RestController("user-article-controller")
public class UserArticleController {
    @Resource(name = "user-article-service")
    private IArticleService articleService;

    @ApiOperation("文章分页查询接口")
    @GetMapping
    public Result<PageResult<UserArticlePageQueryVO>> pageQuery(UserArticlePageQueryDTO userArticlePageQueryDTO) {
        return Result.success(articleService.pageQuery(userArticlePageQueryDTO));
    }

    @ApiOperation("最新发布文章top5接口")
    @GetMapping("/new/top5")
    public Result<List<UserArticlePageQueryVO>> newArticleTop5() {
        List<UserArticlePageQueryVO> userArticlePageQueryVOS = articleService.newArticleTop5();
        return Result.success(userArticlePageQueryVOS);
    }

    @ApiOperation("文章详情接口")
    @GetMapping("/view/{articleId}")
    public Result<UserArticleViewVO> articleView(@PathVariable Long articleId) {
        UserArticleViewVO userArticleViewVO = articleService.articleView(articleId);
        return Result.success(userArticleViewVO);
    }
}
