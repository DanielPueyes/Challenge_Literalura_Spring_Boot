# 📚✨ LiterAlura


**LiterAlura** es una aplicación Java interactiva que te permite 
**buscar, guardar y analizar libros** mediante la API pública de 
[Gutendex](https://gutendex.com/books/). Todo esto en una estructura 
robusta y profesional utilizando Spring Boot y PostgreSQL.

---

## 🚀 Tecnologías Utilizadas



- ✅ **Java 21**
- ✅ **Spring Boot 3**
- ✅ **PostgreSQL**
- ✅ **JPA/Hibernate**
- ✅ **Gutendex API**
- ✅ **Maven**

---

## 🧠 Funcionalidades Principales

![image](https://github.com/user-attachments/assets/0347ee1a-3257-4072-8238-6344ec31b828)



### 🔍 Exploración y Registro
- Buscar libros por nombre desde la API.
- Guardar libros y autores automáticamente en base de datos.
- Evita duplicados mediante validación.

### 📊 Análisis y Estadísticas
- Ver estadísticas de descargas (promedio, mínimo, máximo).
- Mostrar el Top 10 de libros más descargados.

### 🔎 Consultas y Filtros
- Filtrar libros por idioma (`es`, `en`, `fr`, `pt`).
- Buscar autores por nombre o fechas de vida.
- Mostrar autores vivos actualmente o entre dos fechas.
- Filtrar libros con/sin derechos de autor.

---

## 🗂️ Estructura del Proyecto


```shell
com.alura.LiterAlura
├── model
├── repository
├── service
├── utils
├── principal
└── LiterAluraApplication.java

🛠️ Configuración Inicial
1. Clona el repositorio

git clone https://github.com/tuusuario/LiterAlura.git
cd LiterAlura

2. Crea la base de datos en PostgreSQL

CREATE DATABASE literalura;

3. Configura el archivo application.properties

spring.datasource.url=jdbc:postgresql://localhost:5432/literalura
spring.datasource.username=TU_USUARIO
spring.datasource.password=TU_CONTRASEÑA
spring.jpa.hibernate.ddl-auto=update
🔒 Puedes usar variables de entorno o application.yml para ocultar tus credenciales.

▶️ Cómo Ejecutar
Desde tu IDE o terminal con Maven:

./mvnw spring-boot:run
Se mostrará un menú interactivo en consola como este:


📦 Dependencias Principales
<dependencies>
  <dependency>spring-boot-starter-data-jpa</dependency>
  <dependency>spring-boot-starter-web</dependency>
  <dependency>org.postgresql:postgresql</dependency>
  <dependency>com.fasterxml.jackson.core</dependency>
</dependencies>
🌍 Fuente de Datos

Se consume la API pública Gutendex para obtener libros en dominio público.

📊 Ejemplo de Estadísticas

Promedio descargas: 1550.75
Más descargado: Moby Dick (3010 descargas)
Menos descargado: Book of Love (720 descargas)

📌 Autor
📇 Desarrollado por Daniel Pueyes Díaz
🔗 GitHub: https://github.com/DanielPueyes/Challenge_Literalura_Spring_Boot/tree/main/LiterAlura
