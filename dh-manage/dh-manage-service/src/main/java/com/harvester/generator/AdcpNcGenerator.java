package com.harvester.generator;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.harvester.generator.helper.AccessConnectionInfo;
import com.harvester.generator.helper.SpringContextUtil;
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
 * ADCP NC数据文件生成器
 * Created by cui on 2017/9/22.
 */
public class AdcpNcGenerator {

    private String filePath = "E:\\content\\thredds\\public\\testdata\\station_buoy\\adcp\\adcp.nc";

    //变量与字段名字对应关系
    private static Map<String, String> mapVaribleColumn;
    private int size = 35;

    //浮标维度名称
    private String DIMENSION_FUBIAO = "fubiao";
    //时间维度、变量名称
    private String DIMENSION_TIME = "time";

    //浮标号维度、变量名称
    private String DIMENSION_SVAR = "svar_len";
    private String DIMENSION_FUBIAONAME = "fubiao_name";


    static {
        mapVaribleColumn = Maps.newConcurrentMap();
        mapVaribleColumn.put("liushu", "流速");
        mapVaribleColumn.put("liuxxiang", "流向");
    }

    public void generate() {
        NetcdfFileWriter dataFile = null;
        try (Connection connection = getConnection()) {
            dataFile = NetcdfFileWriter.createNew(NetcdfFileWriter.Version.netcdf3, filePath);

            List<String> fubiaoValue = getFubiaoValue(connection);
            List<String> timeValue = getTimeValue(connection);

            //写meta信息
            doNcMetaWrite(dataFile, fubiaoValue, timeValue);

            //写浮标、时间变量数据
            doFubiaoTimeVarWrite(dataFile, fubiaoValue, timeValue);

            //写测量数据
            doMeasuredVarWrite(connection, dataFile, fubiaoValue, timeValue);
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
     * @param fubiaoValue：浮标值
     * @param timeValue：时间值
     * @throws IOException
     */
    private void doNcMetaWrite(NetcdfFileWriter dataFile, List<String> fubiaoValue, List<String> timeValue) throws IOException {
        Dimension fubiaoDim = dataFile.addDimension(null, DIMENSION_FUBIAO, fubiaoValue.size());
        Dimension timeDim = dataFile.addDimension(null, DIMENSION_TIME, timeValue.size());

        //浮标号维度
        Dimension svar_len = dataFile.addDimension(null, DIMENSION_SVAR, 4);
        Dimension fubiao_nameDim = dataFile.addDimension(null, DIMENSION_FUBIAONAME, fubiaoValue.size());

        //浮标号变量
        List<Dimension> dim1 = Lists.newArrayList();
        dim1.add(fubiao_nameDim);
        dim1.add(svar_len);
        Variable fubiao_nameVar = dataFile.addVariable(null, DIMENSION_FUBIAONAME, DataType.CHAR, dim1);

        //时间变量
        List<Dimension> dim2 = Lists.newArrayList();
        dim2.add(timeDim);
        Variable timeVar = dataFile.addVariable(null, DIMENSION_TIME, DataType.DOUBLE, dim2);
        timeVar.addAttribute(new Attribute("long_name", "time"));
        timeVar.addAttribute(new Attribute("units", "minutes since " + timeValue.get(0)));

        List<Dimension> dims = Lists.newArrayList();
        dims.add(fubiaoDim);
        dims.add(timeDim);

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
     * @param fubiaoValue
     * @param timeValue
     * @throws ParseException
     * @throws IOException
     * @throws InvalidRangeException
     */
    private void doFubiaoTimeVarWrite(NetcdfFileWriter dataFile, List<String> fubiaoValue, List<String> timeValue) throws ParseException, IOException, InvalidRangeException {
        //写浮标号数据
        Variable fubiaoVariable = dataFile.findVariable(DIMENSION_FUBIAONAME);
        int[] shape = fubiaoVariable.getShape();
        try {
            ArrayChar ac2 = new ArrayChar.D2(shape[0], shape[1]);
            Index ima = ac2.getIndex();
            for (int i = 0; i < fubiaoValue.size(); i++) {
                ac2.setString(ima.set(i), fubiaoValue.get(i));
            }
            dataFile.write(fubiaoVariable, ac2);
        } catch (IOException e) {
            System.err.println("ERROR writing Achar3");
            assert (false);
        } catch (InvalidRangeException e) {
            e.printStackTrace();
            assert (false);
        }

        //写时间变量数据
        Preconditions.checkArgument(timeValue.size() > 0);
        String startTimeString = timeValue.get(0);

        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmm");
        Date startTime = df.parse(String.valueOf("20" + startTimeString));
        DateTime startDateTime = new DateTime(startTime);
        List<Double> lstOffset = Lists.newArrayList();
        for (String datetime : timeValue) {
            Date tmpDate = df.parse("20" + datetime);
            DateTime tmpDateTime = new DateTime(tmpDate);
            int offset = Minutes.minutesBetween(startDateTime, tmpDateTime).getMinutes();
            lstOffset.add((double) offset);
        }

        Array dataTime = Array.factory(DataType.DOUBLE, new int[]{timeValue.size()});
        for (int i = 0; i < lstOffset.size(); i++) {
            dataTime.setDouble(i, lstOffset.get(i));
        }
        Variable timeVarible = dataFile.findVariable(DIMENSION_TIME);
        dataFile.write(timeVarible, dataTime);
    }

    /**
     * 写测量数据
     *
     * @param connection
     * @param dataFile
     * @param fubiaoValue
     * @param timeValue
     * @throws SQLException
     * @throws IOException
     * @throws InvalidRangeException
     */
    private void doMeasuredVarWrite(Connection connection, NetcdfFileWriter dataFile, List<String> fubiaoValue, List<String> timeValue) throws SQLException, IOException, InvalidRangeException {
        for (int i = 1; i <= size; i++) {
            int[] iDim = new int[]{fubiaoValue.size(), timeValue.size()};
            for (Map.Entry<String, String> entry : mapVaribleColumn.entrySet()) {
                String variableName = entry.getKey();
                String columnName = entry.getValue();

                Array dataArray = Array.factory(DataType.FLOAT, iDim);
                Index2D idx = new Index2D(iDim);

                try (PreparedStatement ps = connection.prepareStatement("select 浮标号,日期时间," + columnName + i + " from TabADCP")) {
                    ResultSet rs = ps.executeQuery();
                    while (rs.next()) {
                        String fubiaoColVal = rs.getString(1);
                        String timeColVal = rs.getString(2);
                        float data = (float) -99.9;
                        try {
                            data = rs.getFloat(3);
                        } catch (Exception e) {
                            System.out.println("error eatened!");
                        }
                        int fubiaoIndex = fubiaoValue.indexOf(fubiaoColVal);
                        int timeIndex = timeValue.indexOf(timeColVal);
                        idx.set(fubiaoIndex, timeIndex);
                        dataArray.setFloat(idx, data);
                    }
                }
                Variable variable = dataFile.findVariable(variableName + i);
                dataFile.write(variable, dataArray);
            }
        }
    }


    private List<String> getFubiaoValue(Connection connection) throws SQLException {
        List<String> result = Lists.newArrayList();
        try (PreparedStatement ps = connection.prepareStatement("select distinct 浮标号 from TabADCP")) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                result.add(rs.getString(1));
            }
        }
        return result;
    }

    private List<String> getTimeValue(Connection connection) throws SQLException {
        List<String> result = Lists.newArrayList();
        try (PreparedStatement ps = connection.prepareStatement("select distinct 日期时间 from TabADCP ORDER BY 日期时间")) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                result.add(rs.getString(1));
            }
        }
        return result;
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
