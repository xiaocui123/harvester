package com.harvester.generator.helper;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Created by cui on 2017/11/3.
 */
@Component("harvesterconfig")
public class HarvesterConfig {

    //tds服务catalog.xml目录
    @Value("${tds.content.root.path}")
    private String tdsRootPath;

    //helper 服务地址
    @Value("${tds.server.url}")
    private String tdsServerUrl;

    //nc 文件存放路径
    @Value("${buoy.file.path}")
    private String buoyFilePath;

    @Value("${ship.nutrient.file.path}")
    private String shipNutrientFilePath;

    @Value("${ship.ctd.file.path}")
    private String sipCtdFilePath;

    @Value("${shared.file.path}")
    private String sharedFilePath;

    @Value("$(general.file.path)")
    private String generalFilePath;

    //ckan服务地址
    @Value("${ckan.url}")
    private String ckanSeverUrl;

    //sysadmin token
    @Value("${ckan.token}")
    private String ckanToken;

    //默认机构ID msdc
    @Value("${ckan.organization}")
    private String defaultOrganization;

    @Value("${tds.dap.root.url}")
    private String TdsDapRootUrl;

    @Value("${tds.httpserver.root.url}")
    private String TdsHttpServerRootUrl;

    public String getTdsRootPath() {
        return tdsRootPath;
    }

    public void setTdsRootPath(String tdsRootPath) {
        this.tdsRootPath = tdsRootPath;
    }

    public String getTdsServerUrl() {
        return tdsServerUrl;
    }

    public void setTdsServerUrl(String tdsServerUrl) {
        this.tdsServerUrl = tdsServerUrl;
    }

    public String getBuoyFilePath() {
        return buoyFilePath;
    }

    public void setBuoyFilePath(String buoyFilePath) {
        this.buoyFilePath = buoyFilePath;
    }

    public String getShipNutrientFilePath() {
        return shipNutrientFilePath;
    }

    public void setShipNutrientFilePath(String shipNutrientFilePath) {
        this.shipNutrientFilePath = shipNutrientFilePath;
    }

    public String getSipCtdFilePath() {
        return sipCtdFilePath;
    }

    public void setSipCtdFilePath(String sipCtdFilePath) {
        this.sipCtdFilePath = sipCtdFilePath;
    }

    public String getSharedFilePath() {
        return sharedFilePath;
    }

    public void setSharedFilePath(String sharedFilePath) {
        this.sharedFilePath = sharedFilePath;
    }

    public String getGeneralFilePath() {
        return generalFilePath;
    }

    public void setGeneralFilePath(String generalFilePath) {
        this.generalFilePath = generalFilePath;
    }

    public String getCkanSeverUrl() {
        return ckanSeverUrl;
    }

    public void setCkanSeverUrl(String ckanSeverUrl) {
        this.ckanSeverUrl = ckanSeverUrl;
    }

    public String getCkanToken() {
        return ckanToken;
    }

    public void setCkanToken(String ckanToken) {
        this.ckanToken = ckanToken;
    }

    public String getDefaultOrganization() {
        return defaultOrganization;
    }

    public void setDefaultOrganization(String defaultOrganization) {
        this.defaultOrganization = defaultOrganization;
    }

    public String getTdsDapRootUrl() {
        return TdsDapRootUrl;
    }

    public void setTdsDapRootUrl(String tdsDapRootUrl) {
        TdsDapRootUrl = tdsDapRootUrl;
    }

    public String getTdsHttpServerRootUrl() {
        return TdsHttpServerRootUrl;
    }

    public void setTdsHttpServerRootUrl(String tdsHttpServerRootUrl) {
        TdsHttpServerRootUrl = tdsHttpServerRootUrl;
    }
}
