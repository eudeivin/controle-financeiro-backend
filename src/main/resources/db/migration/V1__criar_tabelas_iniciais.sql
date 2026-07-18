CREATE TABLE usuarios (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    senha VARCHAR(255) NOT NULL,
    data_criacao TIMESTAMP
);

CREATE TABLE categorias (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    tipo VARCHAR(20) NOT NULL,
    usuario_id BIGINT NOT NULL,
    CONSTRAINT fk_categoria_usuario FOREIGN KEY (usuario_id) REFERENCES usuarios(id)
);

CREATE TABLE transacoes (
    id BIGSERIAL PRIMARY KEY,
    descricao VARCHAR(255) NOT NULL,
    valor NUMERIC(10,2) NOT NULL,
    data DATE NOT NULL,
    tipo VARCHAR(20) NOT NULL,
    categoria_id BIGINT NOT NULL,
    usuario_id BIGINT NOT NULL,
    CONSTRAINT fk_transacao_categoria FOREIGN KEY (categoria_id) REFERENCES categorias(id),
    CONSTRAINT fk_transacao_usuario FOREIGN KEY (usuario_id) REFERENCES usuarios(id)
);