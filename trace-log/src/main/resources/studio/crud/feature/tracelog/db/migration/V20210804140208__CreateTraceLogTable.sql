CREATE TABLE trace_log
(
    id                  bigint(20) NOT NULL AUTO_INCREMENT,
    creation_time       datetime   NOT NULL,
    last_update_time    datetime   NOT NULL,
    principal_user_name varchar(255) DEFAULT NULL,
    uri                 longtext   NOT NULL,
    request_body        longtext     DEFAULT NULL,
    response_body       longtext     DEFAULT NULL,
    trace_request_id    bigint(20) NOT NULL,
    PRIMARY KEY (id)
);