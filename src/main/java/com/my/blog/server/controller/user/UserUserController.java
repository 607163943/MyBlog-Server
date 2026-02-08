package com.my.blog.server.controller.user;

import cn.hutool.core.bean.BeanUtil;
import com.my.blog.common.enums.ExceptionEnums;
import com.my.blog.common.exception.user.UserUserException;
import com.my.blog.common.result.Result;
import com.my.blog.pojo.po.User;
import com.my.blog.pojo.vo.UserInfoVO;
import com.my.blog.pojo.vo.user.UserUserCountChartCardVO;
import com.my.blog.server.service.user.IUserService;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@Api(tags = "用户接口")
@RequestMapping("/user")
@RestController("user-user-controller")
public class UserUserController {
    @Resource(name = "user-user-service")
    private IUserService userService;

    @GetMapping("/info")
    public Result<UserInfoVO> info() {
        // 只有一个用户直接遍历
        List<User> users = userService.list();
        // 没有则说明用户数据丢失，由于是个人博客直接抛异常
        if (users.isEmpty()) {
            throw new UserUserException(ExceptionEnums.USER_DATA_EXCEPTION);
        }
        User user = users.get(0);
        UserInfoVO userInfoVO = BeanUtil.copyProperties(user, UserInfoVO.class);
        return Result.success(userInfoVO);
    }

    @GetMapping("/chart/count")
    public Result<UserUserCountChartCardVO> countUserChartCard() {
        UserUserCountChartCardVO userUserCountChartCardVO = userService.countUserChartCard();
        return Result.success(userUserCountChartCardVO);
    }
}
