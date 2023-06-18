FROM openjdk:11-jdk

# 개발자
MAINTAINER leehyeonmin34

# 빌드 파일을 app.jar로 복사해서 실행
# build할 때 지정한 컨텍스트의 상대주소로 적용된다.
ENV JAR_FILE=/build/libs/dambae200-0.0.1-SNAPSHOT.jar
COPY $JAR_FILE app.jar
ENTRYPOINT ["java","-jar","app.jar", "--spring.profiles.active=prod"]