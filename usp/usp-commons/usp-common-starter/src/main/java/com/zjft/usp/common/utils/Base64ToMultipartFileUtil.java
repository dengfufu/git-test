package com.zjft.usp.common.utils;

import cn.hutool.core.util.StrUtil;
import org.springframework.web.multipart.MultipartFile;
import sun.misc.BASE64Decoder;

import java.io.*;

/**
 * @author zphu
 * @date 2019/8/22 14:01
 * @Version 1.0
 **/
public class Base64ToMultipartFileUtil implements MultipartFile {

    private final byte[] imgContent;
    private final String header;
    private final String originalFilename;
    private final String name;

    public Base64ToMultipartFileUtil(byte[] imgContent, String header) {
        this.imgContent = imgContent;
        this.header = header.split(";")[0];
        if (StrUtil.containsAny(this.header, "image/jpeg")) {
            this.originalFilename = System.currentTimeMillis() + (int) Math.random() * 10000 + ".jpg";
            this.name = System.currentTimeMillis() + (int) Math.random() + ".jpg";
        } else if (StrUtil.containsAny(this.header, "data:image/png")) {
            this.originalFilename = System.currentTimeMillis() + (int) Math.random() * 10000 + ".png";
            this.name = System.currentTimeMillis() + (int) Math.random() + ".png";
        } else {
            this.originalFilename = System.currentTimeMillis() + (int) Math.random() * 10000 + "." + header.split("/")[1];
            this.name = System.currentTimeMillis() + (int) Math.random() + "." + header.split("/")[1];
        }

    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getOriginalFilename() {
        return this.originalFilename;
    }

    @Override
    public String getContentType() {
        return header.split(":")[1];
    }

    @Override
    public boolean isEmpty() {
        return imgContent == null || imgContent.length == 0;
    }

    @Override
    public long getSize() {
        return imgContent.length;
    }

    @Override
    public byte[] getBytes() throws IOException {
        return imgContent;
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return new ByteArrayInputStream(imgContent);
    }

    @Override
    public void transferTo(File dest) throws IOException, IllegalStateException {
        new FileOutputStream(dest).write(imgContent);
    }

    public static MultipartFile base64ToMultipart(String base64) {
        try {
            String[] baseStrs = base64.split(",");

            BASE64Decoder decoder = new BASE64Decoder();
            byte[] b = new byte[0];
            b = decoder.decodeBuffer(baseStrs[1]);

            for (int i = 0; i < b.length; ++i) {
                if (b[i] < 0) {
                    b[i] += 256;
                }
            }
            return new Base64ToMultipartFileUtil(b, baseStrs[0]);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}
