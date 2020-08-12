package com.zjft.generator;

import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: CK
 * @create: 2020-05-26 08:52
 */
public class RootCodeGenerator {

    private static final String url = "jdbc:mysql://10.34.12.145:3306/usp_pay_2?useUnicode=true&useSSL=false&characterEncoding=utf8";
    private static final String driverName = "com.mysql.cj.jdbc.Driver";
    private static final String userName = "root";
    private static final String password = "cloud.Zijin";

    private static final String author = "CK";

    private static final String projectName = "code";

    private static final String table_name = "refund_apply";

    private static final String entityName = "RefundApply";

    private static final String package_name = "com.zjft.usp.pay.common";

    private static final String mapper_package = "common";


    public static void main(String[] args) {
        // 代码生成器
        AutoGenerator mpg = new AutoGenerator();

        // 全局配置
        GlobalConfig gc = new GlobalConfig();
        String projectPath = System.getProperty("user.dir");
        gc.setOutputDir(projectPath + "/usp-service/" + projectName + "/src/main/java");
        gc.setAuthor(author);
        gc.setOpen(false);
        gc.setSwagger2(true);
        gc.setEntityName(entityName);
        gc.setMapperName(entityName + "Mapper");
        gc.setControllerName(entityName + "Controller");
        gc.setServiceName(entityName + "Service");
        gc.setServiceImplName(entityName + "ServiceImpl");
        mpg.setGlobalConfig(gc);

        // 数据源配置
        DataSourceConfig dsc = new DataSourceConfig();
        dsc.setUrl(url);
        dsc.setDriverName(driverName);
        dsc.setUsername(userName);
        dsc.setPassword(password);
        mpg.setDataSource(dsc);

        // 包配置
        PackageConfig pc = new PackageConfig();
        pc.setParent(package_name);
        pc.setEntity("model");
        mpg.setPackageInfo(pc);

        // 自定义配置
        InjectionConfig cfg = new InjectionConfig() {
            @Override
            public void initMap() {
                // to do nothing
            }
        };
        List<FileOutConfig> focList = new ArrayList<>();
        focList.add(new FileOutConfig("/templates/mapper.xml.ftl") {
            @Override
            public String outputFile(TableInfo tableInfo) {
                // 自定义输入文件名称
                return projectPath + "/usp-service/" + projectName + "/src/main/resources/mapper/" + mapper_package
                    + "/" + tableInfo.getEntityName() + "Mapper" + StringPool.DOT_XML;
            }
        });
        cfg.setFileOutConfigList(focList);
        mpg.setCfg(cfg);
        mpg.setTemplate(new TemplateConfig().setXml(null));

        // 策略配置
        StrategyConfig strategy = new StrategyConfig();
        // 增加注解信息，严格根据注解信息关联字段
        strategy.setEntityTableFieldAnnotationEnable(true);
        strategy.setNaming(NamingStrategy.underline_to_camel);
        strategy.setColumnNaming(NamingStrategy.underline_to_camel);
        strategy.setEntityLombokModel(true);
        strategy.setRestControllerStyle(true);
        strategy.setInclude(table_name);
        // 设置主键字段生成于当前实体文件
        // strategy.setSuperEntityColumns("id");
        strategy.setControllerMappingHyphenStyle(true);
        strategy.setTablePrefix(pc.getModuleName() + "_");
        mpg.setStrategy(strategy);
        mpg.setTemplateEngine(new FreemarkerTemplateEngine());
        mpg.execute();
    }
}
