create table auth_entity
(
    id bigint auto_increment,
    creation_time datetime not null,
    last_update_time datetime not null,
    email varchar(255) null,
    uuid varchar(255) null,
    telephone varchar(255) null,
    primary key (id)
);