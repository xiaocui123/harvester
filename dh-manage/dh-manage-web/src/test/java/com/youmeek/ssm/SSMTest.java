package com.youmeek.ssm;

import com.harvester.generator.AdcpNcGeneratorV2;
import com.harvester.generator.NutrientGeoGenerator;
import com.harvester.generator.StationSerialDataGenerator;
import com.harvester.v2.generator.ExcelGenerator;
import com.harvester.v2.generator.JdbcGenerator;
import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.File;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:spring/applicationContext*.xml"})
public class SSMTest {

    private Logger logger = Logger.getLogger(this.getClass().getName());


    @Test
    public void testAdcpNcGenerator() {
        new AdcpNcGeneratorV2().generate();
    }

    @Test
    public void testStationSerialGenerator() {
//        generator.initTimeSerial(new File("E:\\content\\thredds\\public\\testdata\\station_buoy\\adcp\\station.xml"));
//        generator.initTimeSerial(new File("E:\\content\\thredds\\public\\testdata\\station_buoy\\adcp\\tabblog.xml"));
//        generator.initTimeSerial(new File("E:\\content\\thredds\\public\\testdata\\station_buoy\\adcp\\tabfeng.xml"));
//        generator.initTimeSerial(new File("E:\\content\\thredds\\public\\testdata\\station_buoy\\adcp\\tabolym.xml"));
//        generator.initTimeSerial(new File("E:\\content\\thredds\\public\\testdata\\station_buoy\\adcp\\tabqixg.xml"));
        StationSerialDataGenerator generator = new StationSerialDataGenerator();
        generator.initTimeSerial(new File("E:\\content\\thredds\\public\\testdata\\station_buoy\\adcp\\tabshzh.xml"));
        generator.generate();
    }

    @Test
    public void testGeoGenerator(){
        NutrientGeoGenerator generator=new NutrientGeoGenerator();
        generator.generator(new File("E:\\content\\thredds\\public\\testdata\\routes\\nutrients\\nutrients.xlsx"));
    }

    @Test
    public void testJdbcGenerator(){
        JdbcGenerator generator = new JdbcGenerator();
        generator.init(new File("E://数据项目//general-config//dataset_.xml"));
        generator.generate();
    }

    @Test
    public void testExcelGenerator(){
        ExcelGenerator generator=new ExcelGenerator();
        generator.init(new File("E://数据项目//general-config//dataset_excel.xml"));
        generator.generate();
    }

}
