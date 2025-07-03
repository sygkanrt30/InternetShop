create table bucket
(
    username varchar(255) not null
        constraint username primary key
);

create table product
(
    id bigserial
        constraint product_pk primary key,
    name varchar(50) not null
        constraint product_unique_k unique,
    is_available boolean not null,
    count integer not null,
    price bigint not null,
    description text
);

create table users
(
    username varchar(100) not null
        constraint users_pk primary key,
    email varchar(100) not null
        constraint users_uk unique,
    password varchar(255) not null,
    role varchar(20),
    bucket_owner varchar(255) not null
        constraint users_bucket_username_fk
            references bucket
);


create table bucket_items
(
    username   varchar(100) not null
        constraint bucket_items_bucket_username_fk
            references bucket,
    product_id bigint not null
        constraint bucket_items_product_id_fk
            references product,
    quantity   integer not null,
    constraint bucket_items_pk
        primary key (username, product_id)
);