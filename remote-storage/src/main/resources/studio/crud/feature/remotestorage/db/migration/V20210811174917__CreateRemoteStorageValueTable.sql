create table remote_storage_value
(
    id               bigint       not null auto_increment,
    creation_time    datetime,
    last_update_time datetime,
    identifier varchar(1024) not null,
    value longtext not null,
    owner_id varchar(256),
    owner_type varchar(256),
    unique key remote_value_owner_unq (identifier, owner_id, owner_type),
    primary key (id)
);