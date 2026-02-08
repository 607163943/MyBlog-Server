package com.my.blog.server.service.admin.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.my.blog.pojo.po.UploadFileRef;
import com.my.blog.server.mapper.UploadFileRefMapper;
import com.my.blog.server.service.admin.IUploadFileRefService;
import org.springframework.stereotype.Service;

@Service
public class UploadFileRefServiceImpl extends ServiceImpl<UploadFileRefMapper, UploadFileRef> implements IUploadFileRefService {
}
