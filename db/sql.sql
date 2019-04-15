-- 多个库
CREATE DATABASE `andy` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;;

CREATE USER 'release_andy@%' IDENTIFIED BY 'andyPs1!';
GRANT ALL PRIVILEGES ON andy.* TO 'release_andy'@'%' IDENTIFIED BY 'andyPs1!' WITH GRANT OPTION;
flush privileges;

CREATE DATABASE `cindy` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;

CREATE USER 'release_cindy@%' IDENTIFIED BY 'cindyY1!';
GRANT ALL PRIVILEGES ON cindy.* TO 'release_cindy'@'%' IDENTIFIED BY 'cindyY1!' WITH GRANT OPTION;
flush privileges;

CREATE DATABASE `john` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;

CREATE USER 'release_john@%' IDENTIFIED BY 'johnPs1!';
GRANT ALL PRIVILEGES ON john.* TO 'release_john'@'%' IDENTIFIED BY 'johnPs1!' WITH GRANT OPTION;
flush privileges;

CREATE DATABASE `jack` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;

CREATE USER 'release_jack@%' IDENTIFIED BY 'jackPs1!';
GRANT ALL PRIVILEGES ON jack.* TO 'release_jack'@'%' IDENTIFIED BY 'jackPs1!' WITH GRANT OPTION;
flush privileges;

CREATE DATABASE `chris` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;

CREATE USER 'release_chris@%' IDENTIFIED BY 'chrisP1!';
GRANT ALL PRIVILEGES ON chris.* TO 'release_chris'@'%' IDENTIFIED BY 'chrisP1!' WITH GRANT OPTION;
flush privileges;

CREATE DATABASE `barry` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;

CREATE USER 'release_barry@%' IDENTIFIED BY 'barryP1!';
GRANT ALL PRIVILEGES ON barry.* TO 'release_barry'@'%' IDENTIFIED BY 'barryP1!' WITH GRANT OPTION;
flush privileges;

CREATE DATABASE `celia` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;

CREATE USER 'release_celia@%' IDENTIFIED BY 'celiaP1!';
GRANT ALL PRIVILEGES ON celia.* TO 'release_celia'@'%' IDENTIFIED BY 'celiaP1!' WITH GRANT OPTION;
flush privileges;

CREATE DATABASE `jones` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;

CREATE USER 'release_jones@%' IDENTIFIED BY 'jonesP1!';
GRANT ALL PRIVILEGES ON jones.* TO 'release_jones'@'%' IDENTIFIED BY 'jonesP1!' WITH GRANT OPTION;
flush privileges;

-- andy,cindy,john,jack,chris,barry,celia,jones

-- 库里面的表
DROP TABLE IF EXISTS `multi_test`;
CREATE TABLE `multi_test` (
  `id` BIGINT (20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `trigger` INT (11) DEFAULT NULL COMMENT '目标',
  `age` INT (11) DEFAULT NULL COMMENT '年龄',
  `brand` VARCHAR (20) DEFAULT NULL COMMENT '品牌',
  `username` VARCHAR (32) DEFAULT NULL COMMENT '姓名',
  `password` CHAR (16) DEFAULT NULL COMMENT '密码',
  `last_name` VARCHAR (32) DEFAULT NULL COMMENT '姓',
  `content` text NOT NULL COMMENT '内容',
  `end_name` VARCHAR (32) DEFAULT NULL COMMENT '名',
  `status` TINYINT (1) UNSIGNED NOT NULL DEFAULT '0' COMMENT '状态 0-未审核 1-审核通过 2-已出租 3-逻辑删除',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `last_update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最近数据更新时间',
  `amt` DECIMAL (9, 2) NOT NULL DEFAULT '0.00' COMMENT '金额',
  `price` DECIMAL (9, 2) NOT NULL DEFAULT '0.00' COMMENT '价格',
  PRIMARY KEY (`id`)
) ENGINE = INNODB DEFAULT CHARSET = utf8mb4 COMMENT = '多数据源测试表';


INSERT INTO `multi_test` VALUES ('1', '3', '52', 'andy','aa', '22222222', 'hhh', 'fffffffffffff', 'bbbb', '1', '2018-04-09 18:31:46', '2018-04-16 17:58:07', '1.00', '1.00');
INSERT INTO `multi_test` VALUES ('2', '1', '100','cindy', 'wanghaibo', '1111111111111111', 'wang', 'fffffffffffff', 'haibo', '1', '2018-04-09 18:33:22', '2018-04-16 17:47:39', '200.23', '1.00');
INSERT INTO `multi_test` VALUES ('3', '2', '20','john', 'dd', '2222222222222222', 'w', 'fffffffffffff', 'hb', '1', '2018-04-12 14:19:11', '2018-04-16 17:47:39', '23.10', '1.00');
INSERT INTO `multi_test` VALUES ('4', '1', '22','jack', 'ddd', 'fffff', 'dsdd', 'sgsre', 'sagrewg', '1', '2018-04-17 14:43:42', '2018-04-17 14:43:41', '22.00', '1.00');
INSERT INTO `multi_test` VALUES ('6', '1', '22','chris', 'ddd', 'fffff', 'dsdd', 'sgsre', 'sagrewg', '1', '2018-04-17 14:51:13', '2018-04-17 14:51:13', '30.00', '1.00');
INSERT INTO `multi_test` VALUES ('7', '1', '22','barry', 'ddd', 'fffff', 'dsdd', 'sgsre', 'sagrewg', '1', '2018-04-17 14:51:13', '2018-04-17 14:51:13', '30.00', '1.00');
INSERT INTO `multi_test` VALUES ('8', '1', '22','celia', 'ddd', 'fffff', 'dsdd', 'sgsre', 'sagrewg', '1', '2018-04-17 14:51:13', '2018-04-17 14:51:13', '30.00', '1.00');
INSERT INTO `multi_test` VALUES ('8', '1', '22','jones', 'ddd', 'fffff', 'dsdd', 'sgsre', 'sagrewg', '1', '2018-04-17 14:51:13', '2018-04-17 14:51:13', '30.00', '1.00');