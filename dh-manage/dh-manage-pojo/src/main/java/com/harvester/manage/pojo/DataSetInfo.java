package com.harvester.manage.pojo;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DataSetInfo {
    private String datasetId;

    private String datasetName;

    private String datasetGenerator;

    private Date datasetGenerateTime;
    private String datasetGenerateTimeStr;

    private String datasetConfigFilepath;

    private String datasetNcFilepath;

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    public String getDatasetId() {
        return datasetId;
    }

    public void setDatasetId(String datasetId) {
        this.datasetId = datasetId == null ? null : datasetId.trim();
    }

    public String getDatasetName() {
        return datasetName;
    }

    public void setDatasetName(String datasetName) {
        this.datasetName = datasetName == null ? null : datasetName.trim();
    }

    public String getDatasetGenerator() {
        return datasetGenerator;
    }

    public void setDatasetGenerator(String datasetGenerator) {
        this.datasetGenerator = datasetGenerator == null ? null : datasetGenerator.trim();
    }

    public Date getDatasetGenerateTime() {
        return datasetGenerateTime;
    }

    public void setDatasetGenerateTime(Date datasetGenerateTime) {
        this.datasetGenerateTime = datasetGenerateTime;
        if(datasetGenerateTime!=null){
            datasetGenerateTimeStr=sdf.format(datasetGenerateTime);
        }
    }

    public String getDatasetGenerateTimeStr() {
        return datasetGenerateTimeStr;
    }

    public void setDatasetGenerateTimeStr(String datasetGenerateTimeStr) {
        this.datasetGenerateTimeStr = datasetGenerateTimeStr;
    }

    public String getDatasetConfigFilepath() {
        return datasetConfigFilepath;
    }

    public void setDatasetConfigFilepath(String datasetConfigFilepath) {
        this.datasetConfigFilepath = datasetConfigFilepath == null ? null : datasetConfigFilepath.trim();
    }

    public String getDatasetNcFilepath() {
        return datasetNcFilepath;
    }

    public void setDatasetNcFilepath(String datasetNcFilepath) {
        this.datasetNcFilepath = datasetNcFilepath == null ? null : datasetNcFilepath.trim();
    }
}