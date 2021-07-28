create table auth_token_metadata
(
    id bigint auto_increment,
    creation_time datetime not null,
    last_update_time datetime not null,
    token TEXT not null,
    entity_uuid varchar(255) not null,
    device_hash varchar(255) null,
    device_score int null,
    primary key (id)
);