package com.harvester.v2.generator;

import com.harvester.generator.helper.HarvesterConfig;
import com.harvester.generator.helper.SpringContextUtil;
import com.harvester.manage.pojo.DataSetInfo;
import com.harvester.v2.config.DataSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;

/**
 * Created by cui on 2017/11/22.
 */
public class AbstractGenerator implements IGenerator {
    private Logger logger = LoggerFactory.getLogger(getClass());

    protected DataSet dataSet;

    protected String rootDirPath;

    public AbstractGenerator(DataSet dataSet) {
        this.dataSet = dataSet;
        HarvesterConfig harvesterConfig = (HarvesterConfig) SpringContextUtil.getBean("harvesterconfig");
        this.rootDirPath = harvesterConfig.getGeneralFilePath();
    }

    @Override
    public DataSetInfo generate() {
        return null;
    }
}
