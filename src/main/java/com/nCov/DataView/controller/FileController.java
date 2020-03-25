package com.nCov.DataView.controller;

import com.nCov.DataView.model.response.Result;
import com.nCov.DataView.service.FileService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * @program: management
 * @description: 文件控制类
 * @author: ggmr
 * @create: 2018-12-21 20:54
 */
@RestController
@CrossOrigin
@RequestMapping(value = "/file")
public class FileController {
    @Resource
    private FileService fileService;

    @PostMapping("upload")
    public Result upload(@RequestBody MultipartFile file) throws UnsupportedEncodingException {
        return fileService.uploadFile(file);
    }

    @GetMapping("download")
    public void upload(HttpServletRequest request, HttpServletResponse response,
                       @RequestParam(value = "fileAddress") String fileAddress) throws IOException {
        fileService.downloadFile(request, response, fileAddress);
    }
}
