package com.my.blog.pojo.vo.admin;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@ApiModel("分类分页查询结果")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminCategoryPageQueryVO {
    @ApiModelProperty("id")
    private Long id;
    @ApiModelProperty("分类名称")
    private String name;
    @ApiModelProperty ("排序")
    private Integer sort;
    @ApiModelProperty("总文章数")
    private Long articleCount;
    @ApiModelProperty("分类状态")
    private Integer status;
}
