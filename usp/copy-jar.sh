#!/bin/sh

mkdir dist

cp usp-auth/target/usp-auth.jar ./dist/
cp usp-gateway/target/usp-gateway.jar ./dist/
cp usp-service/usp-anyfix/target/usp-anyfix.jar ./dist/
cp usp-service/usp-device/target/usp-device.jar ./dist/
cp usp-service/usp-file/target/usp-file.jar ./dist/
cp usp-service/usp-msg/target/usp-msg.jar ./dist/
cp usp-service/usp-pos/target/usp-pos.jar ./dist/
cp usp-service/usp-uas/target/usp-uas.jar ./dist/
cp usp-monitor/target/usp-monitor.jar ./dist/
cp usp-service/usp-wechat/target/usp-wechat.jar ./dist/
cp usp-service/usp-wms/target/usp-wms.jar ./dist/
cp usp-service/usp-pay/target/usp-pay.jar ./dist/
