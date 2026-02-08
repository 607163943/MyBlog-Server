package com.my.blog.pojo.vo.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@ApiModel("用户最热分类")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserHotCategoryVO {
    @ApiModelProperty("分类id")
    private Long categoryId;
    @ApiModelProperty("分类名称")
    private String categoryName;
    @ApiModelProperty("分类下属发布中的文章数量")
    private Long articleCount;
}
