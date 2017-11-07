package com.harvester.manage.controller;

import com.harvester.HarvesterConstants;
import com.harvester.manage.pojo.PublishInfo;
import com.harvester.manage.pojo.UserInfo;
import com.harvester.manage.service.PublishService;
import com.harvester.vo.JSONResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * Created by cui on 2017/11/6.
 */
@Controller
@RequestMapping("publish")
public class PublishController {
    @Autowired
    private PublishService publishService;

    @RequestMapping(value = "publish")
    @ResponseBody
    public JSONResult publish(@RequestBody PublishInfo publishInfo, HttpServletRequest request) {
        JSONResult result = new JSONResult();
        try {
            String userId = (String) request.getSession().getAttribute(HarvesterConstants.SYSTEM_USER_ID);
            publishInfo.setPublishUser(userId);
            publishInfo.setPublishTime(new Date());
            publishService.publish(publishInfo);
        } catch (Exception e) {
            result.setSuccess(false);
            result.setMessage(e.getMessage());
        }
        return result;
    }
}
