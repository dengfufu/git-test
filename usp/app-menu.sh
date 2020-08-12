#!/bin/sh

## 变量: 服务路径、nacos地址、
export service_home="/home/usp/dist"

## 启动服务, 参数$1: 服务名
startService() {
  echo "--------开始启动$1---------------"
  SERVICE_ID=$(ps -ef | grep name=$1 | grep java | awk '{print "用户号: "$1", 进程号："$2}')
  if [ -n "$SERVICE_ID" ]; then
    echo "  $2. $1  :  $SERVICE_ID  已经存在！"
  else
  nohup java -Xms256m -Xmx512m -jar $service_home/$1.jar -Dname=$1 \
  --nacos-server=10.34.12.153:8848 \
  --config-group=USP_GROUP_DEV \
  --discovery-ip=10.34.12.153 \
  >/dev/null 2>&1 &
  date "+%Y-%m-%d %H:%M:%S $1 start. " >> boot.log
  sleep 5
  fi
}

startMinService() {
  echo "--------开始启动$1---------------"
  SERVICE_ID=$(ps -ef | grep name=$1 | grep java | awk '{print "用户号: "$1", 进程号："$2}')
  if [ -n "$SERVICE_ID" ]; then
    echo "  $2. $1  :  $SERVICE_ID  已经存在！"
  else
  nohup java -Xms256m -Xmx512m -jar $service_home/$1.jar -Dname=$1 \
  --nacos-server=10.34.12.153:8848 \
  --config-group=USP_GROUP_DEV \
  --discovery-ip=10.34.12.153 \
  >/dev/null 2>&1 &
  date "+%Y-%m-%d %H:%M:%S $1 start. " >> boot.log
  sleep 5
  fi
}

## 服务状态，参数$1: 服务名、参数$2:序号
showService() {
  SERVICE_ID=$(ps -ef | grep name=$1 | grep java | awk '{print "用户号: "$1", 进程号："$2}')
  if [ -n "$SERVICE_ID" ]; then
    echo "  $2. $1  :  $SERVICE_ID  "
  else
    echo "  $2. $1  :  不存在或者被停掉!  "
  fi
}

## 服务状态，参数$1: 服务名
stopService() {
  SERVICE_ID=$(ps -ef | grep name=$1 | grep java | awk '{print $2}')
  if [ "$SERVICE_ID" == "" ]; then
    echo "  $1 process not exists or stop success "
  else
    kill -9 $SERVICE_ID
    echo "  $1 $SERVICE_ID killed success "
  fi
}

selectedPS=none

disp_sys_man() {
  clear
  echo
  echo "     #######################################"
  echo "     #                                     #"
  echo "     #       紫金售后服务云管理平台        #"
  echo "     #                                     #"
  echo "     #           菜单                      #"
  echo "     #                                     #"
  echo "     #       1. 服务进程状态               #"
  echo "     #       2. 启动单个服务               #"
  echo "     #       3. 启动所有服务               #"
  echo "     #       4. 停止单个服务               #"
  echo "     #       5. 停止所有服务               #"
  echo "     #       6. 停止网关以外的服务         #"
  echo "     #       0. 退出                       #"
  echo "     #                                     #"
  echo "     #######################################"
  echo
  echo "(Choice) :                    "
}

handler_sys_man() {
  while [ 1 ]; do
    disp_sys_man
    read ans
    case $ans in
    0)
      clear
      exit
      ;;
    1)
      clear
      show
      read ans
      ;;
    2)
      clear
      start_ps
      read ans
      ;;
    3)
      clear
      start_all
      read ans
      ;;
    4)
      clear
      stop_ps
      read ans
      ;;
    5)
      clear
      stop_all
      read ans
      ;;
    6)
      clear
      stop_others
      read ans
      ;;
    *)
      echo "     无效输入!     "
      ;;
    esac
  done
}

## 1. 服务进程状态
show() {
  #clear
  echo ""
  echo "    ------  进程状态  ------- "
  echo ""
  showService usp-gateway 1
  showService usp-auth 2
  showService usp-uas 3
  showService usp-anyfix 4
  showService usp-device 5
  showService usp-file 6
  showService usp-pos 7
  showService usp-msg 8
  showService usp-monitor 9
  showService usp-pay 10
  showService usp-wechat 11
  showService xxl-job-admin 12
  showService xxl-job-executor-sample-springboot 13
  echo ""
  echo "    请按回车键继续...   "
  echo ""
}

## 2.启动单个服务
start_ps() {
  echo ""
  echo "     请选择1-2启动相应服务进程 :   "
  echo ""
  select_ps
  if [ "$selectedPS" != "none" ]; then
    startSelectPS
  fi
  return
}

## 3. 启动所有服务
start_all() {
  startService usp-gateway
#  sleep 5
  startService usp-auth
#  sleep 5
  startService usp-uas
#  sleep 5
  startService usp-anyfix
#  sleep 5
  startService usp-device
#  sleep 5
  startService usp-file
#  sleep 5
  startService usp-pos
#  sleep 5
  startService usp-msg
#  sleep 5
  startMinService usp-monitor
#  sleep 5
  startService usp-pay
#  sleep 5
  startService usp-wechat
#  sleep 5
  startService xxl-job-admin
#  sleep 5
  startService xxl-job-executor-sample-springboot
#  sleep 5
  clear
  show
}

## 4.停止单个服务
stop_ps() {
  echo ""
  echo "     请选择1-2停掉相应服务进程 :   "
  echo ""
  select_ps
  if [ "$selectedPS" != "none" ]; then
    stopSelectPS
  fi
  return
}

## 5. 停止所有服务
stop_all() {
  #clear
  echo ""
  echo "    ------  正在停止所有进程  ------- "
  echo ""
  stopService usp-gateway
  stopService usp-auth
  stopService usp-uas
  stopService usp-anyfix
  stopService usp-device
  stopService usp-file
  stopService usp-pos
  stopService usp-msg
  stopService usp-monitor
  stopService usp-pay
  stopService usp-wechat
  stopService xxl-job-admin
  stopService xxl-job-executor-sample-springboot
  sleep 2
  clear
  show
}

## 6. 停止网关以外的所有服务
stop_others() {
  #clear
  echo ""
  echo "    ------  正在停止网关以外的所有进程  ------- "
  echo ""
#  stopService usp-gateway
  stopService usp-auth
  stopService usp-uas
  stopService usp-anyfix
  stopService usp-device
  stopService usp-file
  stopService usp-pos
  stopService usp-msg
  stopService usp-monitor
  stopService usp-pay
  stopService usp-wechat
  stopService xxl-job-admin
  stopService xxl-job-executor-sample-springboot
  sleep 2
  clear
  show
}

## 选择服务
select_ps() {
  while [ 1 ]; do
    show
    echo "    0. 退出               "
    echo "(Choice) :                "
    read ans2
    case $ans2 in
    0)
      selectedPS=none
      clear
      echo "请按回车键继续..."
      break
      ;;
    1)
      selectedPS=usp-gateway
      ;;
    2)
      selectedPS=usp-auth
      ;;
    3)
      selectedPS=usp-uas
      ;;
    4)
      selectedPS=usp-anyfix
      ;;
    5)
      selectedPS=usp-device
      ;;
    6)
      selectedPS=usp-file
      ;;
    7)
      selectedPS=usp-pos
      ;;
    8)
      selectedPS=usp-msg
      ;;
    9)
      selectedPS=usp-monitor
      ;;
    10)
      selectedPS=usp-pay
      ;;
    11)
      selectedPS=usp-wechat
      ;;
    12)
      selectedPS=xxl-job-admin
      ;;
    13)
      selectedPS=xxl-job-executor-sample-springboot
      ;;
    *)
      selectedPS=none
      clear
      echo ""
      echo "    选择错误，请重新输入:   "
      select_ps
      ;;
    esac
    break
  done
}

## 根据选项启动单个服务
startSelectPS() {
  echo " "
  echo "    正在启动相应服务进程,请稍等 ...   "
  case $selectedPS in
  usp-gateway)
    startService usp-gateway
    ;;
  usp-auth)
    startService usp-auth
    ;;
  usp-uas)
    startService usp-uas
    ;;
  usp-anyfix)
    startService usp-anyfix
    ;;
  usp-device)
    startService usp-device
    ;;
  usp-file)
    startService usp-file
    ;;
  usp-pos)
    startService usp-pos
    ;;
  usp-msg)
    startService usp-msg
    ;;
  usp-monitor)
    startMinService usp-monitor
    ;;
  usp-pay)
    startService usp-pay
    ;;
  usp-wechat)
    startService usp-wechat
    ;;
  xxl-job-admin)
    startService xxl-job-admin
    ;;
  xxl-job-executor-sample-springboot)
    startService xxl-job-executor-sample-springboot
    ;;
  *)
    echo "    选择错误,请重新输入:"
    break
    ;;
  esac
  sleep 5
  clear
  start_ps
}

## 根据选项停止单个服务
stopSelectPS() {
  echo
  echo "    正在停掉相应服务进程, 请稍等 ... "
  case $selectedPS in
  usp-gateway)
    stopService usp-gateway
    ;;
  usp-auth)
    stopService usp-auth
    ;;
  usp-uas)
    stopService usp-uas
    ;;
  usp-anyfix)
    stopService usp-anyfix
    ;;
  usp-device)
    stopService usp-device
    ;;
  usp-file)
    stopService usp-file
    ;;
  usp-pos)
    stopService usp-pos
    ;;
  usp-msg)
    stopService usp-msg
    ;;
  usp-monitor)
    stopService usp-monitor
    ;;
  usp-pay)
    stopService usp-pay
    ;;
  usp-wechat)
    stopService usp-wechat
    ;;
  xxl-job-admin)
    stopService xxl-job-admin
    ;;
  xxl-job-executor-sample-springboot)
    stopService xxl-job-executor-sample-springboot
    ;;
  *)
    echo "    选择错误,请重新输入:   "
    break
    ;;
  esac
  sleep 5
  clear
  stop_ps
}

handler_sys_man
