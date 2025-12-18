# Cambios API: Soporte para Inventario Stock

**Fecha:** 17 de Diciembre de 2025  
**Versión:** 1.0.0

## Resumen

Se ha extendido la API `/createUser` y `/createUsersBatch` para soportar dos tipos de inventario:
1. **Serializado**: Controla items individuales con número de serie único
2. **Stock**: Controla cantidad de productos con control de bodega

## Cambios Realizados

### 1. DTOs Nuevos

#### `InventarioStockDTO.java`
- Representa un registro de inventario tipo stock
- Campos principales:
  - `productoId`: ID del producto
  - `empresaId`: ID de la empresa
  - `cantidadEnBodega`: Cantidad disponible en bodega
  - `costoPromedio`: Costo unitario promedio
  - `ubicacionBodega`: Ubicación física en bodega

#### `MovimientosStockDTO.java`
- Representa un movimiento de inventario stock
- Campos principales:
  - `productoId`: ID del producto
  - `empresaId`: ID de la empresa
  - `empleadoId`: ID del usuario que realiza la acción
  - `clienteId`: ID del cliente (opcional)
  - `tipoMovimiento`: Enum con valores: `compra`, `asignacion_cliente`, `devolucion_cliente`, `ajuste_positivo`, `ajuste_negativo`, `venta_directa`
  - `cantidad`: Cantidad movida
  - `costoUnitario`: Costo unitario del movimiento
  - `fechaMovimiento`: Timestamp del movimiento
  - `notas`: Notas del movimiento

### 2. DAOs Nuevos

#### `InventarioStockDAO.java`
- `decrementarCantidad(productoId, empresaId, cantidad)`: Reduce la cantidad en bodega
- `readOne(productoId, empresaId)`: Obtiene el registro de stock

#### `MovimientosStockDAO.java`
- `create(MovimientosStockDTO)`: Crea un nuevo movimiento de stock

### 3. Extensión de InventoryRequestDTO

Se agregaron campos a `InventoryRequestDTO.java`:
- `tipoInventario`: String ("serializado" o "stock")
- `cantidad`: BigDecimal (cantidad a descontar para stock)
- `productoId`: Integer (ID del producto para stock)

### 4. Lógica de Creación de Usuarios

#### En `/createUser`:
Al crear un usuario con perfil Customer:

**Para inventario "serializado":**
1. Se crea un movimiento en `movimientos_inventario` con tipo "prestamo"
2. Se actualiza el estado del inventario a "prestado"

**Para inventario "stock":**
1. Se decrementa `cantidad_en_bodega` en la tabla `inventario_stock`
2. Se crea un movimiento en `movimientos_stock` con:
   - Tipo: `asignacion_cliente`
   - Empleado ID: ID del usuario que ejecuta la petición
   - Cliente ID: ID del nuevo usuario customer
   - Costo unitario: Obtenido del registro de stock

#### En `/createUsersBatch`:
- NO crea movimientos de inventario (a diferencia de `/createUser`)
- Solo crea los contratos básicos

### 5. Base de Datos

Se crearon dos tablas nuevas en la schema `dommapi`:

#### `inventario_stock`
```sql
CREATE TABLE `inventario_stock` (
  `id` INT PRIMARY KEY AUTO_INCREMENT,
  `producto_id` INT NOT NULL,
  `empresa_id` INT UNSIGNED NOT NULL,
  `cantidad_en_bodega` DECIMAL(10,2) NOT NULL DEFAULT 0.00,
  `costo_promedio` DECIMAL(10,2) NOT NULL DEFAULT 0.00,
  `ubicacion_bodega` VARCHAR(100) NULL,
  `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  UNIQUE KEY (`producto_id`, `empresa_id`)
);
```

#### `movimientos_stock`
```sql
CREATE TABLE `movimientos_stock` (
  `id` INT PRIMARY KEY AUTO_INCREMENT,
  `producto_id` INT NOT NULL,
  `empresa_id` INT NOT NULL,
  `empleado_id` VARCHAR(128) NOT NULL,
  `cliente_id` VARCHAR(128) NULL,
  `tipo_movimiento` ENUM('compra', 'asignacion_cliente', 'devolucion_cliente', 'ajuste_positivo', 'ajuste_negativo', 'venta_directa') NOT NULL,
  `cantidad` DECIMAL(10,2) NOT NULL,
  `costo_unitario` DECIMAL(10,2) NULL,
  `fecha_movimiento` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  `notas` TEXT NULL
);
```

### 6. Cambios en Swagger

Se actualizo la documentación Swagger de `/createUser` y `/createUsersBatch` con:
- Nuevos ejemplos mostrando ambos tipos de inventario
- Documentación de los campos `tipoInventario`, `cantidad`, y `productoId`
- Explicación del flujo diferente para cada tipo de inventario

## Ejemplos de Uso

### Crear usuario con inventario SERIALIZADO

```json
{
  "email": "cliente@empresa.com",
  "displayName": "Juan Pérez",
  "tipoIdentificacion": "CC",
  "numeroIdentificacion": "123456789",
  "phoneNumber": "+573001234567",
  "empresaId": 1,
  "direccion": "Calle 10 #20-30",
  "tipoServicio": "internet",
  "idRaspi": "RASPI-001",
  "inventoryRequests": [
    {
      "inventarioId": 10,
      "precioAsignacion": 150.00,
      "notas": "Router TP-Link",
      "tipoInventario": "serializado"
    }
  ]
}
```

### Crear usuario con inventario STOCK

```json
{
  "email": "cliente2@empresa.com",
  "displayName": "María López",
  "tipoIdentificacion": "CC",
  "numeroIdentificacion": "987654321",
  "phoneNumber": "+573002345678",
  "empresaId": 1,
  "direccion": "Calle 20 #30-40",
  "tipoServicio": "internet",
  "idRaspi": "RASPI-002",
  "inventoryRequests": [
    {
      "productoId": 5,
      "cantidad": 2.00,
      "notas": "2 metros de cable RJ45",
      "tipoInventario": "stock"
    },
    {
      "productoId": 8,
      "cantidad": 1.00,
      "notas": "1 adaptador POE",
      "tipoInventario": "stock"
    }
  ]
}
```

## Notas Importantes

1. El campo `tipoInventario` por defecto es "serializado" si no se proporciona
2. Para stock, es obligatorio proporcionar `productoId` y `cantidad`
3. Para serializado, es obligatorio proporcionar `inventarioId`
4. El costo unitario en movimientos_stock se obtiene automáticamente del registro de `inventario_stock`
5. La auditoría de quién realiza la asignación se registra en `movimientos_stock.empleado_id`

## Archivos Modificados

### Java Source Files
- `InventoryRequestDTO.java` - Extendido con nuevos campos
- `api.java` - Lógica de manejo de ambos tipos de inventario

### DTOs Nuevos
- `InventarioStockDTO.java`
- `MovimientosStockDTO.java`

### DAOs Nuevos
- `InventarioStockDAO.java`
- `MovimientosStockDAO.java`

### Scripts SQL
- `migrations/add_stock_inventory_tables.sql`

## Estado de Compilación

✅ BUILD SUCCESS - 93 source files compiled successfully
