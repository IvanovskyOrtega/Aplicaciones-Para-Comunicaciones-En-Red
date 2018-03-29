create database db1;
drop database db1;
use db1;
create table tabla1(llave int, lol char(20));
drop table tabla2;
insert into Asociado values
(
	'003',
	'Don Ramon',
	'Una casa de la vecindad',
	'5569847414',
	'H',
	0.50,
	'rorro@gmail.com'
);
show databases;
show tables;
update tabla1 set col1=25;
delete from tabla2 where id='666';
select * from t1;
create table t1(nombre CHAR(15), edad int, casado boolean);