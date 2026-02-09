package com.my.blog.pojo.dto.admin;

import com.my.blog.server.config.valid.UpdateValidGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@ApiModel("添加/修改标签参数")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminTagDTO {
    @ApiModelProperty("标签id")
    @NotNull(message = "标签id不能为空",groups = UpdateValidGroup.class)
    private Long id;

    @ApiModelProperty("标签名称")
    @NotEmpty(message = "标签名称不能为空")
    private String name;
}
