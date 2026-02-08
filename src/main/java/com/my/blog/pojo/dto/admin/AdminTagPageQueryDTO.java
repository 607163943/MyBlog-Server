package com.my.blog.pojo.dto.admin;

import com.my.blog.pojo.dto.PageQueryDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@ApiModel("标签分页查询参数")
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminTagPageQueryDTO extends PageQueryDTO {
    @ApiModelProperty("标签名称")
    private String name;
    @ApiModelProperty("标签状态")
    private Integer status;
}
