package com.harvester.manage.pojo;

import java.util.Date;

public class DataSetInfo {
    private String datasetId;

    private String datasetName;

    private String datasetGenerator;

    private Date datasetGenerateTime;

    private String datasetConfigFilepath;

    private String datasetNcFilepath;

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