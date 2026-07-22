-- Paises
INSERT INTO Pais (ISO, nombre_pais) VALUES
    ('ARG', 'Argentina'),
    ('BRA', 'Brasil'),
    ('URU', 'Uruguay'),
    ('CHI', 'Chile'),
    ('ESP', 'España');

-- Usuarios
INSERT INTO Usuario (email, rol, contrasenia, cantidad_ingresos, ISO_pais) VALUES
    ('admin@pronosticos.com',       'ADMINISTRADOR', 'hash_admin_2024',     45, 'ARG'),
    ('tomas.goncalves@fi.uba.ar',   'USUARIO',       'hash_tomas_xyz',      28, 'ARG'),
    ('lucia.fernandez@gmail.com',   'USUARIO',       'hash_lucia_abc',      33, 'ARG'),
    ('martin.silva@hotmail.com',    'USUARIO',       'hash_martin_qwe',     12, 'URU'),
    ('joao.oliveira@uol.com.br',    'USUARIO',       'hash_joao_rty',       19, 'BRA'),
    ('camila.santos@globo.com',     'USUARIO',       'hash_camila_uio',      8, 'BRA'),
    ('pedro.gonzalez@outlook.com',  'USUARIO',       'hash_pedro_pas',      22, 'CHI'),
    ('valentina.lopez@yahoo.es',    'USUARIO',       'hash_valen_dfg',      15, 'ESP'),
    ('carlos.romero@gmail.com',     'USUARIO',       'hash_carlos_hjk',      5, 'ARG'),
    ('sofia.martinez@gmail.com',    'USUARIO',       'hash_sofia_zxc',      31, 'URU');

-- Torneos
INSERT INTO Torneo (nombre_torneo, fecha_inicio, fecha_fin) VALUES
    ('Copa America 2024',       '2024-06-20', '2024-07-14'),
    ('Mundial de Clubes 2025',  '2025-06-15', '2025-07-13'),
    ('Liga Profesional 2024',   '2024-01-26', '2024-12-15'),
    ('Champions League 2024',   '2024-09-17', '2025-05-31');

-- Equipos
INSERT INTO Equipo (nombre_equipo, ciudad) VALUES
    ('River Plate',     'Buenos Aires'),
    ('Boca Juniors',    'Buenos Aires'),
    ('Racing Club',     'Avellaneda'),
    ('Independiente',   'Avellaneda'),
    ('Flamengo',        'Rio de Janeiro'),
    ('Palmeiras',       'Sao Paulo'),
    ('Penarol',         'Montevideo'),
    ('Nacional',        'Montevideo'),
    ('Colo Colo',       'Santiago'),
    ('Real Madrid',     'Madrid'),
    ('FC Barcelona',    'Barcelona'),
    ('Manchester City', 'Manchester');

-- Participaciones (equipos en torneos por anio)
INSERT INTO Participacion (nombre_equipo, nombre_torneo, anio_participacion) VALUES
    -- Liga Profesional 2024
    ('River Plate',     'Liga Profesional 2024',  2024),
    ('Boca Juniors',    'Liga Profesional 2024',  2024),
    ('Racing Club',     'Liga Profesional 2024',  2024),
    ('Independiente',   'Liga Profesional 2024',  2024),

    -- Copa America 2024
    ('River Plate',     'Copa America 2024',      2024),
    ('Flamengo',        'Copa America 2024',      2024),
    ('Penarol',         'Copa America 2024',      2024),
    ('Colo Colo',       'Copa America 2024',      2024),

    -- Mundial de Clubes 2025
    ('River Plate',     'Mundial de Clubes 2025', 2025),
    ('Flamengo',        'Mundial de Clubes 2025', 2025),
    ('Palmeiras',       'Mundial de Clubes 2025', 2025),
    ('Real Madrid',     'Mundial de Clubes 2025', 2025),
    ('Manchester City', 'Mundial de Clubes 2025', 2025),

    -- Champions League 2024
    ('Real Madrid',     'Champions League 2024',  2024),
    ('FC Barcelona',    'Champions League 2024',  2024),
    ('Manchester City', 'Champions League 2024',  2024);

-- Partidos
-- Algunos con resultado cargado (jugados) y otros sin resultado (pendientes)
INSERT INTO Partido (nombre_torneo, equipo_local, equipo_visitante, fecha_hora, result_partido) VALUES
    -- Liga Profesional 2024 (jugados)
    ('Liga Profesional 2024',  'River Plate',   'Boca Juniors',  '2024-04-21 17:00:00', 'LOCAL'),
    ('Liga Profesional 2024',  'Boca Juniors',  'Racing Club',   '2024-05-12 16:30:00', 'EMPATE'),
    ('Liga Profesional 2024',  'Racing Club',   'Independiente', '2024-06-02 18:00:00', 'VISITANTE'),
    ('Liga Profesional 2024',  'Independiente', 'River Plate',   '2024-07-14 17:00:00', 'EMPATE'),

    -- Copa America 2024 (jugados)
    ('Copa America 2024',      'River Plate',   'Flamengo',      '2024-06-22 21:00:00', 'LOCAL'),
    ('Copa America 2024',      'Penarol',       'Colo Colo',     '2024-06-25 20:00:00', 'EMPATE'),
    ('Copa America 2024',      'Flamengo',      'Penarol',       '2024-06-29 21:00:00', 'VISITANTE'),

    -- Champions League 2024 (jugados)
    ('Champions League 2024',  'Real Madrid',   'FC Barcelona',  '2024-10-26 17:00:00', 'LOCAL'),
    ('Champions League 2024',  'Manchester City','Real Madrid',  '2024-11-09 18:30:00', 'EMPATE'),

    -- Mundial de Clubes 2025 (pendientes, sin resultado aun)
    ('Mundial de Clubes 2025', 'River Plate',   'Flamengo',      '2025-06-18 21:00:00', NULL),
    ('Mundial de Clubes 2025', 'Real Madrid',   'Manchester City','2025-06-22 18:00:00', NULL),
    ('Mundial de Clubes 2025', 'Palmeiras',     'Real Madrid',   '2025-06-26 20:00:00', NULL);

-- Pronosticos
INSERT INTO Pronostico (email, nombre_torneo, equipo_local, equipo_visitante, fecha_hora, result_pronosticado) VALUES
    -- Tomas pronostica varios partidos
    ('tomas.goncalves@fi.uba.ar', 'Liga Profesional 2024',  'River Plate',    'Boca Juniors',    '2024-04-21 17:00:00', 'LOCAL'),
    ('tomas.goncalves@fi.uba.ar', 'Liga Profesional 2024',  'Boca Juniors',   'Racing Club',     '2024-05-12 16:30:00', 'LOCAL'),
    ('tomas.goncalves@fi.uba.ar', 'Copa America 2024',      'River Plate',    'Flamengo',        '2024-06-22 21:00:00', 'LOCAL'),
    ('tomas.goncalves@fi.uba.ar', 'Champions League 2024',  'Real Madrid',    'FC Barcelona',    '2024-10-26 17:00:00', 'LOCAL'),
    ('tomas.goncalves@fi.uba.ar', 'Mundial de Clubes 2025', 'River Plate',    'Flamengo',        '2025-06-18 21:00:00', 'LOCAL'),

    -- Lucia pronostica
    ('lucia.fernandez@gmail.com', 'Liga Profesional 2024',  'River Plate',    'Boca Juniors',    '2024-04-21 17:00:00', 'VISITANTE'),
    ('lucia.fernandez@gmail.com', 'Liga Profesional 2024',  'Boca Juniors',   'Racing Club',     '2024-05-12 16:30:00', 'EMPATE'),
    ('lucia.fernandez@gmail.com', 'Copa America 2024',      'Penarol',        'Colo Colo',       '2024-06-25 20:00:00', 'EMPATE'),
    ('lucia.fernandez@gmail.com', 'Mundial de Clubes 2025', 'Real Madrid',    'Manchester City', '2025-06-22 18:00:00', 'VISITANTE'),

    -- Martin pronostica
    ('martin.silva@hotmail.com',  'Copa America 2024',      'Penarol',        'Colo Colo',       '2024-06-25 20:00:00', 'LOCAL'),
    ('martin.silva@hotmail.com',  'Copa America 2024',      'Flamengo',       'Penarol',         '2024-06-29 21:00:00', 'VISITANTE'),
    ('martin.silva@hotmail.com',  'Mundial de Clubes 2025', 'River Plate',    'Flamengo',        '2025-06-18 21:00:00', 'EMPATE'),

    -- Joao pronostica
    ('joao.oliveira@uol.com.br',  'Copa America 2024',      'River Plate',    'Flamengo',        '2024-06-22 21:00:00', 'VISITANTE'),
    ('joao.oliveira@uol.com.br',  'Copa America 2024',      'Flamengo',       'Penarol',         '2024-06-29 21:00:00', 'LOCAL'),
    ('joao.oliveira@uol.com.br',  'Mundial de Clubes 2025', 'River Plate',    'Flamengo',        '2025-06-18 21:00:00', 'VISITANTE'),

    -- Camila pronostica
    ('camila.santos@globo.com',   'Copa America 2024',      'River Plate',    'Flamengo',        '2024-06-22 21:00:00', 'EMPATE'),
    ('camila.santos@globo.com',   'Mundial de Clubes 2025', 'Palmeiras',      'Real Madrid',     '2025-06-26 20:00:00', 'LOCAL'),

    -- Pedro pronostica
    ('pedro.gonzalez@outlook.com','Champions League 2024',  'Real Madrid',    'FC Barcelona',    '2024-10-26 17:00:00', 'LOCAL'),
    ('pedro.gonzalez@outlook.com','Champions League 2024',  'Manchester City','Real Madrid',     '2024-11-09 18:30:00', 'LOCAL'),
    ('pedro.gonzalez@outlook.com','Mundial de Clubes 2025', 'Real Madrid',    'Manchester City', '2025-06-22 18:00:00', 'LOCAL'),

    -- Valentina pronostica
    ('valentina.lopez@yahoo.es',  'Champions League 2024',  'Real Madrid',    'FC Barcelona',    '2024-10-26 17:00:00', 'LOCAL'),
    ('valentina.lopez@yahoo.es',  'Champions League 2024',  'Manchester City','Real Madrid',     '2024-11-09 18:30:00', 'EMPATE'),
    ('valentina.lopez@yahoo.es',  'Mundial de Clubes 2025', 'Real Madrid',    'Manchester City', '2025-06-22 18:00:00', 'EMPATE'),

    -- Sofia pronostica
    ('sofia.martinez@gmail.com',  'Copa America 2024',      'Penarol',        'Colo Colo',       '2024-06-25 20:00:00', 'EMPATE'),
    ('sofia.martinez@gmail.com',  'Copa America 2024',      'Flamengo',       'Penarol',         '2024-06-29 21:00:00', 'VISITANTE');
