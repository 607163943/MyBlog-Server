package com.my.blog.pojo.po;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@TableName("tb_category")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Category {
    // 分类id
    private Long id;
    // 分类名
    private String name;
    // 封面图
    private String cover;
    // 排序
    private Integer sort;
    // 状态 0启用 1禁用
    private Integer status;
    // 创建时间
    private LocalDateTime createTime;
    // 修改时间
    private LocalDateTime updateTime;
}
