#!/usr/bin/env bash

# start.sh
# 서버 구동을 위한 스크립트

ABSPATH=$(readlink -f $0)
ABSDIR=$(dirname $ABSPATH)

REPOSITORY=/home/ec2-user/app
PROJECT_NAME=springboot

echo "> Build 파일 복사"
echo "> cp $REPOSITORY/deploy/*.jar $REPOSITORY/"

cp $REPOSITORY/deloy/*.jar $REPOSITORY/

echo "> 새 어플리케이션 배포"
JAR_NAME=$(ls -tr $REPOSITORY/*.jar | tail -n 1)

echo "> JAR Name: $JAR_NAME"

echo "> $JAR_NAME 에 실행권한 추가"

chmod +x $JAR_NAME

echo "> $JAR_NAME 실행"




nohup java -jar \
    -Dspring.config.location=classpath:/application.properties,/home/ec2-user/app/application-oauth.properties \
    > $REPOSITORY/nohup.out 2>&1 &
