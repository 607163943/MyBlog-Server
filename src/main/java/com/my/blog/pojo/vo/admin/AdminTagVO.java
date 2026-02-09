package com.my.blog.pojo.vo.admin;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@ApiModel("标签VO")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminTagVO {
    @ApiModelProperty("id")
    private Long id;
    @ApiModelProperty("标签名称")
    private String name;
    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;
    @ApiModelProperty("修改时间")
    private LocalDateTime updateTime;
}
