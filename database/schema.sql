CREATE DATABASE IF NOT EXISTS task_db
  DEFAULT CHARACTER SET utf8mb4
  COLLATE utf8mb4_unicode_ci;

USE task_db;

-- 用户表
CREATE TABLE IF NOT EXISTS `user` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `username` varchar(32) NOT NULL COMMENT '用户名',
  `password` varchar(128) NOT NULL COMMENT '密码（加密存储）',
  `role` varchar(16) NOT NULL DEFAULT 'INTERN' COMMENT '角色：TUTOR(导师)/INTERN(实习生)',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_username` (`username`) COMMENT '用户名唯一索引'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- 任务表
CREATE TABLE IF NOT EXISTS `task` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '任务ID',
  `title` varchar(100) NOT NULL COMMENT '任务标题',
  `description` text COMMENT '任务描述',
  `status` varchar(20) NOT NULL DEFAULT 'TODO' COMMENT '任务状态：TODO/IN_PROGRESS/DONE',
  `priority` varchar(10) DEFAULT 'MEDIUM' COMMENT '优先级：LOW/MEDIUM/HIGH',
  `assignee_id` bigint NOT NULL COMMENT '负责人ID',
  `creator_id` bigint NOT NULL COMMENT '创建人ID',
  `due_date` date DEFAULT NULL COMMENT '截止日期',
  `related_news_url` varchar(512) DEFAULT NULL COMMENT '相关资讯链接',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_assignee` (`assignee_id`),
  KEY `idx_status` (`status`),
  CONSTRAINT `fk_task_assignee` FOREIGN KEY (`assignee_id`) REFERENCES `user` (`id`),
  CONSTRAINT `fk_task_creator` FOREIGN KEY (`creator_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='任务表';
