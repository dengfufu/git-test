# 基于哪个镜像
FROM openjdk:8-jre-alpine

ENV sw_service_name=usp-auth sw_backend_service=127.0.0.1:11800

# 与宿主机时间同步
RUN ln -sf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime

# 将本地文件夹挂载到当前容器
VOLUME /tmp

# 拷贝文件到容器
ADD ./target/usp-auth.jar /usp/usp-auth.jar

# 开放端口
EXPOSE 10091

# 配置容器启动后执行的命令
ENTRYPOINT java -Djava.security.egd=file:/dev/./urandom -javaagent:/skywalking/agent/skywalking-agent.jar \
-Dskywalking.agent.service_name=${sw_service_name} \
-Dskywalking.collector.backend_service=${sw_backend_service} \
-Xms256m -Xmx256m -jar /usp/usp-auth.jar --env=nacos-server --env=config-group --env=discovery-ip