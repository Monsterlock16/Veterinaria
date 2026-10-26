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
-- Table `dbo_veterinaria`.`ESPECIALIDAD`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `dbo_veterinaria`.`ESPECIALIDAD` (
  `idESPECIALIDAD` INT NOT NULL AUTO_INCREMENT,
  `nombre` VARCHAR(50) NOT NULL,
  PRIMARY KEY (`idESPECIALIDAD`),
  UNIQUE INDEX `nombre_UNIQUE` (`nombre` ASC))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `dbo_veterinaria`.`VETERINARIO` (Relación 1:N con ESPECIALIDAD)
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `dbo_veterinaria`.`VETERINARIO` (
  `idVETERINARIO` INT NOT NULL AUTO_INCREMENT,
  `PERSONA_idPERSONA` INT NOT NULL,
  `ESPECIALIDAD_idESPECIALIDAD` INT NOT NULL,
  `numero_licencia` VARCHAR(30) NOT NULL,
  PRIMARY KEY (`idVETERINARIO`),
  UNIQUE INDEX `uq_VETERINARIO_PERSONA` (`PERSONA_idPERSONA` ASC),
  UNIQUE INDEX `numero_licencia_UNIQUE` (`numero_licencia` ASC),
  INDEX `fk_VETERINARIO_ESPECIALIDAD1_idx` (`ESPECIALIDAD_idESPECIALIDAD` ASC),
  CONSTRAINT `fk_VETERINARIO_PERSONA1`
    FOREIGN KEY (`PERSONA_idPERSONA`)
    REFERENCES `dbo_veterinaria`.`PERSONA` (`idPERSONA`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_VETERINARIO_ESPECIALIDAD1`
    FOREIGN KEY (`ESPECIALIDAD_idESPECIALIDAD`)
    REFERENCES `dbo_veterinaria`.`ESPECIALIDAD` (`idESPECIALIDAD`)
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
  `fecha_consulta` DATE NOT NULL,
  `hora_consulta` TIME NOT NULL,
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

-- =====================================================
-- Actualización de la base de datos (datos e índices)
-- =====================================================
USE dbo_veterinaria;

-- Cargar ESPECIALIDADES
INSERT IGNORE INTO especialidad (nombre) VALUES
  ('Medicina general'),
  ('Medicina interna'),
  ('Cirugía general'),
  ('Cirugía de tejidos blandos'),
  ('Ortopedia y traumatología'),
  ('Dermatología'),
  ('Cardiología'),
  ('Oftalmología'),
  ('Odontología veterinaria'),
  ('Oncología'),
  ('Neurología'),
  ('Nefrología y urología'),
  ('Gastroenterología'),
  ('Endocrinología'),
  ('Diagnóstico por imágenes'),
  ('Anestesiología y manejo del dolor'),
  ('Urgencias y cuidados intensivos'),
  ('Reproducción y obstetricia'),
  ('Medicina felina'),
  ('Animales exóticos'),
  ('Medicina aviar'),
  ('Medicina equina'),
  ('Rehabilitación y fisioterapia'),
  ('Etología (comportamiento)'),
  ('Nutrición clínica'),
  ('Patología clínica y laboratorio');

-- CITA: ajuste de índice y estados
ALTER TABLE cita ADD INDEX IF NOT EXISTS idx_cita_vet_fecha_hora (VETERINARIO_idVETERINARIO, fecha, hora);
ALTER TABLE cita DROP INDEX IF EXISTS uq_cita_vet_fecha_hora;
ALTER TABLE cita MODIFY estado ENUM('programada','atendida','cancelada','pendiente') NOT NULL;

-- Cargar ESPECIES
INSERT IGNORE INTO especie (nombre) VALUES
  ('Canino'),
  ('Felino'),
  ('Ave'),
  ('Roedor'),
  ('Conejo'),
  ('Reptil'),
  ('Equino');

-- Cargar RAZAS
INSERT IGNORE INTO raza (ESPECIE_idESPECIE, nombre)
SELECT e.idESPECIE, r.nombre
FROM especie e
JOIN (
  SELECT 'Canino' AS especie, 'Criollo' AS nombre UNION ALL
  SELECT 'Canino', 'Buldog' UNION ALL
  SELECT 'Canino', 'Labrador' UNION ALL
  SELECT 'Canino', 'Golden Retriever' UNION ALL
  SELECT 'Canino', 'Pastor Alemán' UNION ALL
  SELECT 'Canino', 'Poodle' UNION ALL
  SELECT 'Canino', 'Chihuahua' UNION ALL
  SELECT 'Canino', 'Beagle' UNION ALL
  SELECT 'Canino', 'Rottweiler' UNION ALL
  SELECT 'Canino', 'Pitbull' UNION ALL
  SELECT 'Canino', 'Schnauzer' UNION ALL
  SELECT 'Canino', 'Husky Siberiano' UNION ALL
  SELECT 'Canino', 'Shih Tzu' UNION ALL
  SELECT 'Canino', 'Yorkshire Terrier' UNION ALL
  SELECT 'Canino', 'Pug' UNION ALL
  SELECT 'Canino', 'Cocker Spaniel' UNION ALL
  SELECT 'Canino', 'Dálmata' UNION ALL
  SELECT 'Felino', 'Criollo' UNION ALL
  SELECT 'Felino', 'Siamés' UNION ALL
  SELECT 'Felino', 'Persa' UNION ALL
  SELECT 'Felino', 'Angora' UNION ALL
  SELECT 'Felino', 'Maine Coon' UNION ALL
  SELECT 'Felino', 'Bengalí' UNION ALL
  SELECT 'Felino', 'Ragdoll' UNION ALL
  SELECT 'Felino', 'Azul Ruso' UNION ALL
  SELECT 'Felino', 'Sphynx' UNION ALL
  SELECT 'Felino', 'Británico de Pelo Corto' UNION ALL
  SELECT 'Ave', 'Canario' UNION ALL
  SELECT 'Ave', 'Periquito' UNION ALL
  SELECT 'Ave', 'Loro' UNION ALL
  SELECT 'Ave', 'Cotorra' UNION ALL
  SELECT 'Ave', 'Cacatúa' UNION ALL
  SELECT 'Ave', 'Agapornis' UNION ALL
  SELECT 'Ave', 'Ninfa' UNION ALL
  SELECT 'Ave', 'Gallina' UNION ALL
  SELECT 'Ave', 'Paloma' UNION ALL
  SELECT 'Roedor', 'Hámster' UNION ALL
  SELECT 'Roedor', 'Cobayo' UNION ALL
  SELECT 'Roedor', 'Rata' UNION ALL
  SELECT 'Roedor', 'Ratón' UNION ALL
  SELECT 'Roedor', 'Jerbo' UNION ALL
  SELECT 'Roedor', 'Chinchilla' UNION ALL
  SELECT 'Roedor', 'Ardilla' UNION ALL
  SELECT 'Conejo', 'Cabeza de León' UNION ALL
  SELECT 'Conejo', 'Mini Lop' UNION ALL
  SELECT 'Conejo', 'Holandés' UNION ALL
  SELECT 'Conejo', 'Enano' UNION ALL
  SELECT 'Conejo', 'Angora' UNION ALL
  SELECT 'Conejo', 'Rex' UNION ALL
  SELECT 'Conejo', 'Gigante de Flandes' UNION ALL
  SELECT 'Reptil', 'Tortuga' UNION ALL
  SELECT 'Reptil', 'Iguana' UNION ALL
  SELECT 'Reptil', 'Gecko' UNION ALL
  SELECT 'Reptil', 'Serpiente' UNION ALL
  SELECT 'Reptil', 'Camaleón' UNION ALL
  SELECT 'Reptil', 'Dragón Barbudo' UNION ALL
  SELECT 'Equino', 'Criollo' UNION ALL
  SELECT 'Equino', 'Paso Fino' UNION ALL
  SELECT 'Equino', 'Pura Sangre' UNION ALL
  SELECT 'Equino', 'Cuarto de Milla' UNION ALL
  SELECT 'Equino', 'Andaluz' UNION ALL
  SELECT 'Equino', 'Percherón' UNION ALL
  SELECT 'Equino', 'Poni'
) r ON r.especie = e.nombre;

SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;