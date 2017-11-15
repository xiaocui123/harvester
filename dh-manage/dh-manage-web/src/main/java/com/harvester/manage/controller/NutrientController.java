package com.harvester.manage.controller;

import com.github.pagehelper.PageHelper;
import com.google.common.base.Preconditions;
import com.harvester.HarvesterConstants;
import com.harvester.generator.NutrientDataGenerator;
import com.harvester.helper.FileUploadDownHelper;
import com.harvester.manage.mapper.NutrientInfoMapper;
import com.harvester.manage.pojo.NutrientInfo;
import com.harvester.manage.pojo.NutrientInfoExt;
import com.harvester.manage.service.NutrientService;
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
 * Created by cui on 2017/11/9.
 */
@Controller
@RequestMapping("nutrient")
public class NutrientController {

    @Autowired
    private NutrientInfoMapper nutrientInfoMapper;

    @Autowired
    private NutrientService nutrientService;

    @RequestMapping("init")
    public String init() {
        return "nutrient/nutrient";
    }

    @RequestMapping(value = "/generate", method = RequestMethod.POST, produces = "application/json;charset=utf8")
    @ResponseBody
    public JSONResult generate(@RequestParam("file") MultipartFile file, HttpServletRequest request) throws IOException {
        JSONResult result = new JSONResult();
        File serverFile = null;
        try {
            if (!file.isEmpty()) {
                serverFile = new FileUploadDownHelper().uploadFile(file, null);
            }

            //解析excel数据生成NC
            NutrientDataGenerator generator = new NutrientDataGenerator();
            NutrientInfo nutrientInfo = generator.generator(serverFile);

            String userId = (String) request.getSession().getAttribute(HarvesterConstants.SYSTEM_USER_ID);
            nutrientInfo.setNutrientGenerator(userId);
            nutrientInfo.setNutrientName(file.getOriginalFilename());
            nutrientInfo.setNutrientGenerateTime(new Date());
            nutrientInfoMapper.insertSelective(nutrientInfo);
        } catch (Exception e) {
            result.setSuccess(false);
            result.setMessage(e.getMessage());
        }
        return result;
    }

    @RequestMapping("query")
    @ResponseBody
    public Page<NutrientInfoExt> query(@RequestBody Page<NutrientInfoExt> page, HttpServletRequest request) {
        PageHelper.offsetPage(page.getOffset(), page.getLimit());
        List<NutrientInfoExt> lstNutrient = nutrientInfoMapper.query();
        com.github.pagehelper.Page<NutrientInfoExt> result = (com.github.pagehelper.Page<NutrientInfoExt>) lstNutrient;
        page.setTotal((int) result.getTotal());
        page.setRows(lstNutrient);
        return page;
    }

    @RequestMapping("downloadFile")
    public void downloadFile(@RequestParam(value = "nutrientId") String nutrientId,
                             HttpServletResponse response) throws UnsupportedEncodingException {
        NutrientInfo nutrientInfo = nutrientInfoMapper.selectByPrimaryKey(nutrientId);
        Preconditions.checkArgument(nutrientInfo != null);
        String absolutePath = nutrientInfo.getNutrientNcFilepath();
        File file = new File(absolutePath);
        String filename = absolutePath.substring(file.getAbsolutePath().lastIndexOf(File.separator) + 1);
        // 清空response
        response.reset();
        response.setCharacterEncoding("UTF-8");
        response.setContentType("multipart/form-data");
        response.setHeader("Content-Disposition",
                "attachment;fileName=" + new String(filename.getBytes("utf-8"), "ISO8859-1"));
        try {
            System.out.println(file.getAbsolutePath());
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
    public JSONResult delete(@RequestBody List<String> lstNutrientId) {
        JSONResult result = new JSONResult();
        try {
            nutrientService.delete(lstNutrientId);
        } catch (Exception e) {
            result.setSuccess(false);
            result.setMessage(e.getMessage());
        }
        return result;
    }

}
