--changeset AUTHOR:segorov
grant all privileges on database users to myuser;
create schema  if not exists users_schema authorization myuser;

create sequence if not exists users_schema.companies_seq
start 1
increment 1
cache 1
;

create sequence if not exists users_schema.addresses_seq
start 1
increment 1
cache 1
;

create sequence if not exists users_schema.users_seq
start 1
increment 1
cache 1
;

create sequence if not exists users_schema.messages_seq
start 1
increment 1
cache 1
;

create sequence if not exists users_schema.subs_seq
start 1
increment 1
cache 1
;



