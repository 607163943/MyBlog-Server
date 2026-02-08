package com.my.blog.pojo.vo.admin;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@ApiModel("管理端首页数据统计卡片VO")
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminHomeDataCardVO {
    @ApiModelProperty("文章数量")
    private Long articleCount;
    @ApiModelProperty("发布文章数量")
    private Long publishArticleCount;
    @ApiModelProperty("草稿文章数量")
    private Long draftArticleCount;
    @ApiModelProperty("分类数量")
    private Long categoryCount;
    @ApiModelProperty("标签数量")
    private Long tagCount;
}
