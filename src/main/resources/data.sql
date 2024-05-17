drop table if exists sensor_tags;
drop table if exists tags;
drop table if exists topics;
drop table if exists sensors;
drop table if exists topic_types;
drop table if exists sensor_types;
drop table if exists broker_error_logs;
drop table if exists broker;
drop table if exists hooks;
drop table if exists hook_types;
drop table if exists goals;
drop table if exists organization;
drop table if exists control_logs;
drop table if exists control_sensors;
drop table if exists user_roles;
drop table if exists roles;
drop table if exists sub_events;
drop table if exists event_types;
drop table if exists point_details;
drop table if exists attendances;
drop table if exists user_oauths;
drop table if exists oauth_providers;
drop table if exists user_achievements;
drop table if exists achievements;
drop table if exists users;

create table users
(
    user_id       varchar(255) primary key,
    user_password varchar(255),
    user_name     varchar(255),
    user_email    varchar(255),
    user_state    varchar(255),
    user_point    int,
    last_access   date
);

insert into users(user_id, user_password, user_name, user_email, user_state, user_point, last_access) values ('haha', '1234', 'haha','haha@gmail.com','ACTIVE', 1000000, null);
insert into users(user_id, user_password, user_name, user_email, user_state, user_point, last_access) values ('haha2', '1234', 'haha2','haha2@gmail.com`','ACTIVE', 1000000, null);
insert into users(user_id, user_password, user_name, user_email, user_state, user_point, last_access) values ('haha3', '1234', 'haha3','haha3@gmail.com','NOT_APPROVED', 0, null);
insert into users(user_id, user_password, user_name, user_email, user_state, user_point, last_access) values ('haha4', '1234', 'haha4','haha3@gmail.com','NOT_APPROVED', 0, null);
insert into users(user_id, user_password, user_name, user_email, user_state, user_point, last_access) values ('haha5', '1234', 'haha5','haha3@gmail.com','NOT_APPROVED', 0, null);
insert into users(user_id, user_password, user_name, user_email, user_state, user_point, last_access) values ('haha6', '1234', 'haha3','haha6@gmail.com','WITHDRAWAL', 0, null);
insert into users(user_id, user_password, user_name, user_email, user_state, user_point, last_access) values ('haha7', '1234', 'haha7','haha7@gmail.com','WITHDRAWAL', 0, null);
insert into users(user_id, user_password, user_name, user_email, user_state, user_point, last_access) values ('haha8', '1234', 'haha8','haha8@gmail.com','WITHDRAWAL', 0, null);
insert into users(user_id, user_password, user_name, user_email, user_state, user_point, last_access) values ('haha9', '1234', 'haha9','haha9@gmail.com','WITHDRAWAL', 0, null);

create table roles
(
    role_id   int primary key auto_increment,
    role_info varchar(255)
);

insert into roles(role_id, role_info) values (1, 'ROLE_ADMIN');
insert into roles(role_id, role_info) values (2, 'ROLE_USER');

create table user_roles
(
    user_role_id int primary key auto_increment,
    user_id      varchar(255),
    role_id      int,
    foreign key (user_id) references users(user_id),
    foreign key (role_id) references roles(role_id)
);

insert into user_roles(user_id, role_id) values ('haha', 1);
insert into user_roles(user_id, role_id) values ('haha2', 2);

create table achievements
(
    achievement_id int primary key auto_increment,
    achievement_img varchar(255),
    achievement_detail varchar(255)
);

insert into achievements(achievement_img, achievement_detail) values ('img1', 'detail1');
insert into achievements(achievement_img, achievement_detail) values ('img2', 'detail2');

create table user_achievements
(
    user_achievement_id int primary key auto_increment,
    user_id varchar(255),
    achievement_id int,
    foreign key (user_id) references users(user_id),
    foreign key (achievement_id) references achievements(achievement_id)
);

insert into user_achievements(user_id, achievement_id) values ('haha', 1);
insert into user_achievements(user_id, achievement_id) values ('haha', 2);

create table oauth_providers
(
    provider_id int primary key auto_increment,
    provider_name varchar(255)
);

insert into oauth_providers(provider_name) values ('google');
insert into oauth_providers(provider_name) values ('facebook');
insert into oauth_providers(provider_name) values ('kakao');
insert into oauth_providers(provider_name) values ('naver');
insert into oauth_providers(provider_name) values ('github');
insert into oauth_providers(provider_name) values ('line');
insert into oauth_providers(provider_name) values ('twitter');
insert into oauth_providers(provider_name) values ('apple');
insert into oauth_providers(provider_name) values ('microsoft');
insert into oauth_providers(provider_name) values ('yahoo');
insert into oauth_providers(provider_name) values ('linkedin');
insert into oauth_providers(provider_name) values ('instagram');
insert into oauth_providers(provider_name) values ('pinterest');
insert into oauth_providers(provider_name) values ('tumblr');
insert into oauth_providers(provider_name) values ('foursquare');
insert into oauth_providers(provider_name) values ('wordpress');
insert into oauth_providers(provider_name) values ('dropbox');
insert into oauth_providers(provider_name) values ('evernote');
insert into oauth_providers(provider_name) values ('flickr');
insert into oauth_providers(provider_name) values ('spotify');
insert into oauth_providers(provider_name) values ('slack');
insert into oauth_providers(provider_name) values ('trello');
insert into oauth_providers(provider_name) values ('vimeo');
insert into oauth_providers(provider_name) values ('youtube');
insert into oauth_providers(provider_name) values ('whatsapp');
insert into oauth_providers(provider_name) values ('telegram');
insert into oauth_providers(provider_name) values ('skype');
insert into oauth_providers(provider_name) values ('wechat');

create table user_oauths
(
    user_oauth_id int primary key auto_increment,
    provider_id int,
    user_id varchar(255),
    provider_key varchar(255),
    foreign key (user_id) references users(user_id),
    foreign key (provider_id) references oauth_providers(provider_id)
);

insert into user_oauths(provider_id, user_id, provider_key) values (1, 'haha', '1234');
insert into user_oauths(provider_id, user_id, provider_key) values (2, 'haha', '1234');
insert into user_oauths(provider_id, user_id, provider_key) values (3, 'haha', '1234');
insert into user_oauths(provider_id, user_id, provider_key) values (4, 'haha', '1234');
insert into user_oauths(provider_id, user_id, provider_key) values (5, 'haha', '1234');

create table attendances
(
    attendance_id int primary key auto_increment,
    user_id varchar(255),
    attendance_date date,
    foreign key (user_id) references users(user_id)
);

insert into attendances(user_id, attendance_date) values ('haha', '2024-05-01');
insert into attendances(user_id, attendance_date) values ('haha', '2024-05-02');
insert into attendances(user_id, attendance_date) values ('haha', '2024-05-03');
insert into attendances(user_id, attendance_date) values ('haha', '2024-05-04');
insert into attendances(user_id, attendance_date) values ('haha', '2024-05-05');
insert into attendances(user_id, attendance_date) values ('haha', '2024-05-06');
insert into attendances(user_id, attendance_date) values ('haha', '2024-05-07');
insert into attendances(user_id, attendance_date) values ('haha', '2024-05-08');
insert into attendances(user_id, attendance_date) values ('haha', '2024-05-09');


create table point_details
(
    point_detail_id int primary key auto_increment,
    user_id varchar(255),
    point_amount int,
    point_usage varchar(255),
    point_date date,
    foreign key (user_id) references users(user_id)
);

insert into point_details(user_id, point_amount, point_usage, point_date) values ('haha', 10000, '적립', '2024-05-01');
insert into point_details(user_id, point_amount, point_usage, point_date) values ('haha', 10000, '적립', '2024-05-02');
insert into point_details(user_id, point_amount, point_usage, point_date) values ('haha', 10000, '적립', '2024-05-03');
insert into point_details(user_id, point_amount, point_usage, point_date) values ('haha', -3000, '사용', '2024-05-04');
insert into point_details(user_id, point_amount, point_usage, point_date) values ('haha', 10000, '적립', '2024-05-05');
insert into point_details(user_id, point_amount, point_usage, point_date) values ('haha', -20000, '사용', '2024-05-06');

create table event_types
(
    event_type_id int primary key auto_increment,
    event_type_name varchar(255)
);

insert into event_types(event_type_name) values ('이벤트1');
insert into event_types(event_type_name) values ('이벤트2');

create table sub_events
(
    sub_event_id int primary key auto_increment,
    event_type_id int,
    user_id varchar(255),
    foreign key (event_type_id) references event_types(event_type_id),
    foreign key (user_id) references users(user_id)
);

insert into sub_events(event_type_id, user_id) values (1, 'haha');
insert into sub_events(event_type_id, user_id) values (2, 'haha');

create table hook_types
(
    hook_type_id int primary key auto_increment,
    hook_type varchar(255)
);

insert into hook_types(hook_type) values ('dooray');

create table hooks
(
    hook_id int primary key auto_increment,
    hook_url varchar(255),
    user_id varchar(255),
    hook_type_id int,
    foreign key (user_id) references users(user_id),
    foreign key (hook_type_id) references hook_types(hook_type_id)
);

insert into hooks(hook_url, user_id, hook_type_id) values ('http://localhost:8080', 'haha', 1);

create table organization
(
    organization_id int primary key auto_increment,
    organization_name varchar(255)
);

insert into organization(organization_name) values ('NHN');

create table goals
(
    goal_id int primary key auto_increment,
    goal_date date,
    goal_amount double,
    amount double,
    unit_price integer
);

insert into goals(goal_date, goal_amount, amount, unit_price) values ('2023-01-01', 6000, 4800, 1500);
insert into goals(goal_date, goal_amount, amount, unit_price) values ('2023-02-01', 5000, 6400, 1500);
insert into goals(goal_date, goal_amount, amount, unit_price) values ('2023-03-01', 7000, 6600, 1500);
insert into goals(goal_date, goal_amount, amount, unit_price) values ('2023-04-01', 6000, 5300, 1500);
insert into goals(goal_date, goal_amount, amount, unit_price) values ('2023-05-01', 5000, 5500, 1500);
insert into goals(goal_date, goal_amount, amount, unit_price) values ('2023-06-01', 6000, 4800, 1500);
insert into goals(goal_date, goal_amount, amount, unit_price) values ('2023-07-01', 5000, 6400, 1500);
insert into goals(goal_date, goal_amount, amount, unit_price) values ('2023-08-01', 7000, 6600, 1500);
insert into goals(goal_date, goal_amount, amount, unit_price) values ('2023-09-01', 6000, 5300, 1500);
insert into goals(goal_date, goal_amount, amount, unit_price) values ('2023-10-01', 5000, 5500, 1500);
insert into goals(goal_date, goal_amount, amount, unit_price) values ('2023-11-01', 5000, 4500, 1500);
insert into goals(goal_date, goal_amount, amount, unit_price) values ('2023-12-01', 5000, 5500, 1500);

insert into goals(goal_date, goal_amount, amount, unit_price) values ('2024-01-01', 6000, 4800, 1500);
insert into goals(goal_date, goal_amount, amount, unit_price) values ('2024-02-01', 5000, 6400, 1500);
insert into goals(goal_date, goal_amount, amount, unit_price) values ('2024-03-01', 7000, 6600, 1500);
insert into goals(goal_date, goal_amount, amount, unit_price) values ('2024-04-01', 6000, 5300, 1500);
insert into goals(goal_date, goal_amount, amount, unit_price) values ('2024-05-01', 3000, null, 1500);

create table control_sensors
(
    control_sensor_id int primary key auto_increment,
    sensor_place varchar(30),
    sensor_ip varchar(30),
    sensor_port varchar(30)
);

insert into control_sensors(sensor_place, sensor_ip, sensor_port) values ('office', '192.168.0.1', '8080');
insert into control_sensors(sensor_place, sensor_ip, sensor_port) values ('home', '172.168.0.2', '8081');

create table control_logs
(
    control_log_id int primary key auto_increment,
    control_sensor_id int,
    control_time date,
    foreign key (control_sensor_id) references control_sensors(control_sensor_id)
);

insert into control_logs(control_sensor_id, control_time) values (1, '2024-05-01');
insert into control_logs(control_sensor_id, control_time) values (2, '2024-05-02');

create table broker
(
    broker_id int primary key auto_increment,
    broker_ip varchar(255),
    broker_port int,
    broker_name varchar(255),
    protocol_type varchar(255)
);

insert into broker(broker_ip, broker_port, broker_name, protocol_type) values ('133.186.220.105', 1883, 'Broker1', 'MQTT');

create table broker_error_logs
(
    broker_error_log_id int primary key auto_increment,
    broker_id int,
    broker_error_type varchar(255),
    broker_error_created_at date,
    broker_error_solved_at date,
    foreign key (broker_id) references broker(broker_id)
);

insert into broker_error_logs(broker_id, broker_error_type, broker_error_created_at, broker_error_solved_at) values (1, '연결 오류', '2024-05-01', '2024-05-02');
insert into broker_error_logs(broker_id, broker_error_type, broker_error_created_at, broker_error_solved_at) values (1, '절단', '2024-05-02', '2024-05-03');
insert into broker_error_logs(broker_id, broker_error_type, broker_error_created_at, broker_error_solved_at) values (1, '연결 박살', '2024-05-03', '2024-05-04');

create table sensor_types
(
    sensor_type varchar(255) primary key
);

insert into sensor_types(sensor_type) values ('전기');
insert into sensor_types(sensor_type) values ('온도');
insert into sensor_types(sensor_type) values ('습도');
insert into sensor_types(sensor_type) values ('co2');

create table topic_types
(
    topic_type varchar(255) primary key
);

insert into topic_types(topic_type) values ('전력');
insert into topic_types(topic_type) values ('전압');
insert into topic_types(topic_type) values ('전력량');
insert into topic_types(topic_type) values ('온도');
insert into topic_types(topic_type) values ('습도');
insert into topic_types(topic_type) values ('co2');
insert into topic_types(topic_type) values ('전류');

create table sensors
(
    sensor_id int primary key auto_increment,
    broker_id int,
    sensor_name varchar(255),
    sensor_registered_at date,
    sensor_type varchar(255),
    foreign key (broker_id) references broker(broker_id)
);

create table topics
(
    topic_id int primary key auto_increment,
    topic varchar(255),
    topic_registered_at date,
    sensor_id int,
    topic_type varchar(255),
    foreign key (sensor_id) references sensors(sensor_id)
);

create table tags
(
    tag_id int primary key auto_increment,
    user_id varchar(255),
    tag_name varchar(255),
    foreign key (user_id) references users(user_id)
);

insert into tags(user_id, tag_name) values ('haha', 'NHN');
insert into tags(user_id, tag_name) values ('haha', 'Office');

create table sensor_tags
(
    sensor_tag_id int primary key auto_increment,
    tag_id int,
    sensor_id int,
    foreign key (tag_id) references tags(tag_id),
    foreign key (sensor_id) references sensors(sensor_id)
);

insert into sensors(sensor_id, broker_id, sensor_name, sensor_registered_at, sensor_type)
values (1, 1, '에어컨', null, '전기');
insert into topics(topic_id, topic, topic_registered_at, sensor_id, topic_type)
values (1, 'data/s/nhnacademy/b/gyeongnam/p/office/d/gems-3500/e/electrical_energy/t/air_conditioner/ph/total/de/w',
        null, 1, '전력');
insert into topics(topic_id, topic, topic_registered_at, sensor_id, topic_type)
values (2, 'data/s/nhnacademy/b/gyeongnam/p/office/d/gems-3500/e/electrical_energy/t/air_conditioner/ph/kwh/de/sum',
        null, 1, '전력량');

insert into sensors(sensor_id, broker_id, sensor_name, sensor_registered_at, sensor_type)
values (2, 1, '복사기', null, '전기');
insert into topics(topic_id, topic, topic_registered_at, sensor_id, topic_type)
values (3, 'data/s/nhnacademy/b/gyeongnam/p/office/d/gems-3500/e/electrical_energy/t/office_copier/ph/total/de/w', null,
        2, '전력');
insert into topics(topic_id, topic, topic_registered_at, sensor_id, topic_type)
values (4, 'data/s/nhnacademy/b/gyeongnam/p/office/d/gems-3500/e/electrical_energy/t/office_copier/ph/kwh/de/sum', null,
        2, '전력량');

insert into sensors(sensor_id, broker_id, sensor_name, sensor_registered_at, sensor_type)
values (3, 1, 'A프로젝트', null, '전기');
insert into topics(topic_id, topic, topic_registered_at, sensor_id, topic_type)
values (5, 'data/s/nhnacademy/b/gyeongnam/p/office/d/gems-3500/e/electrical_energy/t/a_project/ph/kwh/de/sum', null, 3,
        '전력량');

insert into sensors(sensor_id, broker_id, sensor_name, sensor_registered_at, sensor_type)
values (4, 1, 'AC', null, '전기');
insert into topics(topic_id, topic, topic_registered_at, sensor_id, topic_type)
values (15, 'data/s/nhnacademy/b/gyeongnam/p/office/d/gems-3500/e/electrical_energy/t/ac/ph/kwh/de/sum', null, 4,
        '전력량');

insert into sensors(sensor_id, broker_id, sensor_name, sensor_registered_at, sensor_type)
values (5, 1, '에어컨_실내기', null, '전기');
insert into topics(topic_id, topic, topic_registered_at, sensor_id, topic_type)
values (6, 'data/s/nhnacademy/b/gyeongnam/p/office/d/gems-3500/e/electrical_energy/t/ac_indoor_unit/ph/kwh/de/sum',
        null, 5, '전력량');

insert into sensors(sensor_id, broker_id, sensor_name, sensor_registered_at, sensor_type)
values (6, 1, '에어컨_실외기', null, '전기');
insert into topics(topic_id, topic, topic_registered_at, sensor_id, topic_type)
values (7, 'data/s/nhnacademy/b/gyeongnam/p/class_a/d/gems-3500/e/electrical_energy/t/ac_outdoor_unit/ph/kwh/de/sum',
        null, 6, '전력량');

insert into sensors(sensor_id, broker_id, sensor_name, sensor_registered_at, sensor_type)
values (7, 1, '에어컨_실외기', null, '전기');
insert into topics(topic_id, topic, topic_registered_at, sensor_id, topic_type)
values (8, 'data/s/nhnacademy/b/gyeongnam/p/office/d/gems-3500/e/electrical_energy/t/ac_outdoor_unit/ph/kwh/de/sum',
        null, 7, '전력량');

insert into sensors(sensor_id, broker_id, sensor_name, sensor_registered_at, sensor_type)
values (8, 1, '에어컨', null, '전기');
insert into topics(topic_id, topic, topic_registered_at, sensor_id, topic_type)
values (9, 'data/s/nhnacademy/b/gyeongnam/p/office/d/gems-3500/e/electrical_energy/t/air_conditioner/ph/kwh/de/sum',
        null, 8, '전력량');

insert into sensors(sensor_id, broker_id, sensor_name, sensor_registered_at, sensor_type)
values (9, 1, '자동문', null, '전기');
insert into topics(topic_id, topic, topic_registered_at, sensor_id, topic_type)
values (10, 'data/s/nhnacademy/b/gyeongnam/p/class_a/d/gems-3500/e/electrical_energy/t/automatic_door/ph/kwh/de/sum',
        null, 9, '전력량');

insert into sensors(sensor_id, broker_id, sensor_name, sensor_registered_at, sensor_type)
values (10, 1, '자동문', null, '전기');
insert into topics(topic_id, topic, topic_registered_at, sensor_id, topic_type)
values (11, 'data/s/nhnacademy/b/gyeongnam/p/office/d/gems-3500/e/electrical_energy/t/automatic_door/ph/kwh/de/sum',
        null, 10, '전력량');

insert into sensors(sensor_id, broker_id, sensor_name, sensor_registered_at, sensor_type)
values (11, 1, 'B프로젝트', null, '전기');
insert into topics(topic_id, topic, topic_registered_at, sensor_id, topic_type)
values (12, 'data/s/nhnacademy/b/gyeongnam/p/office/d/gems-3500/e/electrical_energy/t/b_project/ph/kwh/de/sum', null,
        11, '전력량');

insert into sensors(sensor_id, broker_id, sensor_name, sensor_registered_at, sensor_type)
values (12, 1, 'barton_heating_window', null, '전기');
insert into topics(topic_id, topic, topic_registered_at, sensor_id, topic_type)
values (13,
        'data/s/nhnacademy/b/gyeongnam/p/office/d/gems-3500/e/electrical_energy/t/barton_heating_window/ph/kwh/de/sum',
        null, 12, '전력량');

insert into sensors(sensor_id, broker_id, sensor_name, sensor_registered_at, sensor_type)
values (13, 1, '빌트인히터', null, '전기');
insert into topics(topic_id, topic, topic_registered_at, sensor_id, topic_type)
values (14, 'data/s/nhnacademy/b/gyeongnam/p/office/d/gems-3500/e/electrical_energy/t/built_in_heating/ph/kwh/de/sum',
        null, 13, '전력량');




insert into sensor_tags(tag_id, sensor_id) values (1, 1);
insert into sensor_tags(tag_id, sensor_id) values (2, 2);
insert into sensor_tags(tag_id, sensor_id) values (1, 2);
insert into sensor_tags(tag_id, sensor_id) values (1, 2);
insert into sensor_tags(tag_id, sensor_id) values (1, 3);
insert into sensor_tags(tag_id, sensor_id) values (1, 4);
insert into sensor_tags(tag_id, sensor_id) values (1, 5);
insert into sensor_tags(tag_id, sensor_id) values (1, 6);
insert into sensor_tags(tag_id, sensor_id) values (2, 5);
insert into sensor_tags(tag_id, sensor_id) values (2, 6);
insert into sensor_tags(tag_id, sensor_id) values (2, 7);
insert into sensor_tags(tag_id, sensor_id) values (2, 8);
insert into sensor_tags(tag_id, sensor_id) values (2, 9);

