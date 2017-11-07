package com.harvester.manage.service.impl;

import com.harvester.manage.mapper.SysUserRoleMapper;
import com.harvester.manage.mapper.UserInfoMapper;
import com.harvester.manage.pojo.SysUserRoleKey;
import com.harvester.manage.pojo.UserInfo;
import com.harvester.manage.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * Created by cui on 2017/5/17.
 */
@Service("UserInfoService")
public class UserInfoServiceImpl implements UserInfoService {

    @Autowired
    private UserInfoMapper userInfoMapper;

    @Autowired
    private SysUserRoleMapper sysUserRoleMapper;

    @Override
    public UserInfo checkUser(UserInfo userInfo) {
        return userInfoMapper.checkUser(userInfo);
    }

    @Override
    public int add(UserInfo user) {
        if (user.getUserId() == null) {
            user.setUserId(UUID.randomUUID().toString());
        }
        user.setUserPsw("96e79218965eb72c92a549dd5a330112");
        return userInfoMapper.insertSelective(user);
    }

    @Override
    public int update(UserInfo user) {
        return userInfoMapper.updateByPrimaryKeySelective(user);
    }

    @Override
    public int delete(String userId) {
        return userInfoMapper.deleteByPrimaryKey(userId);
    }

    @Override
    public UserInfo queryById(String userId) {
        return userInfoMapper.selectByPrimaryKey(userId);
    }

    @Override
    public void addRole(String staffId, List<String> lstRoleId) {
        sysUserRoleMapper.deleteByStaff(staffId);
        if (lstRoleId.size() > 0) {
            for (String roleId : lstRoleId) {
                SysUserRoleKey roleKey = new SysUserRoleKey();
                roleKey.setUserId(staffId);
                roleKey.setRoleId(roleId);
                sysUserRoleMapper.insertSelective(roleKey);
            }
        }
    }
}
