SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema dbo_veterinaria
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `dbo_veterinaria` DEFAULT CHARACTER SET utf8mb4 ;
USE `dbo_veterinaria` ;

-- -----------------------------------------------------
-- Table `dbo_veterinaria`.`PERSONA`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `dbo_veterinaria`.`PERSONA` (
  `idPERSONA` INT NOT NULL AUTO_INCREMENT,
  `nombres` VARCHAR(100) NOT NULL,
  `apellidos` VARCHAR(100) NOT NULL,
  `fecha_nacimiento` DATE NOT NULL,
  `documento` VARCHAR(20) NOT NULL,
  `telefono` VARCHAR(20) NOT NULL,
  `correo` VARCHAR(75) NOT NULL,
  PRIMARY KEY (`idPERSONA`),
  UNIQUE INDEX `correo_UNIQUE` (`correo` ASC),
  UNIQUE INDEX `documento_UNIQUE` (`documento` ASC))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `dbo_veterinaria`.`PROPIETARIO`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `dbo_veterinaria`.`PROPIETARIO` (
  `idPROPIETARIO` INT NOT NULL AUTO_INCREMENT,
  `PERSONA_idPERSONA` INT NOT NULL,
  `fecha_registro` DATE NOT NULL,
  `hora_registro` TIME NOT NULL,
  PRIMARY KEY (`idPROPIETARIO`),
  UNIQUE INDEX `uq_PROPIETARIO_PERSONA` (`PERSONA_idPERSONA` ASC),
  CONSTRAINT `fk_PROPIETARIO_PERSONA`
    FOREIGN KEY (`PERSONA_idPERSONA`)
    REFERENCES `dbo_veterinaria`.`PERSONA` (`idPERSONA`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `dbo_veterinaria`.`VETERINARIO`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `dbo_veterinaria`.`VETERINARIO` (
  `idVETERINARIO` INT NOT NULL AUTO_INCREMENT,
  `PERSONA_idPERSONA` INT NOT NULL,
  `especialidad` VARCHAR(50) NOT NULL,
  `numero_licencia` VARCHAR(30) NOT NULL,
  PRIMARY KEY (`idVETERINARIO`),
  UNIQUE INDEX `uq_VETERINARIO_PERSONA` (`PERSONA_idPERSONA` ASC),
  UNIQUE INDEX `numero_licencia_UNIQUE` (`numero_licencia` ASC),
  CONSTRAINT `fk_VETERINARIO_PERSONA1`
    FOREIGN KEY (`PERSONA_idPERSONA`)
    REFERENCES `dbo_veterinaria`.`PERSONA` (`idPERSONA`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `dbo_veterinaria`.`ESPECIE`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `dbo_veterinaria`.`ESPECIE` (
  `idESPECIE` INT NOT NULL AUTO_INCREMENT,
  `nombre` VARCHAR(50) NOT NULL,
  PRIMARY KEY (`idESPECIE`),
  UNIQUE INDEX `nombre_UNIQUE` (`nombre` ASC))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `dbo_veterinaria`.`RAZA`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `dbo_veterinaria`.`RAZA` (
  `idRAZA` INT NOT NULL AUTO_INCREMENT,
  `ESPECIE_idESPECIE` INT NOT NULL,
  `nombre` VARCHAR(80) NOT NULL,
  PRIMARY KEY (`idRAZA`),
  UNIQUE INDEX `uq_RAZA_especie_nombre` (`ESPECIE_idESPECIE` ASC, `nombre` ASC),
  UNIQUE INDEX `uq_RAZA_id_especie` (`idRAZA` ASC, `ESPECIE_idESPECIE` ASC),
  CONSTRAINT `fk_RAZA_ESPECIE1`
    FOREIGN KEY (`ESPECIE_idESPECIE`)
    REFERENCES `dbo_veterinaria`.`ESPECIE` (`idESPECIE`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `dbo_veterinaria`.`ANIMAL`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `dbo_veterinaria`.`ANIMAL` (
  `idANIMAL` INT NOT NULL AUTO_INCREMENT,
  `PROPIETARIO_idPROPIETARIO` INT NOT NULL,
  `ESPECIE_idESPECIE` INT NOT NULL,
  `RAZA_idRAZA` INT NOT NULL,
  `nombre` VARCHAR(50) NOT NULL,
  `fecha_nacimiento` DATE NOT NULL,
  `sexo` ENUM('Macho', 'Hembra') NOT NULL,
  `peso` DECIMAL(6,2) NOT NULL,
  `color` VARCHAR(25) NOT NULL,
  `estado` ENUM('activo', 'inactivo') NOT NULL DEFAULT 'activo',
  `fecha_ingreso` DATE NOT NULL,
  `hora_ingreso` TIME NOT NULL,
  PRIMARY KEY (`idANIMAL`),
  INDEX `fk_ANIMAL_PROPIETARIO1_idx` (`PROPIETARIO_idPROPIETARIO` ASC),
  INDEX `fk_ANIMAL_ESPECIE1_idx` (`ESPECIE_idESPECIE` ASC),
  INDEX `fk_ANIMAL_RAZA_ESPECIE_idx` (`RAZA_idRAZA` ASC, `ESPECIE_idESPECIE` ASC),
  CONSTRAINT `fk_ANIMAL_PROPIETARIO1`
    FOREIGN KEY (`PROPIETARIO_idPROPIETARIO`)
    REFERENCES `dbo_veterinaria`.`PROPIETARIO` (`idPROPIETARIO`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_ANIMAL_ESPECIE1`
    FOREIGN KEY (`ESPECIE_idESPECIE`)
    REFERENCES `dbo_veterinaria`.`ESPECIE` (`idESPECIE`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_ANIMAL_RAZA_ESPECIE`
    FOREIGN KEY (`RAZA_idRAZA` , `ESPECIE_idESPECIE`)
    REFERENCES `dbo_veterinaria`.`RAZA` (`idRAZA` , `ESPECIE_idESPECIE`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `dbo_veterinaria`.`HISTORIAL_MEDICO`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `dbo_veterinaria`.`HISTORIAL_MEDICO` (
  `idHISTORIAL_MEDICO` INT NOT NULL AUTO_INCREMENT,
  `ANIMAL_idANIMAL` INT NOT NULL,
  `fecha_creacion` DATE NOT NULL,
  `observaciones` TEXT NULL DEFAULT NULL,
  PRIMARY KEY (`idHISTORIAL_MEDICO`),
  UNIQUE INDEX `uq_HISTORIAL_ANIMAL` (`ANIMAL_idANIMAL` ASC),
  CONSTRAINT `fk_HISTORIAL_MEDICO_ANIMAL1`
    FOREIGN KEY (`ANIMAL_idANIMAL`)
    REFERENCES `dbo_veterinaria`.`ANIMAL` (`idANIMAL`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `dbo_veterinaria`.`CITA`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `dbo_veterinaria`.`CITA` (
  `idCITA` INT NOT NULL AUTO_INCREMENT,
  `ANIMAL_idANIMAL` INT NOT NULL,
  `VETERINARIO_idVETERINARIO` INT NOT NULL,
  `fecha` DATE NOT NULL,
  `hora` TIME NOT NULL,
  `motivo` VARCHAR(1000) NOT NULL,
  `estado` ENUM('programada', 'atendida', 'cancelada') NOT NULL,
  PRIMARY KEY (`idCITA`),
  INDEX `fk_CITA_ANIMAL1_idx` (`ANIMAL_idANIMAL` ASC),
  UNIQUE INDEX `uq_cita_vet_fecha_hora` (`VETERINARIO_idVETERINARIO` ASC, `fecha` ASC, `hora` ASC),
  CONSTRAINT `fk_CITA_ANIMAL1`
    FOREIGN KEY (`ANIMAL_idANIMAL`)
    REFERENCES `dbo_veterinaria`.`ANIMAL` (`idANIMAL`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_CITA_VETERINARIO1`
    FOREIGN KEY (`VETERINARIO_idVETERINARIO`)
    REFERENCES `dbo_veterinaria`.`VETERINARIO` (`idVETERINARIO`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


SET FOREIGN_KEY_CHECKS = 0;

-- -----------------------------------------------------
-- Table `dbo_veterinaria`.`CONSULTA`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `dbo_veterinaria`.`CONSULTA` (
  `idCONSULTA` INT NOT NULL AUTO_INCREMENT,
  `HISTORIAL_MEDICO_idHISTORIAL_MEDICO` INT NOT NULL,
  `CITA_idCITA` INT NULL DEFAULT NULL,
  `VETERINARIO_idVETERINARIO` INT NOT NULL,
  `fecha_consulta` DATE NOT NULL DEFAULT (CURRENT_DATE),
  `hora_consulta` TIME NOT NULL DEFAULT (CURRENT_TIME),
  `sintomas` VARCHAR(500) NOT NULL,
  `diagnostico` VARCHAR(500) NOT NULL,
  `tratamiento` VARCHAR(500) NOT NULL,
  `observaciones` VARCHAR(1000) NULL DEFAULT NULL,
  PRIMARY KEY (`idCONSULTA`),
  INDEX `fk_CONSULTA_HISTORIAL_MEDICO1_idx` (`HISTORIAL_MEDICO_idHISTORIAL_MEDICO` ASC),
  INDEX `fk_CONSULTA_VETERINARIO1_idx` (`VETERINARIO_idVETERINARIO` ASC),
  UNIQUE INDEX `uq_consulta_cita` (`CITA_idCITA` ASC),
  CONSTRAINT `fk_CONSULTA_HISTORIAL_MEDICO1`
    FOREIGN KEY (`HISTORIAL_MEDICO_idHISTORIAL_MEDICO`)
    REFERENCES `dbo_veterinaria`.`HISTORIAL_MEDICO` (`idHISTORIAL_MEDICO`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_CONSULTA_CITA1`
    FOREIGN KEY (`CITA_idCITA`)
    REFERENCES `dbo_veterinaria`.`CITA` (`idCITA`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_CONSULTA_VETERINARIO1`
    FOREIGN KEY (`VETERINARIO_idVETERINARIO`)
    REFERENCES `dbo_veterinaria`.`VETERINARIO` (`idVETERINARIO`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
) ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `dbo_veterinaria`.`VACUNAS`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `dbo_veterinaria`.`VACUNAS` (
  `idVACUNAS` INT NOT NULL AUTO_INCREMENT,
  `HISTORIAL_MEDICO_idHISTORIAL_MEDICO` INT NOT NULL,
  `CONSULTA_idCONSULTA` INT NULL DEFAULT NULL,
  `nombre` VARCHAR(150) NOT NULL,
  `dosis` DECIMAL(8,3) NOT NULL,
  `fecha_aplicacion` DATE NOT NULL,
  `proxima_fecha` DATE NULL DEFAULT NULL,
  PRIMARY KEY (`idVACUNAS`),
  INDEX `fk_VACUNAS_HISTORIAL_MEDICO1_idx` (`HISTORIAL_MEDICO_idHISTORIAL_MEDICO` ASC),
  INDEX `fk_VACUNAS_CONSULTA1_idx` (`CONSULTA_idCONSULTA` ASC),
  CONSTRAINT `fk_VACUNAS_HISTORIAL_MEDICO1`
    FOREIGN KEY (`HISTORIAL_MEDICO_idHISTORIAL_MEDICO`)
    REFERENCES `dbo_veterinaria`.`HISTORIAL_MEDICO` (`idHISTORIAL_MEDICO`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_VACUNAS_CONSULTA1`
    FOREIGN KEY (`CONSULTA_idCONSULTA`)
    REFERENCES `dbo_veterinaria`.`CONSULTA` (`idCONSULTA`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
) ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `dbo_veterinaria`.`MEDICAMENTO`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `dbo_veterinaria`.`MEDICAMENTO` (
  `idMEDICAMENTO` INT NOT NULL AUTO_INCREMENT,
  `nombre` VARCHAR(150) NOT NULL,
  `presentacion` VARCHAR(100) NULL DEFAULT NULL,
  PRIMARY KEY (`idMEDICAMENTO`),
  UNIQUE INDEX `uq_MEDICAMENTO_nombre_presentacion` (`nombre` ASC, `presentacion` ASC))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `dbo_veterinaria`.`CONSULTA_MEDICAMENTO`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `dbo_veterinaria`.`CONSULTA_MEDICAMENTO` (
  `idCONSULTA_MEDICAMENTO` INT NOT NULL AUTO_INCREMENT,
  `CONSULTA_idCONSULTA` INT NOT NULL,
  `MEDICAMENTO_idMEDICAMENTO` INT NOT NULL,
  `dosis` DECIMAL(8,3) NOT NULL,
  `frecuencia` VARCHAR(150) NOT NULL,
  `duracion` VARCHAR(60) NOT NULL,
  `indicaciones` VARCHAR(1000) NULL DEFAULT NULL,
  PRIMARY KEY (`idCONSULTA_MEDICAMENTO`),
  INDEX `fk_CONSULTA_MEDICAMENTO_CONSULTA1_idx` (`CONSULTA_idCONSULTA` ASC),
  INDEX `fk_CONSULTA_MEDICAMENTO_MEDICAMENTO1_idx` (`MEDICAMENTO_idMEDICAMENTO` ASC),
  CONSTRAINT `fk_CONSULTA_MEDICAMENTO_CONSULTA1`
    FOREIGN KEY (`CONSULTA_idCONSULTA`)
    REFERENCES `dbo_veterinaria`.`CONSULTA` (`idCONSULTA`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_CONSULTA_MEDICAMENTO_MEDICAMENTO1`
    FOREIGN KEY (`MEDICAMENTO_idMEDICAMENTO`)
    REFERENCES `dbo_veterinaria`.`MEDICAMENTO` (`idMEDICAMENTO`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
) ENGINE = InnoDB;

SET FOREIGN_KEY_CHECKS = 1;


-- -----------------------------------------------------
-- Table `dbo_veterinaria`.`USUARIO`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `dbo_veterinaria`.`USUARIO` (
  `idUSUARIO` INT NOT NULL AUTO_INCREMENT,
  `usuario` VARCHAR(50) NOT NULL,
  `contrasena` VARCHAR(100) NOT NULL,
  `rol` VARCHAR(20) NOT NULL,
  `PERSONA_idPERSONA` INT NOT NULL,
  PRIMARY KEY (`idUSUARIO`),
  UNIQUE INDEX `usuario_UNIQUE` (`usuario` ASC),
  INDEX `fk_USUARIO_PERSONA1_idx` (`PERSONA_idPERSONA` ASC),
  CONSTRAINT `fk_USUARIO_PERSONA1`
    FOREIGN KEY (`PERSONA_idPERSONA`)
    REFERENCES `dbo_veterinaria`.`PERSONA` (`idPERSONA`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;