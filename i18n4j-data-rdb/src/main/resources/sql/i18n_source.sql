SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for i18n_source
-- ----------------------------
CREATE TABLE IF NOT EXISTS `i18n_source` (
  `tag` varchar(50) COLLATE utf8mb4_bin NOT NULL,
  `locale` varchar(50) COLLATE utf8mb4_bin NOT NULL,
  `props` text COLLATE utf8mb4_bin NOT NULL,
  `update_time` bigint NOT NULL,
  PRIMARY KEY (`tag`,`locale`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

SET FOREIGN_KEY_CHECKS = 1;
