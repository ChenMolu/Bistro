package com.rocky.bistro.controller;

import com.rocky.bistro.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/common")
public class CommonController {

    @Value("${rocky.path}")
    private String basePath;

    /**
     * 文件上传
     * @param file
     * @return
     */
    @PostMapping("/upload")
    public R<String> upload(MultipartFile file){
        //file是一个临时文件，请求结束后会删除，需要转存。
        log.info("传递的文件信息：{}",file.toString());
        //获取原始文件名
        String fileName = file.getOriginalFilename();
        //获取文件后缀
        String suffix = fileName.substring(fileName.lastIndexOf("."));
        //使用uuid生成文件命，避免重名
        String newFileName = UUID.randomUUID().toString() + suffix;
        //判断存储目录，如果不存在则创建
        File dir = new File(basePath);
        if (!dir.exists()){
            dir.mkdirs();
        }
        //转存文件
        try {
            file.transferTo(new File(basePath + newFileName));
        }catch (Exception e){
            e.printStackTrace();
        }
        return R.success(newFileName);
    }

    /**
     * 文件下载
     * @param name
     */
    @GetMapping("/download")
    public void download(String name, HttpServletResponse response) throws Exception {
        //通过输入流读取文件
        FileInputStream fileInputStream = new FileInputStream(new File(basePath + name));
        //创建临时字节数组，用来转存到输出流
        int len = 0;
        byte[] bytes = new byte[1024];
        //创建输出流，将文件传回给浏览器
        ServletOutputStream outputStream = response.getOutputStream();
        while((len = fileInputStream.read(bytes)) != -1){
            outputStream.write(bytes,0,len);
            outputStream.flush();
        }
        //关闭流
        fileInputStream.close();
        outputStream.close();
    }
}
