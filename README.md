# Blog Platform Backend

Backend de un blog construido con **Spring Boot**, **Spring Security**, **JPA/Hibernate** y **PostgreSQL**.  
Versión: **1.0.0**

---

## Tecnologías

- Java 21, Spring Boot 4.0.1  
- PostgreSQL + Docker  
- Spring Security + JWT  
- H2 (desarrollo), Lombok, MapStruct  

---

## Requisitos

- Java 21, Maven 3.8+  
- Docker y Docker Compose (opcional para DB)  

---

## Docker

Levanta la base de datos y Adminer:

```bash
docker-compose up -d
```

- PostgreSQL: localhost:5432
- Adminer: http://localhost:8888

## Variables de entorno Docker:
```
POSTGRES_DB=blog_db
POSTGRES_USER=postgres
POSTGRES_PASSWORD=1234
```
## Variables de entorno Spring Boot:
```
SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/blog_db
SPRING_DATASOURCE_USERNAME=postgres
SPRING_DATASOURCE_PASSWORD=1234

JWT_SECRET=clave_secreta
JWT_EXPIRATION_MS=3600000
JWT_EXPIRED_IN=3600
```
- Nota: Definir estas variables en IntelliJ si corres desde allí.
