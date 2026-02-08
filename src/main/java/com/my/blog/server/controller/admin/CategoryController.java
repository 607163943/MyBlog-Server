package com.my.blog.server.controller.admin;

import cn.hutool.core.bean.BeanUtil;
import com.my.blog.common.constants.CategoryStatus;
import com.my.blog.common.result.PageResult;
import com.my.blog.common.result.Result;
import com.my.blog.pojo.dto.admin.AdminCategoryDTO;
import com.my.blog.pojo.dto.admin.AdminCategoryPageQueryDTO;
import com.my.blog.pojo.po.Category;
import com.my.blog.pojo.vo.admin.AdminCategoryPageQueryVO;
import com.my.blog.pojo.vo.admin.AdminCategoryVO;
import com.my.blog.server.config.valid.UpdateValidGroup;
import com.my.blog.server.service.admin.ICategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.groups.Default;
import java.util.List;

@Api(tags = "分类管理")
@RestController("admin-category-controller")
@RequestMapping("/admin/category")
public class CategoryController {

    @Resource
    private ICategoryService categoryService;

    @ApiOperation("分页查询分类")
    @GetMapping
    public Result<PageResult<AdminCategoryPageQueryVO>> pageQuery(AdminCategoryPageQueryDTO adminCategoryPageQueryDTO) {
        PageResult<AdminCategoryPageQueryVO> pageResult = categoryService.pageQuery(adminCategoryPageQueryDTO);
        return Result.success(pageResult);
    }

    @ApiOperation("查询所有激活中分类")
    @GetMapping("/all/active")
    public Result<List<AdminCategoryVO>> categoryAll() {
        List<Category> dictList = categoryService.lambdaQuery()
                .eq(Category::getStatus, CategoryStatus.ENABLE)
                .list();
        List<AdminCategoryVO> adminCategoryVOS = BeanUtil.copyToList(dictList, AdminCategoryVO.class);
        return Result.success(adminCategoryVOS);
    }

    @ApiOperation("根据id查询分类")
    @GetMapping("/{id}")
    public Result<AdminCategoryVO> getById(@PathVariable Long id) {
        Category category = categoryService.getById(id);
        AdminCategoryVO adminCategoryVO = BeanUtil.copyProperties(category, AdminCategoryVO.class);
        return Result.success(adminCategoryVO);
    }

    @ApiOperation("添加分类")
    @PostMapping
    public Result<Object> addCategory(@Validated @RequestBody AdminCategoryDTO adminCategoryDTO) {
        categoryService.addCategory(adminCategoryDTO);
        return Result.success();
    }

    @ApiOperation("修改分类")
    @PutMapping
    public Result<Object> updateCategory(@Validated({Default.class, UpdateValidGroup.class}) @RequestBody AdminCategoryDTO adminCategoryDTO) {
        categoryService.updateCategory(adminCategoryDTO);
        return Result.success();
    }

    @ApiOperation("根据id修改分类状态")
    @PatchMapping("/{id}/status")
    public Result<Object> updateStatus(@PathVariable Long id) {
        categoryService.updateStatus(id);
        return Result.success();
    }

    @ApiOperation("根据id删除分类")
    @DeleteMapping("/{id}")
    public Result<Object> deleteById(@PathVariable Long id) {
        categoryService.deleteById(id);
        return Result.success();
    }

    @ApiOperation("批量删除分类")
    @DeleteMapping
    public Result<Object> deleteByIds(@RequestParam List<Long> ids) {
        categoryService.deleteByIds(ids);
        return Result.success();
    }
}
