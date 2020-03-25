package com.nCov.DataView.service.Impl;

import com.nCov.DataView.model.response.Result;
import com.nCov.DataView.service.FileService;
import com.nCov.DataView.tools.ChangeCharset;
import com.nCov.DataView.tools.ResultTool;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

/**
 * @program: management
 * @description: 文件上传Service方法的实现
 * @author: ggmr
 * @create: 2018-12-18 14:49
 */
@Service
public class FileServiceImpl implements FileService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    //存放文件的路径
    @Value("${upload.path}")
    private String directory;

    @Override
    public Result uploadFile(MultipartFile file) {
        if (file.isEmpty()) {
            return ResultTool.error(400, "上传文件为空");
        }
        //文件存放的id名
        String fileId = UUID.randomUUID().toString();
        //源文件名
        String originalFileName = ChangeCharset.toUtf8(file.getOriginalFilename());
        //在指定的目录位置下存放文件
        String absolutePath = ChangeCharset.toUtf8(directory + File.separator + fileId + "---" + originalFileName);
        logger.info(absolutePath);
        //如果存放文件的文件夹不存在，就创建文件夹
        File destDirectory = new File(directory);
        if (!destDirectory.exists()) {
            destDirectory.mkdirs();
        }

        try (OutputStream os = new FileOutputStream(absolutePath)) {
            os.write(file.getBytes());
        } catch (IOException e) {
            return ResultTool.error(400, "上传出错");
        }
        return ResultTool.success(absolutePath);
    }

    @Override
    public void downloadFile(HttpServletRequest request,
                             HttpServletResponse response, String fileAddress) {

        File downloadFile = new File(fileAddress);
        if (downloadFile.exists()) {
            response.setContentType("application/octet-stream");
            String headerKey = "Content-Disposition";
            String fileName = downloadFile.getName().split("---")[1];
            String headerValue = "attachment; filename=" + new String(fileName.getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1);
            response.setHeader(headerKey, headerValue);
            response.setContentLength((int) downloadFile.length());
            try {
                InputStream myStream = new FileInputStream(fileAddress);
                OutputStream toClient = response.getOutputStream();
                IOUtils.copy(myStream, toClient);
                response.flushBuffer();
                myStream.close();
                toClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            new Exception("不存在文件").printStackTrace();
        }
    }
}
