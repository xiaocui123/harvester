package com.harvester.manage.mapper;

import com.harvester.manage.pojo.NutrientInfo;
import com.harvester.manage.pojo.NutrientInfoExt;

import java.util.List;

public interface NutrientInfoMapper {
    int deleteByPrimaryKey(String nutrientId);

    int insert(NutrientInfo record);

    int insertSelective(NutrientInfo record);

    NutrientInfo selectByPrimaryKey(String nutrientId);

    int updateByPrimaryKeySelective(NutrientInfo record);

    int updateByPrimaryKey(NutrientInfo record);

    List<NutrientInfoExt> query();
}