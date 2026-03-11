# 📝 Notas App — Spring Boot + H2

Aplicación web para gestionar notas personales, construida con **Spring Boot**, **H2 en memoria** y un frontend en HTML/CSS/JS puro.

---

## 🗂️ Estructura del proyecto

```
notas-app/
├── src/
│   ├── main/
│   │   ├── java/com/notas/app/
│   │   │   ├── NotasAppApplication.java       ← Punto de entrada
│   │   │   ├── entity/
│   │   │   │   └── Nota.java                  ← Entidad JPA
│   │   │   ├── repository/
│   │   │   │   └── NotaRepository.java        ← Repositorio JPA
│   │   │   ├── service/
│   │   │   │   └── NotaService.java           ← Lógica de negocio
│   │   │   └── controller/
│   │   │       └── NotaController.java        ← API REST
│   │   └── resources/
│   │       ├── application.properties         ← Configuración
│   │       └── static/
│   │           ├── index.html                 ← Frontend
│   │           ├── styles.css                 ← Estilos
│   │           └── app.js                     ← Lógica JS
│   └── test/
│       ├── java/com/notas/app/
│       │   ├── service/
│       │   │   └── NotaServiceTest.java       ← Pruebas JUnit 5
│       │   └── karate/
│       │       └── NotasApiRunner.java        ← Runner Karate
│       └── resources/
│           ├── karate-config.js               ← Config global Karate
│           └── karate/notas/
│               └── notas.feature              ← Escenarios Karate
└── pom.xml
```

---

## ✅ Requisitos previos

| Herramienta | Versión mínima |
|-------------|---------------|
| Java JDK    | 17            |
| Maven       | 3.8+          |

Verificar instalación:
```bash
java -version
mvn -version
```

---

## 🚀 Ejecutar la aplicación

### 1. Clonar / descomprimir el proyecto

```bash
cd notas-app
```

### 2. Compilar y ejecutar

```bash
mvn spring-boot:run
```

### 3. Acceder a la aplicación

| Recurso        | URL                                  |
|----------------|--------------------------------------|
| Frontend       | http://localhost:8080                |
| Consola H2     | http://localhost:8080/h2-console     |
| API REST       | http://localhost:8080/notas          |

**Credenciales consola H2:**
- JDBC URL: `jdbc:h2:mem:notasdb`
- User: `sa`
- Password: *(vacío)*

---

## 🌐 API REST — Endpoints

### POST /notas — Crear nota
```bash
curl -X POST http://localhost:8080/notas \
  -H "Content-Type: application/json" \
  -d '{"titulo":"Mi nota","contenido":"Contenido de la nota"}'
```
**Respuesta 201 Created:**
```json
{
  "id": 1,
  "titulo": "Mi nota",
  "contenido": "Contenido de la nota",
  "fechaCreacion": "2024-01-15T10:30:00"
}
```

### GET /notas — Obtener todas
```bash
curl http://localhost:8080/notas
```

### GET /notas/{id} — Obtener por id
```bash
curl http://localhost:8080/notas/1
```

### DELETE /notas/{id} — Eliminar
```bash
curl -X DELETE http://localhost:8080/notas/1
```

---

## 🧪 Ejecutar pruebas

### Todas las pruebas (JUnit + Karate)
```bash
mvn test
```

### Solo pruebas unitarias (JUnit 5)
```bash
mvn test -Dtest=NotaServiceTest
```

### Solo pruebas de aceptación (Karate)
> ⚠️ Las pruebas Karate levantan un servidor en un puerto aleatorio automáticamente.

```bash
mvn test -Dtest=NotasApiRunner
```

---

## 🔬 Descripción de las pruebas

### Pruebas unitarias — `NotaServiceTest`

Siguen el patrón **AAA (Arrange, Act, Assert)** con Mockito:

| Test | Qué valida |
|------|-----------|
| `guardarNota_debeRetornarNotaGuardada` | Que se retorna la nota con id asignado |
| `guardarNota_debeLlamarAlRepositorio` | Que se invoca `save()` exactamente una vez |
| `obtenerTodasLasNotas_debeRetornarListaCompleta` | Lista con todas las notas |
| `obtenerTodasLasNotas_listaVaciaSiNoHayNotas` | Lista vacía cuando no hay datos |
| `obtenerNotaPorId_debeRetornarNotaCuandoExiste` | Optional presente con los datos correctos |
| `obtenerNotaPorId_debeRetornarOptionalVacioCuandoNoExiste` | Optional vacío para id inexistente |
| `eliminarNota_debeEliminarCorrectamente` | Que `deleteById()` se llama correctamente |
| `eliminarNota_debeLlamarDeleteByIdConIdCorrecto` | Que se usa el id correcto |

### Pruebas de aceptación — `notas.feature`

Escenarios Karate sobre la API real:

| Escenario | Endpoint | Valida |
|-----------|----------|--------|
| Crear nota exitosamente | POST /notas | Status 201 y campos de respuesta |
| Crear múltiples notas | POST /notas | Creación independiente |
| Obtener todas las notas | GET /notas | Status 200 y estructura del array |
| Obtener nota por id | GET /notas/{id} | Status 200 y datos correctos |
| Id inexistente | GET /notas/999999 | Status 404 |
| Eliminar nota existente | DELETE /notas/{id} | Status 204 y posterior 404 |
| Eliminar nota inexistente | DELETE /notas/999999 | Status 404 |
| Flujo CRUD completo | Todos | Ciclo completo crear→buscar→listar→eliminar |

---

## 🛠️ Tecnologías utilizadas

- **Java 17**
- **Spring Boot 3.2**
- **Spring Web** — API REST
- **Spring Data JPA** — Acceso a datos
- **H2 Database** — Base de datos en memoria
- **JUnit 5 + Mockito** — Pruebas unitarias
- **Karate 1.4** — Pruebas de aceptación
- **HTML5 / CSS3 / JavaScript** — Frontend
