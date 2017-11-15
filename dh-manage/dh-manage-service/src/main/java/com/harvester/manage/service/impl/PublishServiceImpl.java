package com.harvester.manage.service.impl;

import com.harvester.HarvesterConstants;
import com.harvester.generator.helper.HarvesterConfig;
import com.harvester.helper.CkanHelper;
import com.harvester.helper.TdsHelper;
import com.harvester.manage.mapper.*;
import com.harvester.manage.pojo.*;
import com.harvester.manage.service.PublishService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.UUID;

/**
 * Created by cui on 2017/11/3.
 */
@Service("publishServiceImpl")
public class PublishServiceImpl implements PublishService {
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private BuoyInfoMapper buoyInfoMapper;

    @Autowired
    private SharedInfoMapper sharedInfoMapper;

    @Autowired
    private NutrientInfoMapper nutrientInfoMapper;

    @Autowired
    private CtdInfoMapper ctdInfoMapper;

    @Autowired
    private HarvesterConfig harvesterConfig;

    @Autowired
    private PublishInfoMapper publishInfoMapper;

    @Override
    public void publish(PublishInfo publishInfo) {
        try {

            String fileName = getFileName(publishInfo);
            String relativePath = getRelativePath(publishInfo, fileName);

            //更新tds配置xml文件 通知tds服务更新
            String resourceId = publishInfo.getPublishResourceId();
            TdsHelper.updateCatalogXml(resourceId, fileName, relativePath);

            //ckan生成数据集
            if (publishInfo.getPublishDatasetName() == null) {
                publishInfo.setPublishDatasetName(fileName);
            }
            String ckanId = CkanHelper.createDataset(publishInfo, relativePath);

            publishInfo.setPublishSuccessful(true);
            publishInfo.setPublishCkanDatasetId(ckanId);
            publishInfo.setPublishUrl(harvesterConfig.getTdsDapRootUrl() + relativePath + ".html");

            if (publishInfo.getPublishId() == null) {
                publishInfo.setPublishId(UUID.randomUUID().toString());
            }
            publishInfoMapper.insertSelective(publishInfo);

        } catch (Exception e) {
            publishInfo.setPublishSuccessful(false);
            publishInfo.setPublishError(e.getMessage());
            logger.error("发布数据失败！ " + e.getMessage(), e);
        }
    }

    private String getFileName(PublishInfo publishInfo) {
        String publishResourceType = publishInfo.getPublishResourceType();
        String resourceId = publishInfo.getPublishResourceId();
        if (HarvesterConstants.RESOURCE_BUOY_TYPE.equals(publishResourceType)) {
            //浮标
            BuoyInfo buoyInfo = buoyInfoMapper.selectByPrimaryKey(resourceId);
            String filePath = buoyInfo.getBuoyNcFilepath();
            return filePath.substring(filePath.lastIndexOf(File.separator) + 1);
        } else if (HarvesterConstants.RESOURCE_SHARED_TYPE.equals(publishResourceType)) {
            //共享数据
            SharedInfo sharedInfo = sharedInfoMapper.selectByPrimaryKey(resourceId);
            String filePath = sharedInfo.getSharedNcFilepath();
            return filePath.substring(filePath.lastIndexOf(File.separator) + 1);
        } else if (HarvesterConstants.RESOURCE_ROUTES_NUTRIENT_TYPE.equals(publishResourceType)) {
            //船基调查-营养盐
            NutrientInfo nutrientInfo = nutrientInfoMapper.selectByPrimaryKey(resourceId);
            String filePath = nutrientInfo.getNutrientNcFilepath();
            return filePath.substring(filePath.lastIndexOf(File.separator) + 1);
        } else if (HarvesterConstants.RESOURCE_ROUTES_CTD.equals(publishResourceType)) {
            //船基调查-CTD
            CtdInfo ctdInfo = ctdInfoMapper.selectByPrimaryKey(resourceId);
            String filePath = ctdInfo.getCtdNcFilepath();
            return filePath.substring(filePath.lastIndexOf(File.separator) + 1);
        }
        throw new IllegalArgumentException("不支持【" + publishResourceType + "】资源类型的数据发布！");
    }

    private String getRelativePath(PublishInfo publishInfo, String fileName) {
        String resourceType = publishInfo.getPublishResourceType();
        String relativePath = "";
        if (HarvesterConstants.RESOURCE_BUOY_TYPE.equals(resourceType)) {
            relativePath = "test/station_buoy/" + publishInfo.getPublishResourceId() + "/" + fileName;
        } else if (HarvesterConstants.RESOURCE_SHARED_TYPE.equals(resourceType)) {
            //共享
            relativePath = "test/shared/" + publishInfo.getPublishResourceId() + "/" + fileName;
        } else if (HarvesterConstants.RESOURCE_ROUTES_NUTRIENT_TYPE.equals(resourceType)) {
            //船基调查-营养盐
            relativePath = "test/routes/nutrients/" + publishInfo.getPublishResourceId() + "/" + fileName;
        } else if (HarvesterConstants.RESOURCE_ROUTES_CTD.equals(resourceType)) {
            //船基调查-CTD
            relativePath = "test/routes/ctd/" + publishInfo.getPublishResourceId() + "/" + fileName;
        } else {
            throw new IllegalArgumentException("不支持【" + resourceType + "】资源类型的数据发布！");
        }
        return relativePath;
    }

}
