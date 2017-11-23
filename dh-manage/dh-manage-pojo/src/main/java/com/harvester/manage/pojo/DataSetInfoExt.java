package com.harvester.manage.pojo;

/**
 * Created by cui on 2017/11/23.
 */
public class DataSetInfoExt extends DataSetInfo {
    //数据集名称
    private String publishDatasetName;

    //是否发布过
    private boolean published;
    //发布URL
    private String publishUrl;

    public String getPublishDatasetName() {
        return publishDatasetName;
    }

    public void setPublishDatasetName(String publishDatasetName) {
        this.publishDatasetName = publishDatasetName;
    }

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
