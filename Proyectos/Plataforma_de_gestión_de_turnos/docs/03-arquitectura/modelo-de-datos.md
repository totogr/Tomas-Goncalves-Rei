# Modelo de datos

El modelo de datos representa los clientes, profesionales, sus servicios y empleados, los turnos y las entidades de soporte (políticas de cancelación, lista de espera y reseñas).

> Implementación: PostgreSQL, con migraciones versionadas mediante **Liquibase** (`backend/src/main/resources/db/changelog/`).

## Usuarios

Clientes y profesionales son **entidades separadas** que comparten campos base (ver la decisión de diseño en [decisiones-de-diseno.md](decisiones-de-diseno.md)).

### Client
| Campo | Tipo | Descripción |
|-------|------|-------------|
| id | BIGINT (PK) | Identificador único |
| email | VARCHAR | Email de acceso (único) |
| password | VARCHAR | Contraseña hasheada |
| first_name | VARCHAR | Nombre |
| last_name | VARCHAR | Apellido |
| absence_count | INT | Contador de ausencias sin aviso (default 0) |
| receives_reminders | BOOLEAN | Si recibe emails recordatorios (default true) |

### Professional
| Campo | Tipo | Descripción |
|-------|------|-------------|
| id | BIGINT (PK) | Identificador único |
| email | VARCHAR | Email de acceso (único) |
| password | VARCHAR | Contraseña hasheada |
| first_name | VARCHAR | Nombre |
| last_name | VARCHAR | Apellido |
| specialty | VARCHAR | Especialidad (opcional) |
| address | VARCHAR | Dirección |
| neighborhood | VARCHAR | Barrio |
| city | VARCHAR | Ciudad |
| slot_interval_minutes | INT | Intervalo entre slots en minutos (default 30) |

## Servicios y disponibilidad

### Service
| Campo | Tipo | Descripción |
|-------|------|-------------|
| id | BIGINT (PK) | Identificador único |
| professional_id | BIGINT (FK → professional) | Profesional que lo ofrece |
| name | VARCHAR | Nombre del servicio |
| slug | VARCHAR | Versión URL del nombre |
| category | VARCHAR | Categoría del servicio |
| duration | INT | Duración en minutos |
| price | DECIMAL(10,2) | Precio |
| max_capacity | INT | Cupo máximo por turno |
| is_active | BOOLEAN | Si está disponible (default true) |

### Schedule
Horarios laborales semanales. Cada fila representa un rango horario para un día de la semana.

| Campo | Tipo | Descripción |
|-------|------|-------------|
| id | BIGINT (PK) | Identificador único |
| professional_id | BIGINT (FK → professional) | Profesional |
| day_week | INT | Día de la semana (0 = lunes … 6 = domingo) |
| start | TIME | Hora de inicio |
| end | TIME | Hora de fin |

### Schedule_Block
Bloqueos puntuales dentro de un día específico (ausencias, cortes de agenda, etc.).

| Campo | Tipo | Descripción |
|-------|------|-------------|
| id | BIGINT (PK) | Identificador único |
| professional_id | BIGINT (FK → professional) | Profesional |
| block_date | DATE | Fecha del bloqueo |
| start_time | TIME | Hora de inicio del bloqueo |
| end_time | TIME | Hora de fin del bloqueo |

## Empleados

### Employee
| Campo | Tipo | Descripción |
|-------|------|-------------|
| id | BIGINT (PK) | Identificador único |
| professional_id | BIGINT (FK → professional) | Profesional al que pertenece |
| first_name | VARCHAR | Nombre |
| last_name | VARCHAR | Apellido |
| profession | VARCHAR | Rol o especialidad |
| is_active | BOOLEAN | Si está activo (default true) |

### Employee_Service
Tabla de join entre empleados y los servicios que pueden atender.

| Campo | Tipo |
|-------|------|
| employee_id | BIGINT (FK → employee) |
| service_id | BIGINT (FK → service) |

PK compuesta: `(employee_id, service_id)`.

## Turnos

### Appointment
| Campo | Tipo | Descripción |
|-------|------|-------------|
| id | BIGINT (PK) | Identificador único |
| professional_id | BIGINT (FK → professional) | Profesional del turno |
| employee_id | BIGINT (FK → employee, nullable) | Empleado asignado |
| client_id | BIGINT (FK → client) | Cliente que reservó |
| service_id | BIGINT (FK → service) | Servicio reservado |
| start_time | TIMESTAMPTZ | Inicio del turno |
| end_time | TIMESTAMPTZ | Fin del turno |
| status | VARCHAR | Estado del turno (ver tabla de estados) |
| cancelled_by | VARCHAR | `client`, `professional` o null |
| cancelled_date | TIMESTAMPTZ | Fecha de cancelación (nullable) |
| marked_absent_at | TIMESTAMPTZ | Fecha en que se marcó ausencia (nullable) |
| reminder_sent | BOOLEAN | Si ya se envió el recordatorio (default false) |
| created_date | TIMESTAMPTZ | Fecha de creación (default NOW()) |

### Cancellation_Policy
| Campo | Tipo | Descripción |
|-------|------|-------------|
| id | BIGINT (PK) | Identificador único |
| professional_id | BIGINT (FK → professional) | Profesional |
| hours_before | INT | Horas mínimas para cancelar sin penalidad |
| block | BOOLEAN | Si se bloquea al cliente al superar el límite (default false) |
| absent_limit | INT | Cantidad de ausencias antes de aplicar la política (nullable) |

### Wait_List
Lista de espera por slot. Cada entrada representa un cliente que quiere un turno específico si se libera.

| Campo | Tipo | Descripción |
|-------|------|-------------|
| id | BIGINT (PK) | Identificador único |
| client_id | BIGINT (FK → client) | Cliente en espera |
| professional_id | BIGINT | Profesional del slot |
| service_id | BIGINT | Servicio solicitado |
| slot_start | TIMESTAMPTZ | Horario del slot deseado |
| creation_time | TIMESTAMPTZ | Momento de ingreso a la lista (default NOW()) |

Restricción única: `(client_id, professional_id, service_id, slot_start)`.

### Wait_List_Promotions
Ofertas de turno enviadas a clientes en lista de espera. El cliente tiene una ventana de tiempo para confirmar antes de que la oferta pase al siguiente.

| Campo | Tipo | Descripción |
|-------|------|-------------|
| id | BIGINT (PK) | Identificador único |
| wait_list_entry_id | BIGINT | Entrada de lista de espera que originó la promoción |
| client_id | BIGINT (FK → client) | Cliente al que se le ofreció el turno |
| professional_id | BIGINT (FK → professional) | Profesional |
| service_id | BIGINT (FK → service) | Servicio |
| slot_start | TIMESTAMPTZ | Horario del turno ofrecido |
| expires_at | TIMESTAMPTZ | Hasta cuándo puede confirmar el cliente |
| confirmed | BOOLEAN | Si el cliente confirmó (default false) |
| expired | BOOLEAN | Si la oferta venció sin confirmación (default false) |

### Blocked_Client
Clientes bloqueados por un profesional. Un cliente bloqueado no puede reservar turnos con ese profesional.

| Campo | Tipo | Descripción |
|-------|------|-------------|
| id | BIGINT (PK) | Identificador único |
| professional_id | BIGINT (FK → professional) | Profesional que bloqueó |
| client_id | BIGINT (FK → client) | Cliente bloqueado |
| blocked_at | TIMESTAMPTZ | Fecha del bloqueo |

Restricción única: `(professional_id, client_id)`.

## Reseñas

### Review
| Campo | Tipo | Descripción |
|-------|------|-------------|
| id | BIGINT (PK) | Identificador único |
| appointment_id | BIGINT (FK → appointment) | Turno reseñado |
| professional_id | BIGINT (FK → professional) | Profesional evaluado |
| client_id | BIGINT (FK → client) | Cliente que dejó la reseña |
| score | INT | Puntaje del 1 al 5 |

## Autenticación

### Refresh_Token
| Campo | Tipo | Descripción |
|-------|------|-------------|
| token_value | VARCHAR (PK) | Valor del token |
| user_id | BIGINT | ID del usuario (cliente o profesional) |
| user_type | VARCHAR | `CLIENT` o `PROFESSIONAL` |
| expires_at | TIMESTAMPTZ | Fecha de expiración |

### Password_Reset_Token
| Campo | Tipo | Descripción |
|-------|------|-------------|
| id | BIGINT (PK) | Identificador único |
| token_hash | VARCHAR | Hash del token (único) |
| user_id | BIGINT | ID del usuario |
| user_type | VARCHAR | `CLIENT` o `PROFESSIONAL` |
| expires_at | TIMESTAMPTZ | Fecha de expiración |
| used | BOOLEAN | Si ya fue utilizado (default false) |
| created_at | TIMESTAMPTZ | Fecha de creación (default NOW()) |

## Estados de un turno

| Status | Quién lo setea | Descripción |
|--------|----------------|-------------|
| CONFIRMED | Sistema al crear | Recién creado y confirmado |
| COMPLETED | Profesional | El turno se realizó |
| ABSENT | Profesional | El cliente no se presentó |
| CANCELLED | Cliente o profesional | Cancelado (ver `cancelled_by`) |

Cuando el status es `CANCELLED`, el campo `cancelled_by` se setea automáticamente como `client` o `professional` según quién canceló.



> [Diagrama UML](diagramas/UML.svg)