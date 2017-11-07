package com.harvester.manage.pojo;

/**
 * 系统内枫树人员树形结构VO
 * Created by cui on 2017/4/11.
 */
public class DeptUserTreeVO {
    private String id;
    private String pId;
    private String name;
    private String isParent;

    private String type;//0：机构 1：人员

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getpId() {
        return pId;
    }

    public void setpId(String pId) {
        this.pId = pId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getIsParent() {
        return isParent;
    }

    public void setIsParent(String isParent) {
        this.isParent = isParent;
    }

    @Override
    public String toString() {
        return new StringBuilder().append("id=").append(id)
                .append(" pId=").append(pId)
                .append(" name=").append(name)
                .toString();
    }
}
