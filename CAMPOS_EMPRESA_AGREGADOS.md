# Campos Agregados a Empresa

## Resumen de cambios
Se han agregado 6 nuevos campos a la entidad `Empresa` en la base de datos y se han actualizado todos los componentes correspondientes (DTO, DAO y API REST).

## Campos agregados a la BD

```sql
ALTER TABLE dommapi.empresa
ADD COLUMN precio DECIMAL(10, 2) DEFAULT 0 COMMENT 'Precio base del producto o servicio que ofrece la empresa',
ADD COLUMN usa_pasarela BOOLEAN DEFAULT FALSE COMMENT 'Indica si la empresa quiere usar pasarela de pago (TRUE = Sí, FALSE = No)',
ADD COLUMN tarifa_fija_pasarela DECIMAL(10, 2) DEFAULT 0 COMMENT 'Tarifa fija que cobra la pasarela de pago por transacción (ejemplo: 500 COP)',
ADD COLUMN porcentaje_pasarela DECIMAL(5, 2) DEFAULT 0 COMMENT 'Porcentaje que cobra la pasarela de pago por transacción (ejemplo: 2.9)',
ADD COLUMN cobrar_pasarela BOOLEAN DEFAULT FALSE COMMENT 'Indica si se debe cobrar la tarifa de la pasarela al cliente (TRUE = Sí, FALSE = No)',
ADD COLUMN host VARCHAR(255) DEFAULT 'dommatos.com' COMMENT 'Si la empresa tiene alguna página web';
```

## Componentes actualizados

### 1. EmpresaDTO
**Archivo**: `src/main/java/com/dom/ws/rest/bot/DTO/EmpresaDTO.java`

**Campos agregados**:
- `Double precio` - Precio base del producto/servicio
- `Boolean usaPasarela` - Indica uso de pasarela de pago
- `Double tarifaFijaPasarela` - Tarifa fija de pasarela
- `Double porcentajePasarela` - Porcentaje de pasarela
- `Boolean cobrarPasarela` - Indica si cobrar tarifa al cliente
- `String host` - Dominio de la empresa

**Métodos agregados**:
- Getters y Setters para todos los campos
- Nuevo constructor con todos los parámetros

### 2. EmpresaDAO
**Archivo**: `src/main/java/com/dom/ws/rest/bot/DAO/EmpresaDAO.java`

**Cambios realizados**:

#### SQL_INSERT
```java
INSERT INTO dommapi.empresa 
(nombre, nit, direccion, telefono, email, estado, numero_chatbot, precio, usa_pasarela, tarifa_fija_pasarela, porcentaje_pasarela, cobrar_pasarela, host) 
VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
```

#### SQL_UPDATE
```java
UPDATE dommapi.empresa 
SET nombre=?, nit=?, direccion=?, telefono=?, email=?, estado=?, numero_chatbot=?, precio=?, usa_pasarela=?, tarifa_fija_pasarela=?, porcentaje_pasarela=?, cobrar_pasarela=?, host=? 
WHERE id=?
```

#### Métodos actualizados:
- `create()` - Inserta 13 parámetros (7 originales + 6 nuevos)
- `update()` - Actualiza 13 parámetros (7 originales + 6 nuevos)
- `readOne()` - Lee 14 columnas (8 originales + 6 nuevas)
- `readAll()` - Lee 14 columnas (8 originales + 6 nuevas)

### 3. API REST - Endpoints
**Archivo**: `src/main/java/com/dom/ws/rest/bot/Services/api.java`

#### POST `/AccessApi/createEmpresa`
**Descripción mejorada**: "Crea una nueva empresa con información básica y configuración de pasarela de pago."

**Ejemplo de request**:
```json
{
  "nombre": "Empresa ABC",
  "nit": "123456789",
  "direccion": "Calle 10 #20-30",
  "telefono": "+573001234567",
  "email": "contacto@empresaabc.com",
  "estado": 1,
  "numeroChatbot": "591234567890",
  "precio": 49.99,
  "usaPasarela": true,
  "tarifaFijaPasarela": 500.00,
  "porcentajePasarela": 2.9,
  "cobrarPasarela": true,
  "host": "empresaabc.com"
}
```

**Respuestas**:
- `200`: Empresa creada exitosamente (boolean)
- `400`: Error en la creación de proyectos por defecto
- `500`: Error interno del servidor

#### POST `/AccessApi/updateEmpresa`
**Descripción mejorada**: "Actualiza los datos de una empresa existente, incluyendo configuración de pasarela de pago y precio base."

**Ejemplo de request**:
```json
{
  "id": 1,
  "nombre": "Empresa ABC Actualizada",
  "nit": "123456789",
  "direccion": "Calle 20 #30-40",
  "telefono": "+573001234567",
  "email": "nuevocontacto@empresaabc.com",
  "estado": 1,
  "numeroChatbot": "591234567890",
  "precio": 59.99,
  "usaPasarela": true,
  "tarifaFijaPasarela": 500.00,
  "porcentajePasarela": 2.9,
  "cobrarPasarela": false,
  "host": "empresaabc.com"
}
```

**Respuestas**:
- `200`: Empresa actualizada exitosamente (boolean)
- `500`: Error interno del servidor

#### GET `/AccessApi/usersByEmpresa/{empresaId}`
Este endpoint ya existía y retorna todos los usuarios asociados a una empresa. Ahora la empresa tendrá más información con los campos agregados.

## Operaciones CRUD

### CREATE
Los nuevos campos se insertan con valores por defecto si no se proporcionan:
- `precio`: 0
- `usaPasarela`: false
- `tarifaFijaPasarela`: 0
- `porcentajePasarela`: 0
- `cobrarPasarela`: false
- `host`: "dommatos.com"

### READ
Al consultar empresas (readOne, readAll), se retornan todos los campos incluyendo los nuevos.

### UPDATE
Los campos pueden ser actualizados sin necesidad de actualizar todos los datos de la empresa.

## Documentación Swagger
Los endpoints POST `/createEmpresa` y POST `/updateEmpresa` han sido actualizados con:
- Descripción más detallada
- Ejemplos de request completos
- Códigos de respuesta mejorados
- Esquema del objeto EmpresaDTO con todos los campos

## Validación
No se requieren validaciones especiales para los nuevos campos. El DAO maneja valores nulos asignando valores por defecto:

```java
ps.setDouble(8, dto.getPrecio() != null ? dto.getPrecio() : 0);
ps.setBoolean(9, dto.getUsaPasarela() != null ? dto.getUsaPasarela() : false);
// ... resto de campos
```

## Notas importantes
- Los campos de pasarela son opcionales y se pueden usar para implementar lógica de pagos
- El campo `host` es útil para empresas que tengan su propio dominio
- El campo `precio` puede ser utilizado como precio base para cálculos posteriores
- Todos los nuevos campos son nullables en el DTO pero tienen valores por defecto en la BD

## Testing
Para probar los nuevos campos, puedes usar:

```bash
# Crear empresa con nuevos campos
curl -X POST http://localhost:8080/apiBot/AccessApi/createEmpresa \
  -H "Content-Type: application/json" \
  -d '{
    "nombre": "Test Empresa",
    "nit": "987654321",
    "direccion": "Calle Test",
    "telefono": "+573009999999",
    "email": "test@empresa.com",
    "estado": 1,
    "numeroChatbot": "591234567890",
    "precio": 99.99,
    "usaPasarela": true,
    "tarifaFijaPasarela": 500,
    "porcentajePasarela": 2.5,
    "cobrarPasarela": true,
    "host": "testempresa.com"
  }'

# Actualizar empresa
curl -X POST http://localhost:8080/apiBot/AccessApi/updateEmpresa \
  -H "Content-Type: application/json" \
  -d '{
    "id": 1,
    "nombre": "Test Empresa Actualizada",
    ...otros campos...
  }'
```
