package com.harvester.manage.service.impl;

import com.google.common.base.Strings;
import com.harvester.helper.CkanHelper;
import com.harvester.helper.TdsHelper;
import com.harvester.manage.mapper.DataSetInfoMapper;
import com.harvester.manage.mapper.PublishInfoMapper;
import com.harvester.manage.pojo.DataSetInfo;
import com.harvester.manage.pojo.PublishInfo;
import com.harvester.manage.service.DataSetService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

/**
 * Created by cui on 2017/11/25.
 */
@Service("dataSetServiceImpl")
public class DataSetServiceImpl implements DataSetService {
    private Logger logger= LoggerFactory.getLogger(getClass());

    @Autowired
    private DataSetInfoMapper dataSetInfoMapper;

    @Autowired
    private PublishInfoMapper publishInfoMapper;

    @Override
    public void delete(List<String> lstDatasetId) {
        try {
            for (String datasetId : lstDatasetId) {
                List<PublishInfo> lstPublishInfo = publishInfoMapper.selectByResourceId(datasetId);
                for (PublishInfo publishInfo : lstPublishInfo) {
                    if (publishInfo.getPublishSuccessful()) {
                        //TODO　一块删除？
                        TdsHelper.deleteResource(datasetId);

                        String ckanDatasetId = publishInfo.getPublishCkanDatasetId();
                        if (!Strings.isNullOrEmpty(ckanDatasetId)) {
                            CkanHelper.deleteDataset(publishInfo.getPublishCkanDatasetId());
                        }

                        publishInfoMapper.deleteByPrimaryKey(publishInfo.getPublishId());
                    }
                }
                DataSetInfo dataSetInfo = dataSetInfoMapper.selectByPrimaryKey(datasetId);
                try {
                    Files.delete(Paths.get(dataSetInfo.getDatasetNcFilepath()));
                    Files.deleteIfExists(Paths.get(dataSetInfo.getDatasetNcFilepath()).getParent());
                    Files.deleteIfExists(Paths.get(dataSetInfo.getDatasetConfigFilepath()));
                } catch (IOException e) {
                    logger.warn("删除nc文件【"+dataSetInfo.getDatasetNcFilepath()+"】失败");
                }
                dataSetInfoMapper.deleteByPrimaryKey(datasetId);
            }
        } catch (Exception e) {
            logger.error("删除数据源出错!" + e.getMessage(), e);
        }

    }
}
