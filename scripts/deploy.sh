# 2023.8.3(목) 22h30 은영 작성
# reference = https://velog.io/@shawnhansh/shell을-활용하여-배포-자동화하기
REPOSITORY=/home/ubuntu/app
PROJECT_NAME=knou-key-project
WAR_NAME=key-project
TOMCAT=/home/ubuntu/tomcat/tomcat10.1
TODAY=${date "+%Y%m%d"}

echo "> 현재 시간: $(date)" >> $REPOSITORY/$PROJECT_NAME/deploy.log

cd $REPOSITORY/$PROJECT_NAME

echo "> git pull origin devBack" >> $REPOSITORY/$PROJECT_NAME/deploy.log
git pull origin devBack

echo "> application.yml 파일 추가" >> $REPOSITORY/$PROJECT_NAME/deploy.log
cd $REPOSITORY/$PROJECT_NAME/src/main
mkdir "resources"
touch "application-local.yml"

echo "> gradle build" >> $REPOSITORY/$PROJECT_NAME/deploy.log
./gradlew build

echo "> 새로운 war 파일의 이름 변경" >> $REPOSITORY/$PROJECT_NAME/deploy.log
cp $REPOSITORY/$PROJECT_NAME/build/libs/$WAR_NAME-0.0.1-SNAPSHOT-plain.war $REPOSITORY/$PROJECT_NAME/build/libs/$WAR_NAME.war

echo "> 기존 war 백업" >> $REPOSITORY/$PROJECT_NAME/deploy.log
mv $TOMCAT/webapps/$WAR_NAME.war ~/backup/$WAR_NAME_${TODAY}.war

echo "> 기존 war 폴더 삭제" >> $REPOSITORY/$PROJECT_NAME/deploy.log
rm -rf $TOMCAT/webapps/$WAR_NAME

echo "> 새로운 war 파일을 배포 위치로 복사" >> $REPOSITORY/$PROJECT_NAME/deploy.log
cp $REPOSITORY/$PROJECT_NAME/build/libs/$WAR_NAME.war $TOMCAT/webapps

echo "> 톰캣 종료" >> $REPOSITORY/$PROJECT_NAME/deploy.log
$TOMCAT/bin/./shutdown.sh

echo "> 톰캣 시작" >> $REPOSITORY/$PROJECT_NAME/deploy.log
$TOMCAT/bin/.startup.sh

##!/bin/bash
#BUILD_WAR=$(ls /home/ubuntu/app/knou-key-project/build/libs/key-project.war)
#WAR_NAME=$(basename $BUILD_WAR)
#
#echo "> 현재 시간: $(date)" >> /home/ubuntu/app/knou-key-project/deploy.log
#
#echo "> build 파일명: $WAR_NAME" >> /home/ubuntu/app/knou-key-project/deploy.log
#
#echo "> build 파일 복사" >> /home/ubuntu/app/knou-key-project/deploy.log
#DEPLOY_PATH=/home/ubuntu/tomcat/tomcat10.1/webapp
#cp $BUILD_WAR $DEPLOY_PATH
#
#echo "> 현재 실행중인 애플리케이션 pid 확인" >> /home/ubuntu/app/knou-key-project/deploy.log
#CURRENT_PID=$(pgrep -f $WAR_NAME)
#
#if [ -z $CURRENT_PID ]
#then
#  echo "> 현재 구동중인 애플리케이션이 없으므로 종료하지 않습니다." >> /home/ubuntu/app/knou-key-project/deploy.log
#else
#  echo "> kill -9 $CURRENT_PID" >> /home/ubuntu/app/knou-key-project/deploy.log
#  sudo kill -9 $CURRENT_PID
#  sleep 5
#fi
#
#
#DEPLOY_WAR=$DEPLOY_PATH$WAR_NAME
#echo "> DEPLOY_WAR 배포"    >> /home/ubuntu/app/knou-key-project/deploy.log
#sudo nohup java -war $DEPLOY_WAR >> /home/ubuntu/app/knou-key-project/deploy.log 2>/home/ubuntu/app/knou-key-project/deploy_err.log &