Create table  users(username varchar(50)not null primary key ,password varchar(40)not null,enabled boolean not null);
Create table  authority (username varchar(50) not null ,authority varchar(50)not null ,constraint  fk_authorities_users foreign key (username) ref);
create unique index ix_auth_username on authority(username,authority);