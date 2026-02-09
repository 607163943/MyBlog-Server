package com.my.blog.server.service.admin.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.Db;
import com.my.blog.common.enums.ExceptionEnums;
import com.my.blog.common.exception.admin.AdminTagException;
import com.my.blog.common.result.PageResult;
import com.my.blog.common.utils.PageQueryUtils;
import com.my.blog.pojo.dto.admin.AdminTagDTO;
import com.my.blog.pojo.dto.admin.AdminTagPageQueryDTO;
import com.my.blog.pojo.po.ArticleTag;
import com.my.blog.pojo.po.Tag;
import com.my.blog.pojo.vo.admin.AdminTagPageQueryVO;
import com.my.blog.server.mapper.TagMapper;
import com.my.blog.server.service.admin.ITagService;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag> implements ITagService {
    @Resource
    private PageQueryUtils pageQueryUtils;

    // 本类代理
    @Lazy
    @Resource
    private ITagService tagService;

    /**
     * 分页查询标签
     *
     * @param adminTagPageQueryDTO 查询条件
     * @return 分页结果
     */
    @Override
    public PageResult<AdminTagPageQueryVO> pageQuery(AdminTagPageQueryDTO adminTagPageQueryDTO) {
        // 参数校验和初始化
        pageQueryUtils.checkAndInitPageQuery(adminTagPageQueryDTO);

        // 构建分页条件
        Page<Tag> page = new Page<>();
        page.setCurrent(adminTagPageQueryDTO.getPageNum());
        page.setSize(adminTagPageQueryDTO.getPageSize());

        // 查询
        page = lambdaQuery()
                .like(StrUtil.isNotEmpty(adminTagPageQueryDTO.getName()),
                        Tag::getName,
                        adminTagPageQueryDTO.getName())
                .page(page);

        // 构建VO数据
        List<AdminTagPageQueryVO> adminTagPageQueryVOS = BeanUtil.copyToList(page.getRecords(), AdminTagPageQueryVO.class);

        return PageResult.<AdminTagPageQueryVO>builder()
                .pageSize(page.getPages())
                .pageNum(page.getCurrent())
                .total(page.getTotal())
                .result(adminTagPageQueryVOS)
                .build();
    }

    /**
     * 添加标签
     *
     * @param adminTagDTO 标签数据
     */
    @Override
    public void addTag(AdminTagDTO adminTagDTO) {
        // 检测是否存在同名标签
        Long count = lambdaQuery().eq(Tag::getName, adminTagDTO.getName()).count();
        if (count > 0) {
            throw new AdminTagException(ExceptionEnums.ADMIN_TAG_EXIST);
        }

        Tag tag = BeanUtil.copyProperties(adminTagDTO, Tag.class);
        save(tag);
    }

    /**
     * 修改标签
     *
     * @param adminTagDTO 标签数据
     */
    @Override
    public void updateTag(AdminTagDTO adminTagDTO) {
        // 检测修改后是否存在同名标签
        Long count = lambdaQuery().eq(Tag::getName, adminTagDTO.getName())
                .notIn(Tag::getId, adminTagDTO.getId())
                .count();
        if (count > 0) {
            throw new AdminTagException(ExceptionEnums.ADMIN_TAG_EXIST);
        }

        Tag tag = BeanUtil.copyProperties(adminTagDTO, Tag.class);
        updateById(tag);
    }

    /**
     * 删除标签
     *
     * @param id 标签id
     */
    @Transactional
    @Override
    public void deleteById(Long id) {
        // 删除文章标签关联数据
        Db.lambdaUpdate(ArticleTag.class)
                .eq(ArticleTag::getTagId, id)
                .remove();

        // 删除标签数据
        tagService.removeById(id);
    }

    /**
     * 批量删除标签
     *
     * @param ids 标签id集合
     */
    @Transactional
    @Override
    public void deleteByIds(List<Long> ids) {
        // 空集合不处理
        if (ids.isEmpty()) {
            return;
        }

        // 删除文章标签关联数据
        Db.lambdaUpdate(ArticleTag.class)
                .in(ArticleTag::getTagId, ids)
                .remove();

        // 批量删除标签数据
        tagService.removeBatchByIds(ids);
    }

    /**
     * 根据文章id查询标签集合
     * @param articleId 文章id
     * @return 标签集合
     */
    @Override
    public List<AdminTagPageQueryVO> pageQueryTag(Long articleId) {
        return baseMapper.pageQueryTag(articleId);
    }
}
