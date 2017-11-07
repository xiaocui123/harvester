package com.harvester.manage.pojo;

/**
 * Created by cui on 2017/11/6.
 */
public class BuoyInfoExt extends BuoyInfo {
    //是否发布过
    private boolean published;
    //发布URL
    private String publishUrl;

    public boolean isPublished() {
        return published;
    }

    public void setPublished(boolean published) {
        this.published = published;
    }

    public String getPublishUrl() {
        return publishUrl;
    }

    public void setPublishUrl(String publishUrl) {
        this.publishUrl = publishUrl;
    }
}
