Create table  users(username varchar(50)not null primary key ,password varchar(40)not null,enabled boolean not null);
Create table  authorities (username varchar(50) not null ,authority varchar(50)not null ,constraint  fk_authority_users foreign key (username) ref);
create unique index ix_auth_username on authorities(username,authority);

insert ignore into `users` values('user','{noop}EasyBytes@1234','1');
insert ignore into `authorities` values('user','read');

insert ignore into `users` values('admin','{bcrypt}U2FsdGVkX19WrFurHWaAi6Ld0t1SwQLNRQKow0p/Mm8=','1');
insert ignore into `authorities` values('admin','admin');

    create table `customer`(
        `id`int not null auto_increment,
        `email` varchar(45) not null ,
        `pwd` varchar(200) not null ,
        `role` varchar(45) not null ,
        primary key (`id`)
    );

insert into `customer`(`email`,`pwd`,`role`)values ('liza@example.com','{noop}EasyBytes@1234','read');
insert into `customer`(`email`,`pwd`,`role`) values ('admin@example.com','{bcrypt}U2FsdGVkX19WrFurHWaAi6Ld0t1SwQLNRQKow0p/Mm8=','admin');