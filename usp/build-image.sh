#!/bin/sh

cd usp-auth
docker build -t usp/usp-auth .

cd ../usp-gateway
docker build -t usp/usp-gateway .

cd ../usp-monitor
docker build -t usp/usp-monitor .

cd ../usp-service/usp-uas
docker build -t usp/usp-uas .

cd ../usp-anyfix
docker build -t usp/usp-anyfix .

cd ../usp-device
docker build -t usp/usp-device .

cd ../usp-msg
docker build -t usp/usp-msg .

cd ../usp-pos
docker build -t usp/usp-pos .

cd ../usp-file
docker build -t usp/usp-file .
