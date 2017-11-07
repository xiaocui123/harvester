package com.harvester.manage.pojo;

import java.util.Date;

public class PublishInfo {
    private String publishId;

    private String publishResourceId;

    private String publishResourceType;

    private Boolean publishSuccessful;

    private String publishError;

    private String publishUser;

    private Date publishTime;

    private String publishUrl;

    public String getPublishId() {
        return publishId;
    }

    public void setPublishId(String publishId) {
        this.publishId = publishId == null ? null : publishId.trim();
    }

    public String getPublishResourceId() {
        return publishResourceId;
    }

    public void setPublishResourceId(String publishResourceId) {
        this.publishResourceId = publishResourceId == null ? null : publishResourceId.trim();
    }

    public String getPublishResourceType() {
        return publishResourceType;
    }

    public void setPublishResourceType(String publishResourceType) {
        this.publishResourceType = publishResourceType == null ? null : publishResourceType.trim();
    }

    public Boolean getPublishSuccessful() {
        return publishSuccessful;
    }

    public void setPublishSuccessful(Boolean publishSuccessful) {
        this.publishSuccessful = publishSuccessful;
    }

    public String getPublishError() {
        return publishError;
    }

    public void setPublishError(String publishError) {
        this.publishError = publishError == null ? null : publishError.trim();
    }

    public String getPublishUser() {
        return publishUser;
    }

    public void setPublishUser(String publishUser) {
        this.publishUser = publishUser == null ? null : publishUser.trim();
    }

    public Date getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(Date publishTime) {
        this.publishTime = publishTime;
    }

    public String getPublishUrl() {
        return publishUrl;
    }

    public void setPublishUrl(String publishUrl) {
        this.publishUrl = publishUrl == null ? null : publishUrl.trim();
    }
}