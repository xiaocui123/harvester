package com.harvester.manage.service.impl;

import com.google.common.base.Strings;
import com.harvester.helper.CkanHelper;
import com.harvester.helper.TdsHelper;
import com.harvester.manage.mapper.BuoyInfoMapper;
import com.harvester.manage.mapper.PublishInfoMapper;
import com.harvester.manage.pojo.BuoyInfo;
import com.harvester.manage.pojo.PublishInfo;
import com.harvester.manage.service.BuoyInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

/**
 * Created by cui on 2017/11/10.
 */
@Service("buoyInfoServiceImpl")
public class BuoyInfoServiceImpl implements BuoyInfoService {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private BuoyInfoMapper buoyInfoMapper;

    @Autowired
    private PublishInfoMapper publishInfoMapper;

    @Override
    public void delete(List<String> lstBuoyId) {
        try {
            for (String buoyId : lstBuoyId) {
                List<PublishInfo> lstPublishInfo = publishInfoMapper.selectByResourceId(buoyId);
                for (PublishInfo publishInfo : lstPublishInfo) {
                    if (publishInfo.getPublishSuccessful()) {
                        //TODO　一块删除？
                        TdsHelper.deleteResource(buoyId);

                        String ckanDatasetId = publishInfo.getPublishCkanDatasetId();
                        if (!Strings.isNullOrEmpty(ckanDatasetId)) {
                            CkanHelper.deleteDataset(publishInfo.getPublishCkanDatasetId());
                        }

                        publishInfoMapper.deleteByPrimaryKey(publishInfo.getPublishId());
                    }
                }
                BuoyInfo buoyInfo = buoyInfoMapper.selectByPrimaryKey(buoyId);
                try {
                    Files.delete(Paths.get(buoyInfo.getBuoyNcFilepath()));
                    Files.deleteIfExists(Paths.get(buoyInfo.getBuoyNcFilepath()).getParent());
                } catch (IOException e) {
                    logger.warn("删除nc文件【"+buoyInfo.getBuoyNcFilepath()+"】失败");
                }
                buoyInfoMapper.deleteByPrimaryKey(buoyId);
            }
        } catch (Exception e) {
            logger.error("删除数据源出错!" + e.getMessage(), e);
        }
    }
}
