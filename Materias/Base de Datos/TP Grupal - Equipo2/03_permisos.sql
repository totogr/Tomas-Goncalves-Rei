-- Creacion de roles
CREATE ROLE administrador WITH LOGIN PASSWORD 'admin_pass_2024';
CREATE ROLE usuario_app   WITH LOGIN PASSWORD 'user_pass_2024';

-- Permisos del rol administrador

-- Permiso de conexión a la base
GRANT CONNECT ON DATABASE pronosticos_db TO administrador;

-- Permisos sobre el esquema
GRANT ALL PRIVILEGES ON SCHEMA public TO administrador;

-- Permisos sobre todas las tablas existentes
GRANT ALL PRIVILEGES ON ALL TABLES    IN SCHEMA public TO administrador;
GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA public TO administrador;

-- Permisos por defecto para tablas que se creen en el futuro
ALTER DEFAULT PRIVILEGES IN SCHEMA public
    GRANT ALL PRIVILEGES ON TABLES    TO administrador;
ALTER DEFAULT PRIVILEGES IN SCHEMA public
    GRANT ALL PRIVILEGES ON SEQUENCES TO administrador;


-- Permisos del rol usuario_app

-- Permiso de conexion a la base
GRANT CONNECT ON DATABASE pronosticos_db TO usuario_app;

-- Permiso de uso del esquema
GRANT USAGE ON SCHEMA public TO usuario_app;

-- Lectura sobre tablas publicas
GRANT SELECT ON Pais          TO usuario_app;
GRANT SELECT ON Torneo        TO usuario_app;
GRANT SELECT ON Equipo        TO usuario_app;
GRANT SELECT ON Partido       TO usuario_app;
GRANT SELECT ON Participacion TO usuario_app;

-- Sobre Usuario: solo SELECT (no puede modificar otros usuarios)
-- y UPDATE acotado a cantidad_ingresos (para el contador de logins)
GRANT SELECT                          ON Usuario TO usuario_app;
GRANT UPDATE (cantidad_ingresos)      ON Usuario TO usuario_app;

-- Sobre Pronostico: alta y consulta de pronosticos propios.
GRANT SELECT, INSERT, UPDATE, DELETE ON Pronostico TO usuario_app;
