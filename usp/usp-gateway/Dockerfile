# 基于哪个镜像
FROM openjdk:8-jre-alpine

ENV sw_service_name=usp-gateway sw_backend_service=127.0.0.1:11800

# 与宿主机时间同步
RUN ln -sf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime

# 拷贝文件到容器，也可以直接写成ADD registry.jar /app.jar
ADD ./target/usp-gateway.jar /usp/usp-gateway.jar

# 开放10090端口
EXPOSE 10090

# 配置容器启动后执行的命令
ENTRYPOINT java -Djava.security.egd=file:/dev/./urandom -javaagent:/skywalking/agent/skywalking-agent.jar \
-Dskywalking.agent.service_name=${sw_service_name} \
-Dskywalking.collector.backend_service=${sw_backend_service} \
-Xms256m -Xmx256m -jar /usp/usp-gateway.jar --env=nacos-server --env=config-group --env=discovery-ip