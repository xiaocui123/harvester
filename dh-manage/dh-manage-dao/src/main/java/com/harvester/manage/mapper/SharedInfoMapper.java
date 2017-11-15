package com.harvester.manage.mapper;

import com.harvester.manage.pojo.SharedInfo;
import com.harvester.manage.pojo.SharedInfoExt;

import java.util.List;

public interface SharedInfoMapper {
    int deleteByPrimaryKey(String shareId);

    int insert(SharedInfo record);

    int insertSelective(SharedInfo record);

    SharedInfo selectByPrimaryKey(String shareId);

    int updateByPrimaryKeySelective(SharedInfo record);

    int updateByPrimaryKey(SharedInfo record);

    List<SharedInfoExt> query();
}