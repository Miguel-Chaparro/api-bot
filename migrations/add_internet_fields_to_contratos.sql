-- Migration: Add internet type fields to dommapi.contratos_servicio
-- Date: 2026-01-15
-- Description: Adds tipo_internet, puerto, caja, and nodo columns to support different internet service types

ALTER TABLE `dommapi`.`contratos_servicio`
ADD COLUMN `tipo_internet` ENUM('sin_servicio', 'fibra', 'antena') DEFAULT 'sin_servicio' COMMENT 'Tipo de servicio de internet: sin_servicio, fibra o antena',
ADD COLUMN `puerto` VARCHAR(50) NULL COMMENT 'si es fibra se agrega el puerto, de lo contrario no es requerido el campo',
ADD COLUMN `caja` VARCHAR(50) NULL COMMENT 'si es fibra se agrega la caja de instalación, de lo contrario no es requerido el campo',
ADD COLUMN `nodo` VARCHAR(50) NULL COMMENT 'si es antena se agrega la antena, de lo contrario no es requerido el campo';

-- Add indexes for faster queries
CREATE INDEX `idx_contratos_tipo_internet` ON `dommapi`.`contratos_servicio` (`tipo_internet`);
CREATE INDEX `idx_contratos_puerto` ON `dommapi`.`contratos_servicio` (`puerto`);
