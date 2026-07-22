-- Registrar un usuario.
INSERT INTO Usuario (email, rol, contrasenia, cantidad_ingresos, ISO_pais)
VALUES ('nuevo.usuario@fi.uba.ar', 'USUARIO', 'hash_nuevo_pass', 0, 'ARG');

-- Registrar un torneo.
INSERT INTO Torneo (nombre_torneo, fecha_inicio, fecha_fin)
VALUES ('Copa Libertadores 2025', '2025-02-01', '2025-11-30');

-- Registrar un equipo.
INSERT INTO Equipo (nombre_equipo, ciudad)
VALUES ('San Lorenzo', 'Buenos Aires');

-- Registrar un pronóstico deportivo.
INSERT INTO Pronostico (email, nombre_torneo, equipo_local, equipo_visitante, fecha_hora, result_pronosticado)
VALUES ('tomas.goncalves@fi.uba.ar',
        'Mundial de Clubes 2025',
        'Real Madrid',
        'Manchester City',
        '2025-06-22 18:00:00',
        'LOCAL');

-- Registrar un resultado de un partido.
UPDATE Partido
SET result_partido = 'LOCAL'
WHERE nombre_torneo    = 'Mundial de Clubes 2025'
  AND equipo_local     = 'Real Madrid'
  AND equipo_visitante = 'Manchester City'
  AND fecha_hora       = '2025-06-22 18:00:00';

-- Listar todos los usuarios de la aplicación.
SELECT email,
       rol,
       cantidad_ingresos,
       ISO_pais
FROM Usuario
ORDER BY email;

-- Listar los resultados de todos los partidos del torneo.
SELECT nombre_torneo,
       equipo_local,
       equipo_visitante,
       fecha_hora,
       COALESCE(result_partido, 'PENDIENTE') AS resultado
FROM Partido
WHERE nombre_torneo = 'Liga Profesional 2024'
ORDER BY fecha_hora;

-- Listar los aciertos por usuario.
WITH Aciertos AS (
    SELECT p.email, COUNT(*) AS cantidad
    FROM Pronostico p
    INNER JOIN Partido pa ON pa.nombre_torneo    = p.nombre_torneo
                         AND pa.equipo_local     = p.equipo_local
                         AND pa.equipo_visitante = p.equipo_visitante
                         AND pa.fecha_hora       = p.fecha_hora
    WHERE p.result_pronosticado = pa.result_partido AND pa.result_partido IS NOT NULL
    GROUP BY p.email
)
SELECT u.email, COALESCE(a.cantidad, 0) AS cantidad_aciertos
FROM Usuario AS u
LEFT JOIN Aciertos AS a ON u.email = a.email
ORDER BY cantidad_aciertos DESC, u.email;

-- Contabilizar la cantidad de usuarios, agrupados por país.
SELECT pa.ISO,
       pa.nombre_pais,
       COUNT(u.email) AS cantidad_usuarios
FROM Pais pa
LEFT JOIN Usuario u ON u.ISO_pais = pa.ISO
GROUP BY pa.ISO, pa.nombre_pais
ORDER BY cantidad_usuarios DESC, pa.nombre_pais;

-- Actualizar el nombre de un torneo.
UPDATE Torneo
SET nombre_torneo = 'Copa America 2024 - Edicion EEUU'
WHERE nombre_torneo = 'Copa America 2024';

-- Eliminar un torneo.
DELETE FROM Torneo
WHERE nombre_torneo = 'Champions League 2024';

-- Desregistrar a un usuario de la aplicación (dar un ejemplo).
DELETE FROM Usuario
WHERE email = 'carlos.romero@gmail.com';

-- Mostrar los torneos más populares basandose en la cantidad de usuarios pronosticadores que posee dicho torneo.
WITH Usuarios_Por_Torneo AS (
    SELECT nombre_torneo, COUNT(DISTINCT email) AS cant_usuarios
    FROM Pronostico
    GROUP BY nombre_torneo
)
SELECT nombre_torneo, cant_usuarios
FROM Usuarios_Por_Torneo
WHERE cant_usuarios = (
    SELECT MAX(cant_usuarios)
    FROM Usuarios_Por_Torneo
);

-- Registrar un ingreso: la aplicación ejecuta este UPDATE en cada login exitoso del usuario.

UPDATE Usuario
SET cantidad_ingresos = cantidad_ingresos + 1
WHERE email = 'tomas.goncalves@fi.uba.ar';

-- Contabilizar la cantidad de ingresos por usuario.

SELECT email, cantidad_ingresos
FROM Usuario
ORDER BY cantidad_ingresos DESC, email;
