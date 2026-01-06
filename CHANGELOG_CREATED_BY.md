# Cambio: Agregar created_by a Contratos de Servicio

**Fecha:** 6 de enero de 2026  
**Estado:** ✅ COMPLETADO - Compilación exitosa

## Descripción

Se agregó la columna `created_by` a la tabla `contratos_servicio` para registrar cuál usuario de Firebase realizó la creación del contrato cuando se crea un nuevo usuario con perfil Customer.

## Cambios Realizados

### 1. ContratoDTO.java
- **Ubicación:** `src/main/java/com/dom/ws/rest/bot/DTO/ContratoDTO.java`
- **Cambios:**
  - Agregado campo privado: `private String createdBy;` (línea 35)
  - Agregado getter: `public String getCreatedBy() { return createdBy; }`
  - Agregado setter: `public void setCreatedBy(String createdBy) { this.createdBy = createdBy; }`

### 2. ContratoDAO.java
- **Ubicación:** `src/main/java/com/dom/ws/rest/bot/DAO/ContratoDAO.java`
- **Cambios:**
  - **SQL_INSERT:** Actualizado para incluir la columna `created_by` en el INSERT
    - De: 21 parámetros (sin created_by)
    - A: 22 parámetros (con created_by)
  - **create() método:** Agregado binding del parámetro 22 para `created_by`
    ```java
    ps.setString(22, dto.getCreatedBy());
    ```

### 3. api.java - Método doCreateUser()
- **Ubicación:** Línea ~1405
- **Cambio:** Agregada una línea para establecer el `createdBy` antes de crear el contrato
  ```java
  contrato.setCreatedBy(userId);
  ```
  El `userId` es el identificador del usuario de Firebase que está realizando la petición.

### 4. api.java - Método doCreateUserBatchSimple()
- **Ubicación:** Línea ~2507
- **Cambio:** Agregada la misma línea para establecer el `createdBy` en las importaciones en lote
  ```java
  contrato.setCreatedBy(userId);
  ```

### 5. Migración SQL
- **Archivo:** `migrations/add_created_by_to_contratos.sql` (CREADO)
- **Contenido:**
  - ALTER table para agregar columna `created_by` VARCHAR(128)
  - Crear índice `idx_contratos_created_by` para optimizar queries
  - Comentario para update opcional de registros existentes

## Flujo de Trabajo

### En doCreateUser (usuario individual):
1. Usuario con perfil Admin/Técnico crea un nuevo usuario con perfil Customer
2. Se genera un contrato automáticamente
3. Se asigna `contrato.setCreatedBy(userId)` donde userId es el Firebase UID del usuario que crea
4. ContratoDAO.create() inserta el contrato con el created_by registrado

### En doCreateUserBatchSimple (importación en lote):
1. Se crea un usuario con perfil Customer en la importación
2. Se genera un contrato automáticamente
3. Se asigna `contrato.setCreatedBy(userId)` donde userId es el Firebase UID del usuario que ejecuta la importación
4. ContratoDAO.create() inserta el contrato con el created_by registrado

## Compilación

✅ **BUILD SUCCESS**
- 93 archivos fuente compilados sin errores
- Tiempo total: 3.056 segundos
- Fecha: 2026-01-06T10:19:33-05:00

## Próximos Pasos

1. **Ejecutar la migración SQL** contra la base de datos dommapi:
   ```sql
   -- Ejecutar el archivo: migrations/add_created_by_to_contratos.sql
   ```

2. **Desplegar el WAR actualizado:**
   - Archivo: `target/apiBot.war`
   - Nueva compiled el 6 de enero de 2026

3. **Pruebas sugeridas:**
   - Crear un usuario customer y verificar que el `created_by` se registre correctamente
   - Realizar una importación en lote y verificar que el `created_by` se registre en todos los contratos
   - Consultar la tabla `contratos_servicio` para verificar que la columna está poblada

## Consideraciones de Seguridad y Auditoría

- El campo `created_by` registra el Firebase UID del usuario que creó el contrato
- Permite auditoría completa de quién creó cada contrato
- Es útil para tracking de cambios y responsabilidad

## Cambios de Código Específicos

| Archivo | Línea | Tipo | Cambio |
|---------|-------|------|--------|
| ContratoDTO.java | 35 | Campo privado | Agregado `private String createdBy;` |
| ContratoDTO.java | 110+ | Getter/Setter | Agregados getCreatedBy() y setCreatedBy() |
| ContratoDAO.java | 12 | SQL | Agregado `created_by` al INSERT |
| ContratoDAO.java | 41-42 | Binding | Agregado `ps.setString(22, dto.getCreatedBy());` |
| api.java | 1405 | doCreateUser | Agregado `contrato.setCreatedBy(userId);` |
| api.java | 2507 | doCreateUserBatchSimple | Agregado `contrato.setCreatedBy(userId);` |

## Archivos Modificados

1. ✅ `/src/main/java/com/dom/ws/rest/bot/DTO/ContratoDTO.java`
2. ✅ `/src/main/java/com/dom/ws/rest/bot/DAO/ContratoDAO.java`
3. ✅ `/src/main/java/com/dom/ws/rest/bot/Services/api.java`
4. ✅ `/migrations/add_created_by_to_contratos.sql` (NUEVO)

## Impacto

- **Base de datos:** Requiere ejecutar migración SQL
- **API:** Sin cambios en endpoints o request/response
- **Auditoría:** Mejora total - registra quién crea contratos
- **Backward compatibility:** Totalmente compatible
