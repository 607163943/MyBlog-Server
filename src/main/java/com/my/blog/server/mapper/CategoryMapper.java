package com.my.blog.server.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.my.blog.pojo.po.Category;
import com.my.blog.pojo.vo.user.UserCategoryVO;
import com.my.blog.pojo.vo.user.UserHotCategoryVO;

import java.util.List;

public interface CategoryMapper extends BaseMapper<Category> {
    /**
     * 获取最热门的5个分类
     * @return 最热门的5个分类
     */
    List<UserHotCategoryVO> getHotCategoryTop5();

    /**
     * 分页查询分类
     * @param page 分页参数
     * @return 分页结果
     */
    Page<UserCategoryVO> pageQuery(Page<UserCategoryVO> page);
}
