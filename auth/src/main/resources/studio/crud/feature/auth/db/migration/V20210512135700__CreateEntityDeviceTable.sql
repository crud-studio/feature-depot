create table auth_entity_device
(
    hash varchar(128) not null,
    entity_id bigint not null,
    user_agent varchar(1024) null,
    ip varchar(512) null,
    fingerprint varchar(512) null,
    country_iso varchar(16) null,
    status int not null default 0,
    first_seen datetime not null,
    last_seen datetime not null,
    primary key (hash),
    foreign key fk_entity_device_entity_id (entity_id) references auth_entity (id)
);