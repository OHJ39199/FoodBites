-- Este script SQL se utiliza para insertar datos de ejemplo en las tablas de la base de datos.
-- DELETE FROM pedidos;
-- DELETE FROM menu;
-- DELETE FROM food_trucks;
-- DELETE FROM usuarios;
-- DELETE FROM ubicaciones;

-- 1. Insertar datos en la tabla 'usuarios'
INSERT INTO usuarios (id, email, nombre, password, direccion) VALUES
    (1, 'andres@mail.com', 'Admin', 'abc', 'Nueva York, Broadway'),
    (2, 'juan@example.com', 'Juan Perez', 'abc', 'Nueva York, 5th Ave'),
    (3, 'maria@example.com', 'Maria Gomez', 'abc', 'Los Angeles, Sunset Blvd'),
    (4, 'carlos@example.com', 'Carlos Lopez', 'abc', 'Chicago, Michigan Ave'),
    (5, 'andres@gmail.com', 'andres', 'abc', 'intriago'),
    (6, 'emely@mail.com', 'Emely', 'abc', 'carriondas'),
    (7, 'samantha@mail.com', 'samantha', '123', 'Asturias');

-- ALTER TABLE usuarios AUTO_INCREMENT = 8; -- (MySQL specific)

-- 2. Insertar datos en la tabla 'food_trucks'
INSERT INTO food_trucks (id, nombre, tipo_cocina, ubicacion_actual) VALUES
    (1, 'Taco Fiesta', 'Mexicana', 'Nueva York, 5th Ave'),
    (2, 'Burger Bonanza', 'Americana', 'Los Angeles, Sunset Blvd'),
    (3, 'Pizza Express', 'Italiana', 'Chicago, Michigan Ave'),
    (4, 'Sushi Stop', 'Japonesa', 'Nueva York, Broadway'),
    (5, 'Sweet Rolls', 'Postres', 'Nueva York, Central Park'),
    (6, 'La Julineta', 'Hamburguesas', 'Gijón Asturias');

-- ALTER TABLE food_trucks AUTO_INCREMENT = 7; -- (MySQL specific)

-- 3. Insertar datos en la tabla 'ubicaciones'
INSERT INTO ubicaciones (id, calle, ciudad, latitud, longitud) VALUES
    (1, '5th Ave', 'Nueva York', 40.7816, -73.9665),         -- Para Taco Fiesta
    (2, 'Sunset Blvd', 'Los Angeles', 34.0901, -118.3435),   -- Para Burger Bonanza
    (3, 'Michigan Ave', 'Chicago', 41.8876, -87.6234),      -- Para Pizza Express
    (4, 'Broadway', 'Nueva York', 40.7580, -73.9855),        -- Para Sushi Stop
    (5, 'Central Park', 'Nueva York', 40.7829, -73.9654),    -- Para Sweet Rolls
    (6, 'Calle Corrida', 'Gijón', 43.5434, -5.6617),         -- Para La Julineta
    (7, 'Gran Vía', 'Madrid', 40.4200, -3.7037),            -- Ubicación adicional
    (8, 'Las Ramblas', 'Barcelona', 41.3818, 2.1720),       -- Ubicación adicional
    (9, 'Avenida de la Constitución', 'Sevilla', 37.3858, -5.9961); -- Ubicación adicional

-- ALTER TABLE ubicaciones AUTO_INCREMENT = 10; -- (MySQL specific)


-- 4. Insertar datos en la tabla 'food_trucks'
INSERT INTO food_trucks (id, nombre, tipo_cocina, ubicacion_id_id) VALUES
    (1, 'Taco Fiesta', 'Mexicana', 1), -- Referencia a Nueva York, 5th Ave
    (2, 'Burger Bonanza', 'Americana', 2), -- Referencia a Los Angeles, Sunset Blvd
    (3, 'Pizza Express', 'Italiana', 3), -- Referencia a Chicago, Michigan Ave
    (4, 'Sushi Stop', 'Japonesa', 4), -- Referencia a Nueva York, Broadway
    (5, 'Sweet Rolls', 'Postres', 5), -- Referencia a Nueva York, Central Park
    (6, 'La Julineta', 'Hamburguesas', 6); -- Referencia a Gijón, Calle Corrida

-- ALTER TABLE food_trucks AUTO_INCREMENT = 7; -- (MySQL specific)

-- 5. Insertar datos en la tabla 'menu'
INSERT INTO menu (id, nombre, descripcion, precio, food_truck_id_id) VALUES
    (1, 'Tacos al Pastor', 'Tacos con carne de cerdo y piña', 3.5, 1),
    (2, 'Quesadillas', 'Quesadillas de queso con salsa', 4.0, 1),
    (3, 'Enchiladas', 'Enchiladas verdes con pollo', 5.0, 1),
    (4, 'Classic Burger', 'Hamburguesa con lechuga y tomate', 6.0, 2),
    (5, 'Cheeseburger', 'Hamburguesa con queso cheddar', 6.5, 2),
    (6, 'French Fries', 'Papas fritas crujientes', 2.5, 2),
    (7, 'Margherita Pizza', 'Pizza con tomate y albahaca', 8.0, 3),
    (8, 'Pepperoni Pizza', 'Pizza con pepperoni y queso', 9.0, 3),
    (9, 'Garlic Bread', 'Pan con ajo y mantequilla', 3.0, 3),
    (10, 'California Roll', 'Sushi con cangrejo y aguacate', 7.0, 4),
    (11, 'Spicy Tuna Roll', 'Sushi con atún picante', 8.0, 4),
    (12, 'Miso Soup', 'Sopa de miso con tofu', 3.0, 4),
    (13, 'Cinnamon Roll', 'Rollo de canela con glaseado de vainilla', 4.99, 5),
    (14, 'Churro Roll', 'Rollo estilo churro con azúcar y canela', 3.99, 5),
    (15, 'Chocolate Roll', 'Rollo relleno de chocolate y cobertura de cacao', 5.49, 5),
    (16, 'Guacamole', 'Aguacate, tomate y cilantro acompañado de totopos', 9.5, 1),
    (17, 'Pico de Gallo', 'Cebolla, chiles, tomate, pimientos y cilantro', 4.5, 1);

-- ALTER TABLE menu AUTO_INCREMENT = 18; -- (MySQL specific)

-- 6. Insertar datos en la tabla 'pedidos'
INSERT INTO pedidos (id, fecha_pedido, estado, food_truck_id_id, usuario_id_id) VALUES
    (1, '2025-05-09 12:05:00', 'Tu pedido en Taco Fiesta está listo', 1, 2),
    (2, '2025-05-09 12:20:00', 'Tu pedido en Burger Bonanza está en proceso', 2, 3),
    (3, '2025-05-09 12:35:00', 'Tu pedido en Pizza Express ha sido entregado', 3, 4);

-- ALTER TABLE pedidos AUTO_INCREMENT = 4; -- (MySQL specific)