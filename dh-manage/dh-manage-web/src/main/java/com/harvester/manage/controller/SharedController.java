package com.harvester.manage.controller;

import com.github.pagehelper.PageHelper;
import com.google.common.base.Preconditions;
import com.harvester.HarvesterConstants;
import com.harvester.manage.mapper.SharedInfoMapper;
import com.harvester.manage.pojo.SharedInfo;
import com.harvester.manage.pojo.SharedInfoExt;
import com.harvester.manage.service.SharedInfoService;
import com.harvester.vo.JSONResult;
import com.harvester.vo.Page;
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
@RequestMapping("shared")
public class SharedController {
    @Autowired
    private SharedInfoMapper sharedInfoMapper;

    @Autowired
    private SharedInfoService sharedInfoService;

    @RequestMapping("init")
    public String init() {
        return "shared/shared";
    }

    @RequestMapping(value = "/generate", method = RequestMethod.POST, produces = "application/json;charset=utf8")
    @ResponseBody
    public JSONResult generate(@RequestParam("file") MultipartFile file, HttpServletRequest request) throws IOException {
        JSONResult result = new JSONResult();
        try {
            if (!file.isEmpty()) {
                SharedInfo sharedInfo = new SharedInfo();
                sharedInfo.setSharedGenerator((String) request.getSession().getAttribute(HarvesterConstants.SYSTEM_USER_ID));
                sharedInfo.setSharedGenerateTime(new Date());
                sharedInfo.setSharedName(file.getOriginalFilename());
                sharedInfoService.generateNcFile(file, sharedInfo);
            }
        } catch (InterruptedException e) {
            result.setSuccess(false);
            result.setMessage(e.getMessage());
        }
        return result;
    }

    @RequestMapping("query")
    @ResponseBody
    public Page<SharedInfoExt> query(@RequestBody Page<SharedInfoExt> page, HttpServletRequest request) {
        PageHelper.offsetPage(page.getOffset(), page.getLimit());
        List<SharedInfoExt> lstNutrient = sharedInfoMapper.query();
        com.github.pagehelper.Page<SharedInfoExt> result = (com.github.pagehelper.Page<SharedInfoExt>) lstNutrient;
        page.setTotal((int) result.getTotal());
        page.setRows(lstNutrient);
        return page;
    }

    @RequestMapping("downloadFile")
    public void downloadFile(@RequestParam(value = "sharedId") String nutrientId,
                             HttpServletResponse response) throws UnsupportedEncodingException {
        SharedInfo sharedInfo = sharedInfoMapper.selectByPrimaryKey(nutrientId);
        Preconditions.checkArgument(sharedInfo != null);
        String absolutePath = sharedInfo.getSharedNcFilepath();
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
    public JSONResult delete(@RequestBody List<String> lstSharedId) {
        JSONResult result = new JSONResult();
        try {
            sharedInfoService.delete(lstSharedId);
        } catch (Exception e) {
            result.setSuccess(false);
            result.setMessage(e.getMessage());
        }
        return result;
    }


}
