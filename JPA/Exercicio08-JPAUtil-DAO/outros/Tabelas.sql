DROP USER JPA01 CASCADE;
CREATE USER JPA01 IDENTIFIED BY JPA01;
GRANT CONNECT, RESOURCE TO JPA01;

CONNECT JPA01/JPA01

DROP TABLE CLIENTE CASCADE CONSTRAINTS;
DROP SEQUENCE SEQ_CLIENTE;

CREATE SEQUENCE SEQ_CLIENTE;

CREATE TABLE CLIENTE
(NUMERO       NUMBER(5)
   CONSTRAINT CLIENTES_NUMERO_PK
   PRIMARY KEY,
 NOME         VARCHAR2(50),
 SALARIO      NUMBER(9,2));

INSERT INTO CLIENTE
VALUES (SEQ_CLIENTE.NEXTVAL, 'JULIO CESAR', 2500.00);

INSERT INTO CLIENTE
VALUES (SEQ_CLIENTE.NEXTVAL, 'JULIANA SILVA', 1800.00);

INSERT INTO CLIENTE
VALUES (SEQ_CLIENTE.NEXTVAL, 'SERGIO ARANTES', 3800.00);

COMMIT;