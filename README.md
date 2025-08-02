# Biblioteca Virtual (consola + API Gutendex)

Este es un proyecto de consola en Java que permite consultar libros desde una API pública llamada GutenDex API(https://gutendex.com/), 
para procesar su información y almacenarla en una base de datos PostgreSQL utilizando JPA y Hibernate.

## Descripción

La aplicación extrae datos como el título, autor, idioma y número de descargas de libros disponibles públicamente. 
Usa clases `record` para mapear los datos de la API, convierte estos datos en entidades de JPA y los persiste en una base de datos local.

El proyecto está organizado en distintas capas y paquetes para mantener una estructura limpia y modular:

- "model": Entidades JPA
- "repository": Interfaces para acceder a la base de datos
- "client": Cliente HTTP para consumir la API
- "main": Lógica principal con menú en consola

## Funcionalidades

- Consultar libros desde la API pública de Gutendex y registrarlos en la base de datos.
- Extraer datos como título, autor, idioma y número de descargas.
- Persistir la información en una base de datos PostgreSQL.
- Verificar autores duplicados para evitar registros redundantes.
- Aplicación ejecutada totalmente desde consola con menú interactivo.

## Requisitos

- Java 17+
- Maven
- PostgreSQL
- Conexión a Internet (para consultar la API)

---

##  Configuración y uso

1) Clonar el repositorio

git clone https://github.com/MisterCowDev/biblioteca-virtual.git

3) Crear una base de datos
   
CREATE DATABASE db_libros;

5) Configura el archivo application.properties
   
src/main/resources/application.properties

spring.datasource.url=jdbc:postgresql://localhost/db_libros

spring.datasource.username=TU_USUARIO

spring.datasource.password=TU_CONTRASEÑA

spring.jpa.hibernate.ddl-auto=update

spring.jpa.show-sql=true
