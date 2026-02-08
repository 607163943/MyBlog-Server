package com.my.blog.pojo.po;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@TableName("tb_article")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Article {
    // 文章id
    private Long id;
    // 分类id
    private Long categoryId;
    // 标题
    private String title;
    // 摘要
    private String summary;
    // 封面
    private String cover;
    // 内容
    private String content;
    // 状态 0草稿 1发布 2下架
    private Integer status;
    // 首次发布时间
    private LocalDateTime publishTime;
    // 创建时间
    private LocalDateTime createTime;
    // 修改时间
    private LocalDateTime updateTime;
}
