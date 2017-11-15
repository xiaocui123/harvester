package com.harvester.manage.pojo;

import java.text.SimpleDateFormat;
import java.util.Date;

public class SharedInfo {
    private String shareId;

    private String sharedName;

    private String sharedGenerator;

    private Date sharedGenerateTime;
    private String sharedGenerateTimeStr;

    private String sharedNcFilepath;

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    public String getShareId() {
        return shareId;
    }

    public void setShareId(String shareId) {
        this.shareId = shareId == null ? null : shareId.trim();
    }

    public String getSharedName() {
        return sharedName;
    }

    public void setSharedName(String sharedName) {
        this.sharedName = sharedName == null ? null : sharedName.trim();
    }

    public String getSharedGenerator() {
        return sharedGenerator;
    }

    public void setSharedGenerator(String sharedGenerator) {
        this.sharedGenerator = sharedGenerator == null ? null : sharedGenerator.trim();
    }

    public Date getSharedGenerateTime() {
        return sharedGenerateTime;
    }

    public void setSharedGenerateTime(Date sharedGenerateTime) {
        this.sharedGenerateTime = sharedGenerateTime;
        if (sharedGenerateTime != null) {
            this.sharedGenerateTimeStr = sdf.format(sharedGenerateTime);
        }
    }

    public String getSharedGenerateTimeStr() {
        return sharedGenerateTimeStr;
    }

    public void setSharedGenerateTimeStr(String sharedGenerateTimeStr) {
        this.sharedGenerateTimeStr = sharedGenerateTimeStr;
    }

    public String getSharedNcFilepath() {
        return sharedNcFilepath;
    }

    public void setSharedNcFilepath(String sharedNcFilepath) {
        this.sharedNcFilepath = sharedNcFilepath == null ? null : sharedNcFilepath.trim();
    }
}