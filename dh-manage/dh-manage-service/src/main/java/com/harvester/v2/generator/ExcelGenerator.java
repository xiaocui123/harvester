package com.harvester.v2.generator;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.io.Files;
import com.harvester.HarvesterConstants;
import com.harvester.manage.pojo.DataSetInfo;
import com.harvester.v2.config.DataSet;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.joda.time.DateTime;
import org.joda.time.Minutes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ucar.ma2.*;
import ucar.nc2.Attribute;
import ucar.nc2.Dimension;
import ucar.nc2.NetcdfFileWriter;
import ucar.nc2.Variable;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Created by cui on 2017/11/22.
 */
public class ExcelGenerator extends AbstractGenerator {

    private Logger logger = LoggerFactory.getLogger(getClass());

    private Workbook workbook;

    public ExcelGenerator(DataSet dataSet) {
        super(dataSet);
        //init workbook
        String filePath = dataSet.getSource().getExel().getPath();
        Preconditions.checkArgument(!Strings.isNullOrEmpty(filePath), "Excel数据源路径为空！");

        try {
            if (filePath.endsWith(".xlsx")) {
                workbook = new XSSFWorkbook(new FileInputStream(new File(filePath)));
            } else {
                workbook = new HSSFWorkbook(new FileInputStream(new File(filePath)));
            }
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }

    @Override
    public DataSetInfo generate() {
        DataSetInfo dataSetInfo = new DataSetInfo();
        String uuid = UUID.randomUUID().toString();
        dataSetInfo.setDatasetId(uuid);
        NetcdfFileWriter dataFile = null;
        try {
            String ncFilePath = rootDirPath + uuid + File.separator + dataSet.getDatasetName() + ".nc";
            dataSetInfo.setDatasetNcFilepath(ncFilePath);
            try {
                Files.createParentDirs(new File(ncFilePath));
            } catch (IOException e) {
                logger.info("生成NC文件父目录失败！");
                throw new IllegalArgumentException(e);
            }

            dataFile = NetcdfFileWriter.createNew(NetcdfFileWriter.Version.netcdf3, ncFilePath);

            List<Double> lstTime = getTime();

            doNcMetaWrite(dataFile, lstTime);

            doNcDataWrite(dataFile, lstTime);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvalidRangeException e) {
            e.printStackTrace();
        } catch (ParseException e) {
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
        return dataSetInfo;
    }

    private List<Double> getTime() throws IOException, ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        List<Double> lstTime = Lists.newArrayList();
        if (workbook.getNumberOfSheets() > 0) {
            Sheet sheet = workbook.getSheetAt(0);
            String stationName = dataSet.getStation().getStationVariable().getName();
            int stationIndex = Integer.parseInt(dataSet.getStation().getStationVariable().getColumn());
            int startRow = 1;
            for (int rowIndex = startRow, rowLength = sheet
                    .getPhysicalNumberOfRows(); rowIndex < rowLength; rowIndex++) {
                Row row = sheet.getRow(rowIndex);
                if (stationName.equals((String) getCellValue(row.getCell(stationIndex)))) {
                    int dataTimeIndex = Integer.parseInt(dataSet.getTime().getColumn());
                    Cell cell = row.getCell(dataTimeIndex);
                    String dateTime = (String) getCellValue(cell);
                    Date date = sdf.parse(dateTime);
                    DateTime now = new DateTime(date);

                    Date tmp = sdf.parse("1970-1-1 00:00");
                    DateTime tmpDateTime = new DateTime(tmp);
                    int delay = Minutes.minutesBetween(tmpDateTime, now).getMinutes();
                    lstTime.add((double) delay);
                } else {
                    break;
                }
            }
        }
        return lstTime;
    }

    private void doNcMetaWrite(NetcdfFileWriter dataFile, List<Double> lstTime) throws IOException, InvalidRangeException {
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
        timeVar.addAttribute(new Attribute("units", "minutes since " + "1970-1-1 00:00:00"));
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
            Variable depthVar = dataFile.addVariable(null, varDepthName, DataType.DOUBLE, HarvesterConstants.DIMENSION_TIME);
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

    private void doNcDataWrite(NetcdfFileWriter dataFile, List<Double> lstTime) throws IOException, InvalidRangeException {

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
            doWriteMeaturedData(dataFile, dataSet.getDepth());

        }
        for (com.harvester.v2.config.Variable variable : dataSet.getMeaturedVariables().getMeaturedVariable()) {
            //写测量数据
            doWriteMeaturedData(dataFile, variable);

        }
    }

    private void doWriteMeaturedData(NetcdfFileWriter dataFile, com.harvester.v2.config.Variable variable) throws IOException, InvalidRangeException {
        List<Double> lstData = Lists.newArrayList();
        if (workbook.getNumberOfSheets() > 0) {
            Sheet sheet = workbook.getSheetAt(0);
            String stationName = dataSet.getStation().getStationVariable().getName();
            int stationIndex = Integer.parseInt(dataSet.getStation().getStationVariable().getColumn());
            int startRow = 1;
            for (int rowIndex = startRow, rowLength = sheet
                    .getPhysicalNumberOfRows(); rowIndex < rowLength; rowIndex++) {
                Row row = sheet.getRow(rowIndex);
                if (stationName.equals((String) getCellValue(row.getCell(stationIndex)))) {
                    int dataIndex = Integer.parseInt(variable.getColumn());
                    Cell cell = row.getCell(dataIndex);
                    lstData.add((Double) getCellValue(cell));
                } else {
                    break;
                }
            }
        }
        Variable var = dataFile.findVariable(variable.getName());
        Array arrayData = Array.factory(DataType.DOUBLE, new int[]{lstData.size()});
        for (int i = 0; i < lstData.size(); i++) {
            arrayData.setDouble(i, lstData.get(i));
        }
        dataFile.write(var, arrayData);

    }

    private static Object getCellValue(Cell cell) {
        switch (cell.getCellType()) {
            case Cell.CELL_TYPE_STRING:
                return cell.getStringCellValue();

            case Cell.CELL_TYPE_BOOLEAN:
                return cell.getBooleanCellValue();

            case Cell.CELL_TYPE_NUMERIC:
                if (HSSFDateUtil.isCellDateFormatted(cell)) {
                    //  如果是date类型则 ，获取该cell的date值
                    short dataFormat = cell.getCellStyle().getDataFormat();
                    if (dataFormat == 182 || dataFormat == 31 || dataFormat == 57 || dataFormat == 58) {
                        //日期
                        double value = cell.getNumericCellValue();
                        Date date = org.apache.poi.ss.usermodel.DateUtil.getJavaDate(value);
                        return new SimpleDateFormat("yyyy-MM-dd HH:mm").format(date);

                    } else if (dataFormat == HSSFDataFormat.getBuiltinFormat("h:mm")) {
                        //时间
                        return new SimpleDateFormat("HH:mm").format(cell.getDateCellValue());
                    } else {
                        //不支持
                        System.out.println(cell.getRow().getRowNum() + ":" + cell.getColumnIndex());
                        throw new IllegalArgumentException("不支持的日期时间格式【" + dataFormat + "】");
                    }
                } else {
                    // 纯数字
                    return cell.getNumericCellValue();
                }
            case Cell.CELL_TYPE_FORMULA:
                return cell.getNumericCellValue();
        }

        return null;
    }

}
