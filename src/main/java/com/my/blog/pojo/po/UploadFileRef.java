package com.my.blog.pojo.po;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@TableName("tb_upload_file_ref")
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UploadFileRef {
    // 主键
    private Long id;
    // 业务类型
    private Integer bizType;
    // 业务id
    private Long bizId;
    // 文件id
    private Long uploadFileId;
    // 状态
    private Integer status;
    // 创建时间
    private LocalDateTime createTime;
    // 修改时间
    private LocalDateTime updateTime;
}
