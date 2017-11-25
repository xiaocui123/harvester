package com.harvester.manage.mapper;

import com.harvester.manage.pojo.SysDict;

import java.util.List;

public interface SysDictMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SysDict record);

    int insertSelective(SysDict record);

    SysDict selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SysDict record);

    int updateByPrimaryKey(SysDict record);

    List<SysDict> getDicts(List<String> parentIds);
}