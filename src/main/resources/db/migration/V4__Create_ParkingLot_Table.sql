CREATE TABLE `Parking_Lot` (
  `orderId` BIGINT  NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `name`         VARCHAR(20),
  `size` int,
  `status` VARCHAR(20),
  `init_size` int,
  `user_id` BIGINT
);
insert into Parking_Lot values (1,'拱北停车场',10,'开放',10,1);
insert into Parking_Lot values (3,'香洲停车场',5,'开放',5,2);
insert into Parking_Lot values (4,'宁堂停车场',2,'开放',2,2);
insert into Parking_Lot values (5,'海怡停车场',20,'开放',20,2);
insert into Parking_Lot(orderId,name,size,status,init_size) values (6,'东岸停车场',20,'开放',20);
insert into Parking_Lot(orderId,name,size,status,init_size) values (7,'白沙停车场',20,'开放',20);
insert into Parking_Lot(orderId,name,size,status,init_size,user_id) values (8,'唐家停车场',20,'开放',20,4);
insert into Parking_Lot(orderId,name,size,status,init_size,user_id) values (9,'巨龙停车场',20,'开放',20,4);
insert into Parking_Lot(orderId,name,size,status,init_size,user_id) values (10,'金鼎停车场',20,'开放',20,5);
insert into Parking_Lot(orderId,name,size,status,init_size,user_id) values (11,'湾畔停车场',20,'开放',20,5);
insert into Parking_Lot(orderId,name,size,status,init_size,user_id) values (12,'靠谱停车场',20,'开放',20,5);