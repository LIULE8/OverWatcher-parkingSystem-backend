CREATE TABLE parking_Lot (
  parking_lot_id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  parking_lot_name           VARCHAR(20) COMMENT '停车场的名字',
  status         VARCHAR(20) COMMENT '停车场的是否可用',
  size           int COMMENT '停车场现容量',
  init_size      int COMMENT '停车场初始容量',
  user_id        BIGINT COMMENT '停车员id'
);
-- 有人管理的停车场
insert into parking_Lot
values (1, '拱北停车场', '开放', 10, 10, 1),
       (3, '香洲停车场', '开放', 5, 5, 2),
       (4, '宁堂停车场', '开放', 2, 2, 2),
       (5, '海怡停车场', '开放', 20, 20, 2),
       (8, '唐家停车场', '开放', 20, 20, 4),
       (9, '巨龙停车场', '开放', 20, 20, 4),
       (10, '金鼎停车场', '开放', 20, 20, 5),
       (11, '湾畔停车场', '开放', 20, 20, 5),
       (12, '靠谱停车场', '开放', 20, 20, 5);

-- 无人管理的停车场
insert into parking_Lot (parking_lot_id, parking_lot_name, status, size, init_size)
values (6, '东岸停车场', '开放', 20, 20),
       (7, '白沙停车场', '开放', 20, 20);
