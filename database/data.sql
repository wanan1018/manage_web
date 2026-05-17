USE task_db;

-- 插入默认用户（密码均为123456，MD5加密）
INSERT INTO `user` (`username`, `password`, `role`) VALUES
('tutor1', 'e10adc3949ba59abbe56e057f20f883e', 'TUTOR'),
('intern1', 'e10adc3949ba59abbe56e057f20f883e', 'INTERN');

-- 插入测试任务
INSERT INTO `task` (`title`, `description`, `status`, `priority`, `assignee_id`, `creator_id`, `due_date`) VALUES
('学习Spring Boot基础', '完成Spring Boot官方入门教程，了解核心概念', 'TODO', 'HIGH', 2, 1, '2026-05-20'),
('设计数据库表结构', '根据需求文档设计完整的ER图并生成建表语句', 'IN_PROGRESS', 'MEDIUM', 2, 1, '2026-05-18'),
('编写前端登录页面', '使用Vue3和Element Plus实现登录界面', 'TODO', 'MEDIUM', 2, 1, '2026-05-22'),
('开发后端登录接口', '实现JWT认证的登录接口', 'TODO', 'HIGH', 2, 1, '2026-05-21');
