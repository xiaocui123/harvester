package com.harvester.generator.helper;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created by cui on 2017/11/1.
 */
@Component("accessConnectionInfo")
public class AccessConnectionInfo {

    @Value("${data_source_url}")
    private String url;

    @Value("${nc.file.path}")
    private String ncFilePath;

    public String getNcFilePath() {
        return ncFilePath;
    }

    public void setNcFilePath(String ncFilePath) {
        this.ncFilePath = ncFilePath;
    }

    public Connection getConnection() throws ClassNotFoundException, IOException, SQLException {
        Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
        Connection conn = DriverManager.getConnection(url);

        return conn;
    }
}
