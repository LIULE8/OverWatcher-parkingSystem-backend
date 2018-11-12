CREATE TABLE `order` (
  order_id      INT         NOT NULL        AUTO_INCREMENT PRIMARY KEY,
  user_id       BIGINT COMMENT '跟订单相关的停车员',
  parkinglot_id BIGINT COMMENT '跟订单相关的停车场',
  type          VARCHAR(20) NOT NULL
  COMMENT '订单类型',
  status        VARCHAR(20) NOT NULL
  COMMENT '订单状态',
  car_id        VARCHAR(20) NOT NULL
  COMMENT '订单管理的那个车牌号',
  created_date  TIMESTAMP   NOT NULL        DEFAULT CURRENT_TIMESTAMP
  COMMENT '订单创建时间'
);

-- insert into order (type, status, car_id)
-- values ('存车', '存取中', '粤A123123'),
--        ('存车', '存车完成', '粤A225422'),
--        ('取车', '存取中', '粤A2275522'),
--        ('存车', '无人处理', '粤A114111'),
--        ('存车', '存车完成', '粤B111223'),
--        ('取车', '无人处理', '粤B155223'),
--        ('存车', '存取中', '粤C122333'),
--        ('存车', '无人处理', '粤D111111'),
--        ('取车', '无人处理', '粤D251545');