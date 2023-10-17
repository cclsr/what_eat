package com.cclsr.eat.controller;

import com.cclsr.eat.common.R;
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
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.UUID;

@RestController
@Slf4j
@RequestMapping("/common")
public class CommonController {

    @Value("${eat.path}")
    private String basePath;

    @PostMapping("/upload")
    public R<String> upload(MultipartFile file) {
        log.info(file.toString());
        // file是一个临时文件，需要转存到指定文件夹，否则会话结束会被删除

        // 原始文件名
        String originalFilename = file.getOriginalFilename();
        String ext = originalFilename.substring(originalFilename.lastIndexOf("."));
        // 使用UUID重新生成文件名，防止文件名重复覆盖
        String fileName = UUID.randomUUID().toString() + ext;

        // 创建一个目录对象
        File dir = new File(basePath);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        try {
            file.transferTo(new File(basePath + fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return R.success(fileName);
    }

    // 通过输出流向浏览器输出数据

    /**
     * 下载图片
     *
     * @param name
     */
    @GetMapping("/download")
    public void download(String name, HttpServletResponse response) {
        try {
            // 输入流，通过输入流读取文件内容
            FileInputStream fileInputStream = new FileInputStream(new File(basePath + name));

            // 输出流，通过输出流将文件写回浏览器，在浏览器展示图片
            ServletOutputStream outputStream = response.getOutputStream();

            // 设置返回的格式
            response.setContentType("image/jpeg");

            int len = 0;
            byte[] bytes = new byte[1024];
            while ((len = fileInputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, len);
                outputStream.flush();
            }
            // 关闭资源
            outputStream.close();
            fileInputStream.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
