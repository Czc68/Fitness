@echo off
echo 编译并运行健身打卡系统...
echo.

echo 检查Java环境...
java -version
if %errorlevel% neq 0 (
    echo 错误: 未找到Java环境
    pause
    exit /b 1
)

echo.
echo 创建target目录...
if not exist target mkdir target
if not exist target\classes mkdir target\classes

echo.
echo 编译Java文件...
javac -cp "lib/*" -d target/classes src/main/java/com/fitness/booking/*.java src/main/java/com/fitness/booking/*/*.java

if %errorlevel% neq 0 (
    echo 编译失败，尝试使用Maven Wrapper...
    if exist mvnw.cmd (
        echo 使用Maven Wrapper编译...
        mvnw.cmd clean compile
    ) else (
        echo 错误: 无法编译项目
        pause
        exit /b 1
    )
)

echo.
echo 启动应用程序...
java -cp "target/classes;lib/*" com.fitness.booking.BookingApplication

pause 