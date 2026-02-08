package com.my.blog.server.service.user.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.my.blog.pojo.po.ArticleTag;
import com.my.blog.server.mapper.ArticleTagMapper;
import com.my.blog.server.service.user.IArticleTagService;
import org.springframework.stereotype.Service;

@Service("user-article-tag-service")
public class ArticleTagServiceImpl extends ServiceImpl<ArticleTagMapper, ArticleTag> implements IArticleTagService {
}
