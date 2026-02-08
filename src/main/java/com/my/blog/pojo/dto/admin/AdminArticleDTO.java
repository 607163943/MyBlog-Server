package com.my.blog.pojo.dto.admin;

import com.my.blog.server.config.valid.UpdateValidGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@ApiModel("添加/修改文章参数")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminArticleDTO {
    @ApiModelProperty("文章id")
    @NotNull(message = "文章id不能为空",groups = UpdateValidGroup.class)
    private Long id;

    @ApiModelProperty("分类id")
    private Long categoryId;

    @ApiModelProperty("标签id集合")
    private List<Long> tagIds;

    @ApiModelProperty("标题")
    @NotEmpty(message = "标题不能为空")
    private String title;

    @ApiModelProperty("摘要")
    @NotEmpty(message = "摘要不能为空")
    private String summary;

    @ApiModelProperty("封面")
    private String cover;

    @ApiModelProperty("内容")
    @NotEmpty(message = "内容不能为空")
    private String content;

    @ApiModelProperty("状态 0草稿 1发布 2下架")
    @NotNull(message = "状态不能为空")
    private Integer status;

    @ApiModelProperty("上传封面关联id")
    private Long uploadFileRefId;
}
