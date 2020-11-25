create table accounting_office (id bigint not null auto_increment, name varchar(255), random_id varchar(255), primary key (id)) engine=InnoDB;
create table bank_account_data (id bigint not null auto_increment, account_number varchar(255), bank_name varchar(255), city varchar(255), local_number varchar(255), street varchar(255), street_number varchar(255), zip_code varchar(255), primary key (id)) engine=InnoDB;
create table cdn_bnk_nazwy (bna_bna_id bigint not null, bna_miasto varchar(255), bna_nr_lokalu varchar(255), bna_nazwa1 varchar(255), bna_nazwa2 varchar(255), bna_ulica varchar(255), bna_nr_domu varchar(255), bna_kod_pocztowy varchar(255), primary key (bna_bna_id)) engine=InnoDB;
create table cdn_bnk_rachunki (bra_braid bigint not null, bra_rachunek_nr varchar(255), bra_bnaid integer, primary key (bra_braid)) engine=InnoDB;
create table cdn_dekl_nag (dkn_dknid bigint not null, dkn_finalna integer, dkn_kwota decimal(19,2), dkn_ts_mod date, dkn_praid integer, dkn_rok_miesiac integer, dkn_typ_deklar integer, primary key (dkn_dknid)) engine=InnoDB;
create table cdn_dekl_wydr (dkw_dkwid bigint not null, dkw_opis varchar(255), dkw_dknid bigint, dkw_rubryka integer, dkw_wartosc varchar(255), primary key (dkw_dkwid)) engine=InnoDB;
create table cdn_firma (fir_firid bigint not null, fir_wartosc varchar(255), fir_numer integer, fir_opis varchar(255), primary key (fir_firid)) engine=InnoDB;
create table cdn_kontrahenci (knt_knt_id bigint not null auto_increment, knt_miasto varchar(255), knt_email varchar(255), knt_nr_lokalu varchar(255), knt_nazwa1 varchar(255), knt_nazwa2 varchar(255), knt_nazwa3 varchar(255), knt_nip varchar(255), knt_telefon1 varchar(255), knt_regon varchar(255), knt_ulica varchar(255), knt_nr_domu varchar(255), knt_kod_pocztowy varchar(255), primary key (knt_knt_id)) engine=InnoDB;
create table cdn_vat_nag (van_vanid bigint not null auto_increment, van_razem_brutto decimal(19,2), van_podid bigint, van_rok_mies integer, van_dokument varchar(255), van_data_wys datetime, van_knt_nazwa1 varchar(255), van_knt_nazwa2 varchar(255), van_knt_nazwa3 varchar(255), van_razem_netto decimal(19,2), van_data_zap datetime, van_typ integer, van_razemvat decimal(19,2), primary key (van_vanid)) engine=InnoDB;
create table commodity (id bigint not null auto_increment, measure varchar(255), name varchar(255), price decimal(19,2), vat_amount varchar(255), primary key (id)) engine=InnoDB;
create table invoice_commodity (id bigint not null auto_increment, amount decimal(19,2), brutto_amount decimal(19,2), correction_id bigint, discount decimal(19,2), invoice_from_panel_id bigint, measure varchar(255), name varchar(255), netto_amount decimal(19,2), price decimal(19,2), vat varchar(255), vat_amount decimal(19,2), primary key (id)) engine=InnoDB;
create table invoice_from_panel (id bigint not null auto_increment, correction_id bigint, correction_reason varchar(255), invoice_number varchar(255), invoice_type_name varchar(255), issue_date datetime, place_of_issue varchar(255), sell_date datetime, primary key (id)) engine=InnoDB;
create table invoice_type (id bigint not null auto_increment, name varchar(255), numbering varchar(255), prefix varchar(255), primary key (id)) engine=InnoDB;
create table invoice_vat_table (value bigint not null auto_increment, brutto_amount decimal(19,2), invoice_from_panel_id bigint, netto_amount decimal(19,2), vat varchar(255), vat_amount decimal(19,2), primary key (value)) engine=InnoDB;
create table login_log (id bigint not null auto_increment, ip_address varchar(255), login varchar(255), login_date datetime, primary key (id)) engine=InnoDB;
create table measure (value bigint not null auto_increment, label varchar(255), primary key (value)) engine=InnoDB;
create table party_data (id bigint not null auto_increment, city varchar(255), id_name varchar(255), id_value varchar(255), invoice_from_panel_id bigint, name varchar(255), party_id integer, street varchar(255), primary key (id)) engine=InnoDB;
create table summary_data (id bigint not null auto_increment, bank_acc varchar(255), brutto_amount decimal(19,2), comments longtext, invoice_from_panel_id bigint, netto_amount decimal(19,2), paid decimal(19,2), paid_day datetime, payment_day datetime, payment_option_id_value varchar(255), status_id_value varchar(255), vat_amount decimal(19,2), vat_exemption_label_np varchar(255), vat_exemption_label_zw varchar(255), vat_exemption_value_np longtext, vat_exemption_value_zw longtext, primary key (id)) engine=InnoDB;
create table swap (id bigint not null auto_increment, customer_data longblob, database_name varchar(255), declaration_data longblob, primary key (id)) engine=InnoDB;
create table user_data (id bigint not null auto_increment, db_id varchar(255), password varchar(255), role varchar(255), user_name varchar(255), primary key (id)) engine=InnoDB;
create table vat_type (value bigint not null auto_increment, label varchar(255), primary key (value)) engine=InnoDB;
create table web_company_data (id bigint not null auto_increment, company_name varchar(255), db_name varchar(255), officeid varchar(255), random_id varchar(255), primary key (id)) engine=InnoDB;
create table web_contactor (id bigint not null auto_increment, city varchar(255), email varchar(255), local_number varchar(255), name varchar(255), nip varchar(255), phone_number varchar(255), regon varchar(255), street varchar(255), street_number varchar(255), zip_code varchar(255), primary key (id)) engine=InnoDB;
create table web_invoice (id bigint not null auto_increment, brutto decimal(19,2), contactor_id bigint, contactor_name varchar(255), currency varchar(255), doc_name varchar(255), doc_type bigint, issue_date datetime, netto decimal(19,2), payment_date datetime, payment_status decimal(19,2), vat decimal(19,2), primary key (id)) engine=InnoDB;
alter table cdn_bnk_rachunki add constraint FKkpvyjmdtkgc01geiqo358qaf1 foreign key (bra_bnaid) references cdn_bnk_nazwy (bna_bna_id);
alter table cdn_dekl_wydr add constraint FK9ocaneu20kq228gqu69r3uisa foreign key (dkw_dknid) references cdn_dekl_nag (dkn_dknid);
alter table invoice_commodity add constraint FKmht0mog8jp93wkdnuqm5lc66l foreign key (invoice_from_panel_id) references invoice_from_panel (id);
alter table invoice_from_panel add constraint FKnvqst8hthbdcyyev723xnf9gf foreign key (correction_id) references invoice_from_panel (id);
alter table invoice_vat_table add constraint FKq4u63n265l7r1j387omyo2b2i foreign key (invoice_from_panel_id) references invoice_from_panel (id);
alter table party_data add constraint FKhqoy4ynf0bmj3di8fqecmy750 foreign key (invoice_from_panel_id) references invoice_from_panel (id);
alter table summary_data add constraint FK2y7wix39hqv2jjxflxmwnkcg3 foreign key (invoice_from_panel_id) references invoice_from_panel (id);
