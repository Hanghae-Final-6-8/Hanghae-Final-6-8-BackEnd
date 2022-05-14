PROJECT=backend
REPOSITORY=/home/ubuntu/deploy
LOGS_DIRECTORY=/home/ubuntu/deploy/logs
LOG_BACKUP_DIRECTORY=/home/ubuntu/deploy/log-backup

echo "> 현재 구동 중인 애플리케이션 pid 확인"

CURRENT_PID=$(pgrep -fl $PROJECT | grep java | awk '{print $1}')

echo "현재 구동 중인 애플리케이션 pid: $CURRENT_PID"

if [ -z "$CURRENT_PID" ]; then
  echo "현재 구동 중인 애플리케이션이 없으므로 종료하지 않습니다."
else
  echo "> kill -15 $CURRENT_PID"
  kill -15 "$CURRENT_PID"
  sleep 5
fi

echo "> 새 애플리케이션 배포"

JAR_NAME=$(find $REPOSITORY/*.jar | tail -n 1)

echo "> JAR NAME: $JAR_NAME"

echo "> $JAR_NAME 에 실행권한 추가"

sudo chmod +x "$JAR_NAME"

echo "> $JAR_NAME 실행"

if [ -d $LOGS_DIRECTORY ]
then
  echo "$LOGS_DIRECTORY 가 이미 존재합니다."
else
  ln -s /opt/codedeploy-agent/logs /home/ubuntu/deploy/logs
fi

if [ -d $LOG_BACKUP_DIRECTORY ]
then
  echo "$LOG_BACKUP_DIRECTORY 가 이미 존재합니다."
else
  ln -s /opt/codedeploy-agent/log-backup /home/ubuntu/deploy/log-backup
fi

# 실행
sudo nohup java -Duser.timezone=KST -jar "$JAR_NAME" > $REPOSITORY/nohup.out 2>&1 &
