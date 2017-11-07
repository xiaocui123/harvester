package com.harvester.manage.controller;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.harvester.manage.mapper.SysRoleInfoMapper;
import com.harvester.manage.mapper.SysUserRoleMapper;
import com.harvester.manage.pojo.SysRoleInfo;
import com.harvester.manage.pojo.SysUserRoleKey;
import com.harvester.manage.pojo.UserInfo;
import com.harvester.manage.qvo.AddUserRoleQO;
import com.harvester.manage.service.UserInfoService;
import com.harvester.vo.CommonTreeVO;
import com.harvester.vo.JSONResult;
import com.sun.istack.internal.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by cui on 2017/5/27.
 */
@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserInfoService userInfoService;

    @Autowired
    private SysRoleInfoMapper sysRoleMapper;

    @Autowired
    private SysUserRoleMapper sysUserRoleMapper;

    @RequestMapping("add")
    @ResponseBody
    public UserInfo add(@RequestBody UserInfo userInfo) {
        userInfoService.add(userInfo);
        return userInfo;
    }

    @RequestMapping("update")
    @ResponseBody
    public JSONResult update(@RequestBody UserInfo userInfo) {
        JSONResult result = new JSONResult();
        userInfoService.update(userInfo);
        return result;
    }

    @RequestMapping(value = "delete", method = RequestMethod.GET)
    @ResponseBody
    public JSONResult delete(@RequestParam String userId) {
        JSONResult result = new JSONResult();
        userInfoService.delete(userId);
        return result;
    }

    @RequestMapping(value = "queryById", method = RequestMethod.GET)
    @ResponseBody
    public UserInfo queryById(@RequestParam("userId") String userId) {
        return userInfoService.queryById(userId);
    }

    /**
     * 查询用户所属角色列表
     *
     * @param sysUserId
     * @return
     */
    @RequestMapping(value = "role", method = RequestMethod.GET)
    @ResponseBody
    public List<CommonTreeVO> queryRoles(@RequestParam("sysUserId") String sysUserId) {
        List<CommonTreeVO> result = Lists.newArrayList();
        List<SysRoleInfo> lstRoles = sysRoleMapper.queryAll();
        result.addAll(Lists.transform(lstRoles, new Function<SysRoleInfo, CommonTreeVO>() {
            @Nullable
            @Override
            public CommonTreeVO apply(@Nullable SysRoleInfo input) {
                CommonTreeVO treeVO = new CommonTreeVO();
                treeVO.setId(input.getRoleId());
                treeVO.setName(input.getRoleName());
                return treeVO;
            }
        }));
        List<SysUserRoleKey> lstHasRoles = sysUserRoleMapper.queryByStaff(sysUserId);
        List<String> hasRolesId = Lists.transform(lstHasRoles, new Function<SysUserRoleKey, String>() {
            @Nullable
            @Override
            public String apply(@Nullable SysUserRoleKey input) {
                return input.getRoleId();
            }
        });
        for (CommonTreeVO treeVO : result) {
            String roleId = treeVO.getId();
            treeVO.setChecked(hasRolesId.contains(roleId));
        }
        return result;
    }

    @RequestMapping("saveStaffRole")
    @ResponseBody
    public JSONResult saveStaffRole(@RequestBody AddUserRoleQO addStaffRoleQO){
        JSONResult result=new JSONResult();
        userInfoService.addRole(addStaffRoleQO.getStaffId(),addStaffRoleQO.getLstRoleId());
        return result;
    }
}
