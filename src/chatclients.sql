drop database chatclients;


CREATE DATABASE chatclients;

use chatclients;


CREATE TABLE IF NOT EXISTS chatclients.client (
  id_client VARCHAR(25) NOT NULL,
  nombre VARCHAR(45) NOT NULL,
  password VARCHAR(45) NULL,
  nickname VARCHAR(45) NULL,
  isonline BOOL NULL,
  PRIMARY KEY (id_client));




INSERT INTO client(id_client,nombre,password,nickname,isonline)
VALUES('111','test','test','test',true);