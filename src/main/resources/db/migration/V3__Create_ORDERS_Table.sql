CREATE TABLE `orders` (
  `orderId`             INT       NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `user_id`        BIGINT,
  `type`           VARCHAR(20)  NOT NULL,
  `status`         VARCHAR(20)  NOT NULL,
  `car_id`         VARCHAR(20)  NOT NULL,
  `created_date`   TIMESTAMP DEFAULT NOW(),
  `parkinglot_id`  BIGINT
 );

--insert into orders(type,status,car_id) values ('存车','存取中','粤A123123');
--insert into orders(type,status,car_id) values ('存车','存车完成','粤A225422');
--insert into orders(type,status,car_id) values ('取车','存取中','粤A2275522');
--insert into orders(type,status,car_id) values ('存车','无人处理','粤A114111');
--insert into orders(type,status,car_id) values ('存车','存车完成','粤B111223');
--insert into orders(type,status,car_id) values ('取车','无人处理','粤B155223');
--insert into orders(type,status,car_id) values ('存车','存取中','粤C122333');
--insert into orders(type,status,car_id) values ('存车','无人处理','粤D111111');
--insert into orders(type,status,car_id) values ('取车','无人处理','粤D251545');