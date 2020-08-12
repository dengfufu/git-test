package com.zjft.usp.file.config;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import java.util.List;
/**
 * @description: 对应着application.yml中自定义的配置
 * @author chenxiaod
 * @date 2019/8/9 16:12
 */
@Data
@ConfigurationProperties(prefix = "chenille.upload")
public class UploadProperties {

    private String uploadPathName;
    private String baseUrl;
    private List<String> allowTypes;
    private String groupName;
}
