# Ejemplo de Backend TSIS

Este proyecto es una aplicación Spring Boot que demuestra una implementación simple de backend para gestionar estudiantes y grupos. Sirve como ejemplo para el curso de TSIS (Tecnologías para Sistemas de Información).

## Características

- API RESTful para gestionar estudiantes y grupos
- Base de datos H2 en memoria
- Documentación OpenAPI (Swagger UI)
- JPA para persistencia de datos
- Patrón DTO para separación de capas
- Mappers para conversión entre entidades y DTOs
- Manejo de errores con excepciones de dominio
- Acceso no autenticado a los endpoints

## Tecnologías Utilizadas

- Java 17
- Spring Boot 3.2.3
- Spring Data JPA
- Base de datos H2
- SpringDoc OpenAPI
- Maven

## Requisitos Previos

- Java 17 o superior
- Maven 3.6 o superior

## Estructura del Proyecto

```
src/
├── main/
│   ├── java/
│   │   └── mx/
│   │       └── uam/
│   │           └── tsis/
│   │               └── ejemplobackend/
│   │                   ├── modelo/
│   │                   │   ├── Alumno.java
│   │                   │   └── Grupo.java
│   │                   ├── negocio/
│   │                   │   ├── exception/
│   │                   │   │   ├── BusinessException.java
│   │                   │   │   ├── EntityNotFoundException.java
│   │                   │   │   └── DuplicateEntityException.java
│   │                   │   ├── AlumnoService.java
│   │                   │   └── GrupoService.java
│   │                   ├── datos/
│   │                   │   ├── AlumnoRepository.java
│   │                   │   └── GrupoRepository.java
│   │                   ├── presentacion/
│   │                   │   └── MainController.java
│   │                   ├── servicios/
│   │                   │   ├── dto/
│   │                   │   │   ├── AlumnoDTO.java
│   │                   │   │   ├── GrupoDTO.java
│   │                   │   │   └── GrupoCreateDTO.java
│   │                   │   ├── dto/mapper/
│   │                   │   │   ├── AlumnoMapper.java
│   │                   │   │   └── GrupoMapper.java
│   │                   │   ├── exception/
│   │                   │   │   └── GlobalExceptionHandler.java
│   │                   │   ├── AlumnoController.java
│   │                   │   └── GrupoController.java
│   │                   ├── EjemplobackendApplication.java
│   │                   ├── OpenApiConfig.java
│   │                   └── SecurityConfig.java
│   └── resources/
│       └── application.yml
└── test/
    └── java/
        └── mx/
            └── uam/
                └── tsis/
                    └── ejemplobackend/
                        ├── negocio/
                        │   ├── AlumnoServiceTest.java
                        │   └── GrupoServiceTest.java
                        └── servicios/
                            ├── AlumnoControllerIntegrationTest.java
                            └── GrupoControllerIntegrationTest.java
```

## Cómo Compilar

Para compilar el proyecto, ejecute el siguiente comando en el directorio raíz del proyecto:

```bash
mvn clean install
```

Esto compilará el código, ejecutará las pruebas y creará un archivo JAR en el directorio `target`.

## Cómo Ejecutar

### Usando Maven

Para ejecutar la aplicación usando Maven:

```bash
mvn spring-boot:run
```

### Usando el archivo JAR

Después de construir el proyecto, puede ejecutar el archivo JAR generado:

```bash
java -jar target/ejemplobackend-0.0.1-SNAPSHOT.jar
```

## Acceso a la Aplicación

Una vez que la aplicación esté en ejecución, puede acceder a:

- **Documentación de la API**: http://localhost:8080/swagger-ui.html
- **Consola H2**: http://localhost:8080/h2-console
  - URL JDBC: jdbc:h2:mem:testdb
  - Usuario: sa
  - Contraseña: (dejar vacío)

## Endpoints de la API

### API de Estudiantes

- `GET /v1/alumnos` - Obtener todos los estudiantes
- `GET /v1/alumnos/{matricula}` - Obtener un estudiante por ID
- `POST /v1/alumnos` - Crear un nuevo estudiante
- `PUT /v1/alumnos/{matricula}` - Actualizar un estudiante

### API de Grupos

- `GET /v1/grupos` - Obtener todos los grupos
- `POST /v1/grupos` - Crear un nuevo grupo (sin alumnos)
- `POST /v1/grupos/{id}/alumnos?matricula=1234` - Agregar un estudiante a un grupo

## Arquitectura

El proyecto sigue una arquitectura en capas con las siguientes características:

1. **Capa de Presentación (Controllers)**
   - Maneja las peticiones HTTP
   - Trabaja exclusivamente con DTOs
   - Valida los datos de entrada
   - Documenta la API con Swagger

2. **Capa de Negocio (Services)**
   - Implementa la lógica de negocio
   - Trabaja con entidades del dominio
   - Maneja las transacciones
   - Lanza excepciones específicas del dominio

3. **Capa de Datos (Repositories)**
   - Gestiona la persistencia de datos
   - Trabaja con entidades JPA

4. **DTOs y Mappers**
   - `AlumnoDTO` y `GrupoDTO` para transferencia de datos
   - `GrupoCreateDTO` para la creación específica de grupos
   - Mappers para conversión bidireccional entre DTOs y entidades

5. **Manejo de Excepciones**
   - Excepciones específicas del dominio en la capa de negocio
   - `BusinessException` como base para todas las excepciones de negocio
   - `EntityNotFoundException` para entidades no encontradas
   - `DuplicateEntityException` para entidades duplicadas
   - `GlobalExceptionHandler` para traducir excepciones de negocio a respuestas HTTP

## Mejoras Recientes

- **Eliminación de autenticación básica**: Se ha eliminado la autenticación básica para facilitar las pruebas y el desarrollo.
- **Excepciones específicas del dominio**: Se ha implementado una jerarquía de excepciones de negocio para mejorar la separación de capas.
- **Optimización de carga de colecciones**: Se ha configurado la relación `Grupo-Alumnos` con `FetchType.EAGER` para evitar problemas de inicialización perezosa en las pruebas.

## Desarrollo

La aplicación está configurada para usar el perfil "development" por defecto. Este perfil utiliza una base de datos H2 en memoria para facilitar el desarrollo y las pruebas.

## Pruebas

El proyecto incluye pruebas unitarias y de integración. Para ejecutar las pruebas:

```bash
mvn test
```

Las pruebas utilizan Mockito para simular dependencias y JUnit 5 para las aserciones.

## Contacto

Humberto Cervantes hcm@xanum.uam.mx

 