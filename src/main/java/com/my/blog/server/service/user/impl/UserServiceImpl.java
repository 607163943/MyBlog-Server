package com.my.blog.server.service.user.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.Db;
import com.my.blog.common.constants.ArticleStatus;
import com.my.blog.common.constants.CategoryStatus;
import com.my.blog.pojo.po.Article;
import com.my.blog.pojo.po.Category;
import com.my.blog.pojo.po.Tag;
import com.my.blog.pojo.po.User;
import com.my.blog.pojo.vo.user.UserUserCountChartCardVO;
import com.my.blog.server.mapper.UserMapper;
import com.my.blog.server.service.user.IUserService;
import org.springframework.stereotype.Service;


@Service("user-user-service")
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {
    /**
     * 统计用户创作统计
     * @return 用户创作统计
     */
    @Override
    public UserUserCountChartCardVO countUserChartCard() {
        Long articleCount = Db.lambdaQuery(Article.class)
                .eq(Article::getStatus, ArticleStatus.PUBLISH)
                .count();

        Long categoryCount = Db.lambdaQuery(Category.class)
                .eq(Category::getStatus, CategoryStatus.ENABLE)
                .count();

        Long tagCount = Db.lambdaQuery(Tag.class)
                .count();
        return UserUserCountChartCardVO.builder()
                .articleCount(articleCount)
                .categoryCount(categoryCount)
                .tagCount(tagCount)
                .build();
    }
}
