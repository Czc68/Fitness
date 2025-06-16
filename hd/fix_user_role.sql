-- 修复user_role列问题
USE Fitness2023404741;
GO

-- 查看当前user_role列的定义
SELECT 
    COLUMN_NAME,
    DATA_TYPE,
    CHARACTER_MAXIMUM_LENGTH,
    IS_NULLABLE,
    COLUMN_DEFAULT
FROM INFORMATION_SCHEMA.COLUMNS 
WHERE TABLE_NAME = 'users' AND COLUMN_NAME = 'user_role';

-- 查看当前的约束
SELECT 
    CONSTRAINT_NAME,
    CONSTRAINT_TYPE,
    CHECK_CLAUSE
FROM INFORMATION_SCHEMA.CHECK_CONSTRAINTS 
WHERE CONSTRAINT_NAME LIKE '%user_role%';

-- 删除现有的约束（如果存在）
IF EXISTS (SELECT * FROM sys.check_constraints WHERE name LIKE '%user_role%')
BEGIN
    DECLARE @constraint_name NVARCHAR(128)
    SELECT @constraint_name = name FROM sys.check_constraints WHERE name LIKE '%user_role%'
    EXEC('ALTER TABLE users DROP CONSTRAINT ' + @constraint_name)
    PRINT '已删除现有约束: ' + @constraint_name
END

-- 修改user_role列为VARCHAR(10)
ALTER TABLE users ALTER COLUMN user_role VARCHAR(10) NOT NULL DEFAULT '用户';
PRINT '已将user_role列修改为VARCHAR(10)';

-- 添加新的约束
ALTER TABLE users ADD CONSTRAINT CK_users_user_role CHECK (user_role IN ('用户', '管理员'));
PRINT '已添加新的约束';

-- 更新现有数据
UPDATE users SET user_role = '用户' WHERE user_role IS NULL OR user_role = '';
PRINT '已更新现有数据';

-- 验证修改结果
SELECT 
    COLUMN_NAME,
    DATA_TYPE,
    CHARACTER_MAXIMUM_LENGTH,
    IS_NULLABLE,
    COLUMN_DEFAULT
FROM INFORMATION_SCHEMA.COLUMNS 
WHERE TABLE_NAME = 'users' AND COLUMN_NAME = 'user_role';

PRINT 'user_role列修复完成！'; 