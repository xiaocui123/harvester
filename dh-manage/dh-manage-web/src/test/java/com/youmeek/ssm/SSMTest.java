package com.youmeek.ssm;

import com.harvester.generator.AdcpNcGeneratorV2;
import com.harvester.generator.StationSerialDataGenerator;
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

}
