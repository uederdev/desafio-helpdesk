create table users (
    id bigserial not null primary key,
    name varchar(80) not null,
    email varchar(255) not null unique,
    role_name varchar(20) not null
);