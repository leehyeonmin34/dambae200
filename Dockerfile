FROM openjdk:11-jdk

# 개발자
MAINTAINER leehyeonmin34

# 빌드 파일을 app.jar로 복사해서 실행
# build할 때 지정한 컨텍스트의 상대주소로 적용된다.
RUN useradd dambae200
ENV JAR_FILE=/build/libs/dambae200-0.0.1-SNAPSHOT.jar
COPY $JAR_FILE app.jar
ENV AGENT_ID 1

ENTRYPOINT ["nohup","java","-jar",\
           "-javaagent:./pinpoint/pinpoint-bootstrap-2.5.2.jar",\
           "-Dpinpoint.agentId=dambae200-server-$AGENT_ID",\
           "-Dpinpoint.applicationName=dambae200-server",\
           "-Dpinpoint.config=./pinpoint/pinpoint-root.config",\
           "-Dspring.profiles.active=prod","app.jar","2>&1","&"]

# ENTRYPOINT ["java","-jar","app.jar", "--spring.profiles.active=prod"]