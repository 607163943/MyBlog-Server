package com.my.blog.server.service.user;

import com.baomidou.mybatisplus.extension.service.IService;
import com.my.blog.pojo.po.User;
import com.my.blog.pojo.vo.user.UserUserCountChartCardVO;

public interface IUserService extends IService<User> {
    /**
     * 用户创作统计
     * @return 用户创作统计
     */
    UserUserCountChartCardVO countUserChartCard();
}
