--changeset AUTHOR:segorov
create sequence if not exists companies_seq
start 1
increment 1
cache 1
;

create sequence if not exists addresses_seq
start 1
increment 1
cache 1
;

create sequence if not exists users_seq
start 1
increment 1
cache 1
;

create sequence if not exists messages_seq
start 1
increment 1
cache 1
;

create sequence if not exists subs_seq
start 1
increment 1
cache 1
;



