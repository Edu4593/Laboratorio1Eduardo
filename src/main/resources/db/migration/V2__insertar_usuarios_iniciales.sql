CREATE EXTENSION pgcrypto;

INSERT INTO usuario (username, password, role) VALUES
                                                   ('admin', crypt('admin123', gen_salt('bf', 10)), 'SUPER-ADMIN-ROLE'),
                                                   ('user',  crypt('user123',  gen_salt('bf', 10)), 'USER');
