package com.my.blog.common.utils;

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
}
