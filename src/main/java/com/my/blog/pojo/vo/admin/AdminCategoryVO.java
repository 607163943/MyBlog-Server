package com.my.blog.pojo.vo.admin;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@ApiModel("后台分类VO")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminCategoryVO {
    @ApiModelProperty("分类id")
    private Long id;
    @ApiModelProperty("分类名称")
    private String name;
    @ApiModelProperty("分类封面")
    private String cover;
    @ApiModelProperty("排序")
    private Integer sort;
    @ApiModelProperty("分类状态")
    private Integer status;
}
