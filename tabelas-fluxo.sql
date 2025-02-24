CREATE DATABASE gestao_habito_bem_estar

-- Tabela de Usuários
CREATE TABLE usuarios (
    ID INT AUTO_INCREMENT PRIMARY KEY,
    NOME VARCHAR(100) NOT NULL,
    EMAIL VARCHAR(100) NOT NULL UNIQUE,
    SENHA VARCHAR(255) NOT NULL,
    DATA_CRIACAO DATETIME DEFAULT CURRENT_TIMESTAMP
);

-- Tabela de Roles
CREATE TABLE roles (
    ID INT AUTO_INCREMENT PRIMARY KEY,
    NOME VARCHAR(50) NOT NULL UNIQUE,
    DESCRICAO VARCHAR(255)
);

-- Tabela de Relacionamento N:N entre Usuários e Roles
CREATE TABLE usuarios_roles (
    USUARIO_ID INT NOT NULL,
    ROLE_ID INT NOT NULL,
    PRIMARY KEY (USUARIO_ID, ROLE_ID),
    FOREIGN KEY (USUARIO_ID) REFERENCES usuarios(ID) ON DELETE CASCADE,
    FOREIGN KEY (ROLE_ID) REFERENCES roles(ID) ON DELETE CASCADE
);

-- Tabela de Categorias
CREATE TABLE categorias (
    ID INT AUTO_INCREMENT PRIMARY KEY,
    NOME VARCHAR(50) NOT NULL UNIQUE
);

-- Tabela de Hábitos
CREATE TABLE habitos (
    ID INT AUTO_INCREMENT PRIMARY KEY,
    USUARIO_ID INT NOT NULL,
    NOME VARCHAR(100) NOT NULL,
    DESCRICAO VARCHAR(250),
    CATEGORIA_ID INT,
    FREQUENCIA ENUM('diário', 'semanal', 'mensal') NOT NULL,
    META INT NOT NULL,
    DATA_INICIO DATE NOT NULL,
    ATIVO BOOLEAN DEFAULT TRUE,
    FOREIGN KEY (USUARIO_ID) REFERENCES usuarios(ID) ON DELETE CASCADE,
    FOREIGN KEY (CATEGORIA_ID) REFERENCES categorias(ID) ON DELETE SET NULL
);

-- Tabela de Monitoramento de Hábitos
CREATE TABLE monitoramento_habitos (
    ID INT AUTO_INCREMENT PRIMARY KEY,
    HABITO_ID INT NOT NULL,
    DATA DATE NOT NULL,
    STATUS ENUM('pendente', 'completado') DEFAULT 'pendente',
    FOREIGN KEY (HABITO_ID) REFERENCES habitos(ID) ON DELETE CASCADE
);

-- Tabela de Recompensas
CREATE TABLE recompensas (
    ID INT AUTO_INCREMENT PRIMARY KEY,
    USUARIO_ID INT NOT NULL,
    DESCRICAO VARCHAR(250) NOT NULL,
    DATA_CONQUISTA DATE NOT NULL,
    FOREIGN KEY (USUARIO_ID) REFERENCES usuarios(ID) ON DELETE CASCADE
);

-- Tabela de Notificações
CREATE TABLE notificacoes (
    ID INT AUTO_INCREMENT PRIMARY KEY,
    USUARIO_ID INT NOT NULL,
    MENSAGEM VARCHAR(255) NOT NULL,
    DATA_ENVIO DATETIME NOT NULL,
    STATUS ENUM('pendente', 'enviado', 'lido') DEFAULT 'pendente',
    FOREIGN KEY (USUARIO_ID) REFERENCES usuarios(ID) ON DELETE CASCADE
);

-- Por Padrão a ROLE user tem que ser com o ID 2. Na hora do registro de user já faço a relação dela com o usuário;
INSERT INTO roles (ID,NOME,DESCRICAO) VALUES ( 1,"ADMIN","ADMIN");
INSERT INTO roles (ID,NOME,DESCRICAO) VALUES ( 1,"USER","USER");
INSERT INTO usuarios_roles (USUARIO_ID, ROLE_ID) VALUES ( 1, 1);