package com.my.blog.server.controller.user;

import com.my.blog.common.result.Result;
import com.my.blog.pojo.vo.user.UserTagWallVO;
import com.my.blog.server.service.user.ITagService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@Api(tags = "用户端标签接口")
@RequestMapping("/tag")
@RestController
public class UserTagController {
    @Resource(name = "user-tag-service")
    private ITagService tagService;

    @ApiOperation("获取用户标签墙数据")
    @GetMapping("/wall")
    public Result<List<UserTagWallVO>> tagWall() {
        List<UserTagWallVO> userTagWallVOS = tagService.tagWall();
        return Result.success(userTagWallVOS);
    }
}
