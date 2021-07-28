create table parameter
(
    id               bigint       not null auto_increment,
    creation_time    datetime,
    last_update_time datetime,
    description      varchar(255),
    max_value        integer,
    min_value        integer,
    name             varchar(255) not null,
    type             varchar(255) not null,
    value            varchar(255) not null,
    primary key (id)
);