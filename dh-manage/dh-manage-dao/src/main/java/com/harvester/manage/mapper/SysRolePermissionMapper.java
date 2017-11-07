package com.harvester.manage.mapper;


import com.harvester.manage.pojo.SysRolePermission;

import java.util.List;

public interface SysRolePermissionMapper {
    int deleteByPrimaryKey(String rpId);

    int insert(SysRolePermission record);

    int insertSelective(SysRolePermission record);

    SysRolePermission selectByPrimaryKey(String rpId);

    int updateByPrimaryKeySelective(SysRolePermission record);

    int updateByPrimaryKey(SysRolePermission record);

    List<SysRolePermission> queryBySysRole(String sysRoleId);

    void deleteBySysRole(String roleId);
}