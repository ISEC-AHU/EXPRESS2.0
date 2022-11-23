/*
MySQL Data Transfer
Source Host: localhost
Source Database: delivery
Target Host: localhost
Target Database: delivery
Date: 2022/11/16 21:49:53
*/

SET FOREIGN_KEY_CHECKS=0;
-- ----------------------------
-- Table structure for car
-- ----------------------------
CREATE TABLE `car` (
  `id` int(11) NOT NULL,
  `no_load_power` int(11) NOT NULL,
  `max_power` int(11) NOT NULL,
  `speed` int(11) NOT NULL,
  `max_load` int(11) NOT NULL,
  `battery` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- ----------------------------
-- Table structure for car_station
-- ----------------------------
CREATE TABLE `car_station` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `longitude` decimal(10,6) NOT NULL,
  `latitude` decimal(10,6) NOT NULL,
  `big_car_status` tinyint(1) NOT NULL DEFAULT '1',
  `big_car_id` int(11) NOT NULL DEFAULT '1',
  `middle_car_status` tinyint(1) NOT NULL DEFAULT '1',
  `middle_car_id` int(11) NOT NULL DEFAULT '2',
  `small_car_status` tinyint(1) NOT NULL DEFAULT '1',
  `small_car_id` int(11) NOT NULL DEFAULT '3',
  PRIMARY KEY (`id`),
  KEY `big_car_id` (`big_car_id`),
  KEY `middle_car_id` (`middle_car_id`),
  KEY `small_car_id` (`small_car_id`),
  KEY `name` (`name`),
  CONSTRAINT `big_car_id` FOREIGN KEY (`big_car_id`) REFERENCES `car` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `middle_car_id` FOREIGN KEY (`middle_car_id`) REFERENCES `car` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `small_car_id` FOREIGN KEY (`small_car_id`) REFERENCES `car` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for car_to_customer
-- ----------------------------
CREATE TABLE `car_to_customer` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `start` int(11) NOT NULL,
  `end` int(11) NOT NULL,
  `distance` int(11) DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for customer
-- ----------------------------
CREATE TABLE `customer` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `longitude` decimal(10,6) NOT NULL,
  `latitude` decimal(10,6) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for drone
-- ----------------------------
CREATE TABLE `drone` (
  `id` int(11) NOT NULL,
  `speed` int(11) NOT NULL,
  `no_load_power` int(11) NOT NULL,
  `max_power` int(11) NOT NULL,
  `max_load` int(11) NOT NULL,
  `battery` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- ----------------------------
-- Table structure for drone_station
-- ----------------------------
CREATE TABLE `drone_station` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `longitude` decimal(10,6) NOT NULL,
  `latitude` decimal(10,6) NOT NULL,
  `wid` int(11) DEFAULT NULL,
  `big_drone_status` tinyint(1) DEFAULT '1',
  `big_drone_id` int(11) DEFAULT '1',
  `middle_drone_status` tinyint(1) DEFAULT '1',
  `middle_drone_id` int(11) DEFAULT '2',
  `small_drone_status` tinyint(1) DEFAULT '1',
  `small_drone_id` int(11) DEFAULT '3',
  PRIMARY KEY (`id`),
  KEY `wid_warehouse_id` (`wid`),
  KEY `name` (`name`),
  CONSTRAINT `wid_warehouse_id` FOREIGN KEY (`wid`) REFERENCES `warehouse` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for order_record
-- ----------------------------
CREATE TABLE `order_record` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `model` int(11) DEFAULT '1',
  `start_station` varchar(11) COLLATE utf8_unicode_ci NOT NULL,
  `des_longitude` decimal(10,6) NOT NULL,
  `des_latitude` decimal(10,6) NOT NULL,
  `privacy_longitude` decimal(10,6) NOT NULL,
  `privacy_latitude` decimal(10,6) NOT NULL,
  `order_id` int(11) NOT NULL,
  `consignee` varchar(255) CHARACTER SET utf8 NOT NULL,
  `length` int(11) NOT NULL,
  `width` int(11) NOT NULL,
  `weight` double(7,5) NOT NULL,
  `goods` varchar(255) CHARACTER SET utf8 NOT NULL,
  `deadline` int(11) NOT NULL,
  `status` int(11) DEFAULT '0',
  `info` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `privacy_status` tinyint(4) DEFAULT '0',
  `route` varchar(255) COLLATE utf8_unicode_ci DEFAULT '',
  `time` int(11) DEFAULT NULL,
  `energy` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=38 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- ----------------------------
-- Table structure for path
-- ----------------------------
CREATE TABLE `path` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `order_id` int(11) NOT NULL,
  `did` int(11) DEFAULT '0',
  `cid` int(11) DEFAULT '0',
  `station_number` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=157 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for t_map
-- ----------------------------
CREATE TABLE `t_map` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `start` int(11) NOT NULL,
  `end_did` int(11) NOT NULL DEFAULT '0',
  `end_cid` int(11) NOT NULL DEFAULT '0',
  `distance` int(11) DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `start_car_station` (`end_did`)
) ENGINE=InnoDB AUTO_INCREMENT=53 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for warehouse
-- ----------------------------
CREATE TABLE `warehouse` (
  `id` int(11) NOT NULL,
  `name` varchar(255) NOT NULL,
  `longitude` decimal(10,6) NOT NULL,
  `latitude` decimal(10,6) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for workflow_file
-- ----------------------------
CREATE TABLE `workflow_file` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `file` mediumblob,
  `status` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records 
-- ----------------------------
INSERT INTO `car` VALUES ('1', '30', '80', '3', '5000', '1500');
INSERT INTO `car` VALUES ('2', '50', '100', '4', '12000', '2100');
INSERT INTO `car` VALUES ('3', '80', '130', '5', '20000', '2500');
INSERT INTO `car_station` VALUES ('1', 'C1', '117.192553', '31.759471', '1', '1', '1', '2', '1', '3');
INSERT INTO `car_station` VALUES ('2', 'C2', '117.244588', '31.753487', '1', '1', '1', '2', '1', '3');
INSERT INTO `car_station` VALUES ('3', 'C3', '117.240478', '31.724458', '1', '1', '1', '2', '1', '3');
INSERT INTO `car_station` VALUES ('4', 'C4', '117.220845', '31.768617', '1', '1', '1', '2', '1', '3');
INSERT INTO `car_station` VALUES ('5', 'C5', '117.231344', '31.759778', '1', '1', '1', '2', '1', '3');
INSERT INTO `car_station` VALUES ('6', 'C6', '117.231085', '31.713368', '1', '1', '1', '2', '1', '3');
INSERT INTO `car_station` VALUES ('7', 'C7', '117.187768', '31.732940', '1', '1', '1', '2', '1', '3');
INSERT INTO `car_station` VALUES ('8', 'C8', '117.169658', '31.742174', '1', '1', '1', '2', '1', '3');
INSERT INTO `car_station` VALUES ('9', 'C9', '117.231054', '31.724756', '1', '1', '1', '2', '1', '3');
INSERT INTO `car_station` VALUES ('10', 'C10', '117.246237', '31.740925', '1', '1', '1', '2', '1', '3');
INSERT INTO `car_station` VALUES ('11', 'C11', '117.220604', '31.730322', '1', '1', '1', '2', '1', '3');
INSERT INTO `car_to_customer` VALUES ('1', '3', '1', '1279');
INSERT INTO `car_to_customer` VALUES ('2', '6', '1', '889');
INSERT INTO `car_to_customer` VALUES ('3', '4', '2', '1011');
INSERT INTO `car_to_customer` VALUES ('4', '5', '2', '1233');
INSERT INTO `car_to_customer` VALUES ('7', '5', '4', '900');
INSERT INTO `car_to_customer` VALUES ('8', '2', '4', '786');
INSERT INTO `car_to_customer` VALUES ('9', '9', '1', '1560');
INSERT INTO `car_to_customer` VALUES ('10', '10', '1', '3128');
INSERT INTO `car_to_customer` VALUES ('11', '11', '1', '2673');
INSERT INTO `customer` VALUES ('1', 'U1', '117.240473', '31.713253');
INSERT INTO `customer` VALUES ('2', 'U2', '117.231199', '31.770856');
INSERT INTO `customer` VALUES ('4', 'U4', '117.240855', '31.759800');
INSERT INTO `drone` VALUES ('1', '6', '200', '300', '1500', '180');
INSERT INTO `drone` VALUES ('2', '8', '300', '400', '3000', '320');
INSERT INTO `drone` VALUES ('3', '10', '400', '500', '5000', '400');
INSERT INTO `drone_station` VALUES ('1', 'W1', '117.214161', '31.751530', '1', '1', '1', '1', '2', '1', '3');
INSERT INTO `drone_station` VALUES ('2', 'D1', '117.200029', '31.753592', null, '1', '1', '1', '2', '1', '3');
INSERT INTO `drone_station` VALUES ('3', 'D2', '117.209581', '31.762060', null, '1', '1', '1', '2', '1', '3');
INSERT INTO `drone_station` VALUES ('4', 'D3', '117.209867', '31.774985', null, '1', '1', '1', '2', '1', '3');
INSERT INTO `drone_station` VALUES ('5', 'D4', '117.204802', '31.749343', null, '1', '1', '1', '2', '1', '3');
INSERT INTO `drone_station` VALUES ('6', 'D5', '117.217905', '31.756361', null, '1', '1', '1', '2', '1', '3');
INSERT INTO `drone_station` VALUES ('7', 'D6', '117.223643', '31.759551', null, '1', '1', '1', '2', '1', '3');
INSERT INTO `drone_station` VALUES ('8', 'D7', '117.213935', '31.742092', null, '1', '1', '1', '2', '1', '3');
INSERT INTO `drone_station` VALUES ('9', 'D8', '117.189316', '31.743619', null, '1', '1', '1', '2', '1', '3');
INSERT INTO `drone_station` VALUES ('10', 'D9', '117.231199', '31.747284', null, '1', '1', '1', '2', '1', '3');
INSERT INTO `drone_station` VALUES ('11', 'D10', '117.205524', '31.740505', null, '1', '1', '1', '2', '1', '3');
INSERT INTO `drone_station` VALUES ('12', 'D11', '117.180692', '31.751023', null, '1', '1', '1', '2', '1', '3');
INSERT INTO `drone_station` VALUES ('13', 'D12', '117.231273', '31.753442', null, '1', '1', '1', '2', '1', '3');
INSERT INTO `drone_station` VALUES ('14', 'D13', '117.231244', '31.735506', null, '1', '1', '1', '2', '1', '3');
INSERT INTO `drone_station` VALUES ('15', 'D14', '117.202916', '31.723304', null, '1', '1', '1', '2', '1', '3');
INSERT INTO `drone_station` VALUES ('16', 'D15', '117.222616', '31.717974', null, '1', '1', '1', '2', '1', '3');
INSERT INTO `drone_station` VALUES ('17', 'D16', '117.207786', '31.729351', null, '1', '1', '1', '2', '1', '3');
INSERT INTO `order_record` VALUES ('31', '1', 'W1', '117.240473', '31.713253', '117.240473', '31.713253', '10001', 'U1', '15', '15', '2.00000', 'book', '30', '1', 'phone number: 15780864456', '0', 'W1->D7->D16->D15->C6->U1', '16', '286');
INSERT INTO `order_record` VALUES ('32', '1', 'D1', '117.231199', '31.770856', '117.230213', '31.770655', '10002', 'U2', '12', '12', '1.50000', 'apple', '30', '0', 'Please give priority to delivery', '1', 'D1->W1->D5->D6->C5->U2', '22', '204');
INSERT INTO `order_record` VALUES ('33', '1', 'W1', '117.231199', '31.770856', '117.232973', '31.770100', '10003', 'U2', '18', '18', '4.00000', 'banana', '25', '2', 'no information', '1', 'W1->D5->D6->C5->U2', '11', '139');
INSERT INTO `order_record` VALUES ('34', '1', 'W1', '117.240855', '31.759800', '117.241455', '31.760697', '10004', 'U4', '12', '15', '1.50000', 'bread', '30', '3', 'phone number: 12345678901', '1', 'W1->D5->D6->C5->U4', '18', '134');
INSERT INTO `order_record` VALUES ('35', '1', 'D1', '117.240473', '31.713253', '117.240473', '31.713253', '10005', 'U1', '20', '20', '1.80000', 'iphone', '25', '1', 'phone number: 18324569875', '0', 'D1->D4->D7->C11->U1', '22', '201');
INSERT INTO `order_record` VALUES ('36', '1', 'D13', '117.240855', '31.759800', '117.240855', '31.759800', '10006', 'U4', '23', '23', '2.00000', 'apple', '30', '2', 'Please give priority to delivery', '0', 'D13->D9->D12->C5->U4', '20', '187');
INSERT INTO `order_record` VALUES ('37', '1', 'W1', '117.240473', '31.713253', '117.242777', '31.713269', '10007', 'U1', '15', '15', '1.50000', 'banana', '25', '0', 'no information', '1', 'W1->D7->C11->U1', '27', '181');
INSERT INTO `path` VALUES ('108', '12345', '1', '0', '1');
INSERT INTO `path` VALUES ('109', '12345', '8', '0', '2');
INSERT INTO `path` VALUES ('110', '12345', '0', '3', '3');
INSERT INTO `path` VALUES ('128', '10001', '1', '0', '1');
INSERT INTO `path` VALUES ('129', '10001', '8', '0', '2');
INSERT INTO `path` VALUES ('130', '10001', '17', '0', '3');
INSERT INTO `path` VALUES ('131', '10001', '16', '0', '4');
INSERT INTO `path` VALUES ('132', '10001', '0', '6', '5');
INSERT INTO `path` VALUES ('133', '10002', '2', '0', '1');
INSERT INTO `path` VALUES ('134', '10002', '1', '0', '2');
INSERT INTO `path` VALUES ('135', '10002', '6', '0', '3');
INSERT INTO `path` VALUES ('136', '10002', '7', '0', '4');
INSERT INTO `path` VALUES ('137', '10002', '0', '5', '5');
INSERT INTO `path` VALUES ('138', '10003', '1', '0', '1');
INSERT INTO `path` VALUES ('139', '10003', '6', '0', '2');
INSERT INTO `path` VALUES ('140', '10003', '7', '0', '3');
INSERT INTO `path` VALUES ('141', '10003', '0', '5', '4');
INSERT INTO `path` VALUES ('142', '10004', '1', '0', '1');
INSERT INTO `path` VALUES ('143', '10004', '6', '0', '2');
INSERT INTO `path` VALUES ('144', '10004', '7', '0', '3');
INSERT INTO `path` VALUES ('145', '10004', '0', '5', '4');
INSERT INTO `path` VALUES ('146', '10005', '2', '0', '1');
INSERT INTO `path` VALUES ('147', '10005', '5', '0', '2');
INSERT INTO `path` VALUES ('148', '10005', '8', '0', '3');
INSERT INTO `path` VALUES ('149', '10005', '0', '11', '4');
INSERT INTO `path` VALUES ('150', '10006', '14', '0', '1');
INSERT INTO `path` VALUES ('151', '10006', '10', '0', '2');
INSERT INTO `path` VALUES ('152', '10006', '13', '0', '3');
INSERT INTO `path` VALUES ('153', '10006', '0', '5', '4');
INSERT INTO `path` VALUES ('154', '10007', '1', '0', '1');
INSERT INTO `path` VALUES ('155', '10007', '8', '0', '2');
INSERT INTO `path` VALUES ('156', '10007', '0', '11', '3');
INSERT INTO `t_map` VALUES ('1', '9', '0', '7', '1197');
INSERT INTO `t_map` VALUES ('2', '1', '2', '0', '1357');
INSERT INTO `t_map` VALUES ('3', '1', '3', '0', '1249');
INSERT INTO `t_map` VALUES ('4', '2', '0', '1', '963');
INSERT INTO `t_map` VALUES ('5', '1', '8', '0', '1050');
INSERT INTO `t_map` VALUES ('6', '1', '10', '0', '1680');
INSERT INTO `t_map` VALUES ('7', '8', '11', '0', '815');
INSERT INTO `t_map` VALUES ('8', '5', '8', '0', '1182');
INSERT INTO `t_map` VALUES ('9', '5', '11', '0', '986');
INSERT INTO `t_map` VALUES ('10', '6', '7', '0', '648');
INSERT INTO `t_map` VALUES ('11', '7', '13', '0', '991');
INSERT INTO `t_map` VALUES ('12', '10', '13', '0', '685');
INSERT INTO `t_map` VALUES ('13', '6', '13', '0', '1306');
INSERT INTO `t_map` VALUES ('14', '7', '0', '5', '729');
INSERT INTO `t_map` VALUES ('15', '13', '0', '5', '705');
INSERT INTO `t_map` VALUES ('16', '6', '10', '0', '1613');
INSERT INTO `t_map` VALUES ('17', '1', '5', '0', '918');
INSERT INTO `t_map` VALUES ('18', '1', '6', '0', '644');
INSERT INTO `t_map` VALUES ('19', '5', '2', '0', '654');
INSERT INTO `t_map` VALUES ('20', '9', '11', '0', '1573');
INSERT INTO `t_map` VALUES ('21', '11', '0', '7', '1880');
INSERT INTO `t_map` VALUES ('22', '9', '0', '8', '1867');
INSERT INTO `t_map` VALUES ('23', '5', '12', '0', '2289');
INSERT INTO `t_map` VALUES ('24', '9', '12', '0', '1160');
INSERT INTO `t_map` VALUES ('25', '12', '0', '1', '1464');
INSERT INTO `t_map` VALUES ('26', '3', '0', '4', '1292');
INSERT INTO `t_map` VALUES ('27', '6', '0', '4', '1392');
INSERT INTO `t_map` VALUES ('28', '7', '0', '4', '1043');
INSERT INTO `t_map` VALUES ('29', '3', '4', '0', '1439');
INSERT INTO `t_map` VALUES ('33', '3', '0', '1', '1637');
INSERT INTO `t_map` VALUES ('35', '5', '9', '0', '1598');
INSERT INTO `t_map` VALUES ('36', '4', '0', '4', '1257');
INSERT INTO `t_map` VALUES ('37', '13', '0', '2', '1260');
INSERT INTO `t_map` VALUES ('38', '12', '0', '8', '1435');
INSERT INTO `t_map` VALUES ('39', '8', '14', '0', '1795');
INSERT INTO `t_map` VALUES ('41', '10', '14', '0', '1311');
INSERT INTO `t_map` VALUES ('43', '14', '0', '9', '1167');
INSERT INTO `t_map` VALUES ('45', '16', '0', '6', '1099');
INSERT INTO `t_map` VALUES ('46', '16', '0', '9', '1489');
INSERT INTO `t_map` VALUES ('47', '14', '0', '3', '1533');
INSERT INTO `t_map` VALUES ('48', '8', '17', '0', '1890');
INSERT INTO `t_map` VALUES ('49', '16', '17', '0', '1936');
INSERT INTO `t_map` VALUES ('50', '17', '0', '7', '1589');
INSERT INTO `t_map` VALUES ('51', '10', '0', '10', '1387');
INSERT INTO `t_map` VALUES ('52', '8', '0', '11', '1454');
INSERT INTO `warehouse` VALUES ('1', 'W1', '117.180692', '31.751023');
INSERT INTO `workflow_file` VALUES ('1', null, '1');
