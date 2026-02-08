package com.my.blog.pojo.dto.user;

import com.my.blog.pojo.dto.PageQueryDTO;
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
public class UserArticlePageQueryDTO extends PageQueryDTO {
    @ApiModelProperty("分类id")
    private Long categoryId;
    @ApiModelProperty("标签id")
    private Long tagId;
    @ApiModelProperty("关键字")
    private String keyword;
}
