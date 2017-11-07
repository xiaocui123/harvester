package com.harvester.generator;

import com.google.common.collect.Maps;
import ucar.ma2.Array;
import ucar.ma2.ArrayFloat;
import ucar.nc2.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * SODA NC大文件数据切割器，按月切割
 * Created by cui on 2017/10/24.
 */
public class SodaDataCutter {

    public static void main(String[] args) {
        String filename = "H:\\soda3.3.1_mn_ocean_reg_1980.nc";
        NetcdfFile ncfile = null;
        try {
            ncfile = NetcdfFile.open(filename);
            for (int i = 0; i < 12; i++) {
                process(ncfile, i);
            }
        } catch (Exception ioe) {
            ioe.printStackTrace();
            System.out.println("trying to open " + filename);
        } finally {
            if (null != ncfile) try {
                ncfile.close();
            } catch (IOException ioe) {
                System.out.println("trying to close " + filename);
            }
        }
    }

    /**
     * 切分出某一月的nc数据文件
     *
     * @param ncfile
     * @param month
     * @throws Exception
     */
    private static void process(NetcdfFile ncfile, int month) throws Exception {

        String targetNcFilePath = "E://content//thredds/public//testdata/soda3.3.1_mn_ocean_reg_1980//soda3.3.1_0" + month + ".nc";

        String monthCondition = month + ":" + month + ":1";

        NetcdfFileWriter dataFile = null;
        try {
            dataFile = NetcdfFileWriter.createNew(NetcdfFileWriter.Version.netcdf3, targetNcFilePath);

            List<Dimension> lstDimension = ncfile.getDimensions();
            //保存新的维度
            Map<String, Dimension> mapDimension = Maps.newConcurrentMap();
            for (Dimension dimension : lstDimension) {
                String name = dimension.getShortName();
                System.out.println("dimensionName:" + name);
                int shape = dimension.getLength();
                //每个月
                if (name.equals("time")) {
                    shape = 1;
                }
                Dimension newDimension = dataFile.addDimension(null, name, shape);
                mapDimension.put(name, newDimension);
            }

            Map<String, Variable> mapVariable = Maps.newHashMap();
            List<Variable> lstVariable = ncfile.getVariables();
            for (Variable variable : lstVariable) {
                String variableName = variable.getShortName();
                Variable var = null;
                //和维度相同的变量
                if (mapDimension.containsKey(variable.getShortName())) {
                    var = dataFile.addVariable(null, variableName, variable.getDataType(), variableName);
                } else {
                    String dimensionStr = variable.getDimensionsString();
                    var = dataFile.addVariable(null, variableName, variable.getDataType(), dimensionStr);
                }
                mapVariable.put(variableName, var);
                for (Attribute attribute : variable.getAttributes()) {
                    var.addAttribute(attribute);
                }
            }
            dataFile.create();

            //time 变量
            Variable timeVar = ncfile.findVariable(null, "time");
            ArrayFloat.D1 timeArray = (ArrayFloat.D1) timeVar.read(monthCondition);
            Variable newTimeVariable = mapVariable.get("time");
            dataFile.write(newTimeVariable, timeArray);

            //latitude变量
            Variable latitudeVar = ncfile.findVariable(null, "latitude");
            ArrayFloat.D1 latitudeArray = (ArrayFloat.D1) latitudeVar.read();
            Variable newLatitudeVariable = mapVariable.get("latitude");
            dataFile.write(newLatitudeVariable, latitudeArray);

            //longitude
            Variable longitudeVar = ncfile.findVariable(null, "longitude");
            ArrayFloat.D1 longitudeArray = (ArrayFloat.D1) longitudeVar.read();
            Variable newLongitudeVariable = mapVariable.get("longitude");
            dataFile.write(newLongitudeVariable, longitudeArray);

            //depth
            Variable depthVar = ncfile.findVariable(null, "depth");
            ArrayFloat.D1 depthArray = (ArrayFloat.D1) depthVar.read();
            Variable newDepthVariable = mapVariable.get("depth");
            dataFile.write(newDepthVariable, depthArray);

            //其他变量，多维
            for (Variable variable : lstVariable) {
                if (mapDimension.containsKey(variable.getShortName())) {
                    continue;
                }
                Array data = null;
                if (variable.getDimensions().size() == 4) {
                    data = variable.read(monthCondition + ",0:49:1,0:329:1,0:719:1");
                } else {
                    data = variable.read(monthCondition + ",0:329:1,0:719:1");
                }

                Variable newVar = mapVariable.get(variable.getShortName());
                dataFile.write(newVar, data);
            }

            System.out.println("writing nc file 【" + targetNcFilePath + "】成功！");

        } finally {
            if (dataFile != null) {
                dataFile.close();
            }
        }
    }
}
