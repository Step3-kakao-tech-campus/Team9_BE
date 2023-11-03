# 1. build
# gradle:7.3.1-jdk17 이미지를 기반으로 함
FROM gradle:7.3.1-jdk17

# 작업 디렉토리 설정
WORKDIR /home/gradle/project
# Spring 소스 코드를 이미지에 복사
COPY . .

WORKDIR /home/gradle/project/linknamu
# gradle 빌드 시 proxy 설정을 gradle.properties에 추가
RUN echo "systemProp.http.proxyHost=krmp-proxy.9rum.cc\nsystemProp.http.proxyPort=3128\nsystemProp.https.proxyHost=krmp-proxy.9rum.cc\nsystemProp.https.proxyPort=3128" > /root/.gradle/gradle.properties

# gradlew를 이용한 프로젝트 필드
RUN ./gradlew clean build -x test

# 빌드 결과 jar 파일을 실행
CMD ["java", "-jar","-Dhttp.proxyHost=krmp-proxy.9rum.cc", "-Dhttps.proxyPort=3128", "-Dhttps.proxyHost=krmp-proxy.9rum.cc", "-Dhttp.proxyPort=3128",  "-Dspring.profiles.active=prod", "/home/gradle/project/linknamu/build/libs/linknamu.jar"]

