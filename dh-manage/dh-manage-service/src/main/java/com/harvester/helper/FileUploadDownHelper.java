package com.harvester.helper;

import com.google.common.base.Strings;
import org.apache.commons.io.IOUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by cui on 2017/11/9.
 */
public class FileUploadDownHelper {

    public File uploadFile(MultipartFile file, String targetPath) {
        File serverFile;
        InputStream in = null;
        OutputStream out = null;
        try {
            File dir;
            if (Strings.isNullOrEmpty(targetPath)) {
                String rootPath = System.getProperty("catalina.home");
                dir = new File(rootPath + File.separator + "tmpFiles");
            } else {
                dir = new File(targetPath);
            }
            if (!dir.exists())
                dir.mkdirs();

            serverFile = new File(dir.getAbsolutePath() + File.separator + file.getOriginalFilename());
            in = file.getInputStream();
            out = new FileOutputStream(serverFile);
            byte[] b = new byte[1024];
            int len = 0;
            while ((len = in.read(b)) > 0) {
                out.write(b, 0, len);
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("文件上传失败！", e);
        } finally {
            IOUtils.closeQuietly(in);
            IOUtils.closeQuietly(out);
        }
        return serverFile;
    }
}
