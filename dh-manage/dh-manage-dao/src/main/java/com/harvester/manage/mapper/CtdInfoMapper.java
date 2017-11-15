package com.harvester.manage.mapper;

import com.harvester.manage.pojo.CtdInfo;
import com.harvester.manage.pojo.CtdInfoExt;

import java.util.List;

public interface CtdInfoMapper {
    int deleteByPrimaryKey(String ctdId);

    int insert(CtdInfo record);

    int insertSelective(CtdInfo record);

    CtdInfo selectByPrimaryKey(String ctdId);

    int updateByPrimaryKeySelective(CtdInfo record);

    int updateByPrimaryKey(CtdInfo record);

    List<CtdInfoExt> query();
}