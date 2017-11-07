package com.harvester.manage.mapper;

import com.harvester.manage.pojo.BuoyInfo;
import com.harvester.manage.pojo.BuoyInfoExt;

import java.util.List;

public interface BuoyInfoMapper {
    int deleteByPrimaryKey(String buoyNcId);

    int insert(BuoyInfo record);

    int insertSelective(BuoyInfo record);

    BuoyInfo selectByPrimaryKey(String buoyNcId);

    int updateByPrimaryKeySelective(BuoyInfo record);

    int updateByPrimaryKey(BuoyInfo record);

    List<BuoyInfoExt> query();
}