create table accounting_office (id bigint not null auto_increment, name varchar(255), random_id varchar(255), primary key (id)) engine=InnoDB;
create table cdn_dekl_nag (dkn_dknid bigint not null, dkn_finalna integer, dkn_kwota decimal(19,2), dkn_praid integer, dkn_rok_miesiac integer, dkn_typ_deklar integer, primary key (dkn_dknid)) engine=InnoDB;
create table cdn_dekl_wydr (dkw_dkwid bigint not null, dkw_opis varchar(255), dkw_dknid bigint, dkw_rubryka integer, dkw_wartosc varchar(255), primary key (dkw_dkwid)) engine=InnoDB;
create table cdn_firma (fir_firid bigint not null, fir_wartosc varchar(255), fir_numer integer, fir_opis varchar(255), primary key (fir_firid)) engine=InnoDB;
create table swap (id bigint not null auto_increment, customer_data longblob, database_name varchar(255), primary key (id)) engine=InnoDB;
create table user_data (id bigint not null auto_increment, db_id varchar(255), password varchar(255), role varchar(255), user_name varchar(255), primary key (id)) engine=InnoDB;
create table web_company_data (id bigint not null auto_increment, company_name varchar(255), db_name varchar(255), officeid varchar(255), random_id varchar(255), primary key (id)) engine=InnoDB;
alter table cdn_dekl_wydr add constraint FK9ocaneu20kq228gqu69r3uisa foreign key (dkw_dknid) references cdn_dekl_nag (dkn_dknid);