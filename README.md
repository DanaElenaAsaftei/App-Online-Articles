# Práctica 1 – API REST de Blogging (estilo Medium)

Proyecto académico cuyo objetivo es el desarrollo de una **API Web RESTful** para la gestión de artículos tecnológicos, inspirada en plataformas de blogging como **Medium**.  
La API permite crear, consultar y gestionar artículos y usuarios, utilizando un modelo de datos relacional y exponiendo los recursos mediante servicios REST que devuelven información en formato JSON.

## Objetivos
- Diseñar e implementar una **API REST** para la gestión de artículos y usuarios.
- Exponer el modelo de datos relacional mediante **JPA**.
- Implementar parte de la **lógica de negocio** del sistema.
- Soportar autenticación de usuarios para operaciones protegidas.
- Proveer un **cliente REST** para interactuar y testear la API.

## Funcionalidades principales

### Gestión de artículos
- Listado de artículos ordenados por popularidad.
- Filtros por:
  - Tópico (hasta dos tópicos simultáneamente).
  - Autor.
- Consulta del detalle completo de un artículo.
- Incremento automático del número de visualizaciones al consultar un artículo.
- Soporte de artículos públicos y privados (accesibles solo para usuarios autenticados).
- Creación de nuevos artículos (requiere autenticación).
- Eliminación de artículos (opcional, requiere que el usuario sea el autor).

### Gestión de usuarios
- Listado de usuarios en formato JSON (sin información sensible).
- Consulta de un usuario por identificador.
- Inclusión de enlaces HATEOAS al último artículo publicado por el usuario (si es autor).
- Modificación de datos de usuario (opcional, requiere autenticación).

### Autenticación
- Autenticación básica HTTP (HTTP Basic Authentication) mediante anotación `@Secured`.
- Control de acceso a operaciones protegidas.
- Soporte para artículos privados accesibles solo para usuarios registrados.
- Usuario inicial obligatorio:
  - **Usuario:** `sob`
  - **Contraseña:** `sob`

## API REST (versión v1)

### Artículos
- `GET /rest/api/v1/article`
- `GET /rest/api/v1/article?topic={topic}&author={author}`
- `GET /rest/api/v1/article/{id}`
- `POST /rest/api/v1/article`
- `DELETE /rest/api/v1/article/{id}` (opcional)

### Usuarios
- `GET /rest/api/v1/customer`
- `GET /rest/api/v1/customer/{id}`
- `PUT /rest/api/v1/customer/{id}` (opcional)

Todas las respuestas se devuelven en **JSON** y se utilizan **códigos HTTP adecuados** (`200 OK`, `201 Created`, `403 Forbidden`, `404 Not Found`, etc.).

## Tecnologías utilizadas
- Java  
- JAX-RS (servicios REST)  
- JPA (modelo de datos relacional)  
- JSON  
- Cliente REST para pruebas (Postman u otro cliente REST)  

## Testing
- Pruebas de la API mediante cliente REST.
- Posibilidad de definir colecciones de tests automáticos con Postman.
- Verificación del correcto uso de códigos HTTP y autenticación.

## Qué se ha aprendido
- Diseño de **APIs RESTful**.
- Versionado de APIs.
- Modelado de datos con **JPA**.
- Autenticación básica en servicios web.
- Uso de códigos de estado HTTP.
- Separación entre:
  - Recursos REST (`article`, `customer`)
  - Entidades de dominio.

## Inicialización del sistema
- Inicialización automática de la base de datos mediante scripts SQL.
- Creación de un usuario por defecto (`sob` / `sob`) para pruebas iniciales.

## Posibles ampliaciones (opcional)
- Soporte de XML además de JSON.
- Sistema de autenticación alternativo (API Keys, JWT, etc.).
- Gestión avanzada de errores HTTP.
- Registro de nuevos usuarios.
- Interfaz web basada en MVC que consuma esta API REST.
- Diseño responsive para dispositivos móviles y escritorio.

## Notas
Este proyecto forma parte de una práctica académica orientada a comprender el desarrollo de **servicios web REST** y la separación entre back-end y front-end en aplicaciones web modernas.
