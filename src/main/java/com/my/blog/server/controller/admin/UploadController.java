package com.my.blog.server.controller.admin;

import com.my.blog.common.result.Result;
import com.my.blog.pojo.vo.admin.AdminUploadFileVO;
import com.my.blog.server.service.admin.IUploadFileService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

@Api(tags = "上传文件接口")
@RequestMapping("/admin/upload")
@RestController("admin-upload-controller")
public class UploadController {
    @Resource
    private IUploadFileService uploadFileService;

    @ApiOperation("上传图片接口")
    @PostMapping("/image")
    public Result<Object> uploadImageToOss(@RequestParam("file")MultipartFile multipartFile) {
        AdminUploadFileVO adminUploadFileVO = uploadFileService.uploadImageToOss(multipartFile);
        return Result.success(adminUploadFileVO);
    }
}
