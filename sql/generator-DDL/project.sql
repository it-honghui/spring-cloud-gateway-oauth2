create table if not exists tb_wang_project
(
    id           int auto_increment
        primary key,
    user_id      varchar(100) null,
    project_name varchar(100) null,
    constraint id
        unique (id)
);


