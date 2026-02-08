package com.my.blog.server.service.admin;

import com.baomidou.mybatisplus.extension.service.IService;
import com.my.blog.pojo.dto.admin.UserInfoDTO;
import com.my.blog.pojo.dto.admin.UserLoginDTO;
import com.my.blog.pojo.dto.admin.UserPasswordDTO;
import com.my.blog.pojo.po.User;
import com.my.blog.pojo.vo.admin.CaptchaVO;
import com.my.blog.pojo.vo.UserInfoVO;
import com.my.blog.pojo.vo.admin.UserLoginVO;

public interface IUserService extends IService<User> {
    /**
     * 用户登录
     * @param userLoginDTO 用户登录信息
     * @return 用户信息+令牌
     */
    UserLoginVO login(UserLoginDTO userLoginDTO);

    /**
     * 获取验证码
     * @return 验证码数据
     */
    CaptchaVO captcha();

    /**
     * 用户登出
     */
    void logout();

    /**
     * 获取用户信息
     * @return 用户信息
     */
    UserInfoVO userInfo();

    /**
     * 修改用户信息
     * @param userInfoDTO 用户信息
     * @return 修改后的用户信息
     */
    UserInfoVO updateUserInfo(UserInfoDTO userInfoDTO);

    /**
     * 修改用户密码
     * @param userPasswordDTO 用户密码
     */
    void updatePassword(UserPasswordDTO userPasswordDTO);
}
