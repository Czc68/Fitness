@echo off
echo 快速启动健身打卡系统...
echo.

cd hd
echo 使用Maven Wrapper启动Spring Boot应用...
echo 后端服务将在 http://localhost:8080 启动
echo 按 Ctrl+C 停止服务
echo.

mvnw.cmd spring-boot:run

pause 