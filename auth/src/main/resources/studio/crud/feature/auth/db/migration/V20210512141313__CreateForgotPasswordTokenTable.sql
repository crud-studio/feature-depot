create table auth_forgot_password_token
(
    id bigint auto_increment,
    creation_time datetime not null,
    last_update_time datetime not null,
    entity_id bigint not null,
    method_id bigint not null,
    token varchar(256) not null,
    is_expired boolean not null default false,
    device_hash varchar(256) null,
    primary key (id),
    foreign key fk_fpt_entity_id (entity_id) references auth_entity (id),
    foreign key fk_fpt_method_id (entity_id) references auth_entity_authentication_method (id)
);