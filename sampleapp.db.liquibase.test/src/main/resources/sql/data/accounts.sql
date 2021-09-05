--liquibase formatted sql
--changeset sb:example_sampleapp_accounts
insert into sampleapp_accounts (username) values ('jod');
insert into sampleapp_accounts (username) values ('jad');
