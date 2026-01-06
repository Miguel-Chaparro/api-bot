-- Migration: Add created_by field to dommapi.contratos_servicio
-- Date: 2026-01-06
-- Description: Adds created_by column to track which Firebase user created the contract

ALTER TABLE `dommapi`.`contratos_servicio`
ADD COLUMN `created_by` VARCHAR(128) NULL COMMENT 'Usuario firebase que creo el contrato al cliente' AFTER `phone_number`;

-- Add index for created_by for faster queries
CREATE INDEX `idx_contratos_created_by` ON `dommapi`.`contratos_servicio` (`created_by`);

-- Optional: Update existing records with a default value if needed
-- UPDATE `dommapi`.`contratos_servicio` SET `created_by` = 'unknown' WHERE `created_by` IS NULL;
