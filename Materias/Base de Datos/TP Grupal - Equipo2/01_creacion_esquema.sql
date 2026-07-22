-- Crear base de datos
CREATE DATABASE pronosticos_db
  WITH ENCODING 'UTF8'
       TEMPLATE template0;

-- Ingresar a la base creada
\c pronosticos_db

-- Tabla: Pais
CREATE TABLE Pais (
    ISO          CHAR(3)      NOT NULL,
    nombre_pais  VARCHAR(100) NOT NULL,


    CONSTRAINT pk_pais         PRIMARY KEY (ISO),
    CONSTRAINT uq_pais_nombre  UNIQUE (nombre_pais)
);

-- Tabla: Usuario
CREATE TABLE Usuario (
    email              VARCHAR(150) NOT NULL,
    rol                VARCHAR(20)  NOT NULL,
    contrasenia        VARCHAR(255) NOT NULL,
    cantidad_ingresos  INTEGER      NOT NULL DEFAULT 0,
    ISO_pais           CHAR(3)      NOT NULL,


    CONSTRAINT pk_usuario        PRIMARY KEY (email),
    CONSTRAINT fk_usuario_pais   FOREIGN KEY (ISO_pais)
                                 REFERENCES Pais(ISO)
                                 ON UPDATE CASCADE
                                 ON DELETE RESTRICT,

    -- Validacion basica de email
    CONSTRAINT ck_usuario_email  CHECK (email ~* '^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}$'),
    -- Rol dentro de un dominio cerrado
    CONSTRAINT ck_usuario_rol    CHECK (rol IN ('ADMINISTRADOR', 'USUARIO')),
    -- Contador de ingresos no negativo
    CONSTRAINT ck_usuario_ingresos CHECK (cantidad_ingresos >= 0)
);

-- Tabla: Torneo
CREATE TABLE Torneo (
    nombre_torneo  VARCHAR(100) NOT NULL,
    fecha_inicio   DATE         NOT NULL,
    fecha_fin      DATE         NOT NULL,

    CONSTRAINT pk_torneo        PRIMARY KEY (nombre_torneo),
    CONSTRAINT ck_torneo_fechas CHECK (fecha_fin >= fecha_inicio)
);

-- Tabla: Equipo
CREATE TABLE Equipo (
    nombre_equipo  VARCHAR(100) NOT NULL,
    ciudad         VARCHAR(100) NOT NULL,

    CONSTRAINT pk_equipo PRIMARY KEY (nombre_equipo)
);

-- Tabla: Participacion
CREATE TABLE Participacion (
    nombre_equipo      VARCHAR(100) NOT NULL,
    nombre_torneo      VARCHAR(100) NOT NULL,
    anio_participacion INTEGER      NOT NULL,


    CONSTRAINT pk_participacion       PRIMARY KEY (nombre_equipo, nombre_torneo, anio_participacion),
    CONSTRAINT fk_participacion_eq    FOREIGN KEY (nombre_equipo)
                                      REFERENCES Equipo(nombre_equipo)
                                      ON UPDATE CASCADE
                                      ON DELETE CASCADE,
    CONSTRAINT fk_participacion_torn  FOREIGN KEY (nombre_torneo)
                                      REFERENCES Torneo(nombre_torneo)
                                      ON UPDATE CASCADE
                                      ON DELETE CASCADE,

    CONSTRAINT ck_participacion_anio  CHECK (anio_participacion BETWEEN 1900 AND 2100)
);

-- Tabla: Partido
CREATE TABLE Partido (
    nombre_torneo     VARCHAR(100) NOT NULL,
    equipo_local      VARCHAR(100) NOT NULL,
    equipo_visitante  VARCHAR(100) NOT NULL,
    fecha_hora        TIMESTAMP    NOT NULL,
    result_partido    VARCHAR(10),   -- NULL mientras no se haya jugado

    CONSTRAINT pk_partido           PRIMARY KEY (nombre_torneo, equipo_local, equipo_visitante, fecha_hora),
    CONSTRAINT fk_partido_torneo    FOREIGN KEY (nombre_torneo)
                                    REFERENCES Torneo(nombre_torneo)
                                    ON UPDATE CASCADE
                                    ON DELETE CASCADE,
    CONSTRAINT fk_partido_local     FOREIGN KEY (equipo_local)
                                    REFERENCES Equipo(nombre_equipo)
                                    ON UPDATE CASCADE
                                    ON DELETE RESTRICT,
    CONSTRAINT fk_partido_visitante FOREIGN KEY (equipo_visitante)
                                    REFERENCES Equipo(nombre_equipo)
                                    ON UPDATE CASCADE
                                    ON DELETE RESTRICT,

    -- Un equipo no puede jugar contra si mismo
    CONSTRAINT ck_partido_equipos   CHECK (equipo_local <> equipo_visitante),
    -- Dominio cerrado para el resultado
    CONSTRAINT ck_partido_result    CHECK (result_partido IN ('LOCAL', 'VISITANTE', 'EMPATE'))
);

-- Tabla: Pronostico
CREATE TABLE Pronostico (
    email                VARCHAR(150) NOT NULL,
    nombre_torneo        VARCHAR(100) NOT NULL,
    equipo_local         VARCHAR(100) NOT NULL,
    equipo_visitante     VARCHAR(100) NOT NULL,
    fecha_hora           TIMESTAMP    NOT NULL,
    result_pronosticado  VARCHAR(10)  NOT NULL,

    CONSTRAINT pk_pronostico         PRIMARY KEY (email, nombre_torneo, equipo_local, equipo_visitante, fecha_hora),
    CONSTRAINT fk_pronostico_usuario FOREIGN KEY (email)
                                     REFERENCES Usuario(email)
                                     ON UPDATE CASCADE
                                     ON DELETE CASCADE,
    CONSTRAINT fk_pronostico_partido FOREIGN KEY (nombre_torneo, equipo_local, equipo_visitante, fecha_hora)
                                     REFERENCES Partido(nombre_torneo, equipo_local, equipo_visitante, fecha_hora)
                                     ON UPDATE CASCADE
                                     ON DELETE CASCADE,

    CONSTRAINT ck_pronostico_result  CHECK (result_pronosticado IN ('LOCAL', 'VISITANTE', 'EMPATE'))
);

-- Indice sobre la FK de Usuario hacia Pais
CREATE INDEX idx_usuario_pais ON Usuario(ISO_pais);

-- Indices sobre las FKs de Partido
CREATE INDEX idx_partido_torneo    ON Partido(nombre_torneo);
CREATE INDEX idx_partido_local     ON Partido(equipo_local);
CREATE INDEX idx_partido_visitante ON Partido(equipo_visitante);

-- Indices sobre las FKs de Pronostico
CREATE INDEX idx_pronostico_usuario ON Pronostico(email);
CREATE INDEX idx_pronostico_partido ON Pronostico(nombre_torneo, equipo_local, equipo_visitante, fecha_hora);

-- Indices sobre las FKs de Participacion
CREATE INDEX idx_participacion_torneo ON Participacion(nombre_torneo);
CREATE INDEX idx_participacion_equipo ON Participacion(nombre_equipo);
