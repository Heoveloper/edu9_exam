--drop
drop table product;
drop sequence product_pid_seq;

--product ���̺� ����
create table product(
    pid     NUMBER(10),
    pname   VARCHAR2(30),
    count   NUMBER(10),
    price   NUMBER(10)
);
--primary key
alter table product add constraint product_pid_pk primary key (pid);

--��ǰ��ȣ ������ ����
create sequence product_pid_seq;