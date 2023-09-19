create table if not exists tb_wang_auth_url
(
    id  int          null,
    url varchar(100) null
);

create table if not exists tb_wang_role
(
    id        mediumtext   null,
    role_name varchar(100) null
);

create table if not exists tb_wang_url_role_relation
(
    id      int auto_increment
        primary key,
    role_id mediumtext null,
    url_id  mediumtext null,
    constraint id
        unique (id)
);

create table if not exists tb_wang_user
(
    id                      int auto_increment
        primary key,
    username                varchar(100)  null,
    password                varchar(100)  null,
    mobile                  varchar(100)  null,
    pwd                     varchar(100)  null,
    deleted                 int default 0 null,
    version                 int           null,
    avatar_url              varchar(100)  null,
    enabled                 int default 1 null,
    credentials_non_expired int default 1 null,
    account_non_expired     int default 1 null,
    account_non_locked      int default 1 null,
    constraint id
        unique (id)
);

create table if not exists tb_wang_user_role_relation
(
    id      mediumtext null,
    role_id int        null,
    user_id int        null
);


