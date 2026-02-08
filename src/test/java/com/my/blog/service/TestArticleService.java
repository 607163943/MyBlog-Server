package com.my.blog.service;

import com.my.blog.MyBlogApplication;
import com.my.blog.pojo.po.Article;
import com.my.blog.server.service.admin.IArticleService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.List;

@SpringBootTest(classes = MyBlogApplication.class)
public class TestArticleService {
    @Resource
    private IArticleService articleService;


    @Test
    void testArticleQuery() {
        List<Article> articles = articleService.lambdaQuery().eq(Article::getCategoryId, null).list();
        System.out.println(articles);
    }
}
