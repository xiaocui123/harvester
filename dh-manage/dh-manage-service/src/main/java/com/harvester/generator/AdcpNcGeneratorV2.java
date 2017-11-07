package com.harvester.generator;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.harvester.generator.helper.AccessConnectionInfo;
import com.harvester.generator.helper.SpringContextUtil;
import org.javatuples.Pair;
import org.joda.time.DateTime;
import org.joda.time.Minutes;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.stereotype.Service;
import ucar.ma2.*;
import ucar.ma2.Array;
import ucar.nc2.Attribute;
import ucar.nc2.Dimension;
import ucar.nc2.NetcdfFileWriter;
import ucar.nc2.Variable;

import java.io.IOException;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * 采用CF1.6 H2.4 结构存储不同长度的时间序列
 * http://cfconventions.org/cf-conventions/v1.6.0/cf-conventions.html#_contiguous_ragged_array_representation_of_time_series
 * Created by cui on 2017/10/28.
 */
public class AdcpNcGeneratorV2 {

    private String filePath = "E:\\content\\thredds\\public\\testdata\\station_buoy\\adcp\\adcp.nc";

    //变量与字段名字对应关系
    private static Map<String, String> mapVaribleColumn;
    private int size = 35;

    //浮标维度名称
    private String DIMENSION_STATION = "station";
    //时间维度名称
    private String DIMENSION_OBS = "obs";

    private String VARIABLE_TIME = "time";
    private String VARIABLE_ROWSIZE = "row_size";

    //浮标号维度、变量名称
    private String DIMENSION_SVAR = "svar_len";
    private String DIMENSION_STATIONNAME = "station_name";

    //开始时间值 TODO 待优化
    private String startTimeString;

    static {
        mapVaribleColumn = Maps.newConcurrentMap();
        mapVaribleColumn.put("liushu", "流速");
        mapVaribleColumn.put("liuxxiang", "流向");
    }

    public void generate() {
        NetcdfFileWriter dataFile = null;
        try (Connection connection = getConnection()) {
            dataFile = NetcdfFileWriter.createNew(NetcdfFileWriter.Version.netcdf3, filePath);

            List<String> lstStationName = getStationNames(connection);
            Pair<List<Integer>, List<Double>> pair = getRowSizeAndObsValues(connection, lstStationName);
            List<Integer> lstRowSize = pair.getValue0();
            List<Double> lstObsValue = pair.getValue1();

            //写meta信息
            doNcMetaWrite(dataFile, lstStationName.size(), lstObsValue.size(), lstRowSize.size());

            //写浮标、时间变量数据
            doSationNameObsRowSizeVarWrite(dataFile, lstStationName, lstObsValue, lstRowSize);

            //写测量数据
            doMeasuredVarWrite2(connection, dataFile, lstStationName, lstObsValue, lstRowSize);


        } catch (Exception e) {
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

    /**
     * 写meta信息
     *
     * @param dataFile
     * @param stationLength：浮标长度
     * @param obsLength：时间长度
     * @throws IOException
     */
    private void doNcMetaWrite(NetcdfFileWriter dataFile, int stationLength, int obsLength, int rowSizeLength) throws IOException {
        Dimension stationDim = dataFile.addDimension(null, DIMENSION_STATION, stationLength);
        Dimension obsDim = dataFile.addDimension(null, DIMENSION_OBS, obsLength);

        //浮标号维度
        Dimension svar_len = dataFile.addDimension(null, DIMENSION_SVAR, 4);
        Dimension stationNameDim = dataFile.addDimension(null, DIMENSION_STATIONNAME, stationLength);

        //浮标号变量
        List<Dimension> dim1 = Lists.newArrayList();
        dim1.add(stationNameDim);
        dim1.add(svar_len);
        Variable stationNameVar = dataFile.addVariable(null, DIMENSION_STATIONNAME, DataType.CHAR, dim1);

        //time变量
        List<Dimension> dim2 = Lists.newArrayList();
        dim2.add(obsDim);
        Variable timeVar = dataFile.addVariable(null, VARIABLE_TIME, DataType.DOUBLE, dim2);
        timeVar.addAttribute(new Attribute("long_name", "time"));
        timeVar.addAttribute(new Attribute("units", "minutes since " + startTimeString));

        //RowSize变量
        List<Dimension> dim3 = Lists.newArrayList();
        dim3.add(stationDim);
        dataFile.addVariable(null, VARIABLE_ROWSIZE, DataType.INT, dim3);

        List<Dimension> dims = Lists.newArrayList();
        dims.add(obsDim);
        //流速流向
        for (int i = 1; i <= size; i++) {
            for (String varName : mapVaribleColumn.keySet()) {
                String varNameNew = varName + i;
                Variable variable = dataFile.addVariable(null, varNameNew, DataType.FLOAT, dims);
                variable.addAttribute(new Attribute("long_name", mapVaribleColumn.get(varName)));
                variable.addAttribute(new Attribute("units", "P"));
                variable.addAttribute(new Attribute("_FillValue", new Float(-999.9)));
            }
        }

        dataFile.addGroupAttribute(null, new Attribute("version", new Double(0.1)));
        dataFile.addGroupAttribute(null, new Attribute("title", new String("adcp")));
        dataFile.addGroupAttribute(null, new Attribute("institution", new String("Marine Science Data Center")));

        dataFile.create();
    }

    /**
     * 写维度变量数据（浮标号、时间:距开始时间偏移量minute）
     *
     * @param dataFile
     * @param lstStationName
     * @param lstObsValue
     * @throws ParseException
     * @throws IOException
     * @throws InvalidRangeException
     */
    private void doSationNameObsRowSizeVarWrite(NetcdfFileWriter dataFile, List<String> lstStationName, List<Double> lstObsValue, List<Integer> lstRowSize) throws ParseException, IOException, InvalidRangeException {
        //写浮标号数据
        Variable stationVariable = dataFile.findVariable(DIMENSION_STATIONNAME);
        int[] shape = stationVariable.getShape();
        try {
            ArrayChar ac2 = new ArrayChar.D2(shape[0], shape[1]);
            Index ima = ac2.getIndex();
            for (int i = 0; i < lstStationName.size(); i++) {
                ac2.setString(ima.set(i), lstStationName.get(i));
            }
            dataFile.write(stationVariable, ac2);
        } catch (IOException e) {
            System.err.println("ERROR writing Achar3");
            assert (false);
        } catch (InvalidRangeException e) {
            e.printStackTrace();
            assert (false);
        }


        Array dataTime = Array.factory(DataType.DOUBLE, new int[]{lstObsValue.size()});
        for (int i = 0; i < lstObsValue.size(); i++) {
            dataTime.setDouble(i, lstObsValue.get(i));
        }
        Variable timeVarible = dataFile.findVariable(VARIABLE_TIME);
        dataFile.write(timeVarible, dataTime);

        Array dataRowSize = Array.factory(DataType.INT, new int[]{lstRowSize.size()});
        for (int i = 0; i < lstRowSize.size(); i++) {
            dataRowSize.setInt(i, lstRowSize.get(i));
        }
        Variable rowSizeVariable = dataFile.findVariable(VARIABLE_ROWSIZE);
        dataFile.write(rowSizeVariable, dataRowSize);

    }

    /**
     * 写测量数据
     *
     * @param connection
     * @param dataFile
     * @param lstStationName
     * @param lstObsValue
     * @param lstRowSize
     * @throws Exception
     */
    private void doMeasuredVarWrite2(Connection connection, NetcdfFileWriter dataFile, List<String> lstStationName, List<Double> lstObsValue, List<Integer> lstRowSize) throws Exception {
        for (int i = 1; i <= size; i++) {
            for (Map.Entry<String, String> entry : mapVaribleColumn.entrySet()) {
                String variableName = entry.getKey() + i;
                String columnName = entry.getValue() + i;

                Array measuredData = Array.factory(DataType.DOUBLE, new int[]{lstObsValue.size()});
                for (int index = 0; index < lstStationName.size(); index++) {
                    //查询浮标测量数据
                    List<Double> lstMearueData = Lists.newArrayList();
                    String stationName = lstStationName.get(index);
                    try (PreparedStatement ps = connection.prepareStatement("SELECT 日期时间, " + columnName + " from TabAdcp where 浮标号 = '" + stationName + "' order by 日期时间")) {
                        ResultSet rs = ps.executeQuery();
                        while (rs.next()) {
                            String datatime = rs.getString(1);
                            double value = -999.9;
                            try {
                                value = rs.getDouble(2);
                            } catch (Exception e) {
                                System.out.println("station=【" + stationName + "】datatime=【" + datatime + "】数据不标准,使用默认值-999.9代替！");
                            }
                            lstMearueData.add(value);
                        }
                    }
                    //计算开始位置
                    int startIndex = 0;
                    if (index > 0) {
                        startIndex = 0;
                        for (int key = 0; key < index; key++) {
                            startIndex += lstRowSize.get(key);
                        }
                    }
                    for (int k = 0; k < lstMearueData.size(); k++) {
                        measuredData.setDouble(startIndex + k, lstMearueData.get(k));
                    }
                }

                Variable var = dataFile.findVariable(variableName);
                dataFile.write(var, measuredData);
            }
        }

    }


    private List<String> getStationNames(Connection connection) throws SQLException {
        List<String> result = Lists.newArrayList();
        try (PreparedStatement ps = connection.prepareStatement("select distinct 浮标号 from TabADCP ORDER by 浮标号")) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                result.add(rs.getString(1));
            }
        }
        return result;
    }


    /**
     * 查询浮标序列长度、浮标的时间序列值
     *
     * @param connection
     * @param lstStationName
     * @return
     * @throws SQLException
     */
    private Pair<List<Integer>, List<Double>> getRowSizeAndObsValues(Connection connection, List<String> lstStationName) throws SQLException {
        List<Integer> lstRowSize = Lists.newArrayList();
        List<Double> lstTime = Lists.newArrayList();

        //查询最早日期
        try (PreparedStatement ps = connection.prepareStatement("SELECT TOP 1 日期时间 FROM TabAdcp order by 日期时间")) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                startTimeString = rs.getString(1);
            }
        }
        DateTime starTime = convert(startTimeString);

        for (String stationName : lstStationName) {
            int count = 0;
            try (PreparedStatement ps = connection.prepareStatement("select 日期时间 from TabADCP where 浮标号 ='" + stationName + "' ORDER BY 日期时间")) {
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    count++;
                    String timeString = rs.getString(1);
                    DateTime time = convert(timeString);
                    lstTime.add((double) Minutes.minutesBetween(starTime, time).getMinutes());
                }
            }
            lstRowSize.add(count);
        }
        return Pair.with(lstRowSize, lstTime);
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

    private Connection getConnection() {
        try {
            AccessConnectionInfo accessConnectionInfo = (AccessConnectionInfo) SpringContextUtil.getBean("accessConnectionInfo");
            return accessConnectionInfo.getConnection();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        throw new IllegalArgumentException("获取浮标数据库连接失败！");
    }
}
