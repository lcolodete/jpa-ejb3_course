CONNECT EJB01/EJB01

drop table conta;
drop sequence seq_conta;

create sequence seq_conta;

CREATE TABLE CONTA
(NUMERO		NUMBER(5)
     CONSTRAINT CONTAS_NUMERO_PK PRIMARY KEY,
SALDO		NUMBER(9,2));

INSERT INTO CONTA
VALUES (seq_conta.nextval, 50000);

INSERT INTO CONTA
VALUES (seq_conta.nextval, 150000);

INSERT INTO CONTA
VALUES (seq_conta.nextval, 250000);

INSERT INTO CONTA
VALUES (seq_conta.nextval, 350000);

INSERT INTO CONTA
VALUES (seq_conta.nextval, 450000);

COMMIT;

