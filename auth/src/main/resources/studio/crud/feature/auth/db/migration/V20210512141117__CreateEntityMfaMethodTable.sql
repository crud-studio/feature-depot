create table auth_entity_mfa_method
(
    id bigint auto_increment,
    creation_time datetime not null,
    last_update_time datetime not null,
    entity_id bigint not null,
    type varchar(255) not null,
    param1 varchar(1024) null,
    param2 varchar(1024) null,
    param3 varchar(1024) null,
    param4 varchar(1024) null,
    param5 varchar(1024) null,
    primary key (id),
    foreign key fk_emm_entity_id (entity_id) references auth_entity (id)
);