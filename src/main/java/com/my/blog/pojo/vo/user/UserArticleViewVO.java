package com.my.blog.pojo.vo.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@ApiModel("用户端文章VO")
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserArticleViewVO {
    @ApiModelProperty("文章id")
    private Long id;
    @ApiModelProperty("分类id")
    private Long categoryId;
    @ApiModelProperty("分类名称")
    private String categoryName;
    @ApiModelProperty("标签")
    private List<UserTagVO> tags;
    @ApiModelProperty("文章标题")
    private String title;
    @ApiModelProperty("文章内容")
    private String content;
    @ApiModelProperty("发布时间")
    private LocalDateTime publishTime;
}
