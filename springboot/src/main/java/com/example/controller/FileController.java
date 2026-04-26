package com.example.controller;

import cn.hutool.core.io.FileUtil;
import com.example.common.Result;
import com.example.exception.CustomException;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
    文件相关的接口
 */
@RestController //标明为controller
/*
@RequestMapping	所有请求（GET/POST/PUT/DELETE...）
    给整个类的接口，加一个统一的 “前缀路径”,这个类里所有接口的地址，都会变成 /files/xxx。
 */
@RequestMapping("/files")
public class FileController {
    //System.getProperty("user.dir") 获取到你当前这个项目的跟路径
    //文件的上传的目录的路径
    private static final String filePath = System.getProperty("user.dir") + "/files/";

    //专门标记 “POST 请求” 的接口，给方法指定具体得请求路径
    /*
    文件上传
     */
    @PostMapping("/upload")
    //MultipartFile file 通过这个对象来接收前段发送过来的文件
    public Result upload(MultipartFile file){ //文件流的形式接收前段发送过来的文件
        String originalFilename = file.getOriginalFilename(); //xxx.png
        if(!FileUtil.isDirectory(filePath)) {//如果不存在目录,则创建一个
            FileUtil.mkdir(filePath); //创建一个files目录
        }

        //提供文件存储的完整的路径
        //给文件名 加一个唯一的标识
        String fileName = System.currentTimeMillis() + "_" + originalFilename; //1689879870_xxx.png
        String realPath = filePath + fileName; //完整的文件路径
        try {
            FileUtil.writeBytes(file.getBytes(), realPath);
        } catch (IOException e) {
            e.printStackTrace();
            throw new CustomException("500", "文件上传失败");
        }
        //返回一个网络连接
        String url = "http://localhost:9090/files/download/" + fileName;
        return Result.success(url);
    }

    /*
    文件下载
     */
    @GetMapping("/download/{fileName}")
    public void download(@PathVariable String fileName , HttpServletResponse response){
        try {
            //设置文件编码  避免中文编码错误导致下载异常
            response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, StandardCharsets.UTF_8));
            response.setContentType("application/octet-stream");

            OutputStream os = response.getOutputStream();
            String realPath = filePath + fileName; //完整的文件路径
            //获取到文件里的字节数组
            byte[] bytes = FileUtil.readBytes(realPath);
            os.write(bytes);
            os.flush();
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
            throw new CustomException("500", "文件下载失败");
        }

    }

    /**
     * wang-editor编辑器文件上传接口
     */
    @PostMapping("/wang/upload")
    public Map<String, Object> wangEditorUpload(MultipartFile file) {
        String originalFilename = file.getOriginalFilename(); //xxx.png
        if(!FileUtil.isDirectory(filePath)) {//如果不存在目录,则创建一个
            FileUtil.mkdir(filePath); //创建一个files目录
        }

        //提供文件存储的完整的路径
        //给文件名 加一个唯一的标识
        String fileName = System.currentTimeMillis() + "_" + originalFilename; //1689879870_xxx.png
        String realPath = filePath + fileName; //完整的文件路径
        try {
            FileUtil.writeBytes(file.getBytes(), realPath);
        } catch (IOException e) {
            e.printStackTrace();
            throw new CustomException("500", "文件上传失败");
        }
        //返回一个网络连接
        String url = "http://localhost:9090/files/download/" + fileName;
        // wangEditor上传图片成功后， 需要返回的参数
        Map<String, Object> resMap = new HashMap<>();
        List<Map<String, Object>> list = new ArrayList<>();
        Map<String, Object> urlMap = new HashMap<>();
        urlMap.put("url", url);
        list.add(urlMap);
        resMap.put("errno", 0);
        resMap.put("data", list);
        return resMap;
    }
}
