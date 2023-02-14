create table if not exists companies (
	id int8 not null default nextval('companies_seq'),
	name varchar(255) not null,
	phone varchar(24) not null,
	constraint "companies_primary_key" primary key (id),
	unique (name)
);

CREATE unique index if not exists companies_name_idx ON companies(name);
CREATE index if not exists companies_name_phone_idx ON companies(name, phone);

create table if not exists addresses (
	id int8 not null default nextval('addresses_seq'),
	city varchar(255) not null,
	state varchar(64) not null,
	house varchar(64) not null,
	flat_number varchar(64) not null,
	constraint "addresses_primary_key" primary key (id),
	unique (city,state,house,flat_number)
);
CREATE unique index if not exists addresses_unique_idx ON addresses(city,state,house,flat_number);

create table if not exists companies_addresses (
	companies_id int8 not null,
	addresses_id int8 not null,
	constraint "companies_addresses_pk" primary key (companies_id,addresses_id),
	unique (companies_id,addresses_id),
	constraint companies_fk foreign key (companies_id) REFERENCES companies(id) on delete cascade,
	constraint caddresses_fk foreign key (addresses_id) REFERENCES addresses(id) on delete cascade
);
CREATE unique index if not exists companies_addresses_unq_idx ON companies_addresses(companies_id,addresses_id);

create table if not exists users (
	id int8 not null default nextval('users_seq'),
	user_name varchar(128) not null,
	email_address varchar(128) not null,
	first_name varchar(128) not null,
	second_name varchar(128) not null,
	last_name varchar(128) not null,
	password varchar(128) not null,
	gender varchar(128),
	birth_date date,
	city varchar(48),
	image_ref varchar (2048),
	phone_number varchar(24) not null,
	about varchar(255),
	active boolean,
	constraint "users_primary_key" primary key (id),
	unique (user_name, email_address)
);
CREATE unique index if not exists users_unique_idx ON users(user_name, email_address);

create table if not exists user_subs (
	id int8 not null default nextval('subs_seq'),
	follower_id int8 not null,
	followed_id int8 not null,
	active boolean,
	constraint "user_subs_pk" primary key (id),
	unique (follower_id,followed_id),
	constraint follower_fk foreign key (follower_id) REFERENCES users(id) on delete cascade,
	constraint followed_fk foreign key (followed_id) REFERENCES users(id) on delete cascade
);
CREATE unique index if not exists user_subs_unq_idx ON user_subs(follower_id,followed_id);

create table if not exists messages (
	id int8 not null default nextval('messages_seq'),
	message_date date not null,
	title varchar(255) not null,
	description varchar (2048),
	file_name varchar(2048),
	user_id int8,
	constraint "messages_pk" primary key (id),
	unique (message_date,title,user_id),
	constraint messages_fk foreign key (user_id) REFERENCES users(id) on delete cascade
);
CREATE unique index if not exists messages_unq_idx ON messages(message_date,title,user_id);