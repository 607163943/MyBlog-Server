package com.my.blog.server.service.user.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.my.blog.pojo.po.Tag;
import com.my.blog.pojo.vo.user.UserTagWallVO;
import com.my.blog.server.mapper.TagMapper;
import com.my.blog.server.service.user.ITagService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("user-tag-service")
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag> implements ITagService {
    /**
     * 标签墙数据
     * @return 标签墙数据
     */
    @Override
    public List<UserTagWallVO> tagWall() {
        return baseMapper.tagWall();
    }
}
