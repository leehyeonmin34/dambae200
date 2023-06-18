#!/bin/bash

DOCKER_APP_NAME=dambae200

# 현재 blue 컨테이너가 작동 중(up)인지
EXIST_BLUE=$(docker-compose -p ${DOCKER_APP_NAME}-blue -f docker-compose.blue.yml ps | grep running)

# 현재 blue 컨테이너가 작동 중(up)이 아니라면, blue 컨테이너 up, green 컨테이너 down
if [ -z "$EXIST_BLUE" ]; then
    echo "blue up"
    docker-compose -p ${DOCKER_APP_NAME}-blue -f docker-compose.blue.yml up -d

    sleep 10

    docker-compose -p ${DOCKER_APP_NAME}-green -f docker-compose.green.yml down
else
    # 현재 blue 컨테이너가 작동 중(up)이라면, green 컨테이너 up, blue 컨테이너 down
    echo "green up"
    docker-compose -p ${DOCKER_APP_NAME}-green -f docker-compose.green.yml up -d

    sleep 10

    docker-compose -p ${DOCKER_APP_NAME}-blue -f docker-compose.blue.yml down
fi