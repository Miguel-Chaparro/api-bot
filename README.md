# API Bot Project

Este es un proyecto de API REST Java que utiliza Firebase para autenticación y Jersey para los servicios web.

## Requisitos Previos

- Java JDK 8 o superior hasta java 11
- Maven
- Servidor de aplicaciones compatible con Java EE (por ejemplo, Apache Tomcat 9)
- Cuenta de Firebase y proyecto configurado

## Configuración de Firebase

1. Crear un proyecto en Firebase Console (https://console.firebase.google.com/)
2. Generar un archivo de credenciales de servicio:
   - Ve a la Configuración del Proyecto
   - Selecciona "Cuentas de servicio"
   - Haz clic en "Generar nueva clave privada"
   - Guarda el archivo JSON descargado

3. Crear el archivo de credenciales:
   - Coloca el archivo JSON descargado en `src/main/resources/firebase-credentials.json`
   - El archivo debe tener esta estructura:
   ```json
   {
     "type": "service_account",
     "project_id": "tu-proyecto-id",
     "private_key_id": "tu-private-key-id",
     "private_key": "tu-private-key",
     "client_email": "tu-client-email",
     "client_id": "tu-client-id",
     "auth_uri": "https://accounts.google.com/o/oauth2/auth",
     "token_uri": "https://oauth2.googleapis.com/token",
     "auth_provider_x509_cert_url": "https://www.googleapis.com/oauth2/v1/certs",
     "client_x509_cert_url": "tu-client-x509-cert-url"
   }
   ```

## Instalación y Configuración

1. Clonar el repositorio
```bash
git clone [url-del-repositorio]
cd api-bot
```

2. Instalar dependencias con Maven
```bash
mvn clean install
```

3. Desplegar el archivo WAR generado
- El archivo WAR se generará en la carpeta `target/`
- Despliégalo en tu servidor de aplicaciones

## Estructura del Proyecto

- `src/main/java/com/dom/ws/rest/bot/`
  - `Conexion/`: Clases de conexión y configuración
  - `Filters/`: Filtros de autenticación
  - `Services/`: Endpoints de la API

## Seguridad

- La API utiliza autenticación mediante tokens de Firebase
- Todas las peticiones a `/api/*` requieren un token válido
- El token debe enviarse en el header de Authorization: `Bearer [token]`

## Desarrollo

Para ejecutar el proyecto en modo desarrollo:

1. Asegúrate de tener las credenciales de Firebase correctamente configuradas
2. Compila el proyecto: `mvn clean compile`
3. Ejecuta el servidor de desarrollo: `mvn tomcat7:run` (si usas Tomcat)

## Notas Importantes

- No subir el archivo `firebase-credentials.json` al control de versiones
- El archivo está incluido en `.gitignore` por seguridad
- Mantener las dependencias actualizadas, especialmente las relacionadas con seguridad