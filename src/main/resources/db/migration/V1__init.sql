CREATE TABLE IF NOT EXISTS users (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(255) NOT NULL,
    mail VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    role INTEGER NOT NULL,
    token VARCHAR(255)
);

INSERT INTO users ( username, mail, password, role) VALUES 
('admin', 'admin@gmail.com', '$2a$10$bNPFxrKoNWjgvplBgS.8zu8pUpXS0oVVH6Acnl.YlZFTB1pND1zcG', 0);