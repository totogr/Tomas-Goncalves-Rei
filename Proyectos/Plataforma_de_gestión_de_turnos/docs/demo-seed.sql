-- ============================================================================
--  SEED DE DEMO — Sistema de Turnos (Grupo 09)
--  Pegar completo en Adminer (Postgres) y ejecutar.
--  Password de TODOS los usuarios: 12345678
--
--  Fechas:
--   * Turnos PASADOS (estadísticas/reseñas): 2 al 9 de junio 2026.
--   * Turnos PRÓXIMOS (agenda): semana de entrega, 15 al 21 de junio 2026.
--  Slots en horario America/Argentina/Buenos_Aires.
--  Cada profesional tiene un intervalo de slot distinto (15/20/30/45/60 min).
-- ============================================================================

-- 1) Limpieza total de datos (respeta FKs y reinicia autoincrementales)
TRUNCATE
    wait_list_promotions, wait_list, review, blocked_client, schedule_block,
    schedule, appointment, password_reset_token, refresh_token,
    employee_service, employee, cancellation_policy, service, client, professional
RESTART IDENTITY CASCADE;

-- ============================================================================
-- 2) PROFESIONALES — intervalo de slot variado (slot_interval_minutes)
-- ============================================================================
INSERT INTO professional (id, email, password, first_name, last_name, specialty, address, neighborhood, city, slot_interval_minutes) VALUES
(1, 'lucia@demo.com',  '$2a$10$dN0QyvnhwozsuavPmH9BBOO5sExI5hWSzj3NmTl5zFHl6wa0JtPn.', 'Lucía',  'Fernández', 'Peluquería',      'Av. Santa Fe 1234',   'Palermo',  'CABA', 30),
(2, 'martin@demo.com', '$2a$10$dN0QyvnhwozsuavPmH9BBOO5sExI5hWSzj3NmTl5zFHl6wa0JtPn.', 'Martín', 'Gómez',     'Barbería',        'Av. Rivadavia 5000',  'Caballito','CABA', 15),
(3, 'sofia@demo.com',  '$2a$10$dN0QyvnhwozsuavPmH9BBOO5sExI5hWSzj3NmTl5zFHl6wa0JtPn.', 'Sofía',  'Ramírez',   'Manicura',        'Cabildo 2000',        'Belgrano', 'CABA', 20),
(4, 'diego@demo.com',  '$2a$10$dN0QyvnhwozsuavPmH9BBOO5sExI5hWSzj3NmTl5zFHl6wa0JtPn.', 'Diego',  'Torres',    'Masajes',         'Av. Las Heras 3000',  'Recoleta', 'CABA', 60),
(5, 'carla@demo.com',  '$2a$10$dN0QyvnhwozsuavPmH9BBOO5sExI5hWSzj3NmTl5zFHl6wa0JtPn.', 'Carla',  'Méndez',    'Estética facial', 'Av. Cabildo 4500',    'Núñez',    'CABA', 45),
(6, 'javier@demo.com', '$2a$10$dN0QyvnhwozsuavPmH9BBOO5sExI5hWSzj3NmTl5zFHl6wa0JtPn.', 'Javier', 'Ruiz',      'Kinesiología',    'Av. Corrientes 4000', 'Almagro',  'CABA', 30);

-- ============================================================================
-- 3) CLIENTES (16). Password: 12345678. Daniel tiene 2 ausencias.
-- ============================================================================
INSERT INTO client (id, email, password, first_name, last_name, absence_count, receives_reminders) VALUES
(1,  'ana@demo.com',      '$2a$10$dN0QyvnhwozsuavPmH9BBOO5sExI5hWSzj3NmTl5zFHl6wa0JtPn.', 'Ana',      'López',   0, true),
(2,  'bruno@demo.com',    '$2a$10$dN0QyvnhwozsuavPmH9BBOO5sExI5hWSzj3NmTl5zFHl6wa0JtPn.', 'Bruno',    'Díaz',    0, true),
(3,  'camila@demo.com',   '$2a$10$dN0QyvnhwozsuavPmH9BBOO5sExI5hWSzj3NmTl5zFHl6wa0JtPn.', 'Camila',   'Soto',    0, true),
(4,  'daniel@demo.com',   '$2a$10$dN0QyvnhwozsuavPmH9BBOO5sExI5hWSzj3NmTl5zFHl6wa0JtPn.', 'Daniel',   'Pérez',   2, true),
(5,  'elena@demo.com',    '$2a$10$dN0QyvnhwozsuavPmH9BBOO5sExI5hWSzj3NmTl5zFHl6wa0JtPn.', 'Elena',    'Vega',    0, true),
(6,  'franco@demo.com',   '$2a$10$dN0QyvnhwozsuavPmH9BBOO5sExI5hWSzj3NmTl5zFHl6wa0JtPn.', 'Franco',   'Ríos',    0, true),
(7,  'gabriela@demo.com', '$2a$10$dN0QyvnhwozsuavPmH9BBOO5sExI5hWSzj3NmTl5zFHl6wa0JtPn.', 'Gabriela', 'Suárez',  0, true),
(8,  'hernan@demo.com',   '$2a$10$dN0QyvnhwozsuavPmH9BBOO5sExI5hWSzj3NmTl5zFHl6wa0JtPn.', 'Hernán',   'Aguirre', 0, true),
(9,  'ines@demo.com',     '$2a$10$dN0QyvnhwozsuavPmH9BBOO5sExI5hWSzj3NmTl5zFHl6wa0JtPn.', 'Inés',     'Castro',  0, true),
(10, 'julian@demo.com',   '$2a$10$dN0QyvnhwozsuavPmH9BBOO5sExI5hWSzj3NmTl5zFHl6wa0JtPn.', 'Julián',   'Romero',  0, true),
(11, 'karen@demo.com',    '$2a$10$dN0QyvnhwozsuavPmH9BBOO5sExI5hWSzj3NmTl5zFHl6wa0JtPn.', 'Karen',    'Ortiz',   0, true),
(12, 'lucas@demo.com',    '$2a$10$dN0QyvnhwozsuavPmH9BBOO5sExI5hWSzj3NmTl5zFHl6wa0JtPn.', 'Lucas',    'Benítez', 0, true),
(13, 'marina@demo.com',   '$2a$10$dN0QyvnhwozsuavPmH9BBOO5sExI5hWSzj3NmTl5zFHl6wa0JtPn.', 'Marina',   'Acosta',  0, true),
(14, 'nicolas@demo.com',  '$2a$10$dN0QyvnhwozsuavPmH9BBOO5sExI5hWSzj3NmTl5zFHl6wa0JtPn.', 'Nicolás',  'Herrera', 0, true),
(15, 'olivia@demo.com',   '$2a$10$dN0QyvnhwozsuavPmH9BBOO5sExI5hWSzj3NmTl5zFHl6wa0JtPn.', 'Olivia',   'Sosa',    0, true),
(16, 'pablo@demo.com',    '$2a$10$dN0QyvnhwozsuavPmH9BBOO5sExI5hWSzj3NmTl5zFHl6wa0JtPn.', 'Pablo',    'Medina',  0, true);

-- ============================================================================
-- 4) SERVICIOS (service 9 = taller grupal, max_capacity = 3)
-- ============================================================================
INSERT INTO service (id, professional_id, category, slug, name, duration, price, max_capacity, is_active) VALUES
(1,  1, 'Peluquería',   'corte-cabello',    'Corte de cabello',           45,  8000.00, 1, true),
(2,  1, 'Peluquería',   'coloracion',       'Coloración',                 90, 20000.00, 1, true),
(3,  2, 'Barbería',     'corte-barba',      'Corte + barba',              30,  6000.00, 1, true),
(4,  2, 'Barbería',     'afeitado',         'Afeitado clásico',           30,  4500.00, 1, true),
(5,  3, 'Manicura',     'manicura',         'Manicura',                   60,  7000.00, 1, true),
(6,  3, 'Manicura',     'semipermanente',   'Esmaltado semipermanente',   45,  9000.00, 1, true),
(7,  4, 'Masajes',      'descontracturante','Masaje descontracturante',   60, 15000.00, 1, true),
(8,  5, 'Estética',     'limpieza-facial',  'Limpieza facial',            60, 12000.00, 1, true),
(9,  5, 'Estética',     'taller-skincare',  'Taller grupal de skincare',  90,  5000.00, 3, true),
(10, 6, 'Kinesiología', 'sesion-kinesio',   'Sesión kinesiológica',       45, 11000.00, 1, true);

-- ============================================================================
-- 5) HORARIOS DE ATENCIÓN: Lun–Vie 09:00–18:00 todos. Sofía además Sábado.
--    day_week: 1=Lunes ... 7=Domingo.
-- ============================================================================
INSERT INTO schedule (professional_id, day_week, "start", "end")
SELECT p, d, TIME '09:00', TIME '18:00'
FROM generate_series(1,6) AS p, generate_series(1,5) AS d;

INSERT INTO schedule (professional_id, day_week, "start", "end") VALUES
(3, 6, TIME '10:00', TIME '14:00');   -- Sofía: sábados por la mañana

-- ============================================================================
-- 6) HORARIOS BLOQUEADOS (schedule_block) — durante la semana de entrega
-- ============================================================================
INSERT INTO schedule_block (professional_id, block_date, start_time, end_time) VALUES
(1, DATE '2026-06-17', TIME '13:00', TIME '15:00'),   -- Lucía bloquea miércoles 13–15h
(5, DATE '2026-06-18', TIME '10:00', TIME '12:00');   -- Carla bloquea jueves 10–12h

-- ============================================================================
-- 7) CLIENTES BLOQUEADOS
-- ============================================================================
INSERT INTO blocked_client (professional_id, client_id, blocked_at) VALUES
(2, 6,  NOW() - INTERVAL '5 days'),    -- Martín bloquea a Franco
(1, 12, NOW() - INTERVAL '3 days');    -- Lucía bloquea a Lucas

-- ============================================================================
-- 8) TURNOS (appointment)
--    Columnas de v: id, prof, client, service, fecha-hora local, duración(min),
--    estado, cancelado_por, ausente.
-- ============================================================================
INSERT INTO appointment
    (id, professional_id, employee_id, client_id, service_id, start_time, end_time, status, cancelled_by, cancelled_date, marked_absent_at, created_date, reminder_sent)
SELECT
    v.id, v.prof, NULL, v.client, v.service,
    (v.ts::timestamp AT TIME ZONE 'America/Argentina/Buenos_Aires'),
    (v.ts::timestamp AT TIME ZONE 'America/Argentina/Buenos_Aires') + (v.dur || ' minutes')::interval,
    v.status,
    v.cancelled_by,
    CASE WHEN v.cancelled_by IS NOT NULL
         THEN (v.ts::timestamp AT TIME ZONE 'America/Argentina/Buenos_Aires') - INTERVAL '2 days' END,
    CASE WHEN v.is_absent
         THEN (v.ts::timestamp AT TIME ZONE 'America/Argentina/Buenos_Aires') + (v.dur || ' minutes')::interval END,
    TIMESTAMP '2026-05-20 10:00' AT TIME ZONE 'America/Argentina/Buenos_Aires',
    false
FROM (VALUES
    -- ── PASADOS (2–9 jun) — estadísticas y reseñas ──────────────────────────
    -- Lucía (1)
    ( 1, 1, 1,  1, '2026-06-02 10:00', 45, 'COMPLETED', NULL,           false),
    ( 2, 1, 2,  1, '2026-06-02 11:00', 45, 'COMPLETED', NULL,           false),
    ( 3, 1, 3,  2, '2026-06-03 14:00', 90, 'COMPLETED', NULL,           false),  -- sin reseña
    ( 4, 1, 7,  1, '2026-06-04 09:00', 45, 'COMPLETED', NULL,           false),
    ( 5, 1, 8,  2, '2026-06-05 15:00', 90, 'COMPLETED', NULL,           false),
    ( 6, 1, 9,  1, '2026-06-06 10:00', 45, 'COMPLETED', NULL,           false),
    ( 7, 1, 4,  1, '2026-06-08 09:00', 45, 'CANCELLED', 'client',       false),
    -- Martín (2)
    ( 8, 2, 2,  3, '2026-06-02 09:00', 30, 'COMPLETED', NULL,           false),
    ( 9, 2, 2,  3, '2026-06-03 09:30', 30, 'COMPLETED', NULL,           false),
    (10, 2, 4,  3, '2026-06-04 10:00', 30, 'COMPLETED', NULL,           true),   -- Daniel ausente
    (11, 2, 4,  3, '2026-06-05 11:00', 30, 'COMPLETED', NULL,           true),   -- Daniel ausente
    (12, 2, 10, 4, '2026-06-06 09:00', 30, 'COMPLETED', NULL,           false),
    (13, 2, 11, 3, '2026-06-09 10:00', 30, 'COMPLETED', NULL,           false),
    (14, 2, 12, 4, '2026-06-09 14:00', 30, 'COMPLETED', NULL,           false),  -- sin reseña
    -- Sofía (3)
    (15, 3, 5,  5, '2026-06-02 10:00', 60, 'COMPLETED', NULL,           false),
    (16, 3, 3,  5, '2026-06-03 11:00', 60, 'COMPLETED', NULL,           false),
    (17, 3, 13, 6, '2026-06-04 09:00', 45, 'COMPLETED', NULL,           false),
    (18, 3, 14, 5, '2026-06-05 10:00', 60, 'COMPLETED', NULL,           false),
    (19, 3, 15, 6, '2026-06-06 15:00', 45, 'COMPLETED', NULL,           false),
    (20, 3, 1,  5, '2026-06-09 11:00', 60, 'COMPLETED', NULL,           false),  -- sin reseña
    -- Diego (4)
    (21, 4, 6,  7, '2026-06-02 16:00', 60, 'COMPLETED', NULL,           false),  -- sin reseña
    (22, 4, 16, 7, '2026-06-03 10:00', 60, 'COMPLETED', NULL,           false),
    (23, 4, 8,  7, '2026-06-04 11:00', 60, 'COMPLETED', NULL,           false),
    (24, 4, 10, 7, '2026-06-05 09:00', 60, 'COMPLETED', NULL,           false),
    (25, 4, 11, 7, '2026-06-06 16:00', 60, 'COMPLETED', NULL,           false),
    (26, 4, 12, 7, '2026-06-09 10:00', 60, 'COMPLETED', NULL,           false),
    -- Carla (5)
    (27, 5, 1,  8, '2026-06-02 10:00', 60, 'COMPLETED', NULL,           false),
    (28, 5, 13, 8, '2026-06-03 11:00', 60, 'COMPLETED', NULL,           false),
    (29, 5, 14, 9, '2026-06-04 09:00', 90, 'COMPLETED', NULL,           false),
    (30, 5, 15, 8, '2026-06-05 16:00', 60, 'COMPLETED', NULL,           false),
    (31, 5, 16, 8, '2026-06-06 10:00', 60, 'COMPLETED', NULL,           false),
    (32, 5, 2,  8, '2026-06-09 11:00', 60, 'COMPLETED', NULL,           false),  -- sin reseña
    -- Javier (6)
    (33, 6, 4,  10, '2026-06-02 09:00', 45, 'COMPLETED', NULL,          false),
    (34, 6, 4,  10, '2026-06-03 14:00', 45, 'COMPLETED', NULL,          false),
    (35, 6, 5,  10, '2026-06-04 10:00', 45, 'COMPLETED', NULL,          false),
    (36, 6, 7,  10, '2026-06-05 11:00', 45, 'COMPLETED', NULL,          false),
    (37, 6, 9,  10, '2026-06-06 09:00', 45, 'COMPLETED', NULL,          false),
    (38, 6, 10, 10, '2026-06-09 15:00', 45, 'COMPLETED', NULL,          false),

    -- ── PRÓXIMOS (15–21 jun) — agenda de la semana de entrega ───────────────
    -- Lucía (1) — incluye superposición 15/06 (40 y 41)
    (39, 1, 1,  1, '2026-06-15 10:00', 45, 'CONFIRMED', NULL,           false),
    (40, 1, 5,  2, '2026-06-15 11:00', 90, 'CONFIRMED', NULL,           false),
    (41, 1, 2,  1, '2026-06-15 11:30', 45, 'CONFIRMED', NULL,           false),  -- se superpone con 40
    (42, 1, 7,  1, '2026-06-16 14:00', 45, 'CONFIRMED', NULL,           false),
    (43, 1, 8,  2, '2026-06-17 10:00', 90, 'CONFIRMED', NULL,           false),
    (44, 1, 9,  1, '2026-06-18 09:00', 45, 'CONFIRMED', NULL,           false),
    (45, 1, 11, 1, '2026-06-19 16:00', 45, 'CONFIRMED', NULL,           false),
    -- Martín (2) — slots cada 15 min
    (46, 2, 2,  3, '2026-06-15 09:00', 30, 'CONFIRMED', NULL,           false),
    (47, 2, 10, 3, '2026-06-15 09:30', 30, 'CONFIRMED', NULL,           false),
    (48, 2, 11, 4, '2026-06-15 10:00', 30, 'CONFIRMED', NULL,           false),
    (49, 2, 12, 3, '2026-06-16 11:00', 30, 'CONFIRMED', NULL,           false),
    (50, 2, 4,  3, '2026-06-17 15:00', 30, 'CONFIRMED', NULL,           false),
    (51, 2, 13, 4, '2026-06-18 10:00', 30, 'CONFIRMED', NULL,           false),
    (52, 2, 1,  3, '2026-06-19 09:00', 30, 'CONFIRMED', NULL,           false),
    -- Sofía (3) — slots cada 20 min, incluye sábado 20
    (53, 3, 5,  5, '2026-06-15 10:00', 60, 'CONFIRMED', NULL,           false),
    (54, 3, 3,  6, '2026-06-16 09:00', 45, 'CONFIRMED', NULL,           false),
    (55, 3, 14, 5, '2026-06-17 11:00', 60, 'CONFIRMED', NULL,           false),
    (56, 3, 15, 5, '2026-06-18 14:00', 60, 'CONFIRMED', NULL,           false),
    (57, 3, 13, 6, '2026-06-19 10:00', 45, 'CONFIRMED', NULL,           false),
    (58, 3, 16, 5, '2026-06-20 10:00', 60, 'CONFIRMED', NULL,           false),
    -- Diego (4) — slots cada 60 min, una cancelación del profesional
    (59, 4, 6,  7, '2026-06-15 16:00', 60, 'CONFIRMED', NULL,           false),
    (60, 4, 16, 7, '2026-06-16 10:00', 60, 'CONFIRMED', NULL,           false),
    (61, 4, 8,  7, '2026-06-17 09:00', 60, 'CONFIRMED', NULL,           false),
    (62, 4, 12, 7, '2026-06-18 16:00', 60, 'CONFIRMED', NULL,           false),
    (63, 4, 11, 7, '2026-06-19 11:00', 60, 'CANCELLED', 'professional', false),
    -- Carla (5) — taller grupal lleno 3/3 el 16/06 16:00, + cancelación de cliente
    (64, 5, 1,  9, '2026-06-16 16:00', 90, 'CONFIRMED', NULL,           false),
    (65, 5, 2,  9, '2026-06-16 16:00', 90, 'CONFIRMED', NULL,           false),
    (66, 5, 3,  9, '2026-06-16 16:00', 90, 'CONFIRMED', NULL,           false),
    (67, 5, 13, 8, '2026-06-17 10:00', 60, 'CONFIRMED', NULL,           false),
    (68, 5, 16, 8, '2026-06-18 13:00', 60, 'CONFIRMED', NULL,           false),
    (69, 5, 15, 8, '2026-06-19 09:00', 60, 'CANCELLED', 'client',       false),
    -- Javier (6)
    (70, 6, 4,  10, '2026-06-15 09:00', 45, 'CONFIRMED', NULL,          false),
    (71, 6, 5,  10, '2026-06-16 14:00', 45, 'CONFIRMED', NULL,          false),
    (72, 6, 7,  10, '2026-06-17 10:00', 45, 'CONFIRMED', NULL,          false),
    (73, 6, 9,  10, '2026-06-18 15:00', 45, 'CONFIRMED', NULL,          false),
    (74, 6, 10, 10, '2026-06-19 11:00', 45, 'CONFIRMED', NULL,          false)
) AS v(id, prof, client, service, ts, dur, status, cancelled_by, is_absent);

-- ============================================================================
-- 9) RESEÑAS (review) — sobre turnos COMPLETED. Score 1..5.
--    Quedan SIN reseña (reservables en la demo): #3, #14, #20, #21, #32.
-- ============================================================================
INSERT INTO review (professional_id, client_id, appointment_id, score) VALUES
(1, 1, 1,  5), (1, 2, 2, 4), (1, 7, 4, 5), (1, 8, 5, 3), (1, 9, 6, 4),
(2, 2, 8,  5), (2, 2, 9, 4), (2, 10, 12, 5), (2, 11, 13, 4),
(3, 5, 15, 5), (3, 3, 16, 4), (3, 13, 17, 5), (3, 14, 18, 4), (3, 15, 19, 5),
(4, 16, 22, 5), (4, 8, 23, 4), (4, 10, 24, 5), (4, 11, 25, 3), (4, 12, 26, 4),
(5, 1, 27, 5), (5, 13, 28, 4), (5, 14, 29, 5), (5, 15, 30, 4), (5, 16, 31, 5),
(6, 4, 33, 5), (6, 4, 34, 4), (6, 5, 35, 5), (6, 7, 36, 4), (6, 9, 37, 5), (6, 10, 38, 3);

-- ============================================================================
-- 10) LISTA DE ESPERA — taller grupal de Carla (prof 5, service 9) lleno
--     el 16/06 16:00: Daniel, Elena y Gabriela quedan en espera.
-- ============================================================================
INSERT INTO wait_list (client_id, professional_id, service_id, slot_start, creation_time)
SELECT c, 5, 9,
       TIMESTAMP '2026-06-16 16:00' AT TIME ZONE 'America/Argentina/Buenos_Aires',
       NOW() - (c || ' hours')::interval
FROM (VALUES (4), (5), (7)) AS w(c);

-- ============================================================================
-- 11) Reajustar las secuencias de IDs (insertamos ids explícitos arriba)
-- ============================================================================
SELECT setval(pg_get_serial_sequence('professional','id'), (SELECT MAX(id) FROM professional));
SELECT setval(pg_get_serial_sequence('client','id'),        (SELECT MAX(id) FROM client));
SELECT setval(pg_get_serial_sequence('service','id'),       (SELECT MAX(id) FROM service));
SELECT setval(pg_get_serial_sequence('appointment','id'),   (SELECT MAX(id) FROM appointment));
