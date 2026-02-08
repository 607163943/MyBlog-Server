package com.my.blog.pojo.vo.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@ApiModel("用户端标签墙数据")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserTagWallVO {
    @ApiModelProperty("标签id")
    private Long tagId;
    @ApiModelProperty("标签名称")
    private String tagName;
    @ApiModelProperty("标签关联发布中文章数量")
    private Long articleCount;
}
