package com.harvester.manage.mapper;


import com.harvester.manage.pojo.SysUserRoleKey;

import java.util.List;

public interface SysUserRoleMapper {
    int deleteByPrimaryKey(SysUserRoleKey key);

    int insert(SysUserRoleKey record);

    int insertSelective(SysUserRoleKey record);

    /**
     * 查询用户所属角色信息
     * @param staffId
     * @return
     */
    List<SysUserRoleKey> queryByStaff(String staffId);

    //删除用户的角色
    int deleteByStaff(String staffId);
}