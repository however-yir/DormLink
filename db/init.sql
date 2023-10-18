-- ============================================
-- DormLink 数据库初始化脚本
-- 目标：一键初始化可演示环境（表结构 + 索引 + 外键 + 种子数据）
-- 用法：mysql -u root -p < db/init.sql
-- ============================================

CREATE DATABASE IF NOT EXISTS `wms`
  DEFAULT CHARACTER SET utf8mb4
  DEFAULT COLLATE utf8mb4_unicode_ci;

USE `wms`;

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

DROP TABLE IF EXISTS `visitor`;
DROP TABLE IF EXISTS `adjust_room`;
DROP TABLE IF EXISTS `repair`;
DROP TABLE IF EXISTS `notice`;
DROP TABLE IF EXISTS `dorm_room`;
DROP TABLE IF EXISTS `dorm_manager`;
DROP TABLE IF EXISTS `student`;
DROP TABLE IF EXISTS `admin`;
DROP TABLE IF EXISTS `dorm_build`;

-- ----------------------------
-- 楼栋
-- ----------------------------
CREATE TABLE `dorm_build` (
  `id` INT NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `dormbuild_id` INT NOT NULL COMMENT '业务楼栋编号',
  `dormbuild_name` VARCHAR(64) NOT NULL COMMENT '楼栋名称',
  `dormbuild_detail` VARCHAR(255) DEFAULT NULL COMMENT '楼栋描述',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_dorm_build_code` (`dormbuild_id`),
  KEY `idx_dorm_build_name` (`dormbuild_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='宿舍楼栋表';

-- ----------------------------
-- 管理员
-- ----------------------------
CREATE TABLE `admin` (
  `username` VARCHAR(32) NOT NULL COMMENT '管理员账号',
  `password` VARCHAR(128) NOT NULL COMMENT '登录密码',
  `name` VARCHAR(64) NOT NULL COMMENT '姓名',
  `gender` VARCHAR(16) DEFAULT NULL COMMENT '性别',
  `age` INT DEFAULT 0 COMMENT '年龄',
  `phone_num` VARCHAR(32) DEFAULT NULL COMMENT '手机号',
  `email` VARCHAR(128) DEFAULT NULL COMMENT '邮箱',
  `avatar` VARCHAR(255) DEFAULT NULL COMMENT '头像地址',
  PRIMARY KEY (`username`),
  KEY `idx_admin_name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系统管理员表';

-- ----------------------------
-- 学生
-- ----------------------------
CREATE TABLE `student` (
  `username` VARCHAR(32) NOT NULL COMMENT '学生账号',
  `password` VARCHAR(128) NOT NULL COMMENT '登录密码',
  `name` VARCHAR(64) NOT NULL COMMENT '姓名',
  `age` INT DEFAULT 0 COMMENT '年龄',
  `gender` VARCHAR(16) DEFAULT NULL COMMENT '性别',
  `phone_num` VARCHAR(32) DEFAULT NULL COMMENT '手机号',
  `email` VARCHAR(128) DEFAULT NULL COMMENT '邮箱',
  `avatar` VARCHAR(255) DEFAULT NULL COMMENT '头像地址',
  PRIMARY KEY (`username`),
  KEY `idx_student_name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='学生表';

-- ----------------------------
-- 宿管
-- ----------------------------
CREATE TABLE `dorm_manager` (
  `username` VARCHAR(32) NOT NULL COMMENT '宿管账号',
  `password` VARCHAR(128) NOT NULL COMMENT '登录密码',
  `dormbuild_id` INT NOT NULL COMMENT '负责楼栋编号',
  `name` VARCHAR(64) NOT NULL COMMENT '姓名',
  `gender` VARCHAR(16) DEFAULT NULL COMMENT '性别',
  `age` INT DEFAULT 0 COMMENT '年龄',
  `phone_num` VARCHAR(32) DEFAULT NULL COMMENT '手机号',
  `email` VARCHAR(128) DEFAULT NULL COMMENT '邮箱',
  `avatar` VARCHAR(255) DEFAULT NULL COMMENT '头像地址',
  PRIMARY KEY (`username`),
  KEY `idx_dorm_manager_building` (`dormbuild_id`),
  CONSTRAINT `fk_dorm_manager_building`
    FOREIGN KEY (`dormbuild_id`) REFERENCES `dorm_build` (`dormbuild_id`)
    ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='宿舍管理员表';

-- ----------------------------
-- 房间
-- ----------------------------
CREATE TABLE `dorm_room` (
  `dormroom_id` INT NOT NULL COMMENT '房间编号',
  `dormbuild_id` INT NOT NULL COMMENT '所属楼栋编号',
  `floor_num` INT NOT NULL COMMENT '楼层',
  `max_capacity` INT NOT NULL DEFAULT 4 COMMENT '最大容量',
  `current_capacity` INT NOT NULL DEFAULT 0 COMMENT '当前入住人数',
  `first_bed` VARCHAR(32) DEFAULT NULL COMMENT '1号床学生账号',
  `second_bed` VARCHAR(32) DEFAULT NULL COMMENT '2号床学生账号',
  `third_bed` VARCHAR(32) DEFAULT NULL COMMENT '3号床学生账号',
  `fourth_bed` VARCHAR(32) DEFAULT NULL COMMENT '4号床学生账号',
  PRIMARY KEY (`dormroom_id`),
  KEY `idx_dorm_room_building` (`dormbuild_id`),
  KEY `idx_dorm_room_floor` (`floor_num`),
  CONSTRAINT `fk_dorm_room_building`
    FOREIGN KEY (`dormbuild_id`) REFERENCES `dorm_build` (`dormbuild_id`)
    ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='宿舍房间表';

-- ----------------------------
-- 公告
-- ----------------------------
CREATE TABLE `notice` (
  `id` INT NOT NULL AUTO_INCREMENT COMMENT '公告ID',
  `title` VARCHAR(128) NOT NULL COMMENT '公告标题',
  `content` TEXT COMMENT '公告内容',
  `author` VARCHAR(64) DEFAULT NULL COMMENT '发布人账号',
  `release_time` VARCHAR(19) DEFAULT NULL COMMENT '发布时间',
  PRIMARY KEY (`id`),
  KEY `idx_notice_release_time` (`release_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='公告表';

-- ----------------------------
-- 报修
-- ----------------------------
CREATE TABLE `repair` (
  `id` INT NOT NULL AUTO_INCREMENT COMMENT '报修单ID',
  `repairer` VARCHAR(32) NOT NULL COMMENT '报修人账号',
  `dormbuild_id` INT NOT NULL COMMENT '楼栋编号',
  `dormroom_id` INT NOT NULL COMMENT '房间编号',
  `title` VARCHAR(128) NOT NULL COMMENT '报修标题',
  `content` TEXT COMMENT '报修内容',
  `state` VARCHAR(16) NOT NULL DEFAULT '未完成' COMMENT '状态',
  `order_buildtime` VARCHAR(19) DEFAULT NULL COMMENT '创建时间',
  `order_finishtime` VARCHAR(19) DEFAULT NULL COMMENT '完成时间',
  PRIMARY KEY (`id`),
  KEY `idx_repair_repairer` (`repairer`),
  KEY `idx_repair_state` (`state`),
  KEY `idx_repair_room` (`dormroom_id`),
  CONSTRAINT `fk_repair_student`
    FOREIGN KEY (`repairer`) REFERENCES `student` (`username`)
    ON UPDATE CASCADE,
  CONSTRAINT `fk_repair_building`
    FOREIGN KEY (`dormbuild_id`) REFERENCES `dorm_build` (`dormbuild_id`)
    ON UPDATE CASCADE,
  CONSTRAINT `fk_repair_room`
    FOREIGN KEY (`dormroom_id`) REFERENCES `dorm_room` (`dormroom_id`)
    ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='报修工单表';

-- ----------------------------
-- 调宿申请
-- ----------------------------
CREATE TABLE `adjust_room` (
  `id` INT NOT NULL AUTO_INCREMENT COMMENT '申请ID',
  `username` VARCHAR(32) NOT NULL COMMENT '申请人账号',
  `name` VARCHAR(64) DEFAULT NULL COMMENT '申请人姓名',
  `currentroom_id` INT NOT NULL COMMENT '当前房间',
  `currentbed_id` INT NOT NULL COMMENT '当前床位',
  `towardsroom_id` INT NOT NULL COMMENT '目标房间',
  `towardsbed_id` INT NOT NULL COMMENT '目标床位',
  `state` VARCHAR(16) NOT NULL DEFAULT '未处理' COMMENT '申请状态',
  `apply_time` VARCHAR(19) DEFAULT NULL COMMENT '申请时间',
  `finish_time` VARCHAR(19) DEFAULT NULL COMMENT '处理完成时间',
  PRIMARY KEY (`id`),
  KEY `idx_adjust_room_username` (`username`),
  KEY `idx_adjust_room_state` (`state`),
  KEY `idx_adjust_room_current` (`currentroom_id`),
  KEY `idx_adjust_room_target` (`towardsroom_id`),
  CONSTRAINT `fk_adjust_room_student`
    FOREIGN KEY (`username`) REFERENCES `student` (`username`)
    ON UPDATE CASCADE,
  CONSTRAINT `fk_adjust_room_current_room`
    FOREIGN KEY (`currentroom_id`) REFERENCES `dorm_room` (`dormroom_id`)
    ON UPDATE CASCADE,
  CONSTRAINT `fk_adjust_room_towards_room`
    FOREIGN KEY (`towardsroom_id`) REFERENCES `dorm_room` (`dormroom_id`)
    ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='调宿申请表';

-- ----------------------------
-- 访客
-- ----------------------------
CREATE TABLE `visitor` (
  `id` INT NOT NULL AUTO_INCREMENT COMMENT '登记ID',
  `name` VARCHAR(64) NOT NULL COMMENT '访客姓名',
  `gender` VARCHAR(16) DEFAULT NULL COMMENT '访客性别',
  `phone_num` VARCHAR(32) DEFAULT NULL COMMENT '访客手机号',
  `origin_city` VARCHAR(64) DEFAULT NULL COMMENT '来访城市',
  `visit_time` VARCHAR(19) DEFAULT NULL COMMENT '到访时间',
  `content` TEXT COMMENT '来访事由',
  PRIMARY KEY (`id`),
  KEY `idx_visitor_visit_time` (`visit_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='访客登记表';

SET FOREIGN_KEY_CHECKS = 1;

-- ============================================
-- 种子数据
-- ============================================

INSERT INTO `dorm_build` (`dormbuild_id`, `dormbuild_name`, `dormbuild_detail`) VALUES
(1, '竹园A栋', '男生宿舍，靠近东门'),
(2, '梅园B栋', '女生宿舍，靠近图书馆'),
(3, '兰园C栋', '研究生与留学生混住楼');

INSERT INTO `admin` (`username`, `password`, `name`, `gender`, `age`, `phone_num`, `email`, `avatar`) VALUES
('admin', '123456', '系统管理员', '男', 30, '13800000000', 'admin@dormlink.local', '/files/avatar1.jpg');

INSERT INTO `dorm_manager` (`username`, `password`, `dormbuild_id`, `name`, `gender`, `age`, `phone_num`, `email`, `avatar`) VALUES
('dm001', '123456', 1, '王阿姨', '女', 45, '13900010001', 'dm001@dormlink.local', '/files/avatar2.jpg'),
('dm002', '123456', 2, '李老师', '男', 41, '13900010002', 'dm002@dormlink.local', '/files/avatar3.jpg'),
('dm003', '123456', 3, '赵老师', '男', 39, '13900010003', 'dm003@dormlink.local', '/files/avatar1.jpg');

INSERT INTO `student` (`username`, `password`, `name`, `age`, `gender`, `phone_num`, `email`, `avatar`) VALUES
('stu2025001', '123456', '赵一', 19, '男', '13700020001', 'stu1@dormlink.local', '/files/avatar1.jpg'),
('stu2025002', '123456', '钱二', 20, '男', '13700020002', 'stu2@dormlink.local', '/files/avatar2.jpg'),
('stu2025003', '123456', '孙三', 19, '女', '13700020003', 'stu3@dormlink.local', '/files/avatar3.jpg'),
('stu2025004', '123456', '李四', 21, '女', '13700020004', 'stu4@dormlink.local', '/files/avatar1.jpg'),
('stu2025005', '123456', '周五', 20, '男', '13700020005', 'stu5@dormlink.local', '/files/avatar2.jpg');

INSERT INTO `dorm_room` (`dormroom_id`, `dormbuild_id`, `floor_num`, `max_capacity`, `current_capacity`, `first_bed`, `second_bed`, `third_bed`, `fourth_bed`) VALUES
(101, 1, 1, 4, 2, 'stu2025001', 'stu2025002', NULL, NULL),
(102, 1, 1, 4, 1, 'stu2025005', NULL, NULL, NULL),
(201, 2, 2, 4, 1, 'stu2025003', NULL, NULL, NULL),
(202, 2, 2, 4, 1, 'stu2025004', NULL, NULL, NULL),
(301, 3, 3, 4, 0, NULL, NULL, NULL, NULL);

INSERT INTO `notice` (`id`, `title`, `content`, `author`, `release_time`) VALUES
(1, '宿舍晚归管理提醒', '请同学们按时归寝，注意人身与财产安全。', 'admin', '2023-11-15 09:00:00'),
(2, '本周报修集中处理安排', '本周三下午进行集中维修，请提前提交报修申请。', 'dm001', '2023-11-16 10:00:00'),
(3, '冬季防火安全提示', '请勿在宿舍使用大功率违禁电器。', 'dm002', '2023-11-17 18:30:00');

INSERT INTO `repair` (`id`, `repairer`, `dormbuild_id`, `dormroom_id`, `title`, `content`, `state`, `order_buildtime`, `order_finishtime`) VALUES
(1, 'stu2025001', 1, 101, '空调异响', '空调夜间运行有明显异响', '未完成', '2023-11-18 20:10:00', NULL),
(2, 'stu2025004', 2, 202, '门锁松动', '宿舍门锁松动，存在安全隐患', '完成', '2023-11-16 11:30:00', '2023-11-17 16:20:00'),
(3, 'stu2025003', 2, 201, '卫生间漏水', '洗手池下方持续渗水', '完成', '2023-11-15 14:00:00', '2023-11-15 19:15:00');

INSERT INTO `adjust_room` (`id`, `username`, `name`, `currentroom_id`, `currentbed_id`, `towardsroom_id`, `towardsbed_id`, `state`, `apply_time`, `finish_time`) VALUES
(1, 'stu2025003', '孙三', 201, 1, 301, 1, '未处理', '2023-11-18 18:00:00', NULL),
(2, 'stu2025002', '钱二', 101, 2, 202, 2, '通过', '2023-11-14 09:30:00', '2023-11-15 14:00:00');

INSERT INTO `visitor` (`id`, `name`, `gender`, `phone_num`, `origin_city`, `visit_time`, `content`) VALUES
(1, '陈同学家长', '女', '13600030001', '杭州', '2023-11-17 15:30:00', '探望学生，已登记门禁信息'),
(2, '王同学家长', '男', '13600030002', '苏州', '2023-11-18 10:20:00', '送生活用品');
