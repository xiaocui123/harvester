package com.harvester.generator.helper.nutrient;

import com.google.common.base.MoreObjects;
import com.google.common.collect.Lists;

import java.util.List;

/**
 * 营养盐数据结构
 * Created by cui on 2017/11/7.
 */
public class NutrientDataType {
    //海域
    private String haiyu;
    //站位
    private String zhanwei;
    //纬度
    private double lon;
    //经度
    private double lat;

    //采样日期
    private String date;
    //采样时间
    private String time;
    //水深
    private double depth;

    private List<MeaturedDataType> lstMeaturedData;

    public String getHaiyu() {
        return haiyu;
    }

    public void setHaiyu(String haiyu) {
        this.haiyu = haiyu;
    }

    public String getZhanwei() {
        return zhanwei;
    }

    public void setZhanwei(String zhanwei) {
        this.zhanwei = zhanwei;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public double getDepth() {
        return depth;
    }

    public void setDepth(double depth) {
        this.depth = depth;
    }

    public List<MeaturedDataType> getLstMeaturedData() {
        if (lstMeaturedData == null) {
            lstMeaturedData = Lists.newArrayList();
        }
        return lstMeaturedData;
    }

    public void setLstMeaturedData(List<MeaturedDataType> lstMeaturedData) {
        this.lstMeaturedData = lstMeaturedData;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this).add("海域", haiyu).add("站位", zhanwei).add("纬度", lon)
                .add("经度", lat).add("采样日期", date).add("采样时间", time).add("水深", depth).add("测量值", lstMeaturedData).toString();
    }
}
