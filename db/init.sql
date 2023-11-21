-- DormLink database bootstrap SQL
-- Usage:
--   mysql -u root -p < db/init.sql

CREATE DATABASE IF NOT EXISTS `wms`
  DEFAULT CHARACTER SET utf8mb4
  DEFAULT COLLATE utf8mb4_unicode_ci;

USE `wms`;

CREATE TABLE IF NOT EXISTS `admin` (
  `username` VARCHAR(32) NOT NULL,
  `password` VARCHAR(128) NOT NULL,
  `name` VARCHAR(64) NOT NULL,
  `gender` VARCHAR(16) DEFAULT NULL,
  `age` INT DEFAULT 0,
  `phone_num` VARCHAR(32) DEFAULT NULL,
  `email` VARCHAR(128) DEFAULT NULL,
  `avatar` VARCHAR(255) DEFAULT NULL,
  PRIMARY KEY (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS `student` (
  `username` VARCHAR(32) NOT NULL,
  `password` VARCHAR(128) NOT NULL,
  `name` VARCHAR(64) NOT NULL,
  `age` INT DEFAULT 0,
  `gender` VARCHAR(16) DEFAULT NULL,
  `phone_num` VARCHAR(32) DEFAULT NULL,
  `email` VARCHAR(128) DEFAULT NULL,
  `avatar` VARCHAR(255) DEFAULT NULL,
  PRIMARY KEY (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS `dorm_build` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `dormbuild_id` INT NOT NULL,
  `dormbuild_name` VARCHAR(64) NOT NULL,
  `dormbuild_detail` VARCHAR(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_dorm_build_code` (`dormbuild_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS `dorm_manager` (
  `username` VARCHAR(32) NOT NULL,
  `password` VARCHAR(128) NOT NULL,
  `dormbuild_id` INT NOT NULL,
  `name` VARCHAR(64) NOT NULL,
  `gender` VARCHAR(16) DEFAULT NULL,
  `age` INT DEFAULT 0,
  `phone_num` VARCHAR(32) DEFAULT NULL,
  `email` VARCHAR(128) DEFAULT NULL,
  `avatar` VARCHAR(255) DEFAULT NULL,
  PRIMARY KEY (`username`),
  KEY `idx_dorm_manager_building` (`dormbuild_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS `dorm_room` (
  `dormroom_id` INT NOT NULL,
  `dormbuild_id` INT NOT NULL,
  `floor_num` INT NOT NULL,
  `max_capacity` INT NOT NULL DEFAULT 4,
  `current_capacity` INT NOT NULL DEFAULT 0,
  `first_bed` VARCHAR(32) DEFAULT NULL,
  `second_bed` VARCHAR(32) DEFAULT NULL,
  `third_bed` VARCHAR(32) DEFAULT NULL,
  `fourth_bed` VARCHAR(32) DEFAULT NULL,
  PRIMARY KEY (`dormroom_id`),
  KEY `idx_dorm_room_building` (`dormbuild_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS `notice` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `title` VARCHAR(128) NOT NULL,
  `content` TEXT,
  `author` VARCHAR(64) DEFAULT NULL,
  `release_time` VARCHAR(19) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS `repair` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `repairer` VARCHAR(32) NOT NULL,
  `dormbuild_id` INT NOT NULL,
  `dormroom_id` INT NOT NULL,
  `title` VARCHAR(128) NOT NULL,
  `content` TEXT,
  `state` VARCHAR(16) NOT NULL DEFAULT '未完成',
  `order_buildtime` VARCHAR(19) DEFAULT NULL,
  `order_finishtime` VARCHAR(19) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_repair_repairer` (`repairer`),
  KEY `idx_repair_state` (`state`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS `adjust_room` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `username` VARCHAR(32) NOT NULL,
  `name` VARCHAR(64) DEFAULT NULL,
  `currentroom_id` INT NOT NULL,
  `currentbed_id` INT NOT NULL,
  `towardsroom_id` INT NOT NULL,
  `towardsbed_id` INT NOT NULL,
  `state` VARCHAR(16) NOT NULL DEFAULT '未处理',
  `apply_time` VARCHAR(19) DEFAULT NULL,
  `finish_time` VARCHAR(19) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_adjust_room_username` (`username`),
  KEY `idx_adjust_room_state` (`state`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS `visitor` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(64) NOT NULL,
  `gender` VARCHAR(16) DEFAULT NULL,
  `phone_num` VARCHAR(32) DEFAULT NULL,
  `origin_city` VARCHAR(64) DEFAULT NULL,
  `visit_time` VARCHAR(19) DEFAULT NULL,
  `content` TEXT,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

INSERT INTO `admin` (`username`, `password`, `name`, `gender`, `age`, `phone_num`, `email`, `avatar`)
VALUES ('admin', '123456', '系统管理员', '男', 30, '13800000000', 'admin@dormlink.local', '/files/avatar1.jpg')
ON DUPLICATE KEY UPDATE
  `password` = VALUES(`password`),
  `name` = VALUES(`name`),
  `gender` = VALUES(`gender`),
  `age` = VALUES(`age`),
  `phone_num` = VALUES(`phone_num`),
  `email` = VALUES(`email`),
  `avatar` = VALUES(`avatar`);

INSERT INTO `dorm_build` (`dormbuild_id`, `dormbuild_name`, `dormbuild_detail`)
VALUES
  (1, '竹园A栋', '男生宿舍，靠近东门'),
  (2, '梅园B栋', '女生宿舍，靠近图书馆')
ON DUPLICATE KEY UPDATE
  `dormbuild_name` = VALUES(`dormbuild_name`),
  `dormbuild_detail` = VALUES(`dormbuild_detail`);

INSERT INTO `dorm_manager` (`username`, `password`, `dormbuild_id`, `name`, `gender`, `age`, `phone_num`, `email`, `avatar`)
VALUES
  ('dm001', '123456', 1, '王阿姨', '女', 45, '13900010001', 'dm001@dormlink.local', '/files/avatar2.jpg'),
  ('dm002', '123456', 2, '李老师', '男', 41, '13900010002', 'dm002@dormlink.local', '/files/avatar3.jpg')
ON DUPLICATE KEY UPDATE
  `password` = VALUES(`password`),
  `dormbuild_id` = VALUES(`dormbuild_id`),
  `name` = VALUES(`name`),
  `gender` = VALUES(`gender`),
  `age` = VALUES(`age`),
  `phone_num` = VALUES(`phone_num`),
  `email` = VALUES(`email`),
  `avatar` = VALUES(`avatar`);

INSERT INTO `student` (`username`, `password`, `name`, `age`, `gender`, `phone_num`, `email`, `avatar`)
VALUES
  ('stu2025001', '123456', '赵一', 19, '男', '13700020001', 'stu1@dormlink.local', '/files/avatar1.jpg'),
  ('stu2025002', '123456', '钱二', 20, '男', '13700020002', 'stu2@dormlink.local', '/files/avatar2.jpg'),
  ('stu2025003', '123456', '孙三', 19, '女', '13700020003', 'stu3@dormlink.local', '/files/avatar3.jpg'),
  ('stu2025004', '123456', '李四', 21, '女', '13700020004', 'stu4@dormlink.local', '/files/avatar1.jpg')
ON DUPLICATE KEY UPDATE
  `password` = VALUES(`password`),
  `name` = VALUES(`name`),
  `age` = VALUES(`age`),
  `gender` = VALUES(`gender`),
  `phone_num` = VALUES(`phone_num`),
  `email` = VALUES(`email`),
  `avatar` = VALUES(`avatar`);

INSERT INTO `dorm_room` (`dormroom_id`, `dormbuild_id`, `floor_num`, `max_capacity`, `current_capacity`, `first_bed`, `second_bed`, `third_bed`, `fourth_bed`)
VALUES
  (101, 1, 1, 4, 2, 'stu2025001', 'stu2025002', NULL, NULL),
  (102, 1, 1, 4, 1, 'stu2025003', NULL, NULL, NULL),
  (201, 2, 2, 4, 0, NULL, NULL, NULL, NULL),
  (202, 2, 2, 4, 1, 'stu2025004', NULL, NULL, NULL)
ON DUPLICATE KEY UPDATE
  `dormbuild_id` = VALUES(`dormbuild_id`),
  `floor_num` = VALUES(`floor_num`),
  `max_capacity` = VALUES(`max_capacity`),
  `current_capacity` = VALUES(`current_capacity`),
  `first_bed` = VALUES(`first_bed`),
  `second_bed` = VALUES(`second_bed`),
  `third_bed` = VALUES(`third_bed`),
  `fourth_bed` = VALUES(`fourth_bed`);

INSERT INTO `notice` (`id`, `title`, `content`, `author`, `release_time`)
VALUES
  (1, '宿舍晚归管理提醒', '请同学们按时归寝，注意人身与财产安全。', 'admin', '2026-04-01 09:00:00'),
  (2, '本周报修集中处理安排', '本周三下午进行集中维修，请提前提交报修申请。', 'dm001', '2026-04-03 10:00:00')
ON DUPLICATE KEY UPDATE
  `title` = VALUES(`title`),
  `content` = VALUES(`content`),
  `author` = VALUES(`author`),
  `release_time` = VALUES(`release_time`);

INSERT INTO `repair` (`id`, `repairer`, `dormbuild_id`, `dormroom_id`, `title`, `content`, `state`, `order_buildtime`, `order_finishtime`)
VALUES
  (1, 'stu2025001', 1, 101, '空调异响', '空调夜间运行有明显异响', '未完成', '2026-04-05 20:10:00', NULL),
  (2, 'stu2025004', 2, 202, '门锁松动', '宿舍门锁松动，存在安全隐患', '完成', '2026-04-02 11:30:00', '2026-04-03 16:20:00')
ON DUPLICATE KEY UPDATE
  `repairer` = VALUES(`repairer`),
  `dormbuild_id` = VALUES(`dormbuild_id`),
  `dormroom_id` = VALUES(`dormroom_id`),
  `title` = VALUES(`title`),
  `content` = VALUES(`content`),
  `state` = VALUES(`state`),
  `order_buildtime` = VALUES(`order_buildtime`),
  `order_finishtime` = VALUES(`order_finishtime`);

INSERT INTO `adjust_room` (`id`, `username`, `name`, `currentroom_id`, `currentbed_id`, `towardsroom_id`, `towardsbed_id`, `state`, `apply_time`, `finish_time`)
VALUES
  (1, 'stu2025003', '孙三', 102, 1, 201, 1, '未处理', '2026-04-06 18:00:00', NULL),
  (2, 'stu2025002', '钱二', 101, 2, 202, 2, '通过', '2026-04-01 09:30:00', '2026-04-02 14:00:00')
ON DUPLICATE KEY UPDATE
  `username` = VALUES(`username`),
  `name` = VALUES(`name`),
  `currentroom_id` = VALUES(`currentroom_id`),
  `currentbed_id` = VALUES(`currentbed_id`),
  `towardsroom_id` = VALUES(`towardsroom_id`),
  `towardsbed_id` = VALUES(`towardsbed_id`),
  `state` = VALUES(`state`),
  `apply_time` = VALUES(`apply_time`),
  `finish_time` = VALUES(`finish_time`);

INSERT INTO `visitor` (`id`, `name`, `gender`, `phone_num`, `origin_city`, `visit_time`, `content`)
VALUES
  (1, '陈同学家长', '女', '13600030001', '杭州', '2026-04-04 15:30:00', '探望学生，已登记门禁信息'),
  (2, '王同学家长', '男', '13600030002', '苏州', '2026-04-07 10:20:00', '送生活用品')
ON DUPLICATE KEY UPDATE
  `name` = VALUES(`name`),
  `gender` = VALUES(`gender`),
  `phone_num` = VALUES(`phone_num`),
  `origin_city` = VALUES(`origin_city`),
  `visit_time` = VALUES(`visit_time`),
  `content` = VALUES(`content`);
