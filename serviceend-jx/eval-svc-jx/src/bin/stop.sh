#!/bin/sh
APP_NAME=eval-svc-jx
pid=`ps -ef|grep ${APP_NAME}|grep -v grep|grep -v kill|awk '{print $2}'`
if [[ ${pid} ]]; then
    echo 'Stop Process...'
    kill -15 ${pid}
fi
sleep 5
pid=`ps -ef|grep ${APP_NAME}|grep -v grep|grep -v kill|awk '{print $2}'`
if [[ ${pid} ]]; then
    echo 'Kill Process!'
    kill -9 ${pid}
else
    echo 'Stop Success!'
fi
