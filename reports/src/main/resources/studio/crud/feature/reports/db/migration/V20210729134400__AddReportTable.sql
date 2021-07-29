create table report
(
    id               bigint       not null auto_increment,
    creation_time    datetime,
    last_update_time datetime,
    description      TEXT         not null,
    headers          LONGTEXT,
    row_limit            integer,
    name             varchar(255) not null,
    parameter_definitions       LONGTEXT,
    sql_query        LONGTEXT     not null,
    primary key (id)
);
