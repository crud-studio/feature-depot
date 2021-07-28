create table auth_entity_authentication_method
(
    id bigint auto_increment,
    creation_time datetime not null,
    last_update_time datetime not null,
    entity_id bigint not null,
    method_type varchar(255) not null,
    param1 varchar(1024) null,
    param2 varchar(1024) null,
    param3 varchar(1024) null,
    param4 varchar(1024) null,
    param5 varchar(1024) null,
    is_active boolean not null default true,
    is_primary boolean not null default false,
    primary key (id),
    foreign key fk_eam_entity_id (entity_id) references auth_entity (id)
);