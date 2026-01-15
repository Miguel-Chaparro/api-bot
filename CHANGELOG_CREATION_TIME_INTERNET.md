# Cambios: Fecha de Creación de Usuarios e Internet Fields en Contratos

**Fecha:** 15 de enero de 2026  
**Estado:** ✅ COMPLETADO - Compilación exitosa

## Descripción General

Se realizaron dos cambios principales:
1. **Fecha de creación (creation_time)**: Se establece automáticamente en el momento de la creación del usuario con timestamp actual
2. **Internet fields**: Se agregaron 4 nuevos campos a la tabla contratos_servicio para diferenciar tipos de servicios de internet (sin servicio, fibra, antena)

Estos cambios aplican para ambas APIs: `/createUser` (usuario individual) y `/createUsersBatch` (importación en lote).

---

## Cambio 1: Fecha de Creación del Usuario (creation_time)

### UserDTO.java
No requería cambios - el campo `creationTime` ya existía como `Timestamp`.

### UserDAO.java  
- **SQL_INSERT**: Ya incluía la columna `creation_time` en posición 7
- No requería cambios

### api.java - Método doCreateUser()
- **Ubicación:** Línea ~1290
- **Cambio agregado:**
  ```java
  // Establecer la fecha de creación actual
  newUser.setCreationTime(new java.sql.Timestamp(System.currentTimeMillis()));
  ```
  Se ejecuta ANTES de `userDAO.create(newUser)`

### api.java - Método doCreateUserBatchSimple()
- **Ubicación:** Línea ~2430 (en la versión actualizada)
- **Cambio agregado:**
  ```java
  // Establecer la fecha de creación actual (para batch)
  newUser.setCreationTime(new java.sql.Timestamp(System.currentTimeMillis()));
  ```
  Se ejecuta ANTES de `userDAO.create(newUser)`

---

## Cambio 2: Internet Fields en Contratos

### ContratoDTO.java
**Nuevos campos privados agregados (línea 39):**
```java
// Nuevos campos para internet
private String tipoInternet;   // ENUM: 'sin_servicio', 'fibra', 'antena'
private String puerto;          // Puerto de fibra
private String caja;            // Caja de instalación de fibra
private String nodo;            // Antena
```

**Getters y Setters agregados:**
```java
public String getTipoInternet() { return tipoInternet; }
public void setTipoInternet(String tipoInternet) { this.tipoInternet = tipoInternet; }

public String getPuerto() { return puerto; }
public void setPuerto(String puerto) { this.puerto = puerto; }

public String getCaja() { return caja; }
public void setCaja(String caja) { this.caja = caja; }

public String getNodo() { return nodo; }
public void setNodo(String nodo) { this.nodo = nodo; }
```

### UserDTO.java
**Nuevos campos privados agregados (línea 41):**
```java
// Nuevos campos para internet en contrato
private String tipoInternet; // ENUM: 'sin_servicio', 'fibra', 'antena'
private String puerto;       // Puerto de fibra
private String caja;         // Caja de instalación de fibra
private String nodo;         // Antena
```

**Getters y Setters agregados:**
```java
public String getTipoInternet() { return tipoInternet; }
public void setTipoInternet(String tipoInternet) { this.tipoInternet = tipoInternet; }

public String getPuerto() { return puerto; }
public void setPuerto(String puerto) { this.puerto = puerto; }

public String getCaja() { return caja; }
public void setCaja(String caja) { this.caja = caja; }

public String getNodo() { return nodo; }
public void setNodo(String nodo) { this.nodo = nodo; }
```

### ContratoDAO.java
**SQL_INSERT actualizado:**
- **De:** 22 parámetros (sin internet fields)
- **A:** 26 parámetros (con internet fields)

**Nuevas columnas en INSERT:**
```sql
tipo_internet, puerto, caja, nodo
```

**Parameter Binding (líneas 47-50):**
```java
// Nuevos campos de internet
ps.setString(23, dto.getTipoInternet() != null ? dto.getTipoInternet() : "sin_servicio");
ps.setString(24, dto.getPuerto());
ps.setString(25, dto.getCaja());
ps.setString(26, dto.getNodo());
```

### api.java - Método doCreateUser()
- **Ubicación:** Línea ~1404
- **Cambios agregados:**
  ```java
  // Nuevos campos de internet
  contrato.setTipoInternet(newUser.getTipoInternet());
  contrato.setPuerto(newUser.getPuerto());
  contrato.setCaja(newUser.getCaja());
  contrato.setNodo(newUser.getNodo());
  ```

### api.java - Método doCreateUserBatchSimple()
- **Ubicación:** Línea ~2519
- **Cambios agregados:**
  ```java
  // Nuevos campos de internet
  contrato.setTipoInternet(newUser.getTipoInternet());
  contrato.setPuerto(newUser.getPuerto());
  contrato.setCaja(newUser.getCaja());
  contrato.setNodo(newUser.getNodo());
  ```

---

## Migración SQL

**Archivo:** `migrations/add_internet_fields_to_contratos.sql`

```sql
ALTER TABLE `dommapi`.`contratos_servicio`
ADD COLUMN `tipo_internet` ENUM('sin_servicio', 'fibra', 'antena') DEFAULT 'sin_servicio',
ADD COLUMN `puerto` VARCHAR(50) NULL,
ADD COLUMN `caja` VARCHAR(50) NULL,
ADD COLUMN `nodo` VARCHAR(50) NULL;

CREATE INDEX `idx_contratos_tipo_internet` ON `dommapi`.`contratos_servicio` (`tipo_internet`);
CREATE INDEX `idx_contratos_puerto` ON `dommapi`.`contratos_servicio` (`puerto`);
```

---

## Compilación

✅ **BUILD SUCCESS**
- 93 archivos fuente compilados sin errores
- Tiempo total: 2.943 segundos
- Fecha: 2026-01-15T12:01:18-05:00

---

## Flujo de Trabajo en /createUser

1. Usuario con perfil Admin/Técnico crea un nuevo usuario con perfil Customer
2. Se asigna `newUser.setCreationTime(new java.sql.Timestamp(System.currentTimeMillis()))`
3. Se crea el usuario en la BD con fecha actual de creación
4. Se genera el contrato automáticamente
5. Se asignan los campos de internet: `tipoInternet`, `puerto`, `caja`, `nodo`
6. Se inserta el contrato con todos los campos

## Flujo de Trabajo en /createUsersBatch

1. Se procesa cada usuario de la lista (máximo 100)
2. Se asigna `newUser.setCreationTime(new java.sql.Timestamp(System.currentTimeMillis()))`
3. Se crea el usuario en la BD con fecha actual de creación
4. Se genera el contrato automáticamente (sin detalles de inventario)
5. Se asignan los campos de internet: `tipoInternet`, `puerto`, `caja`, `nodo`
6. Se inserta el contrato con todos los campos
7. Se continúa con el siguiente usuario

---

## Ejemplos de Uso

### Ejemplo 1: Crear usuario con servicio Fibra
```json
{
  "email": "cliente@empresa.com",
  "displayName": "Juan García",
  "tipoIdentificacion": "CC",
  "numeroIdentificacion": "123456789",
  "phoneNumber": "+573001234567",
  "empresaId": 1,
  "tipoServicio": "internet",
  "tipoInternet": "fibra",
  "puerto": "PUERTO-A5",
  "caja": "CAJA-001"
}
```

### Ejemplo 2: Crear usuario con servicio Antena
```json
{
  "email": "cliente2@empresa.com",
  "displayName": "María López",
  "tipoIdentificacion": "CC",
  "numeroIdentificacion": "987654321",
  "phoneNumber": "+573002345678",
  "empresaId": 1,
  "tipoServicio": "internet",
  "tipoInternet": "antena",
  "nodo": "NODO-NORTE-02"
}
```

### Ejemplo 3: Crear usuario sin servicio de internet
```json
{
  "email": "cliente3@empresa.com",
  "displayName": "Carlos Pérez",
  "tipoIdentificacion": "CC",
  "numeroIdentificacion": "555555555",
  "phoneNumber": "+573003333333",
  "empresaId": 1,
  "tipoServicio": "energia_solar",
  "tipoInternet": "sin_servicio"
}
```

---

## Archivos Modificados

| Archivo | Cambios |
|---------|---------|
| ContratoDTO.java | ✅ Agregados 4 nuevos campos + getters/setters |
| UserDTO.java | ✅ Agregados 4 nuevos campos + getters/setters |
| ContratoDAO.java | ✅ SQL_INSERT actualizado a 26 parámetros + binding |
| api.java (doCreateUser) | ✅ Agregada fecha de creación + internet fields |
| api.java (doCreateUserBatchSimple) | ✅ Agregada fecha de creación + internet fields |
| migrations/add_internet_fields_to_contratos.sql | ✅ CREADO - SQL para agregar columnas |

---

## Próximos Pasos

1. **Ejecutar la migración SQL:**
   ```bash
   # Ejecutar contra la base de datos dommapi
   mysql -u usuario -p dommapi < migrations/add_internet_fields_to_contratos.sql
   ```

2. **Desplegar el WAR actualizado:**
   - Archivo: `target/apiBot.war`
   - Fecha de compilación: 15 de enero de 2026

3. **Pruebas recomendadas:**
   - Crear usuario con tipoInternet='fibra' + puerto y caja
   - Crear usuario con tipoInternet='antena' + nodo
   - Crear usuario con tipoInternet='sin_servicio' (sin puerto, caja ni nodo)
   - Verificar que creation_time se establece correctamente
   - Probar batch de usuarios verificando que la fecha se asigna a cada uno

---

## Notas Importantes

- **creation_time**: Se establece automáticamente con `System.currentTimeMillis()` en el momento de la creación
- **tipo_internet**: ENUM con valores predefinidos: 'sin_servicio' (default), 'fibra', 'antena'
- **puerto**: Requerido solo si tipo_internet es 'fibra'
- **caja**: Requerido solo si tipo_internet es 'fibra'
- **nodo**: Requerido solo si tipo_internet es 'antena'
- **Validación**: La validación de campos requeridos debe hacerse en la lógica de negocio del cliente API
