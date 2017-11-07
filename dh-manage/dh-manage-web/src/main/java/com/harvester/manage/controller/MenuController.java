package com.harvester.manage.controller;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.harvester.HarvesterConstants;
import com.harvester.manage.mapper.SysMenuInfoMapper;
import com.harvester.manage.pojo.SysMenuInfo;
import com.harvester.manage.pojo.SysRolePermission;
import com.harvester.manage.qvo.MenuVO;
import com.harvester.manage.service.SysRoleService;
import com.harvester.vo.CommonTreeVO;
import com.sun.istack.internal.Nullable;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Created by cui on 2017/4/25.
 */
@Controller
@RequestMapping("/menu")
public class MenuController {

    @Autowired
    SysMenuInfoMapper menuInfoMapper;

    @Autowired
    private SysRoleService sysRoleService;

    @RequestMapping("/getMenuMenu")
    @ResponseBody
    public List<MenuVO> getMenu(HttpServletRequest request, HttpServletResponse response) {
        List<MenuVO> lstMenu = Lists.newArrayList();
        String userId = (String) request.getSession().getAttribute(HarvesterConstants.SYSTEM_USER_ID);
        List<SysMenuInfo> lstSysMenu = menuInfoMapper.queryByUser(userId);

        for (SysMenuInfo sysMenu : lstSysMenu) {
            if (StringUtils.isEmpty(sysMenu.getParentId())) {
                lstMenu.add(getMenuVO(sysMenu));
            }
        }

        for (SysMenuInfo sysMenu : lstSysMenu) {
            if (StringUtils.isNotEmpty(sysMenu.getParentId())) {
                MenuVO menuVO = findMenuVO(lstMenu, sysMenu.getParentId());
                menuVO.getMenus().add(getMenuVO(sysMenu));
            }
        }
        return lstMenu;
    }

    private MenuVO findMenuVO(List<MenuVO> lstMenu, String parentId) {
        for (MenuVO menuVO : lstMenu) {
            if (menuVO.getUuid().equals(parentId)) {
                return menuVO;
            }
        }
        throw new IllegalArgumentException();
    }

    private MenuVO getMenuVO(SysMenuInfo menu) {
        MenuVO menuVO = new MenuVO();
        menuVO.setName(menu.getMenuName());
        menuVO.setUrl(menu.getMenuUrl());
        menuVO.setIconCls(menu.getMenuClass());
        menuVO.setUuid(menu.getMenuId());
        return menuVO;
    }

    @RequestMapping("/init")
    public String init() {
        return "welcome";
    }

    /**
     * 获取权限树形数据
     *
     * @param sysRoleId 角色ID，可以为空
     * @param request
     * @return
     */
    @RequestMapping(value = "permissionTree", method = RequestMethod.GET)
    @ResponseBody
    public List<CommonTreeVO> permissionTree(@RequestParam(value = "sysRoleId", required = false) String sysRoleId, HttpServletRequest request) {
        List<CommonTreeVO> result = Lists.newArrayList();
        List<SysMenuInfo> lstMenuAll = menuInfoMapper.queryAll();
        result.addAll(Lists.transform(lstMenuAll, new Function<SysMenuInfo, CommonTreeVO>() {
            @Nullable
            @Override
            public CommonTreeVO apply(@Nullable SysMenuInfo input) {
                CommonTreeVO treeVO = new CommonTreeVO();
                treeVO.setId(input.getMenuId());
                treeVO.setName(input.getMenuName());
                treeVO.setIsParent(String.valueOf(StringUtils.isEmpty(input.getParentId())));
                treeVO.setpId(input.getParentId());
                return treeVO;
            }
        }));
        if (StringUtils.isNotEmpty(sysRoleId)) {
            List<SysRolePermission> lstPermission = sysRoleService.queryPermission(sysRoleId);
            List<String> lstPermissionId = Lists.transform(lstPermission, new Function<SysRolePermission, String>() {
                @Nullable
                @Override
                public String apply(@Nullable SysRolePermission input) {
                    return input.getPermissionId();
                }
            });
            for (CommonTreeVO treeVO : result) {
                String id = treeVO.getId();
                treeVO.setChecked(lstPermissionId.contains(id));
            }
        }

        return result;
    }
}
