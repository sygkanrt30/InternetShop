create table bucket
(
    username varchar(255) not null
        constraint username primary key,
    list_of_products bigint[] default '{}'::bigint[] not null
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
    role varchar(20) default 'USER'::character varying,
    bucketowner varchar(255) not null
        constraint users_bucket_username_fk
            references bucket
);

INSERT INTO product (id, name, is_available, count, price, description) VALUES (11, 'соль', true, 100, 69, 'Соль поваренная');
INSERT INTO product (id, name, is_available, count, price, description) VALUES (21, 'леденец', true, 200, 39, 'Леденец клубничный');
INSERT INTO product (id, name, is_available, count, price, description) VALUES (22, 'мороженое', true, 70, 109, 'Мороженое крем-брюле');
INSERT INTO product (id, name, is_available, count, price, description) VALUES (23, 'чай', true, 80, 130, 'Пачка(30 пакетиков) черного чая');
INSERT INTO product (id, name, is_available, count, price, description) VALUES (12, 'сметана', true, 70, 109, 'Сметана 20%');
INSERT INTO product (id, name, is_available, count, price, description) VALUES (17, 'кефир', true, 90, 89, 'Кефир 2.5%');
INSERT INTO product (id, name, is_available, count, price, description) VALUES (15, 'кетчуп', true, 90, 129, 'Кетчуп томатный');
INSERT INTO product (id, name, is_available, count, price, description) VALUES (18, 'печенье', true, 100, 199, 'Пачка печенья топленое молоко');
INSERT INTO product (id, name, is_available, count, price, description) VALUES (19, 'зефир', true, 100, 109, 'Зефир Ванильный');
INSERT INTO product (id, name, is_available, count, price, description) VALUES (16, 'вода', true, 100, 49, 'Бутылка 0.5л негазированной воды');
INSERT INTO product (id, name, is_available, count, price, description) VALUES (20, 'кофе', true, 100, 15, 'Кофе растворимый 3в1');
INSERT INTO product (id, name, is_available, count, price, description) VALUES (10, 'хлеб', true, 100, 49, 'Батон летний, нарезной');
INSERT INTO product (id, name, is_available, count, price, description) VALUES (1, 'молоко', true, 100, 99, 'Молоко 2.7%');
INSERT INTO product (id, name, is_available, count, price, description) VALUES (13, 'жевательная резина', true, 100, 35, 'Жевательная резина со вкусом лесных ягод');
INSERT INTO product (id, name, is_available, count, price, description) VALUES (14, 'сахар', true, 100, 149, 'Сахар тростниковый');
