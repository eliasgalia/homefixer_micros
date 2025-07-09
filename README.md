
Cada microservicio contiene:

- `model/`: Entidad JPA principal
- `repository/`: Interface JPA
- `service/`: Lógica de negocio
- `controller/`: Endpoints RESTful con HATEOAS
- `config/`: OpenAPI + Faker (DataLoader)
- `test/`: Unit tests con JUnit 5
- `resources/`: `application.properties`

---

## 🚀 Microservicios Incluidos

### 🔔 `ms-notificaciones`
- **Puerto**: 8083
- **Base de datos**: `homefixer_notificaciones`
- **Entidad**: `Notificacion`
- **Función**: Enviar y gestionar notificaciones tipo `SOLICITUD`, `ASIGNACION`, `COMPLETADO`, `CANCELADO`, `PROMOCIONAL`
- **Endpoints**:
  - `GET /api/notificaciones`
  - `GET /api/notificaciones/{id}`
  - `GET /api/notificaciones/destinatario/{id}`
  - `GET /api/notificaciones/tipo/{tipo}`
  - `GET /api/notificaciones/no-leidas/{id}`
  - `POST /api/notificaciones`
  - `PUT /api/notificaciones/{id}/marcar-leida`
  - `DELETE /api/notificaciones/{id}` _(soft delete)_

### ⭐ `ms-valoraciones`
- **Puerto**: 8084
- **Base de datos**: `homefixer_valoraciones`
- **Entidad**: `Valoracion`
- **Función**: Sistema de reputación para técnicos (puntaje, comentario, recomendación)
- **Endpoints**:
  - `POST /api/valoraciones`
  - `GET /api/valoraciones/tecnico/{id}`
  - `GET /api/valoraciones/cliente/{id}`
  - `GET /api/valoraciones/promedio/{id}`
  - `GET /api/valoraciones/{id}`

### 💳 `ms-pagos`
- **Puerto**: 8085
- **Base de datos**: `homefixer_pagos`
- **Entidad**: `Pago`
- **Función**: Procesar pagos entre clientes y técnicos (con comisión para la plataforma)
- **Endpoints**:
  - `POST /api/pagos/procesar`
  - `GET /api/pagos/cliente/{id}`
  - `GET /api/pagos/tecnico/{id}`
  - `GET /api/pagos/estadisticas`

---

## 🧪 Testing

Cada microservicio incluye pruebas unitarias con `JUnit 5` siguiendo el patrón **ARRANGE – ACT – ASSERT**.  
Los tests se encuentran en: `src/test/java/com/homefixer/service/`

Para ejecutarlos:

```bash
./mvnw test
