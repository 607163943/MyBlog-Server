package com.my.blog.pojo.po;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@TableName("tb_tag")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Tag {
    // id
    private Long id;
    // 标签名
    private String name;
    // 创建时间
    private LocalDateTime createTime;
    // 修改时间
    private LocalDateTime updateTime;
}
