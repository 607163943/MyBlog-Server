package com.my.blog.common.utils;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.my.blog.pojo.dto.PageQueryDTO;
import org.springframework.stereotype.Component;

@Component
public class PageQueryUtils {
    /**
     * 校验并初始化分页参数，若参数非法则采用默认分页参数pageNum:1，pageSize:10
     * @param pageQueryDTO 分页参数
     */
    public void checkAndInitPageQuery(PageQueryDTO pageQueryDTO) {
        Long pageNum = pageQueryDTO.getPageNum();
        if(pageNum==null||pageNum<1) {
            pageQueryDTO.setPageNum(1L);
        }

        Long pageSize = pageQueryDTO.getPageSize();
        if(pageSize==null||pageSize<1) {
            pageQueryDTO.setPageSize(10L);
        }
    }

    public <T> Page<T> createPage(PageQueryDTO pageQueryDTO,Class<T> classz) {
        // 参数校验和初始化
        checkAndInitPageQuery(pageQueryDTO);
        // 构建分页条件
        Page<T> page = new Page<>();
        page.setCurrent(pageQueryDTO.getPageNum());
        page.setSize(pageQueryDTO.getPageSize());

        return page;
    }
}
