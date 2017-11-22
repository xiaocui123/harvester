package com.harvester;

/**
 * Created by cui on 2017/5/16.
 */
public interface HarvesterConstants {

    String SYSTEM_USER = "SYSTEM_USER";
    String SYSTEM_USER_ID = "SYSTEM_USER_ID";

    String TDS_CATALOG_FILE = "catalog.xml";

    String DEFAULT_RESOURCE_TYPE = "OPeNDAP";

    //浮标、共享数据、船基
    String RESOURCE_BUOY_TYPE = "BUOY";
    String RESOURCE_SHARED_TYPE = "SHARED";
    String RESOURCE_ROUTES_NUTRIENT_TYPE = "ROUTES_NUTRIENT";
    String RESOURCE_ROUTES_CTD = "ROUTES_CTD";

    String FILEEXT_NC = "nc";
    String FILEEXT_CNV = "cnv";

    //维度名称
    String DIMENSION_STATION = "station";
    String DIMENSION_TIME = "time";
    String DIMENSION_SVAR = "svar_len";

    String VARIABLE_LONGITUDE = "longitude";
    String VARIABLE_LATITUDE = "latitude";
    String VARIABLE_DEPTH = "depth";
}
