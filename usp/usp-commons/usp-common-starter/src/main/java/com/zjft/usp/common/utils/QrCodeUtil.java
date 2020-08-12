package com.zjft.usp.common.utils;

import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import lombok.extern.slf4j.Slf4j;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author : dcyu
 * @Date : 2019/9/5 18:29
 * @Desc : 二维码生成、解析工具类
 * @Version 1.0.0
 */
@Slf4j
public class QrCodeUtil {

    /** 限制最小宽度 */
    private static int MIN_WIDTH = 100;
    /** 限制最小高度 */
    private static int MIN_HEIGHT = 100;
    /** 字符编码 */
    private static String CHARSET = "UTF-8";

    /**
     * 生成二维码文件字节数组格式
     * @datetime 2019/9/9 11:27
     * @version 
     * @author dcyu 
     * @param content
     * @param imgFile
     * @param width
     * @param height
     * @return byte[]
     */
    public static byte[] createQrCodeImgByte(String content, InputStream imgFile, int width, int height) {
        BufferedImage bufferedImage = createQrCodeImg(content, imgFile, width, height);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            ImageIO.write(bufferedImage, "JPEG", byteArrayOutputStream);
            return byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            log.error("生成二维码失败");
        }
        return null;
    }
    /**
     * 生成二维码图片文件
     * @datetime 2019/9/6 10:19
     * @version
     * @author dcyu
     * @param content: 二维码内容
     * @param imgFile: 头像文件
     * @param width: 二维码宽
     * @param height: 二维码高
     * @return java.awt.image.BufferedImage
     */
    public static BufferedImage createQrCodeImg(String content, InputStream imgFile, int width, int height) {
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        Map hints = new HashMap();
        hints.put(EncodeHintType.MARGIN, 0);
        hints.put(EncodeHintType.CHARACTER_SET, CHARSET);
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.Q);
        /** 限定二维码宽高最小值 */
        if (width < MIN_WIDTH) {
            width = MIN_WIDTH;
        }
        if (height < MIN_HEIGHT) {
            height = MIN_HEIGHT;
        }
        try {
            BitMatrix bitMatrix = multiFormatWriter.encode(content, BarcodeFormat.QR_CODE, width, height, hints);
            BufferedImage qrCodeImage = MatrixToImageWriter.toBufferedImage(bitMatrix);
            /*给二维码绘入图片*/
            BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            if (qrCodeImage != null) {
                Graphics2D graphics = bufferedImage.createGraphics();
                graphics.drawImage(qrCodeImage, 0, 0, width, height, null);
                if (imgFile != null) {
                    BufferedImage iconImage = ImageIO.read(imgFile);
                    /** 图标宽高取值二维码的四分之一 */
                    int iconWidth = width / 4;
                    int iconHeight = height / 4;
                    int x = (qrCodeImage.getWidth() - iconWidth) / 2;
                    int y = (qrCodeImage.getHeight() - iconHeight) / 2;

                    Shape shape = new RoundRectangle2D.Float(x, y, iconWidth, iconHeight, 100, 100);
                    /* 设置抗锯齿 */
                    graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    graphics.setClip(shape);
                    graphics.drawImage(iconImage, x, y, iconWidth, iconHeight, null);
                    /* 边宽 */
                    graphics.setStroke(new BasicStroke(10));
                    graphics.setColor(Color.WHITE);
                    /* 边框 */
                    graphics.drawRoundRect(x, y, iconWidth, iconHeight, 100, 100);
                }
                graphics.dispose();
            }
            /**/
            return bufferedImage;
        }catch (WriterException e) {
            e.printStackTrace();
            log.error("生成二维码失败{}", e);
        }catch (IOException e) {
            e.printStackTrace();
            log.error("生成二维码IO异常{}", e);
        }
        return null;
    }

    /**
     * 解析二维码图片文件
     * @datetime 2019/9/6 10:19
     * @version
     * @author dcyu
     * @param imgFile
     * @return java.lang.String
     */
    public static String parseQrCodeImg(File imgFile) {
        try {
            BufferedImage qrCodeImage = ImageIO.read(imgFile);
            if (qrCodeImage == null) {
                return null;
            }
            LuminanceSource luminanceSource = new BufferedImageLuminanceSource(qrCodeImage);
            HybridBinarizer hybridBinarizer = new HybridBinarizer(luminanceSource);
            BinaryBitmap binaryBitmap = new BinaryBitmap(hybridBinarizer);
            MultiFormatReader multiFormatReader = new MultiFormatReader();
            Map hints = new HashMap();
            hints.put(EncodeHintType.MARGIN, 0);
            hints.put(EncodeHintType.CHARACTER_SET, CHARSET);
            hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.Q);
            Result result = multiFormatReader.decode(binaryBitmap, hints);
            return result.getText();
        } catch (IOException e) {
            e.printStackTrace();
            log.error("解析二维码IO异常{}", e);
        } catch (NotFoundException e) {
            e.printStackTrace();
            log.error("解析二维码文件失败{}", e);
        }
        return "";
    }

//    public static void main(String[] args) {
//        BufferedImage qrImage = createQrCodeImg("https://www.baidu.com", new File("c://p1.jpg"), 50, 50);
//        File outputFile = new File("c:\\qrcode.png");
//        try {
//            ImageIO.write(qrImage, "JPEG", outputFile);
//        } catch (IOException e) {
//
//        }
//
//        String result = parseQrCodeImg(outputFile);
//        System.out.print(result);
//    }
}
