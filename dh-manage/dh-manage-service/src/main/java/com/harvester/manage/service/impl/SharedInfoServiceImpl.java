package com.harvester.manage.service.impl;

import com.google.common.base.Strings;
import com.harvester.generator.helper.HarvesterConfig;
import com.harvester.helper.CkanHelper;
import com.harvester.helper.FileUploadDownHelper;
import com.harvester.helper.TdsHelper;
import com.harvester.manage.mapper.PublishInfoMapper;
import com.harvester.manage.mapper.SharedInfoMapper;
import com.harvester.manage.pojo.PublishInfo;
import com.harvester.manage.pojo.SharedInfo;
import com.harvester.manage.service.SharedInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

/**
 * Created by cui on 2017/11/15.
 */
@Service("SharedInfoServiceImpl")
public class SharedInfoServiceImpl implements SharedInfoService {
    private Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private HarvesterConfig harvesterConfig;

    @Autowired
    private SharedInfoMapper sharedInfoMapper;

    @Autowired
    private PublishInfoMapper publishInfoMapper;

    @Override
    public void generateNcFile(MultipartFile file, SharedInfo sharedInfo) throws IOException, InterruptedException {
        //上传到目标路径下
        if (sharedInfo.getShareId() == null) {
            sharedInfo.setShareId(UUID.randomUUID().toString());
        }
        String path = harvesterConfig.getSharedFilePath() + File.separator + sharedInfo.getShareId();
        FileUploadDownHelper helper = new FileUploadDownHelper();
        helper.uploadFile(file, path);

        String fileName = file.getOriginalFilename();
        sharedInfo.setSharedNcFilepath(path + File.separator + fileName);
        sharedInfoMapper.insertSelective(sharedInfo);
    }

    @Override
    public void delete(List<String> lstSharedId) {
        try {
            for (String sharedId : lstSharedId) {
                List<PublishInfo> lstPublishInfo = publishInfoMapper.selectByResourceId(sharedId);
                for (PublishInfo publishInfo : lstPublishInfo) {
                    if (publishInfo.getPublishSuccessful()) {
                        //TODO　一块删除？
                        TdsHelper.deleteResource(sharedId);

                        String ckanDatasetId = publishInfo.getPublishCkanDatasetId();
                        if (!Strings.isNullOrEmpty(ckanDatasetId)) {
                            CkanHelper.deleteDataset(publishInfo.getPublishCkanDatasetId());
                        }

                        publishInfoMapper.deleteByPrimaryKey(publishInfo.getPublishId());
                    }
                }
                SharedInfo sharedInfo = sharedInfoMapper.selectByPrimaryKey(sharedId);
                try {
                    Files.delete(Paths.get(sharedInfo.getSharedNcFilepath()));
                    Files.deleteIfExists(Paths.get(sharedInfo.getSharedNcFilepath()).getParent());
                    logger.info("删除nc文件【" + sharedInfo.getSharedNcFilepath() + "】成功！");
                } catch (IOException e) {
                    logger.warn("删除nc文件【" + sharedInfo.getSharedNcFilepath() + "】失败！");
                }
                sharedInfoMapper.deleteByPrimaryKey(sharedId);
            }
        } catch (Exception e) {
            logger.error("删除数据源出错!" + e.getMessage(), e);
            throw new IllegalArgumentException(e);
        }

    }
}
