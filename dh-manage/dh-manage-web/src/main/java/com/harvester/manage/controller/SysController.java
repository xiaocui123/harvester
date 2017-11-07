package com.harvester.manage.controller;

import com.harvester.HarvesterConstants;
import com.harvester.manage.pojo.UserInfo;
import com.harvester.vo.JSONResult;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by cui on 2017/5/17.
 */
@Controller
@RequestMapping("sys")
public class SysController {

    @RequestMapping("/login")
    @ResponseBody
    public JSONResult login(UserInfo userInfo, HttpServletRequest request) {
        JSONResult result = new JSONResult();
        UsernamePasswordToken token = new UsernamePasswordToken(userInfo.getUserAccount(),
                userInfo.getUserPsw());
        Subject loginUser = SecurityUtils.getSubject();
        try {
            loginUser.login(token);
            UserInfo usr = (UserInfo) loginUser.getPrincipal();
            request.getSession().setAttribute(HarvesterConstants.SYSTEM_USER, usr);
            request.getSession().setAttribute(HarvesterConstants.SYSTEM_USER_ID, usr.getUserId());
        } catch (AuthenticationException e) {
            result.setSuccess(false);
            result.setMessage("用户名或密码错误！");
        }
        return result;
    }

    @RequestMapping("gotoSys")
    public String gotoSys() {
        return "index";
    }

    @RequestMapping("/logout")
    public String logout() {
        if (SecurityUtils.getSubject().isAuthenticated()) {
            SecurityUtils.getSubject().logout();
        }
        return "redirect:/login.jsp";
    }
}
