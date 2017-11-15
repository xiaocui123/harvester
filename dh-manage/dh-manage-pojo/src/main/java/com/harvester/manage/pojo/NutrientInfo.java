package com.harvester.manage.pojo;

import java.text.SimpleDateFormat;
import java.util.Date;

public class NutrientInfo {
    private String nutrientId;

    private String nutrientName;

    private String nutrientGenerator;

    private Date nutrientGenerateTime;
    private String nutrientGenerateTimeStr;

    private String nutrientNcFilepath;

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    public String getNutrientId() {
        return nutrientId;
    }

    public void setNutrientId(String nutrientId) {
        this.nutrientId = nutrientId == null ? null : nutrientId.trim();
    }

    public String getNutrientName() {
        return nutrientName;
    }

    public void setNutrientName(String nutrientName) {
        this.nutrientName = nutrientName == null ? null : nutrientName.trim();
    }

    public String getNutrientGenerator() {
        return nutrientGenerator;
    }

    public void setNutrientGenerator(String nutrientGenerator) {
        this.nutrientGenerator = nutrientGenerator == null ? null : nutrientGenerator.trim();
    }

    public Date getNutrientGenerateTime() {
        return nutrientGenerateTime;
    }

    public void setNutrientGenerateTime(Date nutrientGenerateTime) {

        this.nutrientGenerateTime = nutrientGenerateTime;
        if(nutrientGenerateTime!=null){
            this.nutrientGenerateTimeStr=sdf.format(nutrientGenerateTime);
        }
    }

    public String getNutrientGenerateTimeStr() {
        return nutrientGenerateTimeStr;
    }

    public void setNutrientGenerateTimeStr(String nutrientGenerateTimeStr) {
        this.nutrientGenerateTimeStr = nutrientGenerateTimeStr;
    }

    public String getNutrientNcFilepath() {
        return nutrientNcFilepath;
    }

    public void setNutrientNcFilepath(String nutrientNcFilepath) {
        this.nutrientNcFilepath = nutrientNcFilepath == null ? null : nutrientNcFilepath.trim();
    }
}