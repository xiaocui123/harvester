package com.harvester.manage.service;

import com.harvester.manage.pojo.CtdInfo;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * Created by cui on 2017/11/15.
 */
public interface CtdInfoService {

    /**
     * 根据上传文件扩展名称处理
     * 1.nc:拷贝到目标文件夹下
     * 2.cnv：需要调用seabird cnv2nc命令行
     * @param file
     * @param ctdInfo
     */
    void generateNcFile(MultipartFile file, CtdInfo ctdInfo) throws IOException, InterruptedException;

    void delete(List<String> lstCtdId);
}
