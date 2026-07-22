# Plataforma de gestión de turnos

Plataforma web para la **gestión de turnos** orientada a profesionales y negocios de servicios (peluquerías, entrenadores, profesionales de la salud, complejos deportivos, etc.). Permite centralizar la administración de agendas, publicar disponibilidad, reservar/cancelar/reprogramar turnos y reducir las pérdidas por ausencias y dobles reservas.

Proyecto desarrollado para la materia **Ingeniería de Software I — FIUBA**.

## Objetivo del proyecto

Resolver la fricción que enfrentan los profesionales que gestionan turnos de forma manual (WhatsApp, agenda en papel): superposición de horarios, cancelaciones de último momento sin penalización, ausencias que dejan lugares vacíos, y comunicación dispersa. La plataforma ofrece una solución centralizada que minimiza la fricción tanto para el profesional como para el cliente final.

El análisis del problema, el relevamiento y la validación están documentados en [`docs/01-discovery/`](docs/01-discovery/).

## Estructura del repositorio

```
.
├── backend/        API REST en Java 21 / Spring Boot (lógica de negocio)
├── frontend/       Aplicación web en React 19 + TypeScript + Vite
├── ingress/        Reverse proxy (Docker) que enruta las peticiones a los contenedores
├── data/           Volumen de datos local (PostgreSQL en desarrollo)
├── docs/           Documentación del proyecto (ver docs/README.md)
├── docker-compose.yml   Orquestación de todos los servicios
├── .gitlab-ci.yml       Pipeline de CI/CD (build, test y deploy a la nube)
├── .env / .env.prod     Variables de entorno (desarrollo / producción)
└── README.md
```

- **Backend:** [backend/README.md](backend/README.md)
- **Frontend:** [frontend/README.md](frontend/README.md)
- **Ingress:** [ingress/README.md](ingress/README.md)

## Cómo instalarlo

Requisitos: [Docker](https://docs.docker.com/get-docker/) y Docker Compose.

Para desarrollo individual de cada subproyecto:
- Backend: JDK 21 y Maven (incluye wrapper `./mvnw`).
- Frontend: Node.js 20+ y npm.

```bash
git clone <url-del-repositorio>
cd <repositorio>
```

## Cómo ejecutarlo

### Opción A — Todo el sistema con Docker Compose (recomendado)

1. Revisar las variables de entorno en `.env`.
2. Levantar el sistema:
   ```bash
   docker compose up -d --build --remove-orphans
   ```
3. Acceder a la aplicación en la URL definida por `EXTERNAL_URL` (por defecto
   `http://localhost:20000`).

### Opción B — Subproyectos por separado (desarrollo)

```bash
# Backend
cd backend && ./mvnw spring-boot:run

# Frontend
cd frontend && npm install && npm run dev
```

### Tests

```bash
# Backend (incluye reporte de cobertura JaCoCo)
cd backend && ./mvnw test

# Frontend
cd frontend && npm test
```

## Cómo desplegarlo

El despliegue se realiza automáticamente sobre el servidor en la nube de la cátedra mediante el pipeline definido en [`.gitlab-ci.yml`](.gitlab-ci.yml). El ambiente productivo se configura con `.env.prod`.

- Ambiente desplegado: `https://grupo-09.tp1.ingsoft1.fiuba.ar`
- Diagrama de despliegue: ![Layout del servidor](docs/server.png)

Detalles de la estrategia de ramas y del flujo de CI/CD en [docs/05-ingenieria-y-calidad/control-de-versiones.md](docs/05-ingenieria-y-calidad/control-de-versiones.md).

## Cómo acceder a la documentación

Toda la documentación está en [`docs/`](docs/). El índice general está en [docs/README.md](docs/README.md) e incluye:

- **Discovery y relevamiento** — problema, requerimientos, encuestas, entrevistas,   user personas, mapas de empatía e hipótesis validadas.
- **Trabajo por sprints** — objetivos, roles, resultados y evidencias de los 4 sprints.
- **Arquitectura** — documento basado en el modelo 4+1, modelo de datos, decisiones de diseño, atributos de calidad y documentación de la API.
- **Uso de IA** durante el desarrollo.
- **Ingeniería y calidad** — control de versiones, testing, Clean Code/SOLID y parametrización.
- **Evidencias de testing**.
- **Video Elevator Pitch** (`docs/video/`).
