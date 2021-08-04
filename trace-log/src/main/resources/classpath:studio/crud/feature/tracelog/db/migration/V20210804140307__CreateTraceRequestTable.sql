CREATE TABLE trace_request
(
    id            bigint(20) NOT NULL AUTO_INCREMENT,
    creation_time datetime   NOT NULL,
    expiry        datetime   NOT NULL,
    trace_target  longtext   NOT NULL,
    trace_type    int(11)    NOT NULL,
    PRIMARY KEY (id)
);