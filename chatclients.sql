-- MySQL Script generated by MySQL Workbench
-- Mon Nov  2 12:38:06 2020
-- Model: New Model    Version: 1.0
-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema chatclients
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema chatclients
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `chatclients` DEFAULT CHARACTER SET utf8 ;
USE `chatclients` ;

-- -----------------------------------------------------
-- Table `chatclients`.`client`
-- -----------------------------------------------------

drop database chatclients;

CREATE DATABASE chatclients;

use chatclients;


CREATE TABLE IF NOT EXISTS `chatclients`.`client` (
  `id_client` VARCHAR(25) NOT NULL,
  `nombre` VARCHAR(45) NOT NULL,
  `password` VARCHAR(45) NULL,
  `nickname` VARCHAR(45) NULL,
  `isonline` BOOL NULL,
  PRIMARY KEY (`id_client`))
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;