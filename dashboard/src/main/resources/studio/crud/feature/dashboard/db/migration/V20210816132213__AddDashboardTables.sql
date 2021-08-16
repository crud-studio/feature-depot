create table dashboard_definition
(
    id               bigint       not null auto_increment,
    creation_time    datetime,
    last_update_time datetime,
    tag              varchar(255) not null,
    title_key        varchar(255) not null,
    primary key (id)
);
create table dashboard_widget
(
    id               bigint       not null auto_increment,
    creation_time    datetime,
    last_update_time datetime,
    params           LONGTEXT,
    title_key        varchar(255) not null,
    type             varchar(255) not null,
    settings LONGTEXT,
    grant_name varchar(255),
    primary key (id)
);
create table dashboard_widget_rel
(
    id                      bigint   not null auto_increment,
    creation_time           datetime,
    last_update_time        datetime,
    sort                    integer  not null,
    width_data                   LONGTEXT not null,
    dashboard_definition_id bigint,
    dashboard_widget_id     bigint,
    primary key (id)
);
alter table dashboard_widget_rel
    add constraint dashboard_widget_rel_definition_fk foreign key (dashboard_definition_id) references dashboard_definition (id);
alter table dashboard_widget_rel
    add constraint dashboard_widget_rel_widget_fk foreign key (dashboard_widget_id) references dashboard_widget (id);
