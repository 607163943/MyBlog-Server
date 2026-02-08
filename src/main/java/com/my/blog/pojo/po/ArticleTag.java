package com.my.blog.pojo.po;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@TableName("tb_article_tag")
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ArticleTag {
    // 文章标签id
    private Long id;
    // 文章id
    private Long articleId;
    // 标签id
    private Long tagId;
    // 创建时间
    private LocalDateTime createTime;
}
