package com.my.blog.server.service.admin.impl;

import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.my.blog.common.enums.ExceptionEnums;
import com.my.blog.common.exception.admin.AdminUploadException;
import com.my.blog.pojo.po.UploadFile;
import com.my.blog.pojo.po.UploadFileRef;
import com.my.blog.pojo.vo.admin.AdminUploadFileVO;
import com.my.blog.server.mapper.UploadFileMapper;
import com.my.blog.server.service.admin.IUploadFileRefService;
import com.my.blog.server.service.admin.IUploadFileService;
import org.dromara.x.file.storage.core.FileInfo;
import org.dromara.x.file.storage.core.FileStorageService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Objects;

@Service
public class UploadFileServiceImpl extends ServiceImpl<UploadFileMapper, UploadFile> implements IUploadFileService {
    @Resource
    private IUploadFileRefService uploadFileRefService;

    @Resource
    private FileStorageService fileStorageService;//注入实列

    /**
     * 上传图片
     *
     * @param multipartFile 图片文件
     * @return 图片信息
     */
    @Transactional
    @Override
    public AdminUploadFileVO uploadImageToOss(MultipartFile multipartFile) {
        // 生成文件md5值
        String md5String;
        try {
            md5String = SecureUtil.md5(multipartFile.getInputStream());
        } catch (IOException e) {
            throw new AdminUploadException(ExceptionEnums.ADMIN_UPLOAD_MD5_CREATE_ERROR);
        }

        // 文件判重
        UploadFile uploadFile = lambdaQuery().eq(UploadFile::getMd5, md5String).one();
        if (uploadFile == null) {
            // 非重复文件执行OSS上传
            FileInfo fileInfo = uploadToOss(multipartFile, md5String);

            // 将上传记录保存到数据库
            uploadFile = UploadFile.builder()
                    .originalName(multipartFile.getOriginalFilename())
                    .url(fileInfo.getUrl())
                    .path(fileInfo.getPath())
                    .objectFileName(fileInfo.getFilename())
                    .md5(md5String)
                    .build();
            save(uploadFile);
        }
        // 无论是否重复，都新添加一份上传文件的引用
        UploadFileRef uploadFileRef = UploadFileRef.builder()
                .uploadFileId(uploadFile.getId())
                .status(1)
                .build();
        uploadFileRefService.save(uploadFileRef);


        return AdminUploadFileVO.builder()
                .uploadFileRefId(uploadFileRef.getId())
                .url(uploadFile.getUrl())
                .build();
    }

    /**
     * 上传文件到OSS，目录采用年月日开头，文件名采用md5值+原扩展名
     * @param multipartFile  文件
     * @param md5String 文件md5值
     * @return 文件信息
     */
    private FileInfo uploadToOss(MultipartFile multipartFile,String md5String) {
        // 设置文件保存名和目录
        LocalDateTime localDateTime = LocalDateTime.now();
        String ext = Objects.requireNonNull(
                        multipartFile.getOriginalFilename())
                .substring(multipartFile.getOriginalFilename().lastIndexOf(".") + 1);

        return fileStorageService.of(multipartFile)
                // 年月日作为目录开头
                .setPath(localDateTime.getYear() + "/" + localDateTime.getMonthValue() + "/" + localDateTime.getDayOfMonth() + "/")
                // md5值+原扩展名作为保存文件名
                .setSaveFilename(md5String + "." + ext)
                .upload();
    }
}
