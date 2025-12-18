-- Migration: Add inventario_stock and movimientos_stock tables
-- Date: 2025-12-17
-- Description: Adds support for stock inventory management with movement tracking

-- Table for stock inventory
CREATE TABLE IF NOT EXISTS `dommapi`.`inventario_stock` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `producto_id` INT NOT NULL,
  `empresa_id` INT UNSIGNED NOT NULL,
  `cantidad_en_bodega` DECIMAL(10,2) NOT NULL DEFAULT 0.00,
  `costo_promedio` DECIMAL(10,2) NOT NULL DEFAULT 0.00,
  `ubicacion_bodega` VARCHAR(100) NULL,
  `updated_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_producto_empresa` (`producto_id`, `empresa_id`),
  KEY `idx_empresa_id` (`empresa_id`),
  CONSTRAINT `fk_inventario_stock_empresa` FOREIGN KEY (`empresa_id`) REFERENCES `empresa` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Table for stock movements
CREATE TABLE IF NOT EXISTS `dommapi`.`movimientos_stock` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `producto_id` INT NOT NULL,
  `empresa_id` INT NOT NULL,
  `empleado_id` VARCHAR(128) NOT NULL,
  `cliente_id` VARCHAR(128) NULL,
  `tipo_movimiento` ENUM('compra', 'asignacion_cliente', 'devolucion_cliente', 'ajuste_positivo', 'ajuste_negativo', 'venta_directa') NOT NULL,
  `cantidad` DECIMAL(10,2) NOT NULL,
  `costo_unitario` DECIMAL(10,2) NULL,
  `fecha_movimiento` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `notas` TEXT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_producto_id` (`producto_id`),
  KEY `idx_empresa_id` (`empresa_id`),
  KEY `idx_empleado_id` (`empleado_id`),
  KEY `idx_cliente_id` (`cliente_id`),
  KEY `idx_tipo_movimiento` (`tipo_movimiento`),
  KEY `idx_fecha_movimiento` (`fecha_movimiento`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Create indexes for better query performance
CREATE INDEX `idx_movimientos_stock_producto_empresa` ON `dommapi`.`movimientos_stock` (`producto_id`, `empresa_id`);
CREATE INDEX `idx_movimientos_stock_empleado_cliente` ON `dommapi`.`movimientos_stock` (`empleado_id`, `cliente_id`);
