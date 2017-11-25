package com.harvester.manage.controller;

import com.github.pagehelper.PageHelper;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.harvester.HarvesterConstants;
import com.harvester.manage.mapper.DataSetInfoMapper;
import com.harvester.manage.pojo.BuoyInfoExt;
import com.harvester.manage.pojo.DataSetInfo;
import com.harvester.manage.pojo.DataSetInfoExt;
import com.harvester.manage.pojo.NutrientInfo;
import com.harvester.manage.service.DataSetService;
import com.harvester.v2.config.DataSet;
import com.harvester.v2.generator.AbstractGenerator;
import com.harvester.v2.generator.ExcelGenerator;
import com.harvester.v2.generator.JdbcGenerator;
import com.harvester.vo.JSONResult;
import com.harvester.vo.Page;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;

/**
 * Created by cui on 2017/11/23.
 */
@Controller
@RequestMapping("dataset")
public class DataSetController {
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private DataSetInfoMapper dataSetInfoMapper;

    @Autowired
    private DataSetService dataSetService;

    @RequestMapping("init")
    public String init(){
        return "dataset/dataset";
    }

    @RequestMapping("create")
    @ResponseBody
    public JSONResult createDataSet(@RequestBody DataSet dataSet, HttpServletRequest request) {
        JSONResult result = new JSONResult();
        DataSetInfo newDataSet = null;
        try {
            AbstractGenerator generator;
            DataSet.Source source = dataSet.getSource();
            if (source.getJdbcType() != null) {
                generator = new JdbcGenerator(dataSet);
            } else if (source.getExel() != null) {
                generator = new ExcelGenerator(dataSet);
            } else {
                throw new IllegalArgumentException("配置没有数据源信息！");
            }
            newDataSet = generator.generate();
            newDataSet.setDatasetGenerator((String) request.getSession().getAttribute(HarvesterConstants.SYSTEM_USER_ID));
            newDataSet.setDatasetGenerateTime(new Date());

            //写配置信息到XML文件
            String rootPath = System.getProperty("catalina.home");
            File dir = new File(rootPath + File.separator + "dataset-config");
            if (!dir.exists()) {
                dir.mkdir();
            }

            //write to xml file
            File file = new File(dir, newDataSet.getDatasetId() + ".xml");
            JAXBContext jaxbContext = JAXBContext.newInstance(DataSet.class);
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
            // output pretty printed
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            jaxbMarshaller.marshal(dataSet, file);

            newDataSet.setDatasetConfigFilepath(file.getAbsolutePath());

            //入库
            dataSetInfoMapper.insertSelective(newDataSet);
        } catch (Exception e) {
            logger.error("创建数据集失败！" + e.getMessage(), e);
            if (newDataSet != null) {
                String ncFilePath = newDataSet.getDatasetNcFilepath();
                if (!Strings.isNullOrEmpty(ncFilePath)) {
                    try {
                        Files.deleteIfExists(Paths.get(ncFilePath));
                        Files.delete(Paths.get(ncFilePath).getParent());
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }

                String configFilePath = newDataSet.getDatasetConfigFilepath();
                if (!Strings.isNullOrEmpty(configFilePath)) {
                    try {
                        Files.deleteIfExists(Paths.get(configFilePath));
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
            }
            result.setSuccess(false);
            result.setMessage(e.getMessage());
        }

        return result;
    }

    @RequestMapping("query")
    @ResponseBody
    public Page<DataSetInfoExt> query(@RequestBody Page<DataSetInfoExt> page, HttpServletRequest request) {
        PageHelper.offsetPage(page.getOffset(), page.getLimit());
        List<DataSetInfoExt> lstDataSet = dataSetInfoMapper.query();
        com.github.pagehelper.Page<DataSetInfoExt> result = (com.github.pagehelper.Page<DataSetInfoExt>) lstDataSet;
        page.setTotal((int) result.getTotal());
        page.setRows(lstDataSet);
        return page;
    }

    @RequestMapping("downloadFile")
    public void downloadFile(@RequestParam(value = "datasetId") String datasetId,
                             HttpServletResponse response) throws UnsupportedEncodingException {
        DataSetInfo datase = dataSetInfoMapper.selectByPrimaryKey(datasetId);
        Preconditions.checkArgument(datase != null);
        String absolutePath = datase.getDatasetNcFilepath();
        File file = new File(absolutePath);
        String filename = absolutePath.substring(file.getAbsolutePath().lastIndexOf(File.separator) + 1);
        // 清空response
        response.reset();
        response.setCharacterEncoding("UTF-8");
        response.setContentType("multipart/form-data");
        response.setHeader("Content-Disposition",
                "attachment;fileName=" + new String(filename.getBytes("utf-8"), "ISO8859-1"));
        try {
            InputStream inputStream = new FileInputStream(file.getAbsolutePath());
            OutputStream os = response.getOutputStream();
            byte[] b = new byte[4 * 1024];
            int length;
            while ((length = inputStream.read(b)) > 0) {
                os.write(b, 0, length);
            }
            inputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @RequestMapping("delete")
    @ResponseBody
    public JSONResult delete(@RequestBody List<String> lstDatasetId) {
        JSONResult result = new JSONResult();
        try {
            dataSetService.delete(lstDatasetId);
        } catch (Exception e) {
            result.setSuccess(false);
            result.setMessage(e.getMessage());
        }
        return result;
    }
}

