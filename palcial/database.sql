-- base de datos de consultas medicas
-- nota: la app tambien crea las tablas sola (ddl-auto=update) y carga estos datos si estan vacias

create database if not exists demo;
use demo;

create table if not exists usuario (
  id bigint not null auto_increment,
  username varchar(255) unique,
  password varchar(255),
  rol varchar(255),
  primary key (id)
);

create table if not exists medico (
  id bigint not null auto_increment,
  nombre varchar(255) not null,
  especialidad varchar(255) not null,
  usuario_id bigint,
  primary key (id),
  foreign key (usuario_id) references usuario (id)
);

create table if not exists consulta (
  id bigint not null auto_increment,
  paciente varchar(40) not null,
  motivo varchar(100) not null,
  consultorio int not null check (consultorio between 1 and 999),
  hora_inicio varchar(255) not null,
  hora_fin varchar(255) not null,
  medico_id bigint not null,
  cuenta_id bigint not null,
  primary key (id),
  foreign key (medico_id) references medico (id),
  foreign key (cuenta_id) references usuario (id)
);

-- usuarios de prueba: admin/admin123, medico/medico123, paciente/paciente123 (clave en bcrypt)
insert into usuario (id, username, password, rol) values
  (1, 'admin', '$2a$10$drJ5Cs1pXs3OwTSGv1aLOuSaFVtHIQHzRYM8EsevRQblUrCOzh/5K', 'ADMINISTRADOR'),
  (2, 'medico', '$2a$10$6sksyv8AFO.sxt2bJNpJY.EqzY2O2k1mQ6cssUdybi0ELWmn0fx1e', 'MEDICO'),
  (3, 'paciente', '$2a$10$QOP2OiAQnZ/bbd5KFQIZzeGDenhu4nFhb73CJr9G7REb5U2C9rq.C', 'PACIENTE');

-- medicos (lista desplegable del formulario de consultas)
insert into medico (id, nombre, especialidad, usuario_id) values
  (1, 'Ana Torres', 'Medicina General', 2),
  (2, 'Carlos Ruiz', 'Pediatria', null);

-- consulta de ejemplo (el nombre del paciente es el usuario de su cuenta)
insert into consulta (paciente, motivo, consultorio, hora_inicio, hora_fin, medico_id, cuenta_id) values
  ('paciente', 'Dolor de cabeza', 101, '08:00', '08:30', 1, 3);
