CREATE TABLE `User` (
  `orderId` BIGINT  NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `name`         VARCHAR(20)  ,
  `user_name`         VARCHAR(20)  ,
  `password` VARCHAR(60)  ,
  `status` VARCHAR(20)  ,
  `email` VARCHAR(20)  ,
  `phone` VARCHAR(20),
  `alive` Boolean default true
);

-- username=wer password=war
INSERT INTO `User` VALUES (1, '黄权','wer', '$2a$10$EgmhRAlC5iw.KeENZ5.RYebSEHTcO8yl9lOf/r36GoyGb4uNOOhr2','上班','544097676@qq.com','19011112222',true);
INSERT INTO `User` VALUES (2, '李亮','liliang', '$2a$10$EgmhRAlC5iw.KeENZ5.RYebSEHTcO8yl9lOf/r36GoyGb4uNOOhr2','下班','544657676@qq.com','12011112222',true);
INSERT INTO `User` VALUES (3, '赵刚','zhaogang', '$2a$10$EgmhRAlC5iw.KeENZ5.RYebSEHTcO8yl9lOf/r36GoyGb4uNOOhr2','下班','854097676@qq.com','14011112222',true);
INSERT INTO `User` VALUES (4, '李建国','lijianguo', '$2a$10$EgmhRAlC5iw.KeENZ5.RYebSEHTcO8yl9lOf/r36GoyGb4uNOOhr2','下班','785097676@qq.com','18011112222',true);
INSERT INTO `User` VALUES (5, '佟志伟','tongzhiwei', '$2a$10$EgmhRAlC5iw.KeENZ5.RYebSEHTcO8yl9lOf/r36GoyGb4uNOOhr2','下班','685097676@qq.com','16015412222',true);
INSERT INTO `User` VALUES (6, '林新余','linxinyu', '$2a$10$EgmhRAlC5iw.KeENZ5.RYebSEHTcO8yl9lOf/r36GoyGb4uNOOhr2','下班','254097676@qq.com','16018112222',true);
INSERT INTO `User` VALUES (7, '林白玔','linbaichuan', '$2a$10$EgmhRAlC5iw.KeENZ5.RYebSEHTcO8yl9lOf/r36GoyGb4uNOOhr2','下班','424097676@qq.com','17011212222',true);
INSERT INTO `User` VALUES (8, '诸葛暗','zhugean', '$2a$10$EgmhRAlC5iw.KeENZ5.RYebSEHTcO8yl9lOf/r36GoyGb4uNOOhr2','下班','528097676@qq.com','15018182222',true);
INSERT INTO `User` VALUES (9, '柯北','kebei', '$2a$10$EgmhRAlC5iw.KeENZ5.RYebSEHTcO8yl9lOf/r36GoyGb4uNOOhr2','下班','248097676@qq.com','15011812222',true);
INSERT INTO `User` VALUES (10, '李黑','lihei', '$2a$10$EgmhRAlC5iw.KeENZ5.RYebSEHTcO8yl9lOf/r36GoyGb4uNOOhr2','下班','254097676@qq.com','16011412222',true);
INSERT INTO `User` VALUES (11, '赵威志','zhaoweizhi', '$2a$10$EgmhRAlC5iw.KeENZ5.RYebSEHTcO8yl9lOf/r36GoyGb4uNOOhr2','下班','654097676@qq.com','15011112222',true);