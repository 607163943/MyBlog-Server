package com.my.blog.server.service.user;

import com.baomidou.mybatisplus.extension.service.IService;
import com.my.blog.common.result.PageResult;
import com.my.blog.pojo.dto.PageQueryDTO;
import com.my.blog.pojo.po.Category;
import com.my.blog.pojo.vo.user.UserCategoryVO;
import com.my.blog.pojo.vo.user.UserHotCategoryVO;

import java.util.List;

public interface ICategoryService extends IService<Category> {
    /**
     * 获取用户最热分类top5
     * @return 用户最热分类top5
     */
    List<UserHotCategoryVO> getHotCategoryTop5();

    /**
     * 分页查询用户分类
     * @param pageQueryDTO 查询条件
     * @return 分页结果
     */
    PageResult<UserCategoryVO> pageQuery(PageQueryDTO pageQueryDTO);
}
