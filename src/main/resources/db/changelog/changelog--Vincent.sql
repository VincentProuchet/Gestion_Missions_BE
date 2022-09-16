/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

DROP TABLE IF EXISTS `city`;
CREATE TABLE IF NOT EXISTS `city` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

/*!40000 ALTER TABLE `city` DISABLE KEYS */;
/*!40000 ALTER TABLE `city` ENABLE KEYS */;

DROP TABLE IF EXISTS `collaborator`;
CREATE TABLE IF NOT EXISTS `collaborator` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `email` varchar(255) DEFAULT NULL,
  `first_name` varchar(255) DEFAULT NULL,
  `is_active` bit(1) DEFAULT NULL,
  `last_name` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `role` int(11) DEFAULT NULL,
  `user_name` varchar(255) DEFAULT NULL,
  `auth_id` int(11) DEFAULT NULL,
  `managerid` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKcqcan6wcufcvke6m94gd1n2dv` (`auth_id`),
  KEY `FKmjwg1a264ptx18aw87gv7ff7b` (`managerid`),
  CONSTRAINT `FKcqcan6wcufcvke6m94gd1n2dv` FOREIGN KEY (`auth_id`) REFERENCES `security_token` (`id`),
  CONSTRAINT `FKmjwg1a264ptx18aw87gv7ff7b` FOREIGN KEY (`managerid`) REFERENCES `collaborator` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

/*!40000 ALTER TABLE `collaborator` DISABLE KEYS */;
/*!40000 ALTER TABLE `collaborator` ENABLE KEYS */;

DROP TABLE IF EXISTS `databasechangelog`;
CREATE TABLE IF NOT EXISTS `databasechangelog` (
  `ID` varchar(255) NOT NULL,
  `AUTHOR` varchar(255) NOT NULL,
  `FILENAME` varchar(255) NOT NULL,
  `DATEEXECUTED` datetime NOT NULL,
  `ORDEREXECUTED` int(11) NOT NULL,
  `EXECTYPE` varchar(10) NOT NULL,
  `MD5SUM` varchar(35) DEFAULT NULL,
  `DESCRIPTION` varchar(255) DEFAULT NULL,
  `COMMENTS` varchar(255) DEFAULT NULL,
  `TAG` varchar(255) DEFAULT NULL,
  `LIQUIBASE` varchar(20) DEFAULT NULL,
  `CONTEXTS` varchar(255) DEFAULT NULL,
  `LABELS` varchar(255) DEFAULT NULL,
  `DEPLOYMENT_ID` varchar(10) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

/*!40000 ALTER TABLE `databasechangelog` DISABLE KEYS */;
INSERT INTO `databasechangelog` (`ID`, `AUTHOR`, `FILENAME`, `DATEEXECUTED`, `ORDEREXECUTED`, `EXECTYPE`, `MD5SUM`, `DESCRIPTION`, `COMMENTS`, `TAG`, `LIQUIBASE`, `CONTEXTS`, `LABELS`, `DEPLOYMENT_ID`) VALUES
	('1', 'liquibase', 'classpath:db/changelog/changelog.sql', '2022-09-16 14:47:34', 1, 'EXECUTED', '8:c9afb0b894d6b5d112adb5a6665f3a84', 'sql', '', NULL, '4.9.1', NULL, NULL, '3332445991'),
	('2', 'liquibase', 'classpath:db/changelog/changelog.sql', '2022-09-16 14:47:34', 2, 'EXECUTED', '8:393d35ce83b38b00bb4f96b2efee2193', 'sql', '', NULL, '4.9.1', NULL, NULL, '3332445991'),
	('3', 'liquibase', 'classpath:db/changelog/changelog.sql', '2022-09-16 14:47:34', 3, 'EXECUTED', '8:10461c4a753b94a51733cab5040149ca', 'sql', '', NULL, '4.9.1', NULL, NULL, '3332445991'),
	('4', 'ligquibase', 'classpath:db/changelog/changelog.sql', '2022-09-16 14:47:34', 4, 'EXECUTED', '8:6aff354c61f6bcc72b7c6e657bb9ff4c', 'sql', '', NULL, '4.9.1', NULL, NULL, '3332445991');
/*!40000 ALTER TABLE `databasechangelog` ENABLE KEYS */;

DROP TABLE IF EXISTS `databasechangeloglock`;
CREATE TABLE IF NOT EXISTS `databasechangeloglock` (
  `ID` int(11) NOT NULL,
  `LOCKED` bit(1) NOT NULL,
  `LOCKGRANTED` datetime DEFAULT NULL,
  `LOCKEDBY` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

/*!40000 ALTER TABLE `databasechangeloglock` DISABLE KEYS */;
INSERT INTO `databasechangeloglock` (`ID`, `LOCKED`, `LOCKGRANTED`, `LOCKEDBY`) VALUES
	(1, b'0', NULL, NULL);
/*!40000 ALTER TABLE `databasechangeloglock` ENABLE KEYS */;

DROP TABLE IF EXISTS `expense`;
CREATE TABLE IF NOT EXISTS `expense` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `cost` decimal(19,2) DEFAULT NULL,
  `date` datetime(6) DEFAULT NULL,
  `tva` float DEFAULT NULL,
  `expense_typeid` int(11) DEFAULT NULL,
  `missionid` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKjg5t24w9l8u610kvu82t3m8h8` (`expense_typeid`),
  KEY `FKc80p0i6t28c52a4imiqq43iam` (`missionid`),
  CONSTRAINT `FKc80p0i6t28c52a4imiqq43iam` FOREIGN KEY (`missionid`) REFERENCES `mission` (`id`),
  CONSTRAINT `FKjg5t24w9l8u610kvu82t3m8h8` FOREIGN KEY (`expense_typeid`) REFERENCES `expense_type` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

/*!40000 ALTER TABLE `expense` DISABLE KEYS */;
/*!40000 ALTER TABLE `expense` ENABLE KEYS */;

DROP TABLE IF EXISTS `expense_type`;
CREATE TABLE IF NOT EXISTS `expense_type` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

/*!40000 ALTER TABLE `expense_type` DISABLE KEYS */;
/*!40000 ALTER TABLE `expense_type` ENABLE KEYS */;

DROP TABLE IF EXISTS `mission`;
CREATE TABLE IF NOT EXISTS `mission` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `bonus` decimal(19,2) DEFAULT NULL,
  `end_date` datetime(6) DEFAULT NULL,
  `mission_transport` varchar(255) DEFAULT NULL,
  `start_date` datetime(6) DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL,
  `collaboratorid` int(11) DEFAULT NULL,
  `end_cityid` int(11) DEFAULT NULL,
  `natureid` int(11) DEFAULT NULL,
  `start_cityid` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKekq6be030jnfpvontdcsikntq` (`collaboratorid`),
  KEY `FKkphcdkjbvempgsuqcogqkig08` (`end_cityid`),
  KEY `FKp1fl8aea4a7vp0pr45epiyqgr` (`natureid`),
  KEY `FKbico4il1r20n6dr3pf35xu3bh` (`start_cityid`),
  CONSTRAINT `FKbico4il1r20n6dr3pf35xu3bh` FOREIGN KEY (`start_cityid`) REFERENCES `city` (`id`),
  CONSTRAINT `FKekq6be030jnfpvontdcsikntq` FOREIGN KEY (`collaboratorid`) REFERENCES `collaborator` (`id`),
  CONSTRAINT `FKkphcdkjbvempgsuqcogqkig08` FOREIGN KEY (`end_cityid`) REFERENCES `city` (`id`),
  CONSTRAINT `FKp1fl8aea4a7vp0pr45epiyqgr` FOREIGN KEY (`natureid`) REFERENCES `nature` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

/*!40000 ALTER TABLE `mission` DISABLE KEYS */;
/*!40000 ALTER TABLE `mission` ENABLE KEYS */;

DROP TABLE IF EXISTS `nature`;
CREATE TABLE IF NOT EXISTS `nature` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `bonus_percentage` float DEFAULT NULL,
  `charged` bit(1) NOT NULL,
  `date_of_validity` datetime(6) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `end_of_validity` datetime(6) DEFAULT NULL,
  `gives_bonus` bit(1) NOT NULL,
  `tjm` decimal(19,2) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

/*!40000 ALTER TABLE `nature` DISABLE KEYS */;
/*!40000 ALTER TABLE `nature` ENABLE KEYS */;

DROP TABLE IF EXISTS `security_token`;
CREATE TABLE IF NOT EXISTS `security_token` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `auth_token` varchar(255) DEFAULT NULL,
  `grant_token` varchar(255) DEFAULT NULL,
  `issued` datetime(6) DEFAULT NULL,
  `refresh_token` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

/*!40000 ALTER TABLE `security_token` DISABLE KEYS */;
/*!40000 ALTER TABLE `security_token` ENABLE KEYS */;

/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
