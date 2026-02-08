package com.my.blog.server.service.user;

import com.baomidou.mybatisplus.extension.service.IService;
import com.my.blog.pojo.po.Tag;
import com.my.blog.pojo.vo.user.UserTagWallVO;

import java.util.List;

public interface ITagService extends IService<Tag> {
    /**
     * 标签墙数据
     * @return 标签墙数据
     */
    List<UserTagWallVO> tagWall();
}
