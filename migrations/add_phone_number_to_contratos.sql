-- Migration: Add phone_number field to dommapi.contratos_servicio
-- Date: 2025-12-16
-- Description: Adds phone_number column to store customer phone number in contracts

ALTER TABLE `dommapi`.`contratos_servicio`
ADD COLUMN `phone_number` VARCHAR(20) NULL COMMENT 'Número de teléfono del cliente' AFTER `contrato_nombre`;

-- Add index for phone_number for faster queries
CREATE INDEX `idx_contratos_phone_number` ON `dommapi`.`contratos_servicio` (`phone_number`);
