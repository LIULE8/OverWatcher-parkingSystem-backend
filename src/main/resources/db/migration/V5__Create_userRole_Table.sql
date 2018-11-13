CREATE TABLE user_role (
  user_id BIGINT,
  role_id BIGINT
);

insert into user_role
values (1, 2),
       (2, 3),
       (3, 1),
       (4, 3),
       (5, 3),
       (6, 1),
       (7, 3),
       (8, 3),
       (9, 3),
       (10, 1),
       (11, 3);
