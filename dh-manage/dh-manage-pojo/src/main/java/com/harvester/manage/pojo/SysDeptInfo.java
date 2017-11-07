package com.harvester.manage.pojo;

import java.util.Date;

public class SysDeptInfo {
    private String departmentId;

    private String departmentName;

    private String parentDepId;

    private Date departmentUpdateDate;

    private Integer deptOrder;

    public String getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(String departmentId) {
        this.departmentId = departmentId == null ? null : departmentId.trim();
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName == null ? null : departmentName.trim();
    }

    public String getParentDepId() {
        return parentDepId;
    }

    public void setParentDepId(String parentDepId) {
        this.parentDepId = parentDepId == null ? null : parentDepId.trim();
    }

    public Date getDepartmentUpdateDate() {
        return departmentUpdateDate;
    }

    public void setDepartmentUpdateDate(Date departmentUpdateDate) {
        this.departmentUpdateDate = departmentUpdateDate;
    }

    public Integer getDeptOrder() {
        return deptOrder;
    }

    public void setDeptOrder(Integer deptOrder) {
        this.deptOrder = deptOrder;
    }
}