# nacos 配置中心

## 开发环境

如果是初次下载项目，
1. 启动`nacos`
2. 打包`usp-config/USP_GROUP_DEV`目录为`USP_GROUP_DEV.zip`，在`nacos`控制台界面上传配置文件
3. 正常启动各个服务

## 测试环境

`usp-config`文件夹下面有2种格式分类: `USP_GROUP_DEV`、`USP_GROUP_PROD`

- USP_GROUP_DEV: 开发环境格式，中间件配置多为单机
- USP_GROUP_DEV: 准生产环境格式，中间件配置多为集群

**当测试环境的中间件是单机的，可以选择`USP_GROUP_DEV`，反之`USP_GROUP_PROD`**

上传`USP_GROUP_PROD.zip`后，需要修改各个配置文件中的中间件以及数据库配置

## 生产环境
1. 启动`nacos`
2. 打包`usp-config/USP_GROUP_PROD`目录为`USP_GROUP_PROD.zip`，在`nacos`控制台界面上传配置文件
3. 修改各个微服务配置文件
4. 正常启动各个服务

## QA
- 单个服务启动本身可选参数为: `nacos-server`、`config-group`
```
// nacos-server 默认为127.0.0.1:8848， config-group默认为USP_GROUP_DEV

// 自定义启动命令，用来切换注册中心以及配置中心中某组的配置文件
java -jar usp-gateway.jar --nacos-server=http:nacos.com --config-group=USP_GROUP_PROD

```  
