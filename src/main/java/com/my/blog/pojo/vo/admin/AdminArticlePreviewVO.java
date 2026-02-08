package com.my.blog.pojo.vo.admin;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@ApiModel("后台文章VO")
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminArticlePreviewVO {
    @ApiModelProperty("文章id")
    private Long id;
    @ApiModelProperty("分类id")
    private Long categoryId;
    @ApiModelProperty("标签")
    private List<AdminTagVO> tags;
    @ApiModelProperty("文章标题")
    private String title;
    @ApiModelProperty("文章摘要")
    private String summary;
    @ApiModelProperty("文章封面")
    private String cover;
    @ApiModelProperty("文章内容")
    private String content;
    @ApiModelProperty("文章状态 0草稿 1发布 2下架")
    private Integer status;
}
