package com.harvester.manage.service.impl;

import com.harvester.vo.Page;
import com.github.pagehelper.PageHelper;
import com.harvester.manage.mapper.SysRoleInfoMapper;
import com.harvester.manage.mapper.SysRolePermissionMapper;
import com.harvester.manage.pojo.SysRoleInfo;
import com.harvester.manage.pojo.SysRolePermission;
import com.harvester.manage.service.SysRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * Created by cui on 2017/5/31.
 */
@Service("SysRoleService")
public class SysRoleServiceImpl implements SysRoleService {

    @Autowired
    private SysRoleInfoMapper sysRoleMapper;

    @Autowired
    private SysRolePermissionMapper sysRolePermissionMapper;

    @Override
    public List<SysRoleInfo> queryAll(Page<SysRoleInfo> page) {
        PageHelper.offsetPage(page.getOffset(), page.getLimit());
        return sysRoleMapper.queryAll();
    }

    @Override
    public List<SysRolePermission> queryPermission(String sysRoleId) {
        return sysRolePermissionMapper.queryBySysRole(sysRoleId);
    }

    @Override
    public int add(SysRoleInfo sysRole, List<String> lstPermissionId) {
        String sysRoleId = UUID.randomUUID().toString();
        sysRole.setRoleId(sysRoleId);
        addRolePermission(lstPermissionId, sysRoleId);
        sysRoleMapper.insertSelective(sysRole);
        return 1;
    }

    @Override
    public int update(SysRoleInfo sysRole, List<String> lstPermissionId) {
        sysRoleMapper.updateByPrimaryKeySelective(sysRole);
        //先删后添加
        sysRolePermissionMapper.deleteBySysRole(sysRole.getRoleId());
        addRolePermission(lstPermissionId, sysRole.getRoleId());
        return 1;
    }

    private void addRolePermission(List<String> lstPermissionId, String sysRoleId) {
        for (String permissionId : lstPermissionId) {
            SysRolePermission rolePermission = new SysRolePermission();
            rolePermission.setRpId(UUID.randomUUID().toString());
            rolePermission.setRoleId(sysRoleId);
            rolePermission.setPermissionId(permissionId);
            sysRolePermissionMapper.insertSelective(rolePermission);
        }
    }

    @Override
    public int delete(String sysRoleId) {
        sysRoleMapper.deleteByPrimaryKey(sysRoleId);
        //删除角色对应的权限信息
        sysRolePermissionMapper.deleteBySysRole(sysRoleId);
        return 0;
    }
}
