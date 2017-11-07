package com.harvester.manage.mapper;

import com.harvester.manage.pojo.SysRoleInfo;

import java.util.List;

public interface SysRoleInfoMapper {
    int deleteByPrimaryKey(String roleId);

    int insert(SysRoleInfo record);

    int insertSelective(SysRoleInfo record);

    SysRoleInfo selectByPrimaryKey(String roleId);

    int updateByPrimaryKeySelective(SysRoleInfo record);

    int updateByPrimaryKey(SysRoleInfo record);

    List<SysRoleInfo> queryAll();
}