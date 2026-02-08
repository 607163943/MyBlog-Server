package com.my.blog.server.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.my.blog.pojo.po.Tag;
import com.my.blog.pojo.vo.user.UserTagWallVO;

import java.util.List;

public interface TagMapper extends BaseMapper<Tag> {
    /**
     * 用户标签墙
     * @return 用户标签墙数据
     */
    List<UserTagWallVO> tagWall();
}
