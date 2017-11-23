package com.harvester.manage.mapper;

import com.harvester.manage.pojo.DataSetInfo;
import com.harvester.manage.pojo.DataSetInfoExt;

import java.util.List;

public interface DataSetInfoMapper {
    int deleteByPrimaryKey(String datasetId);

    int insert(DataSetInfo record);

    int insertSelective(DataSetInfo record);

    DataSetInfo selectByPrimaryKey(String datasetId);

    int updateByPrimaryKeySelective(DataSetInfo record);

    int updateByPrimaryKey(DataSetInfo record);

    List<DataSetInfoExt> query();
}