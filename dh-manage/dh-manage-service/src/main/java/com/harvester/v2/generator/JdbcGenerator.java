package com.harvester.v2.generator;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.io.Files;
import com.harvester.HarvesterConstants;
import com.harvester.generator.helper.HarvesterConfig;
import com.harvester.generator.helper.SpringContextUtil;
import com.harvester.v2.config.DataSet;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.joda.time.DateTime;
import org.joda.time.Minutes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ucar.ma2.*;
import ucar.nc2.Attribute;
import ucar.nc2.Dimension;
import ucar.nc2.NetcdfFileWriter;
import ucar.nc2.Variable;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * jdbc生成NC器
 * Created by cui on 2017/11/21.
 */
public class JdbcGenerator {

    private Logger logger = LoggerFactory.getLogger(getClass());

    private DataSet dataSet;

    private String rootDirPath;

    public void init(File configFile) {
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(DataSet.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            this.dataSet = (DataSet) jaxbUnmarshaller.unmarshal(configFile);
        } catch (JAXBException e) {
            logger.error("初始化配置信息失败！", e);
        }
    }

    public void generate() {
        NetcdfFileWriter dataFile = null;
        try (Connection connection = getConnection()) {
            HarvesterConfig harvesterConfig = (HarvesterConfig) SpringContextUtil.getBean("harvesterconfig");
            this.rootDirPath = harvesterConfig.getBuoyFilePath();

            String ncFilePath = rootDirPath + UUID.randomUUID().toString() + File.separator + dataSet.getDatasetName() + ".nc";

            try {
                Files.createParentDirs(new File(ncFilePath));
            } catch (IOException e) {
                logger.info("生成NC文件父目录失败！");
                throw new IllegalArgumentException(e);
            }

            String startTime = getStartTime(connection);
            List<Double> lstTime = getTime(connection, startTime);

            dataFile = NetcdfFileWriter.createNew(NetcdfFileWriter.Version.netcdf3, ncFilePath);

            doNcMetaWrite(connection, dataFile, startTime, lstTime);

            doNcDataWrite(connection, dataFile, lstTime);


        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvalidRangeException e) {
            e.printStackTrace();
        } finally {
            if (null != dataFile) {
                try {
                    dataFile.close();
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                }
            }
        }
    }

    private void doNcDataWrite(Connection connection, NetcdfFileWriter dataFile, List<Double> lstTime) throws IOException, InvalidRangeException, SQLException {
        //写浮标号
        Variable stationVariable = dataFile.findVariable(HarvesterConstants.DIMENSION_STATION);
        int[] shape = stationVariable.getShape();
        try {
            ArrayChar ac2 = new ArrayChar.D2(shape[0], shape[1]);
            Index ima = ac2.getIndex();
            ac2.setString(ima.set(0), dataSet.getStation().getStationVariable().getName());
            dataFile.write(stationVariable, ac2);
        } catch (Exception e) {
            logger.error("写浮标号数据失败！", e);
        }
        //写经纬度数据
        Array dataLon = Array.factory(DataType.DOUBLE, new int[]{1});
        dataLon.setDouble(0, dataSet.getStation().getLongitude());
        Variable lonVariable = dataFile.findVariable(HarvesterConstants.VARIABLE_LONGITUDE);
        dataFile.write(lonVariable, dataLon);

        Array dataLat = Array.factory(DataType.DOUBLE, new int[]{1});
        dataLat.setDouble(0, dataSet.getStation().getLatitude());
        Variable latVariable = dataFile.findVariable(HarvesterConstants.VARIABLE_LATITUDE);
        dataFile.write(latVariable, dataLat);

        //写时间数据
        Array dataTime = Array.factory(DataType.DOUBLE, new int[]{lstTime.size()});
        for (int i = 0; i < lstTime.size(); i++) {
            dataTime.setDouble(i, lstTime.get(i));
        }
        Variable timeVarible = dataFile.findVariable(HarvesterConstants.DIMENSION_TIME);
        dataFile.write(timeVarible, dataTime);

        if (dataSet.getDepth() != null) {
            //写深度数据
            doWriteMeaturedData(connection, dataFile, dataSet.getDepth());

        }
        for (com.harvester.v2.config.Variable variable : dataSet.getMeaturedVariables().getMeaturedVariable()) {
            //写测量数据
            doWriteMeaturedData(connection, dataFile, variable);

        }
    }

    private void doWriteMeaturedData(Connection connection, NetcdfFileWriter dataFile, com.harvester.v2.config.Variable variable) throws SQLException, IOException, InvalidRangeException {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT ").append(dataSet.getTime().getColumn()).append(",").append(variable.getColumn());
        sql.append(" FROM ").append(dataSet.getSource().getJdbcType().getTable());
        sql.append(" WHERE ").append(dataSet.getStation().getStationVariable().getColumn());
        sql.append(" = ").append("'").append(dataSet.getStation().getStationVariable().getName()).append("'");
        sql.append(" ORDER BY ").append(dataSet.getTime().getColumn());

        ResultSetHandler<List<Double>> handler = new ResultSetHandler<List<Double>>() {
            @Override
            public List<Double> handle(ResultSet rs) throws SQLException {
                List<Double> lstData = Lists.newArrayList();
                while (rs.next()) {
                    lstData.add(rs.getDouble(2));
                }
                return lstData;
            }
        };
        List<Double> data = new QueryRunner().query(connection, sql.toString(), handler);
        Variable var = dataFile.findVariable(variable.getName());

        Array arrayData = Array.factory(DataType.DOUBLE, new int[]{data.size()});
        for (int i = 0; i < data.size(); i++) {
            arrayData.setDouble(i, data.get(i));
        }
        dataFile.write(var, arrayData);

    }

    private String getStartTime(Connection connection) throws SQLException {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT min(").append(dataSet.getTime().getColumn()).append(") FROM ").append(dataSet.getSource().getJdbcType().getTable());
        sql.append(" WHERE ").append(dataSet.getStation().getStationVariable().getColumn());
        sql.append(" = ").append("'").append(dataSet.getStation().getStationVariable().getName()).append("'");
        logger.info("查询最小时间值 SQL = 【{}】", sql.toString());

        ResultSetHandler<String> handler = new ResultSetHandler<String>() {
            @Override
            public String handle(ResultSet rs) throws SQLException {
                String startTime = "";
                while (rs.next()) {
                    startTime = rs.getString(1);
                }
                return startTime;
            }
        };
        QueryRunner runner = new QueryRunner();
        return runner.query(connection, sql.toString(), handler);
    }

    private List<Double> getTime(Connection connection, String startTimeStr) throws SQLException {
        final DateTime starTime = convert(startTimeStr);
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT ").append(dataSet.getTime().getColumn()).append(" FROM ").append(dataSet.getSource().getJdbcType().getTable());
        sql.append(" WHERE ").append(dataSet.getStation().getStationVariable().getColumn()).append(" = ").append("'").append(dataSet.getStation().getStationVariable().getName()).append("'");
        sql.append(" ORDER BY ").append(dataSet.getTime().getColumn());
        logger.info("查询时间序列值SQL=【{}】", sql.toString());
        ResultSetHandler<List<Double>> handler = new ResultSetHandler<List<Double>>() {
            @Override
            public List<Double> handle(ResultSet rs) throws SQLException {
                List<Double> lstTime = Lists.newArrayList();
                while (rs.next()) {
                    String timeString = rs.getString(1);
                    DateTime time = convert(timeString);
                    lstTime.add((double) Minutes.minutesBetween(starTime, time).getMinutes());
                }
                return lstTime;
            }
        };

        QueryRunner runner = new QueryRunner();
        return runner.query(connection, sql.toString(), handler);
    }

    private void doNcMetaWrite(Connection connection, NetcdfFileWriter dataFile, String startTime, List<Double> lstTime) throws IOException {

        DataSet.Station station = dataSet.getStation();
        Dimension stationDim = dataFile.addDimension(null, HarvesterConstants.DIMENSION_STATION, 1);
        Dimension timeDim = dataFile.addDimension(null, HarvesterConstants.DIMENSION_TIME, lstTime.size());

        Dimension svar_len = dataFile.addDimension(null, HarvesterConstants.DIMENSION_SVAR, 10);

        //Variable
        List<Dimension> dim1 = Lists.newArrayList();
        dim1.add(stationDim);
        dim1.add(svar_len);
        Variable stationNameVar = dataFile.addVariable(null, HarvesterConstants.DIMENSION_STATION, DataType.CHAR, dim1);

        Variable timeVar = dataFile.addVariable(null, HarvesterConstants.DIMENSION_TIME, DataType.DOUBLE, HarvesterConstants.DIMENSION_TIME);
        timeVar.addAttribute(new Attribute("units", "minutes since " + startTime));
        for (com.harvester.v2.config.Attribute attr : dataSet.getTime().getAttribute()) {
            timeVar.addAttribute(new Attribute(attr.getKey(), attr.getValue()));
        }

        dataFile.addVariable(null, HarvesterConstants.VARIABLE_LONGITUDE, DataType.FLOAT, HarvesterConstants.DIMENSION_STATION);
        dataFile.addVariable(null, HarvesterConstants.VARIABLE_LATITUDE, DataType.FLOAT, HarvesterConstants.DIMENSION_STATION);

        if (dataSet.getDepth() != null) {
            com.harvester.v2.config.Variable variable = dataSet.getDepth();
            String varDepthName = HarvesterConstants.VARIABLE_DEPTH;
            if (!Strings.isNullOrEmpty(variable.getName())) {
                varDepthName = variable.getName();
            }
            Variable depthVar = dataFile.addVariable(null, varDepthName, DataType.DOUBLE, HarvesterConstants.DIMENSION_STATION);
            for (com.harvester.v2.config.Attribute attr : dataSet.getDepth().getAttribute()) {
                depthVar.addAttribute(new Attribute(attr.getKey(), attr.getValue()));
            }
        }

        for (com.harvester.v2.config.Variable meaturedVariable : dataSet.getMeaturedVariables().getMeaturedVariable()) {
            String varName = meaturedVariable.getName();
            Preconditions.checkArgument(!Strings.isNullOrEmpty(varName), "变量名称为空值！");
            Variable meaturedVar = dataFile.addVariable(null, varName, DataType.DOUBLE, HarvesterConstants.DIMENSION_TIME);

            for (com.harvester.v2.config.Attribute attr : meaturedVariable.getAttribute()) {
                meaturedVar.addAttribute(new Attribute(attr.getKey(), attr.getValue()));
            }
        }

        for (com.harvester.v2.config.Attribute attribute : dataSet.getGlobalAttributes().getGlobalAttribute()) {
            dataFile.addGroupAttribute(null, new Attribute(attribute.getKey(), attribute.getValue()));
        }

        dataFile.create();

    }

    private Connection getConnection() throws ClassNotFoundException, SQLException {
        if (dataSet.getSource().getJdbcType() != null) {
            DataSet.Source.JdbcType jdbcType = dataSet.getSource().getJdbcType();
            Class.forName(jdbcType.getDriver());
            return DriverManager.getConnection(jdbcType.getUrl());
        }
        throw new IllegalArgumentException("不支持的数据源！");

    }

    /**
     * "1708010300" 转化成时间类型
     *
     * @param timeString
     * @return
     */
    private DateTime convert(String timeString) {
        try {
            SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmm");
            Date date = df.parse("20" + timeString);
            DateTime dateTime = new DateTime(date);
            return dateTime;
        } catch (ParseException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public static void main(String[] args) {
        JdbcGenerator generator = new JdbcGenerator();
        generator.init(new File("E://数据项目//general-config//dataset.xml"));
        generator.generate();
    }

}
