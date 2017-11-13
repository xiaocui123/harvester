package com.harvester.manage.controller;

import com.github.pagehelper.PageHelper;
import com.google.common.base.Preconditions;
import com.harvester.generator.StationSerialDataGenerator;
import com.harvester.HarvesterConstants;
import com.harvester.manage.io.FileUploadDownHelper;
import com.harvester.manage.mapper.BuoyInfoMapper;
import com.harvester.manage.pojo.BuoyInfo;
import com.harvester.manage.pojo.BuoyInfoExt;
import com.harvester.manage.pojo.UserInfo;
import com.harvester.manage.service.BuoyInfoService;
import com.harvester.manage.service.PublishService;
import com.harvester.vo.JSONResult;
import com.harvester.vo.Page;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * 浮标数据
 * Created by cui on 2017/10/31.
 */
@Controller
@RequestMapping("buoy")
public class BuoyController {
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private BuoyInfoMapper buoyInfoMapper;

    @Autowired
    private BuoyInfoService buoyInfoService;

    @RequestMapping("init")
    public String init() {
        return "buoy/buoy";
    }

    @RequestMapping(value = "/generate", method = RequestMethod.POST, produces = "application/json;charset=utf8")
    @ResponseBody
    public JSONResult generate(@RequestParam("file") MultipartFile file, HttpServletRequest request) throws IOException {
        JSONResult result = new JSONResult();
        File serverFile = null;
        if (!file.isEmpty()) {
            if (!file.isEmpty()) {
                serverFile = new FileUploadDownHelper().uploadFile(file);
            }
        }

        //生成NC文件
        StationSerialDataGenerator generator = new StationSerialDataGenerator();
        generator.initTimeSerial(serverFile);
        BuoyInfo buoyInfo = generator.generate();
        if (buoyInfo != null) {
            if (buoyInfo.getBuoyNcId() == null) {
                buoyInfo.setBuoyNcId(UUID.randomUUID().toString());
            }
            buoyInfo.setBuoyGenerateTime(new Date());

            UserInfo user = (UserInfo) request.getSession().getAttribute(HarvesterConstants.SYSTEM_USER);
            buoyInfo.setBuoyGenerator(user.getUserId());

            buoyInfoMapper.insertSelective(buoyInfo);
            logger.info("浮标数据插入数据库成功！");

        } else {
            result.setSuccess(false);
            result.setMessage("浮标数据生成出错！");
        }
        return result;
    }

    @RequestMapping("query")
    @ResponseBody
    public Page<BuoyInfoExt> query(@RequestBody Page<BuoyInfoExt> page, HttpServletRequest request) {
        PageHelper.offsetPage(page.getOffset(), page.getLimit());
        List<BuoyInfoExt> lstBuoy = buoyInfoMapper.query();
        com.github.pagehelper.Page<BuoyInfoExt> result = (com.github.pagehelper.Page<BuoyInfoExt>) lstBuoy;
        page.setTotal((int) result.getTotal());
        page.setRows(lstBuoy);
        return page;
    }

    @RequestMapping("downloadFile")
    public void downloadFile(@RequestParam(value = "buoyId") String buoyId,
                             HttpServletResponse response) throws UnsupportedEncodingException {
        BuoyInfo buoyInfo = buoyInfoMapper.selectByPrimaryKey(buoyId);
        Preconditions.checkArgument(buoyInfo != null);
        String absolutePath = buoyInfo.getBuoyNcFilepath();
        File file = new File(absolutePath);
        String filename = absolutePath.substring(file.getAbsolutePath().lastIndexOf(File.separator) + 1);
        // 清空response
        response.reset();
        response.setCharacterEncoding("UTF-8");
        response.setContentType("multipart/form-data");
        response.setHeader("Content-Disposition",
                "attachment;fileName=" + new String(filename.getBytes("utf-8"), "ISO8859-1"));
        InputStream inputStream = null;
        OutputStream os = null;
        try {
            System.out.println(file.getAbsolutePath());
            inputStream = new FileInputStream(file.getAbsolutePath());
            os = response.getOutputStream();
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
        } finally {
            if (inputStream != null) {
                IOUtils.closeQuietly(inputStream);
            }
            if (os != null) {
                IOUtils.closeQuietly(os);
            }
        }
    }

    @RequestMapping(value = "delete")
    @ResponseBody
    public JSONResult delete(@RequestBody List<String> lstBuoyId) {
        JSONResult result = new JSONResult();
        try {
            buoyInfoService.delete(lstBuoyId);
        } catch (Exception e) {
            result.setSuccess(false);
            result.setMessage(e.getMessage());
        }
        return result;
    }
}
