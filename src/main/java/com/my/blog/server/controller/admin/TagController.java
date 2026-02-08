package com.my.blog.server.controller.admin;

import cn.hutool.core.bean.BeanUtil;
import com.my.blog.common.constants.TagStatus;
import com.my.blog.common.result.PageResult;
import com.my.blog.common.result.Result;
import com.my.blog.pojo.dto.admin.AdminTagDTO;
import com.my.blog.pojo.dto.admin.AdminTagPageQueryDTO;
import com.my.blog.pojo.po.Tag;
import com.my.blog.pojo.vo.admin.AdminTagPageQueryVO;
import com.my.blog.pojo.vo.admin.AdminTagVO;
import com.my.blog.server.config.valid.UpdateValidGroup;
import com.my.blog.server.service.admin.ITagService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.groups.Default;
import java.util.List;

@Api(tags = "标签管理")
@RestController("admin-tag-controller")
@RequestMapping("/admin/tag")
public class TagController {

    @Resource
    private ITagService tagService;


    @ApiOperation("分页查询标签")
    @GetMapping
    public Result<PageResult<AdminTagPageQueryVO>> pageQuery(AdminTagPageQueryDTO adminTagPageQueryDTO) {
        PageResult<AdminTagPageQueryVO> pageResult = tagService.pageQuery(adminTagPageQueryDTO);
        return Result.success(pageResult);
    }

    @ApiOperation("查询所有激活中标签")
    @GetMapping("/all/active")
    public Result<List<AdminTagVO>> tagAll() {
        List<Tag> dictList = tagService.lambdaQuery()
                .eq(Tag::getStatus, TagStatus.ENABLE)
                .list();
        List<AdminTagVO> adminTagVOS = BeanUtil.copyToList(dictList, AdminTagVO.class);
        return Result.success(adminTagVOS);
    }

    @ApiOperation("根据id查询标签")
    @GetMapping("/{id}")
    public Result<AdminTagVO> getById(@PathVariable Long id) {
        Tag tag = tagService.getById(id);
        AdminTagVO adminTagVO = BeanUtil.copyProperties(tag, AdminTagVO.class);
        return Result.success(adminTagVO);
    }

    @ApiOperation("添加标签")
    @PostMapping
    public Result<Object> addDict(@Validated @RequestBody AdminTagDTO adminTagDTO) {
        tagService.addTag(adminTagDTO);
        return Result.success();
    }

    @ApiOperation("修改标签")
    @PutMapping
    public Result<Object> updateDict(@Validated({Default.class, UpdateValidGroup.class}) @RequestBody AdminTagDTO adminTagDTO) {
        tagService.updateTag(adminTagDTO);
        return Result.success();
    }

    @ApiOperation("根据id修改标签状态")
    @PatchMapping("/{id}/status")
    public Result<Object> updateStatus(@PathVariable Long id) {
        tagService.updateStatus(id);
        return Result.success();
    }

    @ApiOperation("根据id删除标签")
    @DeleteMapping("/{id}")
    public Result<Object> deleteById(@PathVariable Long id) {
        tagService.deleteById(id);
        return Result.success();
    }

    @ApiOperation("批量删除标签")
    @DeleteMapping
    public Result<Object> deleteByIds(@RequestParam List<Long> ids) {
        tagService.deleteByIds(ids);
        return Result.success();
    }
}
