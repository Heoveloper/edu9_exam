--drop
drop table product;
drop sequence product_pid_seq;

--product 테이블 생성
create table product(
    pid     NUMBER(10),
    pname   VARCHAR2(30),
    count   NUMBER(10),
    price   NUMBER(10)
);
--primary key
alter table product add constraint product_pid_pk primary key (pid);

--상품번호 시퀀스 생성
create sequence product_pid_seq;