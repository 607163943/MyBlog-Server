package com.my.blog.pojo.vo.admin;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@ApiModel("文件上传信息")
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminUploadFileVO {
    @ApiModelProperty("文件地址")
    private String url;
    @ApiModelProperty("文件引用id")
    private Long uploadFileRefId;
}
