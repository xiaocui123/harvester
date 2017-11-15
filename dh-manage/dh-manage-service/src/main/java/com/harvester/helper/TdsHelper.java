package com.harvester.helper;

import com.google.common.collect.Lists;
import com.harvester.HarvesterConstants;
import com.harvester.generator.helper.HarvesterConfig;
import com.harvester.generator.helper.SpringContextUtil;
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

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * tds 帮助类
 * Created by cui on 2017/11/10.
 */
public class TdsHelper {
    private static final Logger logger = LoggerFactory.getLogger(TdsHelper.class);

    public synchronized static void updateCatalogXml(String resourceId, String fileName, String relativePath) throws JDOMException, IOException {
        HarvesterConfig harvesterConfig = (HarvesterConfig) SpringContextUtil.getBean("harvesterconfig");
        String tdsRootPath = harvesterConfig.getTdsRootPath();
        SAXBuilder builder = new SAXBuilder();
        String catalogFilePath = tdsRootPath + HarvesterConstants.TDS_CATALOG_FILE;
        Document doc = builder.build(catalogFilePath);
        Element rootEle = doc.getRootElement();
        Element newEle = new Element("dataset");//新增dataset节点
        newEle.setAttribute("name", fileName);
        newEle.setAttribute("ID", resourceId);
        newEle.setAttribute("serviceName", "all");
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

        reInitTdsCatalog();
    }

    /**
     * 删除某一dataset节点
     *
     * @param resourceId
     */
    public synchronized static void deleteResource(String resourceId) throws Exception {
        HarvesterConfig harvesterConfig = (HarvesterConfig) SpringContextUtil.getBean("harvesterconfig");
        String tdsRootPath = harvesterConfig.getTdsRootPath();
        SAXBuilder builder = new SAXBuilder();
        String catalogFilePath = tdsRootPath + HarvesterConstants.TDS_CATALOG_FILE;
        Document doc = builder.build(catalogFilePath);
        Element rootEle = doc.getRootElement();
        List<Element> lstDataset = rootEle.getChildren("dataset");

        List<Element> elements = Lists.newArrayList();
        Iterator<Element> subchilditr = lstDataset.iterator();
        while (subchilditr.hasNext()) {
            Element subchild = subchilditr.next();
            if (null != subchild.getAttribute("ID") && resourceId.equals(subchild.getAttribute("ID").getValue())) {
                elements.add(subchild);
            }
        }
        for (Element element : elements) {
            element.getParent().removeContent(element);
            logger.info("delete tds dataset id = 【"+resourceId+"】");
        }

        XMLOutputter out = new XMLOutputter();
        out.setFormat(Format.getCompactFormat().setEncoding("UTF-8"));
        FileWriter fileWriter = new FileWriter(catalogFilePath);
        try {
            out.output(doc, fileWriter); //写文件
        } finally {
            fileWriter.close();
        }

        reInitTdsCatalog();
    }

    private static void reInitTdsCatalog() {
        HarvesterConfig harvesterConfig = (HarvesterConfig) SpringContextUtil.getBean("harvesterconfig");
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
}
