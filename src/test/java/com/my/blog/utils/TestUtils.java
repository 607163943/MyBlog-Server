package com.my.blog.utils;

import cn.hutool.core.util.IdUtil;
import com.my.blog.MyBlogApplication;
import org.dromara.x.file.storage.core.FileInfo;
import org.dromara.x.file.storage.core.FileStorageService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest(classes = MyBlogApplication.class)
public class TestUtils {

    @Resource
    private FileStorageService fileStorageService;

    @Test
    void testUUID() {
        System.out.println(IdUtil.fastSimpleUUID());
        System.out.println(IdUtil.randomUUID());
        System.out.println(IdUtil.simpleUUID());
    }

    @Test
    void testGetFileExt() {
        String file="test.png";
        String ext = file.substring(file.lastIndexOf(".")+1);
        System.out.println(ext);
    }

    @Test
    void testOssFileDelete() {
        FileInfo fileInfo = new FileInfo();
        fileInfo
                .setPlatform("aliyun-oss-1")
                .setPath("test/")
                        .setFilename("123.png");
        fileStorageService.delete(fileInfo);
    }
}
