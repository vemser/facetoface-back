
CREATE TABLE EDICAO (
  ID_EDICAO NUMBER NOT NULL,
  NOME VARCHAR2(255) NOT NULL,
  PRIMARY KEY (ID_EDICAO)
);


CREATE TABLE TRILHA (
  ID_TRILHA NUMBER NOT NULL,
  NOME VARCHAR2(255) NOT NULL,
  PRIMARY KEY (ID_TRILHA)
);

CREATE TABLE PERFIL (
  ID_PERFIL NUMBER NOT NULL,
  NOME VARCHAR2(252) NOT NULL,
  PRIMARY KEY (ID_PERFIL)
);

CREATE TABLE USUARIO (
  ID_USUARIO NUMBER NOT NULL,
  SENHA VARCHAR2(255) NOT NULL,
  ID_TRILHA NUMBER NOT NULL,
  GENERO VARCHAR2(20) NOT NULL,
  NOME_COMPLETO VARCHAR2(255) NOT NULL,
  CIDADE VARCHAR2(255) NOT NULL,
  ESTADO VARCHAR2(255) NOT NULL,
  EMAIL VARCHAR2(255) UNIQUE NOT NULL,
  ATIVO CHAR(1),
  CONSTRAINT FK_USUARIO_ID_TRILHA FOREIGN KEY (ID_TRILHA) REFERENCES TRILHA(ID_TRILHA),
  PRIMARY KEY (ID_USUARIO)
 );

CREATE TABLE CANDIDATO (
  ID_CANDIDATO NUMBER NOT NULL,
  ID_EDICAO NUMBER NOT NULL,
  OBSERVACOES VARCHAR2(255) NULL,
  NOTA_PROVA NUMBER NOT NULL,
  ID_TRILHA NUMBER NOT NULL,
  GENERO VARCHAR2(20) NOT NULL,
  NOME_COMPLETO VARCHAR2(255) NOT NULL,
  CIDADE VARCHAR2(255) NOT NULL,
  ESTADO VARCHAR2(255) NOT NULL,
  EMAIL VARCHAR2(255) UNIQUE NOT NULL,
  ATIVO CHAR(1),
  CONSTRAINT FK_CANDIDATO_ID_EDICAO FOREIGN KEY (ID_EDICAO) REFERENCES EDICAO(ID_EDICAO),
  CONSTRAINT FK_CANDIDATO_ID_TRILHA FOREIGN KEY (ID_TRILHA) REFERENCES TRILHA(ID_TRILHA),
  PRIMARY KEY (ID_CANDIDATO)
 );

  CREATE TABLE FOTOS (
    ID_FOTOS NUMBER NOT NULL,
    NOME VARCHAR2(255),
    TIPO VARCHAR2(255),
    DADO BLOB,
    ID_USUARIO NUMBER,
    ID_CANDIDATO NUMBER,
    PRIMARY KEY (ID_FOTOS),
    CONSTRAINT FK_USUARIO_FOTOS FOREIGN KEY (ID_USUARIO) REFERENCES USUARIO (ID_USUARIO),
    CONSTRAINT FK_CANDIDATO_FOTOS FOREIGN KEY (ID_CANDIDATO) REFERENCES CANDIDATO (ID_CANDIDATO)
);

  CREATE TABLE CURRICULO (
    ID_CURRICULO NUMBER NOT NULL,
    NOME VARCHAR2(255),
    TIPO VARCHAR2(255),
    DADO BFILE,
    ID_CANDIDATO,
    CONSTRAINT FK_CANDIDATO_CURRICULO FOREIGN KEY (ID_CANDIDATO) REFERENCES CANDIDATO (ID_CANDIDATO)
    );

CREATE TABLE USUARIO_PERFIL (
  ID_USUARIO NUMBER NOT NULL,
  ID_PERFIL NUMBER NOT NULL,
  PRIMARY KEY (ID_USUARIO, ID_PERFIL),
  CONSTRAINT FK_USUARIO_PERFIL_PERFIL FOREIGN KEY (ID_PERFIL) REFERENCES PERFIL (ID_PERFIL),
  CONSTRAINT FK_USUARIO_PERFIL_USUARIO FOREIGN KEY (ID_USUARIO) REFERENCES USUARIO (ID_USUARIO)
);

CREATE TABLE LINGUAGEM (
  ID_LINGUAGEM NUMBER NOT NULL,
  NOME VARCHAR2(255) NOT NULL,
  PRIMARY KEY (ID_LINGUAGEM)
);

CREATE TABLE CANDIDATO_LINGUAGEM (
  ID_CANDIDATO NUMBER NOT NULL,
  ID_LINGUAGEM NUMBER NOT NULL,
  PRIMARY KEY (ID_CANDIDATO, ID_LINGUAGEM),
  CONSTRAINT FK_CANDIDATO_LINGUAGEM_2 FOREIGN KEY (ID_LINGUAGEM) REFERENCES LINGUAGEM (ID_LINGUAGEM),
  CONSTRAINT FK_CANDIDATO_LINGUAGEM_3 FOREIGN KEY (ID_CANDIDATO) REFERENCES CANDIDATO (ID_CANDIDATO)
);

CREATE TABLE ENTREVISTAS (
  ID_ENTREVISTA NUMBER NOT NULL,
  LEGENDA VARCHAR2(20) NOT NULL,
  ID_USUARIO NUMBER NOT NULL,
  ID_CANDIDATO NUMBER NOT NULL,
  DATA_HORA_ENTREVISTA TIMESTAMP NOT NULL,
  CIDADE VARCHAR2(255) NOT NULL,
  ESTADO VARCHAR2(255) NOT NULL,
  OBSERVACOES VARCHAR2(255) NOT NULL,
  PRIMARY KEY (ID_ENTREVISTA),
  CONSTRAINT FK_ENTREVISTA_USUARIO_2 FOREIGN KEY (ID_USUARIO) REFERENCES USUARIO (ID_USUARIO),
  CONSTRAINT FK_ENTREVISTA_CANDIDATO__3 FOREIGN KEY (ID_CANDIDATO) REFERENCES CANDIDATO (ID_CANDIDATO)
);


CREATE SEQUENCE SEQ_PERFIL
 START WITH     1
 INCREMENT BY   1
 NOCACHE
 NOCYCLE;

 CREATE SEQUENCE SEQ_TRILHA
 START WITH     1
 INCREMENT BY   1
 NOCACHE
 NOCYCLE;

 CREATE SEQUENCE SEQ_FOTOS
START WITH 1
INCREMENT  BY 1
NOCACHE NOCYCLE;

CREATE SEQUENCE SEQ_CURRICULO
START WITH 1
INCREMENT  BY 1
NOCACHE NOCYCLE;

CREATE SEQUENCE SEQ_ID_EDICAO
START WITH 1
INCREMENT BY 1
NOCACHE NOCYCLE;

CREATE SEQUENCE SEQ_ID_LINGUAGEM
START WITH 1
INCREMENT BY 1
NOCACHE NOCYCLE;

CREATE SEQUENCE SEQ_ID_CANDIDATO
START WITH 1
INCREMENT BY 1
NOCACHE NOCYCLE;

CREATE SEQUENCE SEQ_ID_USUARIO
START WITH 1
INCREMENT BY 1
NOCACHE NOCYCLE;

CREATE SEQUENCE SEQ_ID_ENTREVISTAS
START WITH 1
INCREMENT BY 1
NOCACHE NOCYCLE;

INSERT INTO PERFIL (ID_PERFIL, NOME)
VALUES (seq_perfil.nextval, 'ROLE_ADMIN'); -- 1

INSERT INTO PERFIL (ID_PERFIL, NOME)
VALUES (seq_perfil.nextval, 'ROLE_GESTAO'); -- 2

INSERT INTO PERFIL (ID_PERFIL, NOME)
VALUES (seq_perfil.nextval, 'ROLE_INSTRUTOR'); -- 3


-- TRILHA ----

INSERT INTO TRILHA (ID_TRILHA,NOME)
VALUES (SEQ_TRILHA.nextval,'BACKEND');

INSERT INTO TRILHA (ID_TRILHA,NOME)
VALUES (SEQ_TRILHA.nextval,'FRONTEND');

INSERT INTO TRILHA (ID_TRILHA,NOME)
VALUES (SEQ_TRILHA.nextval,'QA');

INSERT INTO TRILHA (ID_TRILHA,NOME)
VALUES (SEQ_TRILHA.nextval,'COLABORADOR');


--- USUARIO ADMINISTRADOR ----

INSERT INTO USUARIO(ID_USUARIO, SENHA, ID_TRILHA, GENERO, NOME_COMPLETO, CIDADE, ESTADO, EMAIL, ATIVO)
VALUES (SEQ_ID_USUARIO.nextval,'$2a$10$6OwPdlN0MI9xK7gvqX.Y1OJSKhC8pE6vnWEOcuXgLkhpOrblNgwV2', 4, 'MASCULINO', 'ADMIN', 'PORTO ALEGRE', 'RIO GRANDE DO SUL', 'julio.gabriel@dbccompany.com', 'T');

-- tabela n - n ----

INSERT INTO USUARIO_PERFIL(ID_USUARIO, ID_PERFIL)
VALUES(1,3);

SELECT * FROM USUARIO u WHERE u.EMAIL = 'julio.gabriel@dbccompany.com';