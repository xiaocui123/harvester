package com.harvester.manage.mapper;


import com.harvester.manage.pojo.UserInfo;

public interface UserInfoMapper {
    int deleteByPrimaryKey(String userId);

    int insert(UserInfo record);

    int insertSelective(UserInfo record);

    UserInfo selectByPrimaryKey(String userId);

    int updateByPrimaryKeySelective(UserInfo record);

    int updateByPrimaryKey(UserInfo record);

    /**
     * 验证用户名密码是否正确
     * @param userInfo
     * @return
     */
    UserInfo checkUser(UserInfo userInfo);
}