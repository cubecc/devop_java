drop table web_user;

CREATE TABLE web_user (
    id int NOT NULL AUTO_INCREMENT,
    login varchar(255) NOT NULL,
    password varchar(255),
    username varchar(255),
    PRIMARY KEY (id)
); 


create unique index login on `devop`.`web_user`(`login`);
-- create unique index `PRIMARY` on `devop`.`web_users`(`id`);

insert into web_user (login,password,username) values ('user1','password','Chan Tai Man');
insert into web_user (login,password,username) values ('user2','password','Wong Siu Ming');


select * from web_user