-- database passowrd for admin user "root" is welcome1

create database HeartsDB;
-- drop database HeartsDB;
show databases;

grant usage on *.* to HeartsUser@localhost identified by 'Hearts4MyAdmin!]';
--SELECT User FROM mysql.user;
--DROP USER 'HeartsUser'@'localhost';
 
grant all privileges on HeartsDB.* to HeartsUser@localhost;

$ mysql -u HeartsUser -p 'Hearts4MyAdmin!]' HeartsDB;

use HeartsDB;

/*All Players who wants to play the game gets stored in PLAYER table*/
create table player (
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

create table game (
   game_id BIGINT NOT NULL,
   player_id BIGINT NOT NULL,
   position BIGINT NOT NULL,
   points BIGINT DEFAULT 0,
   PRIMARY KEY (game_id, player_id, position),
   CONSTRAINT fk_player FOREIGN KEY (player_id) REFERENCES player (player_id)
);

create table game_status (
   game_id BIGINT NOT NULL,
   status  VARCHAR(1) NOT NULL default 'G',
   PRIMARY KEY (game_id, status),
   CONSTRAINT fk_game_status FOREIGN KEY (game_id) REFERENCES game (game_id)
);

create table player_cards (
   game_id BIGINT NOT NULL,
   hand_id BIGINT NOT NULL,
   player_id BIGINT NOT NULL,
   card_id BIGINT NOT NULL,
   status VARCHAR(1) NOT NULL,
   PRIMARY KEY (game_id, hand_id, player_id),
   CONSTRAINT fk_game_cards FOREIGN KEY (game_id) REFERENCES game (game_id),
   CONSTRAINT fk_player_cards FOREIGN KEY (player_id) REFERENCES player (player_id)  
);


create table game_moves (
   game_id BIGINT NOT NULL,
   hand_id BIGINT NOT NULL,
   round_id BIGINT NOT NULL,
   player_id BIGINT NOT NULL,
   card_id BIGINT NOT NULL,
   score BIGINT NOT NULL,
   PRIMARY KEY (game_id, hand_id, round_id, player_id),
   CONSTRAINT fk_game_moves FOREIGN KEY (game_id) REFERENCES game (game_id),
   CONSTRAINT fk_player_moves FOREIGN KEY (player_id) REFERENCES player (player_id)  
);


show tables;

