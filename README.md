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
## Uso
Al levantar keycloak ya se configuró para que tenga un realm con un usuario y contraseña, el cual se utilizará para las pruebas en Postman.

### 1. Obtener Token JWT (KeyCloak)
- Endpoint: `POST http://localhost:8080/realms/pedidos-realm/protocol/openid-connect/token`.
- Body:
  `grant_type: password`
  `client_id: pedidos-api`
  `username: wilfredo`
  `password: 2299` 

- Con el Token se procede a realizar las más solicitudes.

### 2. Cargar de pedidos
- Endpoint: `http://localhost:8081/pedidos/cargar`.
- Authorization: Bearer Token (colocar el JWT generado anteriormente)
- Headers: Idempotency-Key (colocar clave)
- Body: file.csv (adjuntar archivo)
  
### 3. Respuestas
#### Cargados correctamente
```json
{
    "totalProcesados": 10,
    "guardados": 10,
    "conError": 0,
    "erroresDetalle": [],
    "erroresAgrupados": {}
}
```
#### Cargados con errores
```json
{
    {
    "totalProcesados": 8,
    "guardados": 1,
    "conError": 7,
    "erroresDetalle": [
        {
            "linea": 3,
            "numeroPedido": "P101",
            "motivos": [
                "CLIENTE_NO_ENCONTRADO"
            ]
        },
        {
            "linea": 4,
            "numeroPedido": "P102",
            "motivos": [
                "FECHA_INVALIDA"
            ]
        },
        {
            "linea": 5,
            "numeroPedido": "P-103",
            "motivos": [
                "NUMERO_PEDIDO_INVALIDO",
                "ESTADO_INVALIDO"
            ]
        },
        {
            "linea": 6,
            "numeroPedido": "P104",
            "motivos": [
                "ZONA_INVALIDA"
            ]
        },
        {
            "linea": 7,
            "numeroPedido": "P105",
            "motivos": [
                "CADENA_FRIO_NO_SOPORTADA"
            ]
        },
        {
            "linea": 8,
            "numeroPedido": "",
            "motivos": [
                "NUMERO_PEDIDO_VACIO"
            ]
        },
        {
            "linea": 9,
            "numeroPedido": "P100",
            "motivos": [
                "DUPLICADO"
            ]
        }
    ],
    "erroresAgrupados": {
        "CADENA_FRIO_NO_SOPORTADA": 1,
        "NUMERO_PEDIDO_INVALIDO": 1,
        "ESTADO_INVALIDO": 1,
        "CLIENTE_NO_ENCONTRADO": 1,
        "FECHA_INVALIDA": 1,
        "NUMERO_PEDIDO_VACIO": 1,
        "DUPLICADO": 1,
        "ZONA_INVALIDA": 1
    }
}
}
```
#### Idempotency-key repetida

```json
{
    "code": "DUPLICATE_REQUEST",
    "message": "Carga ya procesada previamente",
    "details": [
        "Esta carga ya fue procesada previamente"
    ],
    "correlationId": null,
    "timestamp": "2025-11-23T14:46:22.6011022"
}
```

## Testing

### 1. Ejecutar test y reporte de JaCoCo
```bash
mvn clean test jacoco:report
```
### 2. Ver reporte de cobertura
```bash
start target/site/jacoco/index.html
```
- Cobertura del 86% en servicios de dominio.

## Documentación
Acceder a la documetación en tiempo de ejecución:
- OpenApi: `http://localhost:8081/v3/api-docs`
- Swagger-UI: `http://localhost:8081/swagger-ui.html`
