package com.harvester.helper;

import com.google.common.base.Function;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.harvester.HarvesterConstants;
import com.harvester.generator.helper.HarvesterConfig;
import com.harvester.generator.helper.SpringContextUtil;
import com.harvester.manage.pojo.PublishInfo;
import eu.trentorise.opendata.jackan.CheckedCkanClient;
import eu.trentorise.opendata.jackan.CkanClient;
import eu.trentorise.opendata.jackan.model.CkanDataset;
import eu.trentorise.opendata.jackan.model.CkanDatasetBase;
import eu.trentorise.opendata.jackan.model.CkanResource;
import eu.trentorise.opendata.jackan.model.CkanTag;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import java.util.List;

/**
 * ckan帮助类
 * Created by cui on 2017/11/10.
 */
public class CkanHelper {

    private static Logger logger = LoggerFactory.getLogger(CkanHelper.class);

    public static String createDataset(PublishInfo publishInfo, String relativePath) {
        HarvesterConfig harvesterConfig = (HarvesterConfig) SpringContextUtil.getBean("harvesterconfig");
        try {
            String datasetName = publishInfo.getPublishDatasetName();
            if (datasetName.indexOf(".") > 0) {
                datasetName = datasetName.substring(0, datasetName.indexOf("."));
            }

            CkanClient myClient = new CheckedCkanClient(harvesterConfig.getCkanSeverUrl(), harvesterConfig.getCkanToken());
            CkanDatasetBase dataset = new CkanDatasetBase();
            dataset.setOwnerOrg(harvesterConfig.getDefaultOrganization());
            dataset.setTitle(datasetName);
            dataset.setName(publishInfo.getPublishResourceId());
            dataset.setId(publishInfo.getPublishId());
            dataset.setNotes(publishInfo.getPublishDatasetDescription());

            List<CkanResource> lstResource = Lists.newArrayList();
            CkanResource resource = new CkanResource();
            resource.setUrl(harvesterConfig.getTdsDapRootUrl() + relativePath);
            resource.setName("OPeNDAP");
            resource.setDescription("THREDDS OPeNDAP");
            resource.setResourceType(HarvesterConstants.DEFAULT_RESOURCE_TYPE);
            lstResource.add(resource);

            CkanResource fileInfoResource = new CkanResource();
            fileInfoResource.setName("File Information");
            fileInfoResource.setDescription("This URL provides a standard OPeNDAP html interface for selecting data from this dataset. Change the extension to .info for a description of the dataset.");
            fileInfoResource.setUrl(harvesterConfig.getTdsDapRootUrl() + relativePath + ".html");
            lstResource.add(fileInfoResource);

            CkanResource httpServerResource = new CkanResource();
            httpServerResource.setName("THREDDS_HTTP_Service");
            httpServerResource.setDescription("THREDDS HTTP Service");
            httpServerResource.setUrl(harvesterConfig.getTdsHttpServerRootUrl() + relativePath);
            lstResource.add(httpServerResource);

            dataset.setResources(lstResource);

            List<CkanTag> lstCkanTag = Lists.transform(publishInfo.getLstTagName(), new Function<String, CkanTag>() {
                @Nullable
                @Override
                public CkanTag apply(@Nullable String input) {
                    return new CkanTag(input);
                }
            });
            dataset.setTags(lstCkanTag);

            CkanDataset createdDataset = myClient.createDataset(dataset);

            logger.info("create new dataset:" + createdDataset.getId());
            return createdDataset.getId();

        } catch (Exception e) {
            throw new IllegalArgumentException("写数据集失败!", e);
        }

    }

    public static void deleteDataset(String datasetId) {
        HarvesterConfig harvesterConfig = (HarvesterConfig) SpringContextUtil.getBean("harvesterconfig");
        CkanClient myClient = new CheckedCkanClient(harvesterConfig.getCkanSeverUrl(), harvesterConfig.getCkanToken());
        myClient.deleteDataset(datasetId);
        logger.info("delete ckan dataset id = 【" + datasetId + "】");
    }
}
