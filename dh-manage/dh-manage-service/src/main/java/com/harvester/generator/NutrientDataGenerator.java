package com.harvester.generator;

import com.google.common.collect.Lists;
import com.google.common.io.Files;
import com.harvester.generator.helper.HarvesterConfig;
import com.harvester.generator.helper.SpringContextUtil;
import com.harvester.generator.helper.nutrient.MeaturedDataType;
import com.harvester.generator.helper.nutrient.NutrientDataType;
import com.harvester.generator.helper.nutrient.NutrientExcelReader;
import com.harvester.manage.pojo.NutrientInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ucar.ma2.*;
import ucar.nc2.Dimension;
import ucar.nc2.NetcdfFileWriter;
import ucar.nc2.Variable;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

/**
 * Created by cui on 2017/11/7.
 */
public class NutrientDataGenerator {

    private Logger logger = LoggerFactory.getLogger(getClass());

    //站们维度名称
    private String DIMENSION_STATION = "station";
    //时间维度名称
    private String DIMENSION_DATETIME = "datetime";
    //水深维度名称
    private String DIMENSION_DEPTH = "depth";
    //水层维度名称
    private String DIMENSION_LAYER = "layer";

    //字符串辅助变量
    private String DIMENSION_SVAR = "svar_len";

    //海域变量
    private String VARIABLE_SEAS = "seas";
    //站点变量
    private String VARIABLE_STATIONNAME = "station_name";
    //经纬度变量
    private String VARIABLE_LON = "lon";
    private String VARIABLE_LAT = "lat";
    //日期时间变量
    private String VARIABLE_DATETIME = "datetime";
    //水深
    private String VARIABLE_DEPTH = "depth";

    //row_size变量
    private String VARIABLE_ROWSIZE = "row_size";
    //水层
    private String VARIABLE_LAYER = "layer";
    //PO4
    private String VARIABLE_PO4 = "PO4";
    //NO3
    private String VARIABLE_NO3 = "NO3";
    //SIO3
    private String VARIABLE_SIO3 = "SIO3";


    public NutrientInfo generator(File excelFile) {
        NutrientInfo nutrientInfo = new NutrientInfo();
        String uuid = UUID.randomUUID().toString();
        nutrientInfo.setNutrientId(uuid);

        List<NutrientDataType> lstNutrient = NutrientExcelReader.readExcel(excelFile);

        NetcdfFileWriter dataFile = null;
        try {
            String filePath = getFilePath(uuid, excelFile);
            nutrientInfo.setNutrientNcFilepath(filePath);

            dataFile = NetcdfFileWriter.createNew(NetcdfFileWriter.Version.netcdf3, filePath);

            //添加维度
            int dimLength = lstNutrient.size();
            System.out.println("站点维度：" + dimLength);
            Dimension svar_len = dataFile.addDimension(null, DIMENSION_SVAR, 50);
            Dimension stationDim = dataFile.addDimension(null, DIMENSION_STATION, dimLength);
            Dimension datetimeDim = dataFile.addDimension(null, DIMENSION_DATETIME, dimLength);
            Dimension depthDim = dataFile.addDimension(null, DIMENSION_DEPTH, dimLength);

            int layerLength = getLayerLength(lstNutrient);
            System.out.println("水层维度：" + layerLength);
            Dimension layerDim = dataFile.addDimension(null, DIMENSION_LAYER, layerLength);

            //添加变量

            List<Dimension> lstStationDim = Lists.newArrayList();
            lstStationDim.add(stationDim);
            lstStationDim.add(svar_len);

            //海域变量
            Variable seasVar = dataFile.addVariable(null, VARIABLE_SEAS, DataType.CHAR, lstStationDim);

            //站点变量
            Variable stationNameVar = dataFile.addVariable(null, VARIABLE_STATIONNAME, DataType.CHAR, lstStationDim);

            //lon、lat变量
            dataFile.addVariable(null, VARIABLE_LON, DataType.DOUBLE, DIMENSION_STATION);
            dataFile.addVariable(null, VARIABLE_LAT, DataType.DOUBLE, DIMENSION_STATION);

            //采样日期变量
            dataFile.addVariable(null, VARIABLE_DATETIME, DataType.CHAR, lstStationDim);

            //水深变量
            dataFile.addVariable(null, VARIABLE_DEPTH, DataType.DOUBLE, DIMENSION_STATION);

            //ROW_SIZE
            dataFile.addVariable(null, VARIABLE_ROWSIZE, DataType.INT, DIMENSION_STATION);

            //水层变量
            dataFile.addVariable(null, VARIABLE_LAYER, DataType.DOUBLE, DIMENSION_LAYER);

            //PO4、NO3、SIO3变量
            dataFile.addVariable(null, VARIABLE_PO4, DataType.DOUBLE, DIMENSION_LAYER);
            dataFile.addVariable(null, VARIABLE_NO3, DataType.DOUBLE, DIMENSION_LAYER);
            dataFile.addVariable(null, VARIABLE_SIO3, DataType.DOUBLE, DIMENSION_LAYER);

            dataFile.create();

            //写海域
            writeSeasVariable(dataFile, lstNutrient);

            //写日期时间
            writeDateTimeVariable(dataFile, lstNutrient);

            //写站位
            writeStationVariable(dataFile, lstNutrient);

            //写水深、经度、纬度
            writeDepthLonLat(dataFile, lstNutrient);

            writeRowSize(dataFile, lstNutrient);

            //水层
            writeLayerMeaturedVar(dataFile, lstNutrient);

            return nutrientInfo;


        } catch (Exception e) {
            logger.error("营养盐数据生成失败！"+e.getMessage(),e);
        } finally {
            if (dataFile != null) {
                try {
                    dataFile.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        throw new IllegalArgumentException("营养盐生成数据失败！");
    }

    /**
     * 返回生成NC文件名称，与excel同名
     *
     * @param uuid
     * @param excelFile
     * @return
     */
    private String getFilePath(String uuid, File excelFile) throws IOException {
        String absolutePath = excelFile.getAbsolutePath();
        String fileName = absolutePath.substring(absolutePath.lastIndexOf(File.separator) + 1, absolutePath.lastIndexOf(".")) + ".nc";

        HarvesterConfig harvesterConfig = (HarvesterConfig) SpringContextUtil.getBean("harvesterconfig");
        String filePath = harvesterConfig.getShipNutrientFilePath() + uuid + File.separator + fileName;
        Files.createParentDirs(new File(filePath));
        return filePath;
    }

    private void writeRowSize(NetcdfFileWriter dataFile, List<NutrientDataType> lstNutrient) throws IOException, InvalidRangeException {
        Array rowsizeData = Array.factory(DataType.INT, new int[]{lstNutrient.size()});
        for (int i = 0; i < lstNutrient.size(); i++) {
            rowsizeData.setInt(i, lstNutrient.get(i).getLstMeaturedData().size());
        }
        Variable rowsizeVar = dataFile.findVariable(VARIABLE_ROWSIZE);
        dataFile.write(rowsizeVar, rowsizeData);
    }

    private void writeLayerMeaturedVar(NetcdfFileWriter dataFile, List<NutrientDataType> lstNutrient) throws IOException, InvalidRangeException {
        int layerLength = getLayerLength(lstNutrient);

        Array layerdata = Array.factory(DataType.DOUBLE, new int[]{layerLength});

        Array PO4Data = Array.factory(DataType.DOUBLE, new int[]{layerLength});
        Array NO3Data = Array.factory(DataType.DOUBLE, new int[]{layerLength});
        Array SIO3Data = Array.factory(DataType.DOUBLE, new int[]{layerLength});

        int index = 0;
        for (NutrientDataType nutrient : lstNutrient) {
            for (MeaturedDataType meaturedData : nutrient.getLstMeaturedData()) {
                layerdata.setDouble(index, meaturedData.getLevel());
                PO4Data.setDouble(index, meaturedData.getPo4());
                NO3Data.setDouble(index, meaturedData.getNo3());
                SIO3Data.setDouble(index, meaturedData.getSio3());
                index++;
            }
        }
        Variable layerVariable = dataFile.findVariable(VARIABLE_LAYER);
        dataFile.write(layerVariable, layerdata);

        Variable PO4Var = dataFile.findVariable(VARIABLE_PO4);
        dataFile.write(PO4Var, PO4Data);

        Variable NO3Var = dataFile.findVariable(VARIABLE_NO3);
        dataFile.write(NO3Var, NO3Data);

        Variable SIO3Var = dataFile.findVariable(VARIABLE_SIO3);
        dataFile.write(SIO3Var, SIO3Data);
    }


    private void writeDateTimeVariable(NetcdfFileWriter dataFile, List<NutrientDataType> lstNutrient) {
        Variable stationVariable = dataFile.findVariable(VARIABLE_DATETIME);
        int[] shape = stationVariable.getShape();
        try {
            ArrayChar aChar = new ArrayChar.D2(shape[0], shape[1]);
            Index ima = aChar.getIndex();
            for (int i = 0; i < lstNutrient.size(); i++) {
                String datetime = lstNutrient.get(i).getDate() + " " + lstNutrient.get(i).getTime();
                aChar.setString(ima.set(i), datetime);
            }
            dataFile.write(stationVariable, aChar);
        } catch (IOException e) {
            System.err.println("ERROR writing Achar3");
        } catch (InvalidRangeException e) {
            e.printStackTrace();
        }
    }

    private void writeStationVariable(NetcdfFileWriter dataFile, List<NutrientDataType> lstNutrient) {
        Variable stationVariable = dataFile.findVariable(VARIABLE_STATIONNAME);
        int[] shape = stationVariable.getShape();
        try {
            ArrayChar aChar = new ArrayChar.D2(shape[0], shape[1]);
            Index ima = aChar.getIndex();
            for (int i = 0; i < lstNutrient.size(); i++) {
                aChar.setString(ima.set(i), lstNutrient.get(i).getZhanwei());
            }
            dataFile.write(stationVariable, aChar);
        } catch (IOException e) {
            System.err.println("ERROR writing Achar3");
        } catch (InvalidRangeException e) {
            e.printStackTrace();
        }

    }

    private void writeSeasVariable(NetcdfFileWriter dataFile, List<NutrientDataType> lstNutrient) {
        Variable seasVariable = dataFile.findVariable(VARIABLE_SEAS);
        int[] shape = seasVariable.getShape();
        try {
            ArrayChar aChar = new ArrayChar.D2(shape[0], shape[1]);
            Index ima = aChar.getIndex();
            for (int i = 0; i < lstNutrient.size(); i++) {
                aChar.setString(ima.set(i), lstNutrient.get(i).getHaiyu());
            }
            dataFile.write(seasVariable, aChar);
        } catch (IOException e) {
            System.err.println("ERROR writing Achar3");
        } catch (InvalidRangeException e) {
            e.printStackTrace();
        }
    }

    private void writeDepthLonLat(NetcdfFileWriter dataFile, List<NutrientDataType> lstNutrient) throws IOException, InvalidRangeException {
        Array dataDepth = Array.factory(DataType.DOUBLE, new int[]{lstNutrient.size()});
        for (int i = 0; i < lstNutrient.size(); i++) {
            dataDepth.setDouble(i, lstNutrient.get(i).getDepth());
        }
        Variable depthVar = dataFile.findVariable(VARIABLE_DEPTH);
        dataFile.write(depthVar, dataDepth);

        Array lonData = Array.factory(DataType.DOUBLE, new int[]{lstNutrient.size()});
        for (int i = 0; i < lstNutrient.size(); i++) {
            lonData.setDouble(i, lstNutrient.get(i).getLon());
        }
        Variable lonVar = dataFile.findVariable(VARIABLE_LON);
        dataFile.write(lonVar, lonData);

        Array latData = Array.factory(DataType.DOUBLE, new int[]{lstNutrient.size()});
        for (int i = 0; i < lstNutrient.size(); i++) {
            latData.setDouble(i, lstNutrient.get(i).getLat());
        }
        Variable latVar = dataFile.findVariable(VARIABLE_LAT);
        dataFile.write(latVar, latData);
    }

    private int getLayerLength(List<NutrientDataType> lstNutrient) {
        int count = 0;
        for (NutrientDataType nutrient : lstNutrient) {
            count += nutrient.getLstMeaturedData().size();
        }
        return count;
    }
}
