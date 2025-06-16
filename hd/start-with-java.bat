@echo off
echo 设置JAVA_HOME...
set JAVA_HOME=C:\Program Files\Microsoft\jdk-11.0.16.101-hotspot

echo 清理并编译项目...
call mvnw.cmd clean compile

echo 运行Spring Boot应用...
call mvnw.cmd spring-boot:run

pause 