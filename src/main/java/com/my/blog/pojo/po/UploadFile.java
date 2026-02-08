package com.my.blog.pojo.po;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@TableName("tb_upload_file")
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UploadFile {
    // 文件id
    private Long id;
    // 文件名
    private String originalName;
    // 文件存储名
    private String objectFileName;
    // 文件存储目录
    private String path;
    // 文件访问url
    private String url;
    // 文件md5
    private String md5;
    // 文件创建时间
    private LocalDateTime createTime;
}
