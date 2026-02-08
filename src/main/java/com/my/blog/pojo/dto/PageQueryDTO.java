package com.my.blog.pojo.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@ApiModel("分页查询参数")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageQueryDTO {
    @ApiModelProperty("当前页码，默认1")
    private Long pageNum = 1L;
    @ApiModelProperty("每页数量，默认10")
    private Long pageSize = 10L;
}
