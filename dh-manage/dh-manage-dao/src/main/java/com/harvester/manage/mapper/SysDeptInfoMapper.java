package com.harvester.manage.mapper;

import com.harvester.manage.pojo.DeptUserTreeVO;
import com.harvester.manage.pojo.SysDeptInfo;

import java.util.List;

public interface SysDeptInfoMapper {
    int deleteByPrimaryKey(String departmentId);

    int insert(SysDeptInfo record);

    int insertSelective(SysDeptInfo record);

    SysDeptInfo selectByPrimaryKey(String departmentId);

    int updateByPrimaryKeySelective(SysDeptInfo record);

    int updateByPrimaryKey(SysDeptInfo record);

    List<DeptUserTreeVO> queryTree();
}