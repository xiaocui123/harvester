package com.harvester.manage.qvo;

import com.google.common.collect.Lists;

import java.util.List;

/**
 * 添加用户角色请求实体
 * Created by cui on 2017/6/2.
 */
public class AddUserRoleQO {
    private String staffId;
    private List<String> lstRoleId;

    public String getStaffId() {
        return staffId;
    }

    public void setStaffId(String staffId) {
        this.staffId = staffId;
    }

    public List<String> getLstRoleId() {
        if (lstRoleId == null) {
            lstRoleId = Lists.newArrayList();
        }
        return lstRoleId;
    }

    public void setLstRoleId(List<String> lstRoleId) {
        this.lstRoleId = lstRoleId;
    }
}
