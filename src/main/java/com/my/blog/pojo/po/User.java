package com.my.blog.pojo.po;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@TableName("tb_user")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    //  id
    private Long id;
    //  用户名
    private String username;
    //  密码
    private String password;
    //  昵称
    private String nickname;
    //  头像
    private String avatar;
    //  邮箱
    private String email;
    //  状态
    private Integer status;
    //  最后登录时间
    private LocalDateTime lastLoginTime;
    //  创建时间
    private LocalDateTime createTime;
    //  更新时间
    private LocalDateTime updateTime;
}
