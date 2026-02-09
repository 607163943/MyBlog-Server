package com.my.blog.server.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.my.blog.pojo.po.Tag;
import com.my.blog.pojo.vo.admin.AdminTagPageQueryVO;
import com.my.blog.pojo.vo.user.UserTagWallVO;

import java.util.List;

public interface TagMapper extends BaseMapper<Tag> {
    /**
     * 用户标签墙
     * @return 用户标签墙数据
     */
    List<UserTagWallVO> tagWall();

    /**
     * 根据id查询文章下属标签集合
      * @param articleId 文章id
     * @return 标签集合
     */
    List<AdminTagPageQueryVO> pageQueryTag(Long articleId);
}
