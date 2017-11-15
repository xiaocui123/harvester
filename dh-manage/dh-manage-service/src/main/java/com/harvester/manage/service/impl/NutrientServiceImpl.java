package com.harvester.manage.service.impl;

import com.google.common.base.Strings;
import com.harvester.helper.CkanHelper;
import com.harvester.helper.TdsHelper;
import com.harvester.manage.mapper.NutrientInfoMapper;
import com.harvester.manage.mapper.PublishInfoMapper;
import com.harvester.manage.pojo.NutrientInfo;
import com.harvester.manage.pojo.PublishInfo;
import com.harvester.manage.service.NutrientService;
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
@Service("nutrientServiceImpl")
public class NutrientServiceImpl implements NutrientService {
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private PublishInfoMapper publishInfoMapper;

    @Autowired
    private NutrientInfoMapper nutrientInfoMapper;

    @Override
    public void delete(List<String> lstNutrientId) {
        try {
            for (String nutrientId : lstNutrientId) {
                List<PublishInfo> lstPublishInfo = publishInfoMapper.selectByResourceId(nutrientId);
                for (PublishInfo publishInfo : lstPublishInfo) {
                    if (publishInfo.getPublishSuccessful()) {
                        //TODO　一块删除？
                        TdsHelper.deleteResource(nutrientId);

                        String ckanDatasetId = publishInfo.getPublishCkanDatasetId();
                        if (!Strings.isNullOrEmpty(ckanDatasetId)) {
                            CkanHelper.deleteDataset(publishInfo.getPublishCkanDatasetId());
                        }

                        publishInfoMapper.deleteByPrimaryKey(publishInfo.getPublishId());
                    }
                }
                NutrientInfo nutrient = nutrientInfoMapper.selectByPrimaryKey(nutrientId);
                try {
                    Files.delete(Paths.get(nutrient.getNutrientNcFilepath()));
                    Files.deleteIfExists(Paths.get(nutrient.getNutrientNcFilepath()).getParent());
                    logger.info("删除nc文件【" + nutrient.getNutrientNcFilepath() + "】成功！");
                } catch (IOException e) {
                    logger.warn("删除nc文件【" + nutrient.getNutrientNcFilepath() + "】失败！");
                }
                nutrientInfoMapper.deleteByPrimaryKey(nutrientId);
            }
        } catch (Exception e) {
            logger.error("删除数据源出错!" + e.getMessage(), e);
            throw new IllegalArgumentException(e);
        }
    }
}
