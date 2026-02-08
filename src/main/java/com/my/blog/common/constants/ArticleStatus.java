package com.my.blog.common.constants;

public interface ArticleStatus {
    // 草稿
    Integer DRAFT = 0;
    String DRAFT_LABEL="草稿";
    // 发布
    Integer PUBLISH = 1;
    String PUBLISH_LABEL="发布";
    // 下架
    Integer OFF_SHELF = 2;
    String OFF_SHELF_LABEL="下架";
}
