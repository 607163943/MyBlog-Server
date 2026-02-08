package com.my.blog.server.service.user.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.my.blog.common.result.PageResult;
import com.my.blog.common.utils.PageQueryUtils;
import com.my.blog.pojo.dto.PageQueryDTO;
import com.my.blog.pojo.po.Category;
import com.my.blog.pojo.vo.user.UserCategoryVO;
import com.my.blog.pojo.vo.user.UserHotCategoryVO;
import com.my.blog.server.mapper.CategoryMapper;
import com.my.blog.server.service.user.ICategoryService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service("user-category-service")
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements ICategoryService {
    @Resource
    private PageQueryUtils pageQueryUtils;

    /**
     * 获取最热门的5个分类
     * @return 最热门的5个分类
     */
    @Override
    public List<UserHotCategoryVO> getHotCategoryTop5() {
        return baseMapper.getHotCategoryTop5();
    }

    /**
     * 分页查询分类
     * @param pageQueryDTO 查询条件
     * @return 分页结果
     */
    @Override
    public PageResult<UserCategoryVO> pageQuery(PageQueryDTO pageQueryDTO) {
        // 检查请求参数并初始化分页参数
        pageQueryUtils.checkAndInitPageQuery(pageQueryDTO);

        // 构建分页
        Page<UserCategoryVO> page = Page.of(pageQueryDTO.getPageNum(), pageQueryDTO.getPageSize());

        // 分页查询
        page=baseMapper.pageQuery(page);

        return PageResult.<UserCategoryVO>builder()
                .result(page.getRecords())
                .total(page.getTotal())
                .build();
    }
}
