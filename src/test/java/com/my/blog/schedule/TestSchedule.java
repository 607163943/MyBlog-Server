package com.my.blog.schedule;

import com.my.blog.MyBlogApplication;
import com.my.blog.common.constants.CommonConstant;
import com.my.blog.pojo.po.UploadFile;
import com.my.blog.pojo.po.UploadFileRef;
import com.my.blog.server.service.admin.IUploadFileRefService;
import com.my.blog.server.service.admin.IUploadFileService;
import org.dromara.x.file.storage.core.FileInfo;
import org.dromara.x.file.storage.core.FileStorageService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@SpringBootTest(classes = MyBlogApplication.class)
public class TestSchedule {
    @Resource
    private FileStorageService fileStorageService;

    @Resource
    private IUploadFileRefService uploadFileRefService;
    @Resource
    private IUploadFileService uploadFileService;

    @Test
    public void testClearUploadFile() {
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
