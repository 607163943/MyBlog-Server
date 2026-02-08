package com.my.blog.pojo.dto.admin;

import com.my.blog.server.config.valid.UpdateValidGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@ApiModel("后台分类数据")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminCategoryDTO {
    @ApiModelProperty("分类id")
    @NotNull(message = "分类id不能为空",groups = UpdateValidGroup.class)
    private Long id;

    @ApiModelProperty("分类名称")
    @NotEmpty(message = "分类名称不能为空")
    private String name;

    @ApiModelProperty("分类封面")
    private String cover;

    @ApiModelProperty("排序")
    @NotNull(message = "排序不能为空")
    private Integer sort;

    @ApiModelProperty("状态")
    @NotNull(message = "状态不能为空")
    private Integer status;

    @ApiModelProperty("上传封面关联id")
    private Long uploadFileRefId;
}
