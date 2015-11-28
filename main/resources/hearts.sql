-- database passowrd for admin user "root" is welcome1

create database HeartsDB;
-- drop database HeartsDB;
show databases;

grant usage on *.* to HeartsUser@localhost identified by 'Hearts4MyAdmin!]';
create user 'HeartsUser'@'%' identified by 'Hearts4MyAdmin!]';
--SELECT User FROM mysql.user;
--DROP USER 'HeartsUser'@'localhost';

grant all privileges on HeartsDB.* to HeartsUser@localhost;
grant all privileges on HeartsDB.* to 'HeartsUser'@'%';

mysql -u HeartsUser -p 'Hearts4MyAdmin!]' HeartsDB;

use HeartsDB;

/*All Players who wants to play the game gets stored in PLAYER table*/
create table PLAYER (
   player_id BIGINT NOT NULL AUTO_INCREMENT,
   sso_id VARCHAR(30) NOT NULL,
   password VARCHAR(100) NOT NULL,
   first_name VARCHAR(30) NOT NULL,
   last_name  VARCHAR(30) NOT NULL,
   email VARCHAR(30) NOT NULL,
   state VARCHAR(30) NOT NULL,  
   PRIMARY KEY (player_id),
   UNIQUE (sso_id)
);

create table GAME (
   game_id BIGINT NOT NULL,
   status  VARCHAR(1) NOT NULL default 'W',
   PRIMARY KEY (game_id)
);

create table GAME_PLAYER (
   game_id BIGINT NOT NULL,
   player_id BIGINT NOT NULL,
   position BIGINT NOT NULL,
   score BIGINT DEFAULT 0,
   messages VARCHAR(1024),
   PRIMARY KEY (game_id, player_id),
   CONSTRAINT fk_pg_player FOREIGN KEY (player_id) REFERENCES PLAYER (player_id),
   CONSTRAINT fk_pg_game FOREIGN KEY (game_id) REFERENCES GAME (game_id)
);

create table GAME_MOVE (
   player_id BIGINT NOT NULL,
   game_id BIGINT NOT NULL,
   hand_id BIGINT NOT NULL,
   card_id VARCHAR(10) NOT NULL,
   round_id BIGINT,
   time_stamp TIMESTAMP,
   PRIMARY KEY (player_id, game_id, hand_id, card_id)
);

select * from GAME_MOVE;
truncate table GAME_MOVE;
delete from GAME_PLAYER where game_id = 10;
delete from GAME where game_id = 10;
