CREATE TABLE role (
  id BIGINT  AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(20) COMMENT '角色'
);
insert into Role values (1,'经理');
insert into Role values (2,'管理员');
insert into Role values (3,'员工');