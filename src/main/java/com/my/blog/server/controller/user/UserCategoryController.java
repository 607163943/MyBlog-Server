package com.my.blog.server.controller.user;

import com.my.blog.common.result.PageResult;
import com.my.blog.common.result.Result;
import com.my.blog.pojo.dto.PageQueryDTO;
import com.my.blog.pojo.vo.user.UserCategoryVO;
import com.my.blog.pojo.vo.user.UserHotCategoryVO;
import com.my.blog.server.service.user.ICategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@Api(tags = "用户端分类接口")
@RequestMapping("/category")
@RestController
public class UserCategoryController {
    @Resource
    private ICategoryService categoryService;

    @ApiOperation("获取用户端最热分类top5")
    @GetMapping("/hot/top5")
    public Result<List<UserHotCategoryVO>> getHotCategoryTop5() {
        List<UserHotCategoryVO> userHotCategoryVOs = categoryService.getHotCategoryTop5();
        return Result.success(userHotCategoryVOs);
    }

    @ApiOperation("分页查询用户端分类")
    @GetMapping
    public Result<PageResult<UserCategoryVO>> pageQuery(PageQueryDTO pageQueryDTO) {
        PageResult<UserCategoryVO> pageResult=categoryService.pageQuery(pageQueryDTO);
        return Result.success(pageResult);
    }
}
