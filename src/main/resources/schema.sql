create table cdn_dekl_nag (dkn_dknid bigint not null, dkn_finalna integer, dkn_kwota decimal(19,2), dkn_ts_mod date, dkn_praid integer, dkn_rok_miesiac integer, dkn_typ_deklar integer, primary key (dkn_dknid)) engine=InnoDB;
create table cdn_dekl_wydr (dkw_dkwid bigint not null, dkw_opis varchar(255), dkw_dknid bigint, dkw_rubryka integer, dkw_wartosc varchar(255), primary key (dkw_dkwid)) engine=InnoDB;
create table cdn_firma (fir_firid bigint not null, fir_wartosc varchar(255), fir_numer integer, fir_opis varchar(255), primary key (fir_firid)) engine=InnoDB;
create table cdn_bnk_nazwy (bna_bna_id bigint not null, bna_miasto varchar(255), bna_nr_lokalu varchar(255), bna_nazwa1 varchar(255), bna_nazwa2 varchar(255), bna_ulica varchar(255), bna_nr_domu varchar(255), bna_kod_pocztowy varchar(255), primary key (bna_bna_id)) engine=InnoDB;
create table cdn_bnk_rachunki (bra_braid bigint not null, bra_rachunek_nr varchar(255), bra_bnaid integer, primary key (bra_braid)) engine=InnoDB;
