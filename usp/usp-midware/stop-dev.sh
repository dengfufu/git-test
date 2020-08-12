#!/bin/sh

# 开发环境中间件关闭脚本

# 关闭nacos
cd nacos/bin
./shutdown.sh

cd ../..

# 关闭redis
for pid in `ps -ef|grep redis-server |grep -v grep|awk '{print $2}'`
do
  echo "kill -9 redis :"$pid
  kill -9 $pid
done

# 关闭kafka
cd kafka_2.12-2.2.2
bin/zookeeper-server-stop.sh
bin/kafka-server-stop.sh

# 关闭skywalking todo

