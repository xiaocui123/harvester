package com.harvester.manage.service;

import com.harvester.manage.pojo.SharedInfo;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * Created by cui on 2017/11/15.
 */
public interface SharedInfoService {

    void generateNcFile(MultipartFile file, SharedInfo sharedInfo) throws IOException, InterruptedException;

    void delete(List<String> lstSharedId);
}
