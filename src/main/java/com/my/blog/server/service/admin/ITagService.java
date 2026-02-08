package com.my.blog.server.service.admin;

import com.baomidou.mybatisplus.extension.service.IService;
import com.my.blog.common.result.PageResult;
import com.my.blog.pojo.dto.admin.AdminTagDTO;
import com.my.blog.pojo.dto.admin.AdminTagPageQueryDTO;
import com.my.blog.pojo.po.Tag;
import com.my.blog.pojo.vo.admin.AdminTagPageQueryVO;

import java.util.List;

public interface ITagService extends IService<Tag> {
    /**
     * 分页查询标签
     * @param adminTagPageQueryDTO 查询条件
     * @return 分页结果
     */
    PageResult<AdminTagPageQueryVO> pageQuery(AdminTagPageQueryDTO adminTagPageQueryDTO);

    /**
     * 新增标签
     * @param adminTagDTO 标签数据
     */
    void addTag(AdminTagDTO adminTagDTO);

    /**
     * 修改标签
     * @param adminTagDTO 标签数据
     */
    void updateTag(AdminTagDTO adminTagDTO);

    /**
     * 修改标签状态
     * @param id 标签id
     */
    void updateStatus(Long id);

    /**
     * 删除标签
     * @param id 标签id
     */
    void deleteById(Long id);

    /**
     * 批量删除标签
     * @param ids 标签id集合
     */
    void deleteByIds(List<Long> ids);
}
