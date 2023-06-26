# 베이스 이미지 설정 (도커이미지에 자바 버전을 세팅)
FROM openjdk:17

# 빌드파일의 경로로 변수설정
ARG JAR_FILE=build/libs/passioncode-0.0.1-SNAPSHOT.jar

# 빌드파일을 컨테이너로 복사
COPY ${JAR_FILE} app.jar

# jar 파일 실행
ENTRYPOINT [ "java","-jar","/app.jar" ]