package com.my.blog.server.controller.admin;

import com.my.blog.common.result.Result;
import com.my.blog.pojo.dto.admin.UserInfoDTO;
import com.my.blog.pojo.dto.admin.UserLoginDTO;
import com.my.blog.pojo.dto.admin.UserPasswordDTO;
import com.my.blog.pojo.vo.admin.CaptchaVO;
import com.my.blog.pojo.vo.UserInfoVO;
import com.my.blog.pojo.vo.admin.UserLoginVO;
import com.my.blog.server.service.admin.IUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@Api(tags = "用户管理")
@RestController("admin-user-controller")
@RequestMapping("/admin/user")
public class UserController {
    @Resource
    private IUserService userService;

    @ApiOperation("用户登录")
    @PostMapping("/login")
    public Result<UserLoginVO> login(@Validated @RequestBody UserLoginDTO userLoginDTO) {
        UserLoginVO userLoginVO = userService.login(userLoginDTO);

        return Result.success(userLoginVO);
    }

    @ApiOperation("获取用户信息")
    @GetMapping("/info")
    public Result<UserInfoVO> userInfo() {
        UserInfoVO userInfoVO = userService.userInfo();
        return Result.success(userInfoVO);
    }

    @ApiOperation("更新用户信息")
    @PutMapping("/info")
    public Result<UserInfoVO> updateUserInfo(@Validated @RequestBody UserInfoDTO userInfoDTO) {
        UserInfoVO userInfoVO = userService.updateUserInfo(userInfoDTO);
        return Result.success(userInfoVO);
    }

    @ApiOperation("修改密码")
    @PatchMapping("/password")
    public Result<Object> updatePassword(@Validated @RequestBody UserPasswordDTO userPasswordDTO) {
        userService.updatePassword(userPasswordDTO);
        return Result.success();
    }

    @ApiOperation("获取验证码")
    @GetMapping("/captcha")
    public Result<CaptchaVO> captcha() {
        CaptchaVO captchaVO = userService.captcha();

        return Result.success(captchaVO);

    }

    @ApiOperation("用户登出")
    @PostMapping("/logout")
    public Result<Object> logout() {
        userService.logout();
        return Result.success();
    }
}
