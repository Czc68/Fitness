-- 修复user_role列长度问题
-- 将user_role列从CHAR(4)改为VARCHAR(10)以支持更长的角色名称

-- 先删除现有的约束
IF EXISTS (SELECT * FROM sys.check_constraints WHERE name = 'CK__users__user_role__[0-9A-F]*')
BEGIN
    DECLARE @constraint_name NVARCHAR(128)
    SELECT @constraint_name = name FROM sys.check_constraints WHERE name LIKE 'CK__users__user_role__%'
    EXEC('ALTER TABLE users DROP CONSTRAINT ' + @constraint_name)
END

-- 修改列类型
ALTER TABLE users ALTER COLUMN user_role VARCHAR(10);

-- 重新添加约束
ALTER TABLE users ADD CONSTRAINT CK_users_user_role CHECK (user_role IN ('用户', '管理员'));

-- 更新现有数据
UPDATE users SET user_role = '用户' WHERE user_role IS NULL OR user_role = '';

PRINT '数据库表结构修复完成！'; 