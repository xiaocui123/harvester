package com.harvester.manage.pojo;

import java.text.SimpleDateFormat;
import java.util.Date;

public class CtdInfo {
    private String ctdId;

    private String ctdName;

    private String ctdGenerator;

    private Date ctdGenerateTime;
    private String ctdGenerateTimeStr;

    private String ctdNcFilepath;

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    public String getCtdId() {
        return ctdId;
    }

    public void setCtdId(String ctdId) {
        this.ctdId = ctdId == null ? null : ctdId.trim();
    }

    public String getCtdName() {
        return ctdName;
    }

    public void setCtdName(String ctdName) {
        this.ctdName = ctdName == null ? null : ctdName.trim();
    }

    public String getCtdGenerator() {
        return ctdGenerator;
    }

    public void setCtdGenerator(String ctdGenerator) {
        this.ctdGenerator = ctdGenerator == null ? null : ctdGenerator.trim();
    }

    public Date getCtdGenerateTime() {
        return ctdGenerateTime;
    }

    public void setCtdGenerateTime(Date ctdGenerateTime) {
        this.ctdGenerateTime = ctdGenerateTime;
        if (ctdGenerateTime != null) {
            this.ctdGenerateTimeStr = sdf.format(ctdGenerateTime);
        }
    }

    public String getCtdGenerateTimeStr() {
        return ctdGenerateTimeStr;
    }

    public void setCtdGenerateTimeStr(String ctdGenerateTimeStr) {
        this.ctdGenerateTimeStr = ctdGenerateTimeStr;
    }

    public String getCtdNcFilepath() {
        return ctdNcFilepath;
    }

    public void setCtdNcFilepath(String ctdNcFilepath) {
        this.ctdNcFilepath = ctdNcFilepath == null ? null : ctdNcFilepath.trim();
    }
}