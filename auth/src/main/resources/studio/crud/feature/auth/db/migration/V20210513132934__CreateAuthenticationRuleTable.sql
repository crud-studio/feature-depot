create table auth_authentication_rule
(
    id bigint auto_increment,
    creation_time datetime not null,
    last_update_time datetime not null,
    min_score bigint not null default 0,
    action int null,
    primary key (id)
);