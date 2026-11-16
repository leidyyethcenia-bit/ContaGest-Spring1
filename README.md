# ContaGest Spring - Módulo de clientes (Spring Boot)

Módulo de clientes de un **software para despacho contable**, construido con el framework
**Spring Boot**. Incluye páginas web (Spring MVC + Thymeleaf), una **API REST** en formato JSON y
almacenamiento de datos con **Spring Data JPA (Hibernate)** sobre la base de datos **H2**.

- **Evidencia:** GA7-220501096-AA3-EV01 - Codificación de módulos del software
- **Aprendiz:** Leidy Yethcenia Muñoz Rodríguez
- **Framework:** Spring Boot 3.4.1 (Spring MVC, Spring Data JPA, Bean Validation, Thymeleaf)
- **Lenguaje:** Java 17 o superior
- **Base de datos:** H2 en modo archivo (no requiere instalación)

## ¿Por qué Spring Boot?

| Necesidad del despacho contable | Qué aporta Spring Boot |
|---|---|
| Aplicación web con páginas y formularios | Spring MVC + Thymeleaf |
| Servicios para otras aplicaciones (por ejemplo una app móvil) | API REST con `@RestController` |
| Guardar clientes sin escribir SQL a mano | Spring Data JPA: el CRUD se genera automáticamente |
| Datos válidos (NIT, correo, teléfono) | Validación con anotaciones (`@NotBlank`, `@Pattern`, `@Email`) |
| Arrancar rápido sin instalar ni configurar un servidor | Servidor Tomcat embebido y configuración automática |

Comparado con la versión de servlets y JDBC (evidencias anteriores), el framework elimina el código
repetitivo: no hay que abrir conexiones, escribir `PreparedStatement`, ni registrar servlets uno
por uno.

## Cómo ejecutarlo

Requisitos: JDK 17+ y Maven (GitHub Codespaces ya los incluye).

```bash
./ejecutar.sh        # Linux, Mac y Codespaces  (equivale a: mvn spring-boot:run)
ejecutar.bat         # Windows
```

La primera vez Maven descarga las dependencias (1 a 3 minutos). Cuando aparezca
`Started ContaGestApplication`, abra **http://localhost:8080**. En Codespaces, abra la pestaña
**Puertos** y pulse el ícono del navegador junto al puerto 8080. Para detener: `Ctrl + C`.

Al iniciar por primera vez se cargan tres clientes de ejemplo.

## Arquitectura por capas

```
Navegador / app móvil
        |
 Controlador   ClienteWebControlador (HTML)   ClienteApiControlador (JSON)
        |
 Servicio      ClienteServicio  (reglas de negocio y transacciones)
        |
 Repositorio   ClienteRepositorio  (Spring Data JPA)
        |
 Base de datos H2  (tabla clientes)
```

## Estructura del proyecto

```
src/main/java/com/contagest/
  ContaGestApplication.java        clase principal (@SpringBootApplication)
  modelo/                          Cliente (entidad JPA) y TipoPersona
  repositorio/ClienteRepositorio   acceso a datos (Spring Data JPA)
  servicio/ClienteServicio         lógica de negocio
  controlador/                     controladores web y REST, manejo de errores de la API
  excepcion/                       excepciones propias
  config/CargadorDatosIniciales    datos de ejemplo al primer inicio
src/main/resources/
  application.properties           configuración (puerto, base de datos, JPA)
  templates/                       vistas Thymeleaf (inicio, lista, formulario, detalle, error)
  static/css/estilos.css           estilos
pom.xml                            dependencias del proyecto (Maven)
```

## Almacenamiento de datos

- **Spring Data JPA + Hibernate:** la clase `Cliente` (anotada con `@Entity`) se convierte en la
  tabla `clientes`. Hibernate crea y ajusta la tabla automáticamente (`ddl-auto=update`).
- **H2 en modo archivo:** los datos se guardan en la carpeta `data/` y se conservan al reiniciar.
- **Consola de H2:** con la aplicación en ejecución, abra **http://localhost:8080/h2-console**,
  use la URL `jdbc:h2:file:./data/contagest-spring`, usuario `sa` y contraseña vacía, para ver
  la tabla y ejecutar consultas SQL (`SELECT * FROM clientes`).
- **Cambiar de base de datos:** para usar MySQL o PostgreSQL solo se agrega el driver en el
  `pom.xml` y se cambian las líneas `spring.datasource.*` de `application.properties`.
  El código Java no cambia.

## Operaciones CRUD

| Operación | Página web | API REST | Método del servicio |
|---|---|---|---|
| Crear (INSERT) | `POST /clientes/guardar` | `POST /api/clientes` | `registrar` |
| Consultar (SELECT) | `GET /clientes`, `/clientes?q=`, `/clientes/{id}` | `GET /api/clientes`, `/api/clientes/{id}` | `listar`, `consultarPorId` |
| Actualizar (UPDATE) | `POST /clientes/guardar` (con id) | `PUT /api/clientes/{id}` | `actualizar` |
| Eliminar (DELETE) | `POST /clientes/{id}/eliminar` | `DELETE /api/clientes/{id}` | `eliminar` |

## Probar la API REST

Con la aplicación en ejecución, en otra terminal:

```bash
# Listar clientes
curl http://localhost:8080/api/clientes

# Consultar un cliente
curl http://localhost:8080/api/clientes/1

# Crear un cliente (responde 201)
curl -X POST http://localhost:8080/api/clientes -H "Content-Type: application/json" \
  -d '{"nit":"901555666-2","razonSocial":"Taller Los Andes","tipoPersona":"JURIDICA","correo":"taller@andes.co","telefono":"3001112233","activo":true}'

# Actualizar
curl -X PUT http://localhost:8080/api/clientes/1 -H "Content-Type: application/json" \
  -d '{"nit":"900123456-7","razonSocial":"Ferreteria El Tornillo y Cia","tipoPersona":"JURIDICA","correo":"contacto@eltornillo.com","telefono":"3101234567","activo":true}'

# Eliminar (responde 204)
curl -X DELETE http://localhost:8080/api/clientes/3

# Datos inválidos (responde 400 con el detalle de cada campo)
curl -X POST http://localhost:8080/api/clientes -H "Content-Type: application/json" \
  -d '{"nit":"12","razonSocial":"","correo":"malo"}'
```

Códigos de respuesta: `200` correcto, `201` creado, `204` eliminado, `400` datos inválidos,
`404` cliente no existe, `409` NIT repetido.

## Estándar de codificación aplicado

| Elemento | Convención | Ejemplo |
|---|---|---|
| Paquetes | minúscula, uno por capa | `com.contagest.servicio` |
| Clases | PascalCase, sustantivo; sufijo según su función | `ClienteServicio`, `ClienteRepositorio`, `ClienteWebControlador` |
| Métodos | camelCase, inician con verbo | `registrar`, `consultarPorId`, `mostrarDetalle` |
| Variables y atributos | camelCase, descriptivos | `razonSocial`, `clienteRepositorio` |
| Constantes | MAYUSCULAS_CON_GUION_BAJO | `VISTA_LISTA`, `REDIRIGIR_A_LISTA` |
| Excepciones | PascalCase terminado en `Exception` | `NitDuplicadoException` |
| Tablas y columnas | minúscula, snake_case | `clientes`, `razon_social` |
| URLs | minúscula y plural | `/clientes`, `/api/clientes` |
| Plantillas | minúscula, agrupadas por módulo | `clientes/lista.html` |

Los nombres de los métodos del repositorio que Spring Data interpreta (`findByNit`,
`existsByNitAndIdNot`...) llevan sus palabras clave en inglés porque así lo exige el framework.

**Comentarios:** todas las clases tienen comentario Javadoc que explica su función, los métodos
públicos están documentados y las líneas con lógica menos evidente llevan comentarios en línea.

## Buenas prácticas aplicadas

- Inyección de dependencias por constructor (Spring entrega los objetos ya creados).
- Separación en capas: el controlador no contiene reglas de negocio ni consultas.
- Validación en la entidad: se aplica igual en el formulario web y en la API.
- Patrón Post-Redirect-Get tras guardar o eliminar (evita reenviar el formulario al recargar).
- Textos escapados en las plantillas (`th:text`), lo que evita ataques XSS.
- Eliminar y guardar solo por POST; consultar por GET.
- Transacciones con `@Transactional` en el servicio.

## Control de versiones

Repositorio remoto: ver `ENLACE_REPOSITORIO.txt` (en la carpeta comprimida).

```bash
git add .
git commit -m "feat: modulo de clientes con Spring Boot"
git push origin main
```
