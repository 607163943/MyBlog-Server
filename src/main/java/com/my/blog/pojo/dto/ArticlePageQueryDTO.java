package com.my.blog.pojo.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@ApiModel("文章分页查询参数")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ArticlePageQueryDTO extends PageQueryDTO {
    @ApiModelProperty("文章标题")
    private String title;
    @ApiModelProperty("文章状态")
    private Integer status;
    @ApiModelProperty("分类id")
    private Long categoryId;
    @ApiModelProperty("标签id")
    private Long tagId;
}
