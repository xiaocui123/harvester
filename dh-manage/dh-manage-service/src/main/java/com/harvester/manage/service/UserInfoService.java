package com.harvester.manage.service;

import com.harvester.manage.pojo.UserInfo;

import java.util.List;

/**
 * Created by cui on 2017/5/17.
 */
public interface UserInfoService {

    /**
     * 用户登录
     * @param userInfo
     * @return
     */
    public UserInfo checkUser(UserInfo userInfo);

    int add(UserInfo user);

    int update(UserInfo user);

    int delete(String userId);

    UserInfo queryById(String userId);

    void addRole(String staffId, List<String> lstRoleId);
}
