package com.my.blog.server.controller.admin;

import com.my.blog.common.result.Result;
import com.my.blog.pojo.vo.admin.AdminHomeChartCardVO;
import com.my.blog.pojo.vo.admin.AdminHomeDataCardVO;
import com.my.blog.server.service.admin.IHomeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@Api(tags = "后台首页")
@RestController("admin-home-controller")
@RequestMapping("/admin/home")
public class HomeController {
    @Resource
    private IHomeService homeService;

    @ApiOperation("首页数据统计卡片")
    @GetMapping("/card/data")
    public Result<AdminHomeDataCardVO> dataCardCount() {
        return Result.success(homeService.dataCardCount());
    }

    @ApiOperation("首页图表卡片")
    @GetMapping("/card/chart")
    public Result<AdminHomeChartCardVO> chartCardCount() {
        return Result.success(homeService.chartCardCount());
    }
}
