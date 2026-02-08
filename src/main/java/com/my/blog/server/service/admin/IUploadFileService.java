package com.my.blog.server.service.admin;

import com.baomidou.mybatisplus.extension.service.IService;
import com.my.blog.pojo.po.UploadFile;
import com.my.blog.pojo.vo.admin.AdminUploadFileVO;
import org.springframework.web.multipart.MultipartFile;

public interface IUploadFileService extends IService<UploadFile> {
    /**
     * 上传图片
     * @param multipartFile 图片文件
     * @return 图片信息
     */
    AdminUploadFileVO uploadImageToOss(MultipartFile multipartFile);
}
