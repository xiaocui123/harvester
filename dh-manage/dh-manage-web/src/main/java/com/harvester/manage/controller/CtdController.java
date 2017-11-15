package com.harvester.manage.controller;

import com.github.pagehelper.PageHelper;
import com.google.common.base.Preconditions;
import com.harvester.HarvesterConstants;
import com.harvester.manage.mapper.CtdInfoMapper;
import com.harvester.manage.pojo.CtdInfo;
import com.harvester.manage.pojo.CtdInfoExt;
import com.harvester.manage.pojo.NutrientInfo;
import com.harvester.manage.service.CtdInfoService;
import com.harvester.vo.JSONResult;
import com.harvester.vo.Page;
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

/**
 * Created by cui on 2017/11/15.
 */
@Controller
@RequestMapping("ctd")
public class CtdController {
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private CtdInfoMapper ctdInfoMapper;

    @Autowired
    private CtdInfoService ctdInfoService;

    @RequestMapping("init")
    public String init() {
        return "ctd/ctd";
    }

    @RequestMapping(value = "/generate", method = RequestMethod.POST, produces = "application/json;charset=utf8")
    @ResponseBody
    public JSONResult generate(@RequestParam("file") MultipartFile file, HttpServletRequest request) throws IOException {
        JSONResult result = new JSONResult();
        try {
            if (!file.isEmpty()) {
                CtdInfo ctdInfo = new CtdInfo();
                ctdInfo.setCtdGenerator((String) request.getSession().getAttribute(HarvesterConstants.SYSTEM_USER_ID));
                ctdInfo.setCtdGenerateTime(new Date());
                ctdInfo.setCtdName(file.getOriginalFilename());
                ctdInfoService.generateNcFile(file, ctdInfo);
            }
        } catch (InterruptedException e) {
            result.setSuccess(false);
            result.setMessage(e.getMessage());
        }
        return result;
    }

    @RequestMapping("query")
    @ResponseBody
    public Page<CtdInfoExt> query(@RequestBody Page<CtdInfoExt> page, HttpServletRequest request) {
        PageHelper.offsetPage(page.getOffset(), page.getLimit());
        List<CtdInfoExt> lstCtd = ctdInfoMapper.query();
        com.github.pagehelper.Page<CtdInfoExt> result = (com.github.pagehelper.Page<CtdInfoExt>) lstCtd;
        page.setTotal((int) result.getTotal());
        page.setRows(lstCtd);
        return page;
    }

    @RequestMapping("downloadFile")
    public void downloadFile(@RequestParam(value = "ctdId") String ctdId,
                             HttpServletResponse response) throws UnsupportedEncodingException {
        CtdInfo ctdInfo = ctdInfoMapper.selectByPrimaryKey(ctdId);
        Preconditions.checkArgument(ctdInfo != null);
        String absolutePath = ctdInfo.getCtdNcFilepath();
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
    public JSONResult delete(@RequestBody List<String> lstCtdId) {
        JSONResult result = new JSONResult();
        try {
            ctdInfoService.delete(lstCtdId);
        } catch (Exception e) {
            result.setSuccess(false);
            result.setMessage(e.getMessage());
        }
        return result;
    }


}
