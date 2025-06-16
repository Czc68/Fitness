# 健身打卡系统

这是一个基于Spring Boot和SQL Server的健身打卡系统，提供用户登录、打卡记录、数据可视化等功能。

## 功能特性

- 🔐 用户登录认证
- 📊 每日健身打卡
- 📈 数据可视化图表
- 📝 打卡历史记录
- 🗑️ 记录管理（删除、编辑）
- 📊 统计数据展示

## 技术栈

### 前端
- HTML5 + CSS3 + JavaScript
- Chart.js (数据可视化)
- Font Awesome (图标)
- 响应式设计

### 后端
- Spring Boot 2.7.0
- Java 11
- SQL Server 数据库
- JDBC Template
- RESTful API

## 数据库配置

系统使用SQL Server数据库，数据库名称为 `Fitness2023404741`。

### 数据库连接配置
- 服务器: localhost:1433
- 数据库: Fitness2023404741
- 用户名: sa
- 密码: 123456

请确保SQL Server服务已启动，并且数据库已创建。

## 快速开始

### 1. 环境要求
- Java 11 或更高版本
- Maven 3.6 或更高版本
- SQL Server 2012 或更高版本

### 2. 数据库准备
确保SQL Server数据库已创建并运行，数据库名称为 `Fitness2023404741`。

### 3. 启动后端服务
```bash
cd hd
mvn clean install
mvn spring-boot:run
```

后端服务将在 `http://localhost:8080` 启动。

### 4. 访问前端页面
在浏览器中打开 `login.html` 文件，或通过 `http://localhost:8080/login.html` 访问。

## 默认用户

系统启动时会自动创建以下测试用户：

### 普通用户
- 用户名: `testuser`
- 密码: `123456`

### 管理员用户
- 用户名: `admin`
- 密码: `admin123`

## API 接口

### 认证相关
- `POST /api/auth/login` - 用户登录
- `POST /api/auth/logout` - 用户退出
- `GET /api/auth/validate` - 验证Token

### 用户相关
- `GET /api/user/profile` - 获取用户统计信息
- `GET /api/user/info` - 获取用户基本信息

### 打卡相关
- `POST /api/checkins/submit` - 提交打卡
- `GET /api/checkins/history` - 获取打卡历史
- `DELETE /api/checkins/{id}` - 删除打卡记录
- `PUT /api/checkins/{id}` - 更新打卡记录
- `GET /api/checkins/stats` - 获取统计数据

## 打卡数据字段

- `weight` - 体重 (kg)
- `sleep_hours` - 睡眠时长 (小时)
- `water_cups` - 饮水量 (杯)
- `steps` - 步数
- `mood` - 心情 (Excellent/Good/Neutral/Tired/Bad)
- `completed_plan_today` - 是否完成训练计划 (true/false)
- `general_notes` - 备注

## 数据可视化

系统提供以下图表：
- 体重变化趋势图
- 睡眠质量趋势图
- 步数统计图
- 心情分布饼图

## 项目结构

```
Fitness/
├── hd/                          # Java后端项目
│   ├── src/main/java/com/fitness/booking/
│   │   ├── config/              # 配置类
│   │   ├── controller/          # 控制器
│   │   ├── dao/                 # 数据访问层
│   │   ├── model/               # 实体类
│   │   └── service/             # 服务层
│   ├── src/main/resources/      # 配置文件
│   └── pom.xml                  # Maven配置
├── css/                         # 样式文件
├── js/                          # JavaScript文件
├── images/                      # 图片资源
├── login.html                   # 登录页面
├── data.html                    # 打卡数据页面
└── README.md                    # 项目说明
```

## 注意事项

1. **安全性**: 当前版本使用简单的密码存储，生产环境应使用加密。
2. **Token管理**: 当前使用内存存储Token，生产环境应使用Redis或数据库。
3. **数据库连接**: 请确保SQL Server服务正常运行，并检查连接配置。
4. **端口冲突**: 如果8080端口被占用，可在 `application.properties` 中修改 `server.port`。

## 故障排除

### 数据库连接失败
- 检查SQL Server服务是否启动
- 验证数据库连接字符串和凭据
- 确保数据库 `Fitness2023404741` 已创建

### 前端无法访问后端API
- 确保后端服务在8080端口运行
- 检查浏览器控制台是否有CORS错误
- 验证API请求路径是否正确

### 登录失败
- 检查用户名和密码是否正确
- 查看后端日志是否有错误信息
- 确保数据库中有用户数据

## 开发说明

### 添加新功能
1. 在 `model` 包中创建实体类
2. 在 `dao` 包中创建数据访问层
3. 在 `service` 包中创建业务逻辑
4. 在 `controller` 包中创建API接口
5. 更新前端页面以使用新功能

### 数据库修改
如需修改数据库结构，请：
1. 更新相应的实体类
2. 修改DAO层的SQL语句
3. 更新数据库创建脚本

## 许可证

本项目仅供学习和演示使用。 