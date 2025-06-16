-- 健身打卡系统数据库初始化脚本
-- 数据库名称: Fitness2023404741

USE Fitness2023404741;
GO

-- 1. 创建users表
CREATE TABLE users (
    user_id INT IDENTITY(1,1) PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    full_name VARCHAR(100),
    date_of_birth DATE,
    gender CHAR(10) CHECK (gender IN('男','女','保密')),
    height DECIMAL(5,2),
    weight DECIMAL(5,2),
    fitness_level VARCHAR(20),
    profile_picture_url VARCHAR(255),
    created_at DATETIME DEFAULT GETDATE(),
    updated_at DATETIME DEFAULT GETDATE(),
    status VARCHAR(20) DEFAULT 'active',
    user_role VARCHAR(10) DEFAULT '用户' CHECK (user_role IN ('用户', '管理员'))
);

-- 创建更新触发器实现updated_at自动更新
CREATE TRIGGER trg_users_updated_at
ON users
AFTER UPDATE
AS
BEGIN
    SET NOCOUNT ON;
    UPDATE users
    SET updated_at = GETDATE()
    WHERE user_id IN (SELECT user_id FROM inserted);
END;
GO

-- 2. 创建plans表
CREATE TABLE plans (
    plan_id INT IDENTITY(1,1) PRIMARY KEY,
    user_id INT FOREIGN KEY REFERENCES users(user_id),
    plan_name VARCHAR(100) NOT NULL,
    plan_description TEXT,
    plan_type VARCHAR(50),
    duration INT,
    duration_unit VARCHAR(10),
    difficulty VARCHAR(20),
    is_public BIT DEFAULT 0,
    created_at DATETIME DEFAULT GETDATE(),
    updated_at DATETIME DEFAULT GETDATE()
);

-- 创建更新触发器实现updated_at自动更新
CREATE TRIGGER trg_plans_updated_at
ON plans
AFTER UPDATE
AS
BEGIN
    SET NOCOUNT ON;
    UPDATE plans
    SET updated_at = GETDATE()
    WHERE plan_id IN (SELECT plan_id FROM inserted);
END;
GO

-- 3. 创建workouts训练项目表
CREATE TABLE workouts (
    workout_id INT IDENTITY(1,1) PRIMARY KEY,
    workout_name VARCHAR(100) NOT NULL UNIQUE,
    description TEXT,
    body_part VARCHAR(50),
    equipment VARCHAR(50),
    video_url VARCHAR(255),
    created_by INT FOREIGN KEY REFERENCES users(user_id)
);

-- 4. 创建计划详情表plan_workouts
CREATE TABLE plan_workouts (
    plan_workout_id INT IDENTITY(1,1) PRIMARY KEY,
    plan_id INT FOREIGN KEY REFERENCES plans(plan_id) NOT NULL,
    workout_id INT FOREIGN KEY REFERENCES workouts(workout_id) NOT NULL,
    day_in_plan INT NOT NULL,
    sequence_num INT,
    sets INT,
    reps VARCHAR(50),
    rest_seconds INT,
    target_weight DECIMAL(5,2),
    notes TEXT
);

-- 5. 创建训练记录表workout_logs
CREATE TABLE workout_logs (
    log_id INT IDENTITY(1,1) PRIMARY KEY,
    user_id INT FOREIGN KEY REFERENCES users(user_id) NOT NULL,
    workout_id INT FOREIGN KEY REFERENCES workouts(workout_id) NOT NULL,
    plan_id INT FOREIGN KEY REFERENCES plans(plan_id),
    plan_workout_id INT FOREIGN KEY REFERENCES plan_workouts(plan_workout_id),
    log_date DATE NOT NULL,
    actual_sets INT NOT NULL,
    actual_reps VARCHAR(50),
    actual_weight DECIMAL(5,2),
    distance DECIMAL(6,2),
    duration_minutes INT,
    perceived_effort VARCHAR(20),
    notes TEXT,
    created_at DATETIME DEFAULT GETDATE()
);

-- 6. 创建健身打卡记录表checkins
CREATE TABLE checkins (
    checkin_id INT IDENTITY(1,1) PRIMARY KEY,
    user_id INT FOREIGN KEY REFERENCES users(user_id) NOT NULL,
    checkin_date DATE NOT NULL,
    checkin_time TIME NOT NULL,
    workout_type VARCHAR(50),
    duration_minutes INT,
    calories_burned INT,
    notes TEXT,
    mood VARCHAR(20),
    created_at DATETIME DEFAULT GETDATE(),
    updated_at DATETIME DEFAULT GETDATE()
);

-- 创建更新触发器实现updated_at自动更新
CREATE TRIGGER trg_checkins_updated_at
ON checkins
AFTER UPDATE
AS
BEGIN
    SET NOCOUNT ON;
    UPDATE checkins
    SET updated_at = GETDATE()
    WHERE checkin_id IN (SELECT checkin_id FROM inserted);
END;
GO

-- 7. 创建目标表goals
CREATE TABLE goals (
    goal_id INT IDENTITY(1,1) PRIMARY KEY,
    user_id INT FOREIGN KEY REFERENCES users(user_id) NOT NULL,
    goal_name VARCHAR(100) NOT NULL,
    goal_description TEXT,
    goal_type VARCHAR(50),
    target_value DECIMAL(10,2),
    current_value DECIMAL(10,2) DEFAULT 0,
    unit VARCHAR(20),
    start_date DATE,
    target_date DATE,
    status VARCHAR(20) DEFAULT 'active',
    created_at DATETIME DEFAULT GETDATE(),
    updated_at DATETIME DEFAULT GETDATE()
);

-- 创建更新触发器实现updated_at自动更新
CREATE TRIGGER trg_goals_updated_at
ON goals
AFTER UPDATE
AS
BEGIN
    SET NOCOUNT ON;
    UPDATE goals
    SET updated_at = GETDATE()
    WHERE goal_id IN (SELECT goal_id FROM inserted);
END;
GO

-- 8. 插入一些基础数据

-- 插入一些示例训练项目
INSERT INTO workouts (workout_name, description, body_part, equipment) VALUES
('俯卧撑', '标准俯卧撑，锻炼胸部和手臂肌肉', '胸部', '无器械'),
('深蹲', '标准深蹲，锻炼腿部肌肉', '腿部', '无器械'),
('仰卧起坐', '锻炼腹部肌肉', '腹部', '无器械'),
('跑步', '有氧运动，提高心肺功能', '全身', '无器械'),
('哑铃弯举', '使用哑铃锻炼手臂肌肉', '手臂', '哑铃');

PRINT '数据库表结构创建完成！';
PRINT '已创建以下表：';
PRINT '- users (用户表)';
PRINT '- plans (计划表)';
PRINT '- workouts (训练项目表)';
PRINT '- plan_workouts (计划详情表)';
PRINT '- workout_logs (训练记录表)';
PRINT '- checkins (打卡记录表)';
PRINT '- goals (目标表)';
PRINT '已插入5个基础训练项目。'; 