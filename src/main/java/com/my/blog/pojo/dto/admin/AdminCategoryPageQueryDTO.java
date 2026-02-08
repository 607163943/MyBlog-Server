package com.my.blog.pojo.dto.admin;

import com.my.blog.pojo.dto.PageQueryDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@ApiModel("分类分页查询参数")
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminCategoryPageQueryDTO extends PageQueryDTO {
    @ApiModelProperty("分类名称")
    private String name;
    @ApiModelProperty("分类状态")
    private Integer status;
    @ApiModelProperty("排序规则，0升序 1降序")
    private Integer isAsc;
}
