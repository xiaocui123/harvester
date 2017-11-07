package com.harvester.manage.pojo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class BuoyInfo {
    private String buoyNcId;

    private String buoyNcTable;

    private String buoyNcStarttime;
    private String buoyNcStarttimeStr;

    private String buoyNcEndtime;
    private String buoyNcEndtimeStr;

    private String buoyGenerator;

    private Date buoyGenerateTime;
    private String buoyGenerateTimeStr;

    private String buoyNcFilepath;

    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmm");

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    public String getBuoyNcId() {
        return buoyNcId;
    }

    public void setBuoyNcId(String buoyNcId) {
        this.buoyNcId = buoyNcId == null ? null : buoyNcId.trim();
    }

    public String getBuoyNcTable() {
        return buoyNcTable;
    }

    public void setBuoyNcTable(String buoyNcTable) {
        this.buoyNcTable = buoyNcTable == null ? null : buoyNcTable.trim();
    }

    public String getBuoyNcStarttime() {
        return buoyNcStarttime;
    }

    public void setBuoyNcStarttime(String buoyNcStarttime) {
        this.buoyNcStarttime = buoyNcStarttime == null ? null : buoyNcStarttime.trim();
        if (buoyNcStarttime != null && !buoyNcStarttime.isEmpty()) {
            Date date = null;
            try {
                date = simpleDateFormat.parse("20" + buoyNcStarttime);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            this.buoyNcStarttimeStr = sdf.format(date);
        }
    }

    public String getBuoyNcEndtime() {
        return buoyNcEndtime;
    }

    public void setBuoyNcEndtime(String buoyNcEndtime) {
        this.buoyNcEndtime = buoyNcEndtime;
        if (buoyNcEndtime != null && !buoyNcEndtime.isEmpty()) {
            Date date = null;
            try {
                date = simpleDateFormat.parse("20" + buoyNcEndtime);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            this.buoyNcEndtimeStr = sdf.format(date);
        }
    }

    public String getBuoyGenerator() {
        return buoyGenerator;
    }

    public void setBuoyGenerator(String buoyGenerator) {
        this.buoyGenerator = buoyGenerator == null ? null : buoyGenerator.trim();
    }

    public Date getBuoyGenerateTime() {
        return buoyGenerateTime;
    }

    public void setBuoyGenerateTime(Date buoyGenerateTime) {
        this.buoyGenerateTime = buoyGenerateTime;
        if (this.buoyGenerateTime != null) {
            this.buoyGenerateTimeStr = sdf.format(buoyGenerateTime);
        }
    }

    public String getBuoyNcFilepath() {
        return buoyNcFilepath;
    }

    public void setBuoyNcFilepath(String buoyNcFilepath) {
        this.buoyNcFilepath = buoyNcFilepath == null ? null : buoyNcFilepath.trim();
    }

    public String getBuoyGenerateTimeStr() {
        return buoyGenerateTimeStr;
    }

    public void setBuoyGenerateTimeStr(String buoyGenerateTimeStr) {
        this.buoyGenerateTimeStr = buoyGenerateTimeStr;
    }

    public String getBuoyNcStarttimeStr() {
        return buoyNcStarttimeStr;
    }

    public void setBuoyNcStarttimeStr(String buoyNcStarttimeStr) {
        this.buoyNcStarttimeStr = buoyNcStarttimeStr;
    }

    public String getBuoyNcEndtimeStr() {
        return buoyNcEndtimeStr;
    }

    public void setBuoyNcEndtimeStr(String buoyNcEndtimeStr) {
        this.buoyNcEndtimeStr = buoyNcEndtimeStr;
    }
}