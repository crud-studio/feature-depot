create table note
(
    id bigint auto_increment,
    creation_time datetime not null,
    last_update_time datetime not null,
    content longtext not null,
    creator_type varchar(1024) not null,
    creator_id varchar(1024) not null,
    target_type varchar(1024) not null,
    target_id varchar(1024) not null,
    primary key (id)
);