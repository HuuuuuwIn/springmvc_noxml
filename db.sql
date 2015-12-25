/*
SQLyog Ultimate v8.32 
MySQL - 5.5.25-log : Database - iadlogsys
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`iadlogsys` /*!40100 DEFAULT CHARACTER SET utf8 */;

USE `iadlogsys`;

/*Table structure for table `t_node` */

DROP TABLE IF EXISTS `t_node`;

CREATE TABLE `t_node` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(30) DEFAULT NULL COMMENT '节点名',
  `ip` varchar(20) DEFAULT NULL,
  `user` varchar(20) DEFAULT NULL COMMENT '用户名',
  `pwd` varchar(30) DEFAULT NULL COMMENT '密码',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COMMENT='节点表';

/*Data for the table `t_node` */

insert  into `t_node`(`id`,`name`,`ip`,`user`,`pwd`) values (1,'192.168.0.239','192.168.0.239','root','zzcm2014'),(2,'192.168.0.180','192.168.0.180','root','zzcm2014');

/*Table structure for table `t_task` */

DROP TABLE IF EXISTS `t_task`;

CREATE TABLE `t_task` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(30) DEFAULT NULL COMMENT '任务别名',
  `group_name` varchar(30) DEFAULT NULL COMMENT '任务组名',
  `n_from` int(11) DEFAULT NULL COMMENT '源地址',
  `dir_from` varchar(50) DEFAULT NULL COMMENT '源文件夹',
  `filename` varchar(30) DEFAULT NULL COMMENT '文件名',
  `dir_to` varchar(50) DEFAULT NULL COMMENT '目标文件夹',
  `n_to` int(11) DEFAULT NULL COMMENT '目标地址',
  `crontime` varchar(30) DEFAULT NULL COMMENT '执行时间',
  `enable` tinyint(1) DEFAULT '0' COMMENT '是否启用',
  `sync` tinyint(1) DEFAULT '0' COMMENT '是否同步',
  `status` tinyint(1) DEFAULT '0' COMMENT '状态',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8 COMMENT='任务表';

/*Data for the table `t_task` */

insert  into `t_task`(`id`,`name`,`group_name`,`n_from`,`dir_from`,`filename`,`dir_to`,`n_to`,`crontime`,`enable`,`sync`,`status`) values (1,'first','group_f',2,'/opt','cid.jar','D:/test/',NULL,'0 * * * * ?',1,0,1),(5,'job1','group1',1,NULL,NULL,NULL,NULL,'5/20 * * * * ?',0,0,NULL),(6,'job2','group2',1,NULL,NULL,NULL,NULL,'5/30 * * * * ?',0,0,NULL),(8,'job3','group3',2,'/opt','nginxLog.jar','D:/test/',NULL,'0 0/2 * * * ?',1,0,1),(9,'job4','group4',2,'/opt','sparktest.jar','D:/test',NULL,'0 0/2 * * * ?',1,1,1),(10,'job5','group5',2,'/opt','kafkatest.jar','D:/test/',NULL,'0 0/3 * * * ?',0,0,NULL),(11,'job6','group6',2,'/opt','metrics-core-2.2.0.jar','D:/test/',NULL,'0 * * * * ?',0,0,NULL);

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
