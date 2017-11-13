package com.harvester.generator;

import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.io.Files;
import com.harvester.generator.helper.AccessConnectionInfo;
import com.harvester.generator.helper.HarvesterConfig;
import com.harvester.generator.helper.SpringContextUtil;
import com.harvester.manage.pojo.BuoyInfo;
import com.harvester.station.config.*;
import org.javatuples.Pair;
import org.joda.time.DateTime;
import org.joda.time.Minutes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ucar.ma2.*;
import ucar.ma2.Array;
import ucar.nc2.Attribute;
import ucar.nc2.Dimension;
import ucar.nc2.NetcdfFileWriter;
import ucar.nc2.Variable;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * 定点浮标时间序列数据NC文件生成器
 * Created by cui on 2017/10/30.
 */
public class StationSerialDataGenerator {
    private Logger logger = LoggerFactory.getLogger(getClass());

    private Timeserial timeserial;

    //浮标维度名称
    private String DIMENSION_STATION = "station";
    //时间维度名称
    private String DIMENSION_OBS = "obs";

    private String VARIABLE_TIME = "time";
    private String VARIABLE_ROWSIZE = "row_size";

    //经纬度变量
    private String VARIABLE_LON = "lon";
    private String VARIABLE_LAT = "lat";

    //浮标号维度、变量名称
    private String DIMENSION_SVAR = "svar_len";
    private String DIMENSION_STATIONNAME = "station_name";

    private String rootDirPath;

    private SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmm");

    public void initTimeSerial(File file) {
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(Timeserial.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            this.timeserial = (Timeserial) jaxbUnmarshaller.unmarshal(file);

            HarvesterConfig harvesterConfig = (HarvesterConfig) SpringContextUtil.getBean("harvesterconfig");
            this.rootDirPath = harvesterConfig.getBuoyFilePath();

        } catch (JAXBException e) {
            throw new IllegalArgumentException("解析浮标配置文件失败！", e);
        }
    }

    public BuoyInfo generate() {
        BuoyInfo buoyInfo = new BuoyInfo();
        String uuid = UUID.randomUUID().toString();
        buoyInfo.setBuoyNcId(uuid);
        NetcdfFileWriter dataFile = null;
        String fileName = timeserial.getFileName();
        String ncFilePath = rootDirPath + uuid + File.separator + fileName;
        try {
            Files.createParentDirs(new File(ncFilePath));
        } catch (IOException e) {
            logger.info("生成NC文件父目录失败！");
            throw new IllegalArgumentException(e);
        }
        Preconditions.checkArgument(!Strings.isNullOrEmpty(fileName), "nc文件名称为空！");
        try (Connection connection = getConnection()) {
            dataFile = NetcdfFileWriter.createNew(NetcdfFileWriter.Version.netcdf3, ncFilePath);

            Pair<String, String> timeScopePair = getTimeScope(connection);

            Pair<List<Integer>, List<Double>> rowSizeAndObsValues = getRowSizeAndObsValues(connection, timeScopePair);

            //写meta信息
            String startTime = timeScopePair.getValue0();
            int obsLength = rowSizeAndObsValues.getValue1().size();
            doNcMetaWrite(dataFile, startTime, obsLength);

            //写浮标、时间变量数据
            doSationNameObsRowSizeVarWrite(dataFile, rowSizeAndObsValues);

            //写测量数据
            doMeasuredVarWrite2(dataFile, connection, timeScopePair, rowSizeAndObsValues);
            logger.info("浮标数据【" + ncFilePath + "】生成成功！");

            buoyInfo.setBuoyNcTable(timeserial.getTableName());
            buoyInfo.setBuoyNcStarttime(timeScopePair.getValue0());
            buoyInfo.setBuoyNcEndtime(timeScopePair.getValue1());
            buoyInfo.setBuoyNcFilepath(ncFilePath);
            return buoyInfo;
        } catch (Exception e) {
            logger.error("浮标数据生成失败！" + e.getMessage(), e);
        } finally {
            if (null != dataFile) {
                try {
                    dataFile.close();
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                }
            }
        }
        return null;
    }

    /**
     * 获取序列时间范围
     *
     * @param connection
     * @return
     */
    private Pair<String, String> getTimeScope(Connection connection) throws Exception {
        String startTimeStr = null, endTimeStr = null;
        if (timeserial.getTimeRange() != null) {
            TimeRangeType timeRange = timeserial.getTimeRange();
            Preconditions.checkArgument(timeRange.getStartTime() != null && timeRange.getEndTime() != null);
            Date startTime = timeRange.getStartTime().toGregorianCalendar().getTime();
            Date endTime = timeRange.getEndTime().toGregorianCalendar().getTime();
            startTimeStr = df.format(startTime).substring(2);
            endTimeStr = df.format(endTime).substring(2);
        } else {
            try (PreparedStatement ps = connection.prepareStatement("SELECT min(日期时间) FROM " + timeserial.getTableName())) {
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    startTimeStr = rs.getString(1);
                }
            }
            try (PreparedStatement ps = connection.prepareStatement("SELECT max(日期时间) FROM " + timeserial.getTableName())) {
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    endTimeStr = rs.getString(1);
                }
            }
        }
        return Pair.with(startTimeStr, endTimeStr);
    }

    /**
     * 查询浮标序列长度、浮标的时间序列值
     *
     * @param connection
     * @param timeScopePair
     * @return
     * @throws SQLException
     */
    private Pair<List<Integer>, List<Double>> getRowSizeAndObsValues(Connection connection, Pair<String, String> timeScopePair) throws SQLException {
        List<Integer> lstRowSize = Lists.newArrayList();
        List<Double> lstTime = Lists.newArrayList();

        String startTime = timeScopePair.getValue0();
        String endTime = timeScopePair.getValue1();
        DateTime starTime = convert(startTime);

        List<String> lstStationName = getStationNames();

        for (String stationName : lstStationName) {
            int count = 0;
            try (PreparedStatement ps = connection.prepareStatement("select 日期时间 from " + timeserial.getTableName() + " where 浮标号 ='" + stationName + "' and 日期时间 >= '" + startTime + "' and 日期时间 <= '" + endTime + "' ORDER BY 日期时间")) {
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

    private void doNcMetaWrite(NetcdfFileWriter dataFile, String startTime, int obsLength) throws Exception {
        int stationLength = timeserial.getStations().getStation().size();
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
        timeVar.addAttribute(new Attribute("units", "minutes since " + startTime));

        //RowSize变量
        List<Dimension> dim3 = Lists.newArrayList();
        dim3.add(stationDim);
        dataFile.addVariable(null, VARIABLE_ROWSIZE, DataType.INT, dim3);

        //lon、lat变量
        dataFile.addVariable(null, VARIABLE_LON, DataType.DOUBLE, dim3);
        dataFile.addVariable(null, VARIABLE_LAT, DataType.DOUBLE, dim3);


        List<Dimension> dims = Lists.newArrayList();
        dims.add(obsDim);
        //测量变量
        List<VariableType> lstVar = timeserial.getVariables().getVariable();
        for (VariableType var : lstVar) {
            ColumnType columnType = var.getColumnName();
            if (columnType.getMin() == null && columnType.getMin() == null) {
                Variable variable = dataFile.addVariable(null, var.getVarName(), DataType.DOUBLE, dims);
                for (AttributionType attribute : var.getAttributions().getAttribution()) {
                    variable.addAttribute(new Attribute(attribute.getAttriName(), new String(attribute.getAttriValue())));
                }
            } else if (columnType.getMin() != null && columnType.getMax() != null) {
                int start = columnType.getMin();
                int end = columnType.getMax();
                for (int index = start; index <= end; index++) {
                    String varName = var.getVarName() + index;
                    Variable variable = dataFile.addVariable(null, varName, DataType.DOUBLE, dims);
                    for (AttributionType attribute : var.getAttributions().getAttribution()) {
                        variable.addAttribute(new Attribute(attribute.getAttriName(), new String(attribute.getAttriValue())));
                    }
                }
            } else {
                throw new IllegalArgumentException("变量【" + columnType.getColumnName() + "】设置不正确！");
            }
        }
        dataFile.create();
    }

    private void doSationNameObsRowSizeVarWrite(NetcdfFileWriter dataFile, Pair<List<Integer>, List<Double>> rowSizeAndObsValues) throws Exception {
        //写浮标号数据
        Variable stationVariable = dataFile.findVariable(DIMENSION_STATIONNAME);
        int[] shape = stationVariable.getShape();
        List<String> lstStationName = getStationNames();
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

        //写lon数据
        List<Double> lstLon = Lists.transform(timeserial.getStations().getStation(), new Function<StationType, Double>() {
            @Override
            public Double apply(StationType input) {
                return input.getLon();
            }
        });
        Array dataLon = Array.factory(DataType.DOUBLE, new int[]{lstLon.size()});
        for (int i = 0; i < lstLon.size(); i++) {
            dataLon.setDouble(i, lstLon.get(i));
        }
        Variable lonVariable = dataFile.findVariable(VARIABLE_LON);
        dataFile.write(lonVariable, dataLon);

        //写lat数据
        List<Double> lstLat = Lists.transform(timeserial.getStations().getStation(), new Function<StationType, Double>() {
            @Override
            public Double apply(StationType input) {
                return input.getLat();
            }
        });
        Array dataLat = Array.factory(DataType.DOUBLE, new int[]{lstLat.size()});
        for (int i = 0; i < lstLat.size(); i++) {
            dataLat.setDouble(i, lstLat.get(i));
        }
        Variable latVariable = dataFile.findVariable(VARIABLE_LAT);
        dataFile.write(latVariable, dataLat);

        //写obs数据
        List<Double> lstObsValue = rowSizeAndObsValues.getValue1();
        Array dataTime = Array.factory(DataType.DOUBLE, new int[]{lstObsValue.size()});
        for (int i = 0; i < lstObsValue.size(); i++) {
            dataTime.setDouble(i, lstObsValue.get(i));
        }
        Variable timeVarible = dataFile.findVariable(VARIABLE_TIME);
        dataFile.write(timeVarible, dataTime);

        //写row_size数据
        List<Integer> lstRowSize = rowSizeAndObsValues.getValue0();
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
     * @param dataFile
     * @param connection
     * @param timeScopePair
     * @param rowSizeAndObsValues
     */
    private void doMeasuredVarWrite2(NetcdfFileWriter dataFile, Connection connection, Pair<String, String> timeScopePair, Pair<List<Integer>, List<Double>> rowSizeAndObsValues) throws Exception {
        List<Integer> lstRowSize = rowSizeAndObsValues.getValue0();
        List<Double> lstObsValue = rowSizeAndObsValues.getValue1();
        List<String> lstStationName = getStationNames();

        List<VariableType> lstVar = timeserial.getVariables().getVariable();
        for (VariableType var : lstVar) {
            ColumnType columnType = var.getColumnName();
            if (columnType.getMin() == null && columnType.getMin() == null) {
                write(dataFile, connection, lstRowSize, lstObsValue, lstStationName, var.getVarName(), columnType.getColumnName(), timeScopePair);
            } else if (columnType.getMin() != null && columnType.getMax() != null) {
                int start = columnType.getMin();
                int end = columnType.getMax();
                for (int index = start; index <= end; index++) {
                    String variableName = var.getVarName() + index;
                    String columnName = columnType.getColumnName() + index;
                    write(dataFile, connection, lstRowSize, lstObsValue, lstStationName, variableName, columnName, timeScopePair);
                }
            } else {
                throw new IllegalArgumentException("变量【" + columnType.getColumnName() + "】设置不正确！");
            }
        }
    }

    /**
     * 写变量数据
     *
     * @param dataFile
     * @param connection
     * @param lstRowSize
     * @param lstObsValue
     * @param lstStationName
     * @param variableName
     * @param columnName
     * @param timeScopePair  @throws SQLException
     * @throws IOException
     * @throws InvalidRangeException
     */
    private void write(NetcdfFileWriter dataFile, Connection connection, List<Integer> lstRowSize, List<Double> lstObsValue, List<String> lstStationName, String variableName, String columnName, Pair<String, String> timeScopePair) throws SQLException, IOException, InvalidRangeException {
        Array measuredData = Array.factory(DataType.DOUBLE, new int[]{lstObsValue.size()});
        for (int index = 0; index < lstStationName.size(); index++) {
            //查询浮标测量数据
            List<Double> lstMearueData = Lists.newArrayList();
            String stationName = lstStationName.get(index);
            String startTime = timeScopePair.getValue0();
            String endTime = timeScopePair.getValue1();

            try (PreparedStatement ps = connection.prepareStatement("SELECT 日期时间, " + columnName + " from " + timeserial.getTableName() + " where 浮标号 = '" + stationName + "' and 日期时间 >= '" + startTime + "' and 日期时间 <= '" + endTime + "' order by 日期时间")) {
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
        Variable variable = dataFile.findVariable(variableName);
        dataFile.write(variable, measuredData);
    }


    private List<String> getStationNames() {
        return Lists.transform(timeserial.getStations().getStation(), new Function<StationType, String>() {
            @Override
            public String apply(StationType input) {
                return input.getStationName();
            }
        });
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
