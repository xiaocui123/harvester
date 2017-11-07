package com.harvester.manage.service.impl;

import com.google.common.collect.Lists;
import com.harvester.HarvesterConstants;
import com.harvester.generator.helper.HarvesterConfig;
import com.harvester.manage.mapper.BuoyInfoMapper;
import com.harvester.manage.mapper.PublishInfoMapper;
import com.harvester.manage.pojo.BuoyInfo;
import com.harvester.manage.pojo.PublishInfo;
import com.harvester.manage.service.PublishService;
import eu.trentorise.opendata.jackan.CheckedCkanClient;
import eu.trentorise.opendata.jackan.CkanClient;
import eu.trentorise.opendata.jackan.model.CkanDataset;
import eu.trentorise.opendata.jackan.model.CkanDatasetBase;
import eu.trentorise.opendata.jackan.model.CkanResource;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
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
    private HarvesterConfig harvesterConfig;

    @Autowired
    private PublishInfoMapper publishInfoMapper;

    @Override
    public void publish(PublishInfo publishInfo) {
        try {

            String fileName = getFileName(publishInfo);
            String relativePath = getRelativePath(publishInfo.getPublishResourceType(), fileName);

            //更新tds配置xml文件
            String resourceId = publishInfo.getPublishResourceId();
            updateCatalogXml(resourceId, fileName, relativePath);

            //通知tds服务更新
            reInitTdsCatalog();

            //ckan生成数据集
            createDataset(fileName, relativePath);

            publishInfo.setPublishSuccessful(true);
            publishInfo.setPublishUrl(harvesterConfig.getTdsDapRootUrl() + relativePath + ".html");

        } catch (Exception e) {
            publishInfo.setPublishSuccessful(false);
            publishInfo.setPublishError(e.getMessage());
            logger.error("发布数据失败！ " + e.getMessage(), e);
        }
        if (publishInfo.getPublishId() == null) {
            publishInfo.setPublishId(UUID.randomUUID().toString());
        }
        publishInfoMapper.insertSelective(publishInfo);
    }

    private String getFileName(PublishInfo publishInfo) {
        String publishResourceType = publishInfo.getPublishResourceType();
        String resourceId = publishInfo.getPublishResourceId();
        //浮标
        if (HarvesterConstants.RESOURCE_BUOY_TYPE.equals(publishResourceType)) {
            BuoyInfo buoyInfo = buoyInfoMapper.selectByPrimaryKey(resourceId);
            String filePath = buoyInfo.getBuoyNcFilepath();
            return filePath.substring(filePath.lastIndexOf(File.separator) + 1);
        } else if (HarvesterConstants.RESOURCE_SHARED_TYPE.equals(publishResourceType)) {
            //TODO 共享数据
        } else if (HarvesterConstants.RESOURCE_ROUTES_TYPE.equals(publishResourceType)) {
            //TODO 船基调查
        }
        throw new IllegalArgumentException("不支持【" + publishResourceType + "】资源类型的数据发布！");
    }

    private String getRelativePath(String resourceType, String fileName) {
        String relativePath = "";
        if (HarvesterConstants.RESOURCE_BUOY_TYPE.equals(resourceType)) {
            relativePath = "test/station_buoy/" + fileName;
        } else if (HarvesterConstants.RESOURCE_SHARED_TYPE.equals(resourceType)) {
            //TODO
        } else if (HarvesterConstants.RESOURCE_ROUTES_TYPE.equals(resourceType)) {
            //TODO
        } else {
            throw new IllegalArgumentException("不支持【" + resourceType + "】资源类型的数据发布！");
        }
        return relativePath;
    }

    private void updateCatalogXml(String resourceId, String fileName, String relativePath) throws JDOMException, IOException {
        String tdsRootPath = harvesterConfig.getTdsRootPath();
        SAXBuilder builder = new SAXBuilder();
        String catalogFilePath = tdsRootPath + HarvesterConstants.TDS_CATALOG_FILE;
        Document doc = builder.build(catalogFilePath);
        Element rootEle = doc.getRootElement();
        Element newEle = new Element("dataset");//新增dataset节点
        newEle.setAttribute("name", fileName);
        newEle.setAttribute("ID", resourceId);
        newEle.setAttribute("serviceName", "dap");
        newEle.setAttribute("urlPath", relativePath);
        newEle.setAttribute("dataType", "Grid");
        rootEle.addContent(newEle);

        XMLOutputter out = new XMLOutputter();
        out.setFormat(Format.getCompactFormat().setEncoding("UTF-8"));
        FileWriter fileWriter = new FileWriter(catalogFilePath);
        try {
            out.output(doc, fileWriter); //写文件
        } finally {
            fileWriter.close();
        }
    }

    private void reInitTdsCatalog() {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        try {
            HttpGet get = new HttpGet(harvesterConfig.getTdsServerUrl());
            CloseableHttpResponse httpResponse = null;
            httpResponse = httpClient.execute(get);
            try {
                //response实体
                HttpEntity entity = httpResponse.getEntity();
                if (null != entity) {
                    logger.info("响应状态码:" + httpResponse.getStatusLine());
                    logger.info("-------------------------------------------------");
                    logger.info("响应内容:" + EntityUtils.toString(entity));
                    logger.info("-------------------------------------------------");
                }
            } finally {
                httpResponse.close();
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("通知TDS更新目录服务失败！", e);
        } finally {
            try {
                if (httpClient != null) {
                    httpClient.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void createDataset(String fileName, String relativePath) {
        try {
            if (fileName.indexOf(".") > 0) {
                fileName = fileName.substring(0, fileName.indexOf("."));
            }

            CkanClient myClient = new CheckedCkanClient(harvesterConfig.getCkanSeverUrl(), harvesterConfig.getCkanToken());
            CkanDatasetBase dataset = new CkanDatasetBase();
            dataset.setOwnerOrg(harvesterConfig.getDefaultOrganization());
            dataset.setName(fileName.toLowerCase());

            CkanResource resource = new CkanResource();
            resource.setUrl(harvesterConfig.getTdsDapRootUrl() + relativePath + ".html");
            resource.setName(fileName);
            resource.setResourceType(HarvesterConstants.DEFAULT_RESOURCE_TYPE);
            List<CkanResource> lstResource = Lists.newArrayList();
            lstResource.add(resource);

            dataset.setResources(lstResource);

            CkanDataset createdDataset = myClient.createDataset(dataset);

            logger.info("create new dataset:" + createdDataset.getId());
        } catch (Exception e) {
            throw new IllegalArgumentException("写数据集失败!", e);
        }

    }
}
