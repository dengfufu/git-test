package com.zjft.usp.auth.config;

import cn.hutool.core.img.ImgUtil;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import javax.imageio.ImageIO;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

/**
 * @author: CK
 * @create: 2020-03-31 16:17
 */
@Configuration
public class KaptchaConfig {

    @Bean(name = "captchaProducer")
    public DefaultKaptcha getDefaultKaptcha() {
        DefaultKaptcha captchaProducer = new DefaultKaptcha();
        Properties properties = new Properties();
        properties.setProperty("kaptcha.session.key", "code");
        properties.setProperty("kaptcha.image.width", "110");
        properties.setProperty("kaptcha.image.height", "40");
        properties.setProperty("kaptcha.border", "yes");
        properties.setProperty("kaptcha.border.color", "217,217,217");
        properties.setProperty("kaptcha.background.clear.from", "white");
        properties.setProperty("kaptcha.background.clear.to", "white");
        properties.setProperty("kaptcha.textproducer.font.color", "black");
        properties.setProperty("kaptcha.textproducer.char.length", "4");
        properties.setProperty("kaptcha.textproducer.font.names", "Arial");
        properties.setProperty("kaptcha.textproducer.font.size", "24");
        properties.setProperty("kaptcha.textproducer.char.space", "2");
        properties.setProperty("kaptcha.word.impl", "com.google.code.kaptcha.text.impl.DefaultWordRenderer");
        properties.setProperty("kaptcha.textproducer.char.string", "abcdefhjkmnpqrstwxz2345678");
        properties.setProperty("kaptcha.noise.color", "black");
        properties.setProperty("kaptcha.noise.impl", "com.google.code.kaptcha.impl.DefaultNoise");
        properties.setProperty("kaptcha.obscurificator.impl", "com.zjft.usp.auth.config.ZjftGimpy");

        Config config = new Config(properties);
        captchaProducer.setConfig(config);
        return captchaProducer;
    }

    public static void main(String[] args) throws IOException {
//        DefaultKaptcha captchaProducer = new DefaultKaptcha();
//        Properties properties = new Properties();
//        properties.setProperty("kaptcha.session.key", "code");
//        properties.setProperty("kaptcha.image.width", "110");
//        properties.setProperty("kaptcha.image.height", "40");
//        properties.setProperty("kaptcha.border", "yes");
//        properties.setProperty("kaptcha.border.color", "217,217,217");
//        properties.setProperty("kaptcha.background.clear.from", "white");
//        properties.setProperty("kaptcha.background.clear.to", "white");
//        properties.setProperty("kaptcha.textproducer.font.color", "black");
//        properties.setProperty("kaptcha.textproducer.char.length", "4");
//        properties.setProperty("kaptcha.textproducer.font.names", "Arial");
//        properties.setProperty("kaptcha.textproducer.font.size", "24");
//        properties.setProperty("kaptcha.textproducer.char.space", "2");
//        properties.setProperty("kaptcha.word.impl", "com.google.code.kaptcha.text.impl.DefaultWordRenderer");
//        properties.setProperty("kaptcha.textproducer.char.string", "abcde2345678gfynmnpwx");
//
//        properties.setProperty("kaptcha.noise.color", "black");
//        properties.setProperty("kaptcha.noise.impl", "com.google.code.kaptcha.impl.DefaultNoise");
//        properties.setProperty("kaptcha.obscurificator.impl", "com.zjft.usp.auth.config.ZjftGimpy");
////        properties.setProperty("kaptcha.obscurificator.impl", "com.google.code.kaptcha.impl.WaterRipple");
//
//        Config config = new Config(properties);
//        captchaProducer.setConfig(config);
//
//        String code = captchaProducer.createText();
//        BufferedImage bufferedImage = captchaProducer.createImage("yyyy");
//
////        String captchaBase64 = "data:image/jpeg;base64," + ImgUtil.toBase64(bufferedImage,"jpg");
//
////        System.out.println(captchaBase64);
//        OutputStream outputStream = new FileOutputStream(new File("test.jpg"));
//        ImageIO.write(bufferedImage, "jpg", outputStream);
//        outputStream.close();



    }
}
