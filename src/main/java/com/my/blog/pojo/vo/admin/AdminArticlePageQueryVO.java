package com.my.blog.pojo.vo.admin;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@ApiModel("文章分页查询结果")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminArticlePageQueryVO {
    @ApiModelProperty("id")
    private Long id;
    @ApiModelProperty("标题")
    private String title;
    @ApiModelProperty("状态")
    private Integer status;
    @ApiModelProperty("分类id")
    private Long categoryId;
    @ApiModelProperty("分类名称")
    private String categoryName;
    @ApiModelProperty("标签")
    private List<AdminTagPageQueryVO> tags;
    @ApiModelProperty("修改时间")
    private LocalDateTime updateTime;
}
