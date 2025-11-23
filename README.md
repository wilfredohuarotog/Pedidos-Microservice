# Pedidos Microservice

Microservicio para cargar pedidos desde archivos CSV. Arquitectura hexagonal y procedimiento eficiente (batch).

## Tecnologías y herramientas utilizadas
- Java 17
- Spring Boot 3.2
- PostgreSQL (motor de DB)
- Maven (gestión de proyecto)
- Flyway (control de versiones de DB)
- OpenCsv (Procesamiento de archivos csv)
- OpenAPI / Swagger (Documentación)
- OAuth2 Resource Server (Validación de JWT)
- KeyCloak (registro de usurios y generación de JWT)
- Docker (Contenerizado de db PostgreSQL y KeyCloak)
- Junit5 / Mockito (pruebas unitarias)
- Postman (pruebas y generación de colección)
 
## Ejecución del proyecto

### 1. Clonar el repositorio
```bash
git clone https://github.com/wilfredohuarotog/Pedidos-Microservice.git
```
### 2. Ingresar al directorio
```
cd Pedidos-Microservice
```
### 3. Levantamos PostgreSQL y KeyCloak (docker-compose.yml)
```bash
docker compose up -d
```
### 4. Levantamos la aplicación con maven
```bash
mvn spring-boot:run
```









