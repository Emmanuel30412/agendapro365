# AgendaPro365 Backend

API RESTful desarrollada con Java Spring Boot para la gestión de citas, clientes, profesionales y pagos.

## Tecnologías usadas

- Java 17
- Spring Boot 3.4.8
- PostgreSQL
- Swagger para documentación API
- JUnit + Mockito para pruebas unitarias

## Cómo ejecutar el proyecto localmente

1. Clona el repositorio
   ```bash
   git clone https://github.com/tu-usuario/agendaPro365-backend.git
   cd agendaPro365-backend

2. Configura la base de datos PostgreSQL (host, usuario, contraseña) en application.properties
3. Ejecuta el proyecto con Maven

./mvnw spring-boot:run

4. Accede a Swagger UI para probar endpoints
http://localhost:8080/swagger-ui/index.html

Ejecutar pruebas unitarias
./mvnw test


---

### 4. Comandos Git para subir tu proyecto

Abre consola en la carpeta raíz de tu proyecto y ejecuta:

```bash
git init                           # si no está inicializado aún
git add .                         # añade todos los archivos excepto los ignorados
git commit -m "Primer commit: backend AgendaPro365 con servicios y controladores"
git remote add origin https://github.com/tu-usuario/agendaPro365-backend.git
git branch -M main                # cambia la rama a main si es necesario
git push -u origin main           # sube al repositorio remoto
