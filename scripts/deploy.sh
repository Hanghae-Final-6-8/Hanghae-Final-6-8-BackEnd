#!/bin/bash

PROJECT=final
REPOSITORY=/home/ubuntu/sparta

CONTAINER_ID=$(docker container ls -f "name=final" -q)

echo "> 컨테이너 ID : ${CONTAINER_ID}"

if [ -z ${CONTAINER_ID} ]
then
  echo "> 현재 구동중인 애플리케이션이 없으므로 종료하지 않습니다." >> /home/docker/deploy.log
else
  echo "> docker stop ${CONTAINER_ID}"
  sudo docker stop ${CONTAINER_ID}
  echo "> docker rm ${CONTAINER_ID}"
  sudo docker rm ${CONTAINER_ID}
  sleep 5
fi

cd /home/ubuntu/final && docker build -t final .
docker run --name final -d -e active=prod -p 8080:8080 final
