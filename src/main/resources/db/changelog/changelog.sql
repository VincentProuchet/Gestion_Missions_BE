-- liquibase formatted sql
-- changeset liquibase:1
drop table if exists city;
drop table if exists collaborator;
drop table if exists expense;
drop table if exists expense_type;
drop table if exists mission;
drop table if exists nature;
create table city (id integer not null auto_increment, name varchar(255), primary key (id)) engine=InnoDB;
create table collaborator (dtype varchar(31) not null, id integer not null auto_increment, email varchar(255), first_name varchar(255), last_name varchar(255), password varchar(255), role integer, managerid integer,username varchar(50), primary key (id)) engine=InnoDB;
create table expense (id integer not null auto_increment, cost decimal(19,2), date datetime(6), tva float, expense_typeid integer, missionid integer, primary key (id)) engine=InnoDB;
create table expense_type (id integer not null auto_increment, name varchar(255), primary key (id)) engine=InnoDB;
create table mission (id integer not null auto_increment, bonus decimal(19,2), end_date datetime(6), mission_transport varchar(255), start_date datetime(6), status varchar(255), collaboratorid integer, end_cityid integer, natureid integer, start_cityid integer, primary key (id)) engine=InnoDB;
create table nature (id integer not null auto_increment, bonus_percentage float, charged bit not null, date_of_validity datetime(6), description varchar(255), end_of_validity datetime(6), gives_bonus bit not null, tjm decimal(19,2), primary key (id)) engine=InnoDB;
alter table collaborator add constraint FKmjwg1a264ptx18aw87gv7ff7b foreign key (managerid) references collaborator (id);
alter table expense add constraint FKjg5t24w9l8u610kvu82t3m8h8 foreign key (expense_typeid) references expense_type (id);
alter table expense add constraint FKc80p0i6t28c52a4imiqq43iam foreign key (missionid) references mission (id);
alter table mission add constraint FKekq6be030jnfpvontdcsikntq foreign key (collaboratorid) references collaborator (id);
alter table mission add constraint FKkphcdkjbvempgsuqcogqkig08 foreign key (end_cityid) references city (id);
alter table mission add constraint FKp1fl8aea4a7vp0pr45epiyqgr foreign key (natureid) references nature (id);
alter table mission add constraint FKbico4il1r20n6dr3pf35xu3bh foreign key (start_cityid) references city (id);
-- changeset liquibase:2
INSERT INTO `city` (`id`, `name`) VALUES (1, 'Montpellier');

-- changeset ligquibase: Vincent -- adding expenseType values 
INSERT INTO `expense_type` ( `name`) VALUES ( 'transport');
INSERT INTO `expense_type` ( `name`) VALUES ( 'hébergement');
INSERT INTO `expense_type` ( `name`) VALUES ( 'fourniture de bureau');
INSERT INTO `expense_type` ( `name`) VALUES ( 'matériel informatique');

-- changeset liquibase: Vincent --adding userConnectiondetails
drop table if exists users;
drop table if exists authorities;
create table users(
	username varchar(50) not null primary key,
	password varchar(500) not null,
	enabled boolean not null
);

create table authorities (
	username varchar(50) not null,
	authority varchar(50) not null,
	constraint fk_authorities_users foreign key(username) references users(username)
);
create unique index ix_auth_username on authorities (username,authority);



alter table collaborator add constraint userCredentials foreign key (username) references users(username);