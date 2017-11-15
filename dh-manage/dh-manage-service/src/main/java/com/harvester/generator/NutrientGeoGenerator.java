package com.harvester.generator;

import com.google.common.collect.Lists;
import com.harvester.generator.helper.HarvesterConfig;
import com.harvester.generator.helper.SpringContextUtil;
import com.harvester.generator.helper.nutrient.MeaturedDataType;
import com.harvester.generator.helper.nutrient.NutrientDataType;
import com.harvester.generator.helper.nutrient.NutrientExcelReader;
import org.aspectj.weaver.ast.Var;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ucar.ma2.*;
import ucar.nc2.Attribute;
import ucar.nc2.Dimension;
import ucar.nc2.NetcdfFileWriter;
import ucar.nc2.Variable;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by cui on 2017/11/12.
 */
public class NutrientGeoGenerator {

    private Logger logger = LoggerFactory.getLogger(getClass());

    //站们维度名称
    private String DIMENSION_STATION = "station";
    //lat
    private String DIMENSION_LAT = "latitude";
    private String DIMENSION_LON = "longitude";

    //字符串辅助变量
    private String DIMENSION_SVAR = "svar_len";

    //站点变量
    private String VARIABLE_STATIONNAME = "station_name";
    //经纬度变量
    private String VARIABLE_LON = "longitude";
    private String VARIABLE_LAT = "latitude";
    //PO4
    private String VARIABLE_PO4 = "PO4";
    //NO3
    private String VARIABLE_NO3 = "NO3";
    //SIO3
    private String VARIABLE_SIO3 = "SIO3";


    public void generator(File excelFile) {
        List<NutrientDataType> lstNutrient = NutrientExcelReader.readExcel(excelFile);
        Collections.sort(lstNutrient, new Comparator<NutrientDataType>() {
            @Override
            public int compare(NutrientDataType o1, NutrientDataType o2) {
                return new Double(o1.getLat()).compareTo(new Double(o2.getLat()));
            }
        });

        NetcdfFileWriter dataFile = null;
        try {
            String filePath = getFilePath(excelFile);

            dataFile = NetcdfFileWriter.createNew(NetcdfFileWriter.Version.netcdf3, filePath);

            //添加维度
            int dimLength = lstNutrient.size();
            System.out.println("站点维度：" + dimLength);
            Dimension svar_len = dataFile.addDimension(null, DIMENSION_SVAR, 50);
            Dimension stationDim = dataFile.addDimension(null, DIMENSION_STATION, dimLength);

            Dimension latDim = dataFile.addDimension(null, DIMENSION_LAT, dimLength);
            Dimension lonDim = dataFile.addDimension(null, DIMENSION_LON, dimLength);


            //添加变量

            List<Dimension> lstStationDim = Lists.newArrayList();
            lstStationDim.add(stationDim);
            lstStationDim.add(svar_len);

            //站点变量
            Variable stationNameVar = dataFile.addVariable(null, VARIABLE_STATIONNAME, DataType.CHAR, lstStationDim);

            //lon、lat变量
            Variable lonVar = dataFile.addVariable(null, VARIABLE_LON, DataType.DOUBLE, DIMENSION_LON);
            lonVar.addAttribute(new Attribute("long_name", "longitude"));
            lonVar.addAttribute(new Attribute("axis", "X"));
            lonVar.addAttribute(new Attribute("standard_name", "longitude"));

            ArrayChar aChar = new ArrayChar.D1(20);
            String tmp = "degrees_east";
            for (int index = 0; index < tmp.length(); index++) {
                aChar.setChar(index, tmp.charAt(index));
            }
            lonVar.addAttribute(new Attribute("units", aChar));

            Variable latVar = dataFile.addVariable(null, VARIABLE_LAT, DataType.DOUBLE, DIMENSION_LAT);
            latVar.addAttribute(new Attribute("long_name", "latitude"));
            latVar.addAttribute(new Attribute("axis", "Y"));
            latVar.addAttribute(new Attribute("standard_name", "latitude"));

            ArrayChar bChar = new ArrayChar.D1(20);
            String btmp = "degrees_north";
            for (int index = 0; index < btmp.length(); index++) {
                bChar.setChar(index, btmp.charAt(index));
            }
            latVar.addAttribute(new Attribute("units", bChar));

            List<Dimension> lstMeaturedDim = Lists.newArrayList();
            lstMeaturedDim.add(latDim);
            lstMeaturedDim.add(lonDim);
            //PO4、NO3、SIO3变量
            dataFile.addVariable(null, VARIABLE_PO4, DataType.DOUBLE, lstMeaturedDim);
            dataFile.addVariable(null, VARIABLE_NO3, DataType.DOUBLE, lstMeaturedDim);
            dataFile.addVariable(null, VARIABLE_SIO3, DataType.DOUBLE, lstMeaturedDim);

            dataFile.create();


            //写站位
            writeStationVariable(dataFile, lstNutrient);

            //写水深、经度、纬度
            writeLonLat(dataFile, lstNutrient);

            //水层
            writeLayerMeaturedVar(dataFile, lstNutrient);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (dataFile != null) {
                try {
                    dataFile.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void writeLayerMeaturedVar(NetcdfFileWriter dataFile, List<NutrientDataType> lstNutrient) throws IOException, InvalidRangeException {

        int length = lstNutrient.size();
        int[] iDim = new int[]{length, length};
        Index2D idx = new Index2D(iDim);
        Array PO4Data = Array.factory(DataType.DOUBLE, iDim);
        Array NO3Data = Array.factory(DataType.DOUBLE, iDim);
        Array SIO3Data = Array.factory(DataType.DOUBLE, iDim);

        int index = 0;
        for (NutrientDataType nutrient : lstNutrient) {
            MeaturedDataType meaturedData = nutrient.getLstMeaturedData().get(0);
            PO4Data.setDouble(idx.set(index, index), meaturedData.getPo4());
            NO3Data.setDouble(idx.set(index, index), meaturedData.getNo3());
            SIO3Data.setDouble(idx.set(index, index), meaturedData.getSio3());
            index++;
            System.out.println("index=" + index);
        }
        Variable PO4Var = dataFile.findVariable(VARIABLE_PO4);
        dataFile.write(PO4Var, PO4Data);

        Variable NO3Var = dataFile.findVariable(VARIABLE_NO3);
        dataFile.write(NO3Var, NO3Data);

        Variable SIO3Var = dataFile.findVariable(VARIABLE_SIO3);
        dataFile.write(SIO3Var, SIO3Data);
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

    private void writeLonLat(NetcdfFileWriter dataFile, List<NutrientDataType> lstNutrient) throws IOException, InvalidRangeException {
        Array lonData = Array.factory(DataType.DOUBLE, new int[]{lstNutrient.size()});
        double test=lstNutrient.get(0).getLon();
        double offset=0.002;
        for (int i = 0; i < lstNutrient.size(); i++) {
            lonData.setDouble(i, test+i*offset);
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

    /**
     * 返回生成NC文件名称，与excel同名
     *
     * @param excelFile
     * @return
     */
    private String getFilePath(File excelFile) {
        String absolutePath = excelFile.getAbsolutePath();
        String fileName = absolutePath.substring(absolutePath.lastIndexOf(File.separator) + 1, absolutePath.lastIndexOf(".")) + ".nc";

        HarvesterConfig harvesterConfig = (HarvesterConfig) SpringContextUtil.getBean("harvesterconfig");
        String rootDirPath = harvesterConfig.getShipNutrientFilePath();
        return rootDirPath + fileName;
    }

}
