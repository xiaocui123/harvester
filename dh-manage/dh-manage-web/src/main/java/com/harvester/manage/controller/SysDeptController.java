package com.harvester.manage.controller;

import com.harvester.manage.pojo.DeptUserTreeVO;
import com.harvester.manage.pojo.SysDeptInfo;
import com.harvester.manage.service.SysDeptInfoService;
import com.harvester.vo.JSONResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by cui on 2017/5/27.
 */
@Controller
@RequestMapping("/department")
public class SysDeptController {
    @Autowired
    private SysDeptInfoService sysDeptInfoService;

    @RequestMapping("init")
    public String init(){
        return "sys/department";
    }

    @RequestMapping("add")
    @ResponseBody
    public SysDeptInfo add(@RequestBody SysDeptInfo sysDeptInfo) {
        sysDeptInfoService.add(sysDeptInfo);
        return sysDeptInfo;
    }

    @RequestMapping("update")
    @ResponseBody
    public JSONResult udpate(@RequestBody SysDeptInfo sysDeptInfo) {
        JSONResult result = new JSONResult();
        sysDeptInfoService.update(sysDeptInfo);
        return result;
    }

    @RequestMapping(value = "delete", method = RequestMethod.GET)
    @ResponseBody
    public JSONResult delete(@RequestParam("deptId") String deptId) {
        JSONResult result = new JSONResult();
        sysDeptInfoService.delete(deptId);
        return result;
    }

    @RequestMapping(value = "queryTree", method = RequestMethod.GET)
    @ResponseBody
    public List<DeptUserTreeVO> queryTree() {
        return sysDeptInfoService.queryDeptUser();
    }
}
