@echo off
echo 启动健身打卡系统...
echo.

echo 检查Java环境...
java -version
if %errorlevel% neq 0 (
    echo 错误: 未找到Java环境，请安装Java 11或更高版本
    pause
    exit /b 1
)

echo.
echo 进入后端项目目录...
cd hd

echo.
echo 检查Maven Wrapper...
if exist mvnw.cmd (
    echo 使用Maven Wrapper启动项目...
    echo 后端服务将在 http://localhost:8080 启动
    echo 按 Ctrl+C 停止服务
    echo.
    mvnw.cmd spring-boot:run
) else (
    echo 错误: 未找到Maven Wrapper文件
    echo 请确保mvnw.cmd文件存在于hd目录中
    pause
    exit /b 1
)

pause 