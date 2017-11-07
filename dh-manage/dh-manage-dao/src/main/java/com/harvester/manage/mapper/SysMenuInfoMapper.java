package com.harvester.manage.mapper;


import com.harvester.manage.pojo.SysMenuInfo;

import java.util.List;

public interface SysMenuInfoMapper {
    int deleteByPrimaryKey(String menuId);

    int insert(SysMenuInfo record);

    int insertSelective(SysMenuInfo record);

    SysMenuInfo selectByPrimaryKey(String menuId);

    int updateByPrimaryKeySelective(SysMenuInfo record);

    int updateByPrimaryKey(SysMenuInfo record);

    List<SysMenuInfo> queryByUser(String userId);

    List<SysMenuInfo> queryAll();
}