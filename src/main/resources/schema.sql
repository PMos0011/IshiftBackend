create table cdn_dekl_nag (dkn_dknid bigint not null, dkn_finalna integer, dkn_kwota decimal(19,2), dkn_ts_mod date, dkn_praid integer, dkn_rok_miesiac integer, dkn_typ_deklar integer, primary key (dkn_dknid)) engine=InnoDB;
create table cdn_dekl_wydr (dkw_dkwid bigint not null, dkw_opis varchar(255), dkw_dknid bigint, dkw_rubryka integer, dkw_wartosc varchar(255), primary key (dkw_dkwid)) engine=InnoDB;
create table cdn_firma (fir_firid bigint not null, fir_wartosc varchar(255), fir_numer integer, fir_opis varchar(255), primary key (fir_firid)) engine=InnoDB;
create table bank_account_data (id bigint not null auto_increment, account_number varchar(255), bank_name varchar(255), city varchar(255), local_number varchar(255), street varchar(255), street_number varchar(255), zip_code varchar(255), primary key (id)) engine=InnoDB;
create table web_contactor (id bigint not null auto_increment, city varchar(255), email varchar(255), local_number varchar(255), name varchar(255), nip varchar(255), phone_number varchar(255), regon varchar(255), street varchar(255), street_number varchar(255), zip_code varchar(255), primary key (id)) engine=InnoDB;
create table web_invoice (id bigint not null auto_increment, brutto decimal(19,2), contactor_id bigint, contactor_name varchar(255), currency varchar(255), doc_name varchar(255), doc_type bigint, issue_date datetime, netto decimal(19,2), payment_date datetime, payment_status decimal(19,2), vat decimal(19,2), primary key (id)) engine=InnoDB;
create table commodity (id bigint not null auto_increment, measure varchar(255), name varchar(255), price decimal(19,2), vat_amount decimal(19,2), primary key (id)) engine=InnoDB;
create table invoice_type (id bigint not null auto_increment, name varchar(255), numbering varchar(255), prefix varchar(255), primary key (id)) engine=InnoDB;
create table measure (value bigint not null auto_increment, label varchar(255), primary key (value)) engine=InnoDB;
create table invoice_from_panel (id bigint not null auto_increment, invoice_number varchar(255), invoice_type_name varchar(255), issue_date datetime, place_of_issue varchar(255), sell_date datetime, primary key (id)) engine=InnoDB;
create table summary_data (id bigint not null auto_increment, bank_acc varchar(255), brutto_amount decimal(19,2), comments longtext, invoice_from_panel_id bigint, netto_amount decimal(19,2), paid decimal(19,2), paid_day datetime, payment_day datetime, payment_option_id_value varchar(255), status_id_value varchar(255), vat_amount decimal(19,2), primary key (id)) engine=InnoDB;
create table invoice_vat_table (value bigint not null auto_increment, brutto_amount decimal(19,2), invoice_from_panel_id bigint, netto_amount decimal(19,2), vat decimal(19,2), vat_amount decimal(19,2), primary key (value)) engine=InnoDB;
create table party_data (id bigint not null auto_increment, city varchar(255), id_name varchar(255), id_value varchar(255), invoice_from_panel_id bigint, name varchar(255), party_id integer, street varchar(255), primary key (id)) engine=InnoDB;
create table invoice_commodity (id bigint not null auto_increment, amount decimal(19,2), brutto_amount decimal(19,2), discount decimal(19,2), invoice_from_panel_id bigint, measure varchar(255), name varchar(255), netto_amount decimal(19,2), price decimal(19,2), vat decimal(19,2), vat_amount decimal(19,2), primary key (id)) engine=InnoDB;
create table vat_type (value bigint not null auto_increment, label varchar(255), primary key (value)) engine=InnoDB;