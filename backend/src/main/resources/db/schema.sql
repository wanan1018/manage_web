-- ================================================================
--  @project   task-management-system
--  @author    wanan1018
--  @github    https://github.com/wanan1018/manage_web
--  @date      2026-05-17
--  @file      schema.sql
-- ================================================================
DROP TABLE IF EXISTS `task`;
DROP TABLE IF EXISTS `user`;

CREATE TABLE `user` (
  `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
  `username` VARCHAR(32) NOT NULL UNIQUE,
  `password` VARCHAR(128) NOT NULL,
  `role` VARCHAR(16) NOT NULL DEFAULT 'INTERN',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE `task` (
  `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
  `title` VARCHAR(100) NOT NULL,
  `description` TEXT,
  `status` VARCHAR(20) NOT NULL DEFAULT 'TODO',
  `priority` VARCHAR(10) DEFAULT 'MEDIUM',
  `assignee_id` BIGINT NOT NULL,
  `creator_id` BIGINT NOT NULL,
  `due_date` DATE DEFAULT NULL,
  `related_news_url` VARCHAR(512) DEFAULT NULL,
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP
);
