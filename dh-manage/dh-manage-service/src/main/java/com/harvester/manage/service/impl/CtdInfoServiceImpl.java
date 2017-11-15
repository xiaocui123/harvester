package com.harvester.manage.service.impl;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.harvester.HarvesterConstants;
import com.harvester.generator.helper.HarvesterConfig;
import com.harvester.helper.CkanHelper;
import com.harvester.helper.FileUploadDownHelper;
import com.harvester.helper.TdsHelper;
import com.harvester.manage.mapper.CtdInfoMapper;
import com.harvester.manage.mapper.PublishInfoMapper;
import com.harvester.manage.pojo.CtdInfo;
import com.harvester.manage.pojo.NutrientInfo;
import com.harvester.manage.pojo.PublishInfo;
import com.harvester.manage.service.CtdInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

/**
 * Created by cui on 2017/11/15.
 */
@Service("ctdInfoServiceImpl")
public class CtdInfoServiceImpl implements CtdInfoService {
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private HarvesterConfig harvesterConfig;

    @Autowired
    private CtdInfoMapper ctdInfoMapper;

    @Autowired
    private PublishInfoMapper publishInfoMapper;

    @Override
    public void generateNcFile(MultipartFile file, CtdInfo ctdInfo) throws IOException, InterruptedException {
        //上传到目标路径下
        if (ctdInfo.getCtdId() == null) {
            ctdInfo.setCtdId(UUID.randomUUID().toString());
        }
        String path = harvesterConfig.getSipCtdFilePath() + File.separator + ctdInfo.getCtdId();
        FileUploadDownHelper helper = new FileUploadDownHelper();
        helper.uploadFile(file, path);

        String fileName = file.getOriginalFilename();
        String fileExt = fileName.substring(fileName.lastIndexOf(".") + 1);
        logger.info("文件扩展名=【{}】", fileExt);
        if (HarvesterConstants.FILEEXT_NC.equals(fileExt)) {
            ctdInfo.setCtdNcFilepath(path + File.separator + fileName);
        } else if (HarvesterConstants.FILEEXT_CNV.equals(fileExt)) {
            String filePath = null;
            try {
                filePath = cnv2nc(path, fileName);
            } catch (Exception e) {
                logger.error("seabird cnv2cn command execute failed!", e);
                //清理已上传文件
                Files.deleteIfExists(Paths.get(path + File.separator + fileName));
                Files.deleteIfExists(Paths.get(path));
                throw e;
            }
            ctdInfo.setCtdNcFilepath(filePath);
        } else {
            throw new IllegalArgumentException("CTD不支持【" + fileExt + "】文件的上传");
        }
        ctdInfoMapper.insertSelective(ctdInfo);
    }

    /**
     * 调用shell命令生成nc文件
     *
     * @param path
     * @param fileName
     * @return
     * @throws IOException
     * @throws InterruptedException
     */
    private String cnv2nc(String path, String fileName) throws IOException, InterruptedException {
        List<String> commands = Lists.newArrayList();
        commands.add("seabird");
        commands.add("cnv2nc");
        commands.add(fileName);

        ProcessBuilder pb = new ProcessBuilder(commands);
        pb.directory(new File(path));
        pb.redirectErrorStream(true);
        Process process = pb.start();

        //Read output
        StringBuilder out = new StringBuilder();
        BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line = null, previous = null;
        while ((line = br.readLine()) != null)
            if (!line.equals(previous)) {
                previous = line;
                out.append(line).append('\n');
                System.out.println(line);
            }

        //Check result
        if (process.waitFor() == 0) {
            fileName = fileName.substring(0, fileName.lastIndexOf(".")) + ".nc";
            return fileName;
        }
        throw new IllegalArgumentException("seabird cnv2cn command execute failed!");
    }

    @Override
    public void delete(List<String> lstCtdId) {
        try {
            for (String ctdId : lstCtdId) {
                List<PublishInfo> lstPublishInfo = publishInfoMapper.selectByResourceId(ctdId);
                for (PublishInfo publishInfo : lstPublishInfo) {
                    if (publishInfo.getPublishSuccessful()) {
                        //TODO　一块删除？
                        TdsHelper.deleteResource(ctdId);

                        String ckanDatasetId = publishInfo.getPublishCkanDatasetId();
                        if (!Strings.isNullOrEmpty(ckanDatasetId)) {
                            CkanHelper.deleteDataset(publishInfo.getPublishCkanDatasetId());
                        }

                        publishInfoMapper.deleteByPrimaryKey(publishInfo.getPublishId());
                    }
                }
                CtdInfo ctdInfo = ctdInfoMapper.selectByPrimaryKey(ctdId);
                try {
                    Files.delete(Paths.get(ctdInfo.getCtdNcFilepath()));
                    Files.deleteIfExists(Paths.get(ctdInfo.getCtdNcFilepath()).getParent());
                    logger.info("删除nc文件【" + ctdInfo.getCtdNcFilepath() + "】成功！");
                } catch (IOException e) {
                    logger.warn("删除nc文件【" + ctdInfo.getCtdNcFilepath() + "】失败！");
                }
                ctdInfoMapper.deleteByPrimaryKey(ctdId);
            }
        } catch (Exception e) {
            logger.error("删除数据源出错!" + e.getMessage(), e);
            throw new IllegalArgumentException(e);
        }
    }
}
