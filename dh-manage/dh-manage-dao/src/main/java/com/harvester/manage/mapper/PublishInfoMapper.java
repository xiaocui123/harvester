package com.harvester.manage.mapper;

import com.harvester.manage.pojo.PublishInfo;

import java.util.List;

public interface PublishInfoMapper {
    int deleteByPrimaryKey(String publishId);

    int insert(PublishInfo record);

    int insertSelective(PublishInfo record);

    PublishInfo selectByPrimaryKey(String publishId);

    int updateByPrimaryKeySelective(PublishInfo record);

    int updateByPrimaryKey(PublishInfo record);

    List<PublishInfo> selectByResourceId(String resourceId);
}