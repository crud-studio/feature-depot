create table media_file
(
    id               bigint       not null auto_increment,
    creation_time    datetime,
    last_update_time datetime,
    alias            varchar(255),
    creator_object_id  bigint,
    creator_object_type      varchar(255),
    description      TEXT,
    extension        varchar(255),
    file_hash        varchar(255),
    location         varchar(255) not null,
    name             varchar(255) not null,
    remote_name      varchar(255) not null,
    size             bigint,
    storage_type     varchar(255),
    uuid             varchar(255) not null,
    acl_mode VARCHAR(255) DEFAULT 'PRIVATE' not null,
    primary key (id)
);