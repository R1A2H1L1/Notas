# Notas App 

Aplicación web para gestionar notas personales, construida con **Spring Boot**, **H2 en memoria** y un frontend en HTML/CSS/JS puro.



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



- **Java 17**
- **Spring Boot 3.2**
- **Spring Web** — API REST
- **Spring Data JPA** — Acceso a datos
- **H2 Database** — Base de datos en memoria
- **JUnit 5 + Mockito** — Pruebas unitarias
- **Karate 1.4** — Pruebas de aceptación
- **HTML5 / CSS3 / JavaScript** — Frontend
