package com.my.blog.server.schedule;

import com.my.blog.common.constants.CommonConstant;
import com.my.blog.common.constants.UploadFileRefStatus;
import com.my.blog.pojo.po.UploadFile;
import com.my.blog.pojo.po.UploadFileRef;
import com.my.blog.server.service.admin.IUploadFileRefService;
import com.my.blog.server.service.admin.IUploadFileService;
import org.dromara.x.file.storage.core.FileInfo;
import org.dromara.x.file.storage.core.FileStorageService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class OssImageClearSchedule {
    @Resource
    private IUploadFileRefService uploadFileRefService;
    @Resource
    private IUploadFileService uploadFileService;

    @Resource
    private FileStorageService fileStorageService;

    /**
     * 每小时清理一次超过24小时的文件引用
     */
    @Scheduled(cron = "* * 0/1 * * ? ")
    public void clearUploadFileRef() {
        // 删除超过24小时的未使用文件引用数据
        uploadFileRefService.lambdaUpdate()
                .eq(UploadFileRef::getStatus, UploadFileRefStatus.NOT_USE)
                // 今天往前退24小时，在这之前的都是创建超过24小时的
                .lt(UploadFileRef::getCreateTime, LocalDateTime.now().minusHours(24))
                .remove();
    }

    /**
     * 每小时清理一次超过24小时未使用的文件
     */
    @Scheduled(cron = "* * 0/1 * * ? ")
    public void clearUploadFile() {
        // 查询所有有引用的文件id
        List<UploadFileRef> uploadFileRefs = uploadFileRefService.lambdaQuery().list();
        List<Long> uploadFileIds = uploadFileRefs.stream().map(UploadFileRef::getUploadFileId).collect(Collectors.toList());
        // 查询未使用的文件
        List<UploadFile> uploadFiles = uploadFileService.lambdaQuery()
                .notIn(!uploadFileIds.isEmpty(), UploadFile::getId, uploadFileIds)
                .list();

        // 循环清理Oss存储文件
        for (UploadFile uploadFile : uploadFiles) {
            FileInfo fileInfo = new FileInfo();
            fileInfo.setPlatform(CommonConstant.OSS_Platform)
                    .setPath(uploadFile.getPath())
                    .setFilename(uploadFile.getObjectFileName());
            fileStorageService.delete(fileInfo);
        }

        // 清理数据库文件记录
        List<Long> uploadFileList=uploadFiles.stream().map(UploadFile::getId).collect(Collectors.toList());
        uploadFileService.removeBatchByIds(uploadFileList);
    }
}
