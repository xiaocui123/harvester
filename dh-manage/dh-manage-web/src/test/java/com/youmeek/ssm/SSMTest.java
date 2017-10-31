package com.youmeek.ssm;

import com.harvester.generator.AdcpNcGeneratorV2;
import com.harvester.generator.StationSerialDataGenerator;
import com.harvester.TestJdbc;
import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.io.File;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:spring/applicationContext*.xml"})
public class SSMTest {

    private Logger logger = Logger.getLogger(this.getClass().getName());

    @Resource
    private AdcpNcGeneratorV2 adcpNcGenerator;

    @Autowired
    private StationSerialDataGenerator generator;


    @Resource
    private TestJdbc testJdbc;


    @Test
    public void test2() {
        testJdbc.test();
    }

    @Test
    public void testAdcpNcGenerator() {
        adcpNcGenerator.generate();
    }

    @Test
    public void testStationSerialGenerator() {
//        generator.initTimeSerial(new File("E:\\content\\thredds\\public\\testdata\\station_buoy\\adcp\\station.xml"));
//        generator.initTimeSerial(new File("E:\\content\\thredds\\public\\testdata\\station_buoy\\adcp\\tabblog.xml"));
//        generator.initTimeSerial(new File("E:\\content\\thredds\\public\\testdata\\station_buoy\\adcp\\tabfeng.xml"));
//        generator.initTimeSerial(new File("E:\\content\\thredds\\public\\testdata\\station_buoy\\adcp\\tabolym.xml"));
//        generator.initTimeSerial(new File("E:\\content\\thredds\\public\\testdata\\station_buoy\\adcp\\tabqixg.xml"));
        generator.initTimeSerial(new File("E:\\content\\thredds\\public\\testdata\\station_buoy\\adcp\\tabshzh.xml"));
        generator.generate();
    }

}
