insert into metalocal (tp_metalocal,ds_metalocal_singular,ds_metalocal_plural, ds_lingua) values ('MESA','Mesa', 'Mesas', 'PT');
insert into metalocal (tp_metalocal,ds_metalocal_singular,ds_metalocal_plural, ds_lingua) values ('QUARTO','Quarto', 'Quartos', 'PT');

insert into metalocal (tp_metalocal,ds_metalocal_singular,ds_metalocal_plural, ds_lingua) values ('MESA','Table', 'Tables', 'EN');
insert into metalocal (tp_metalocal,ds_metalocal_singular,ds_metalocal_plural, ds_lingua) values ('QUARTO','Room', 'Rooms', 'EN');

insert into metalocal (tp_metalocal,ds_metalocal_singular,ds_metalocal_plural, ds_lingua) values ('MESA','Tabella', 'Tabelle', 'IT');
insert into metalocal (tp_metalocal,ds_metalocal_singular,ds_metalocal_plural, ds_lingua) values ('QUARTO','Camere', 'Camere', 'IT');

insert into metalocal (tp_metalocal,ds_metalocal_singular,ds_metalocal_plural, ds_lingua) values ('MESA','Tableau', 'Tableaux', 'FR');
insert into metalocal (tp_metalocal,ds_metalocal_singular,ds_metalocal_plural, ds_lingua) values ('QUARTO','Chambre', 'Chambres', 'FR');

insert into metalocal (tp_metalocal,ds_metalocal_singular,ds_metalocal_plural, ds_lingua) values ('MESA','Tabla', 'Tablas', 'ES');
insert into metalocal (tp_metalocal,ds_metalocal_singular,ds_metalocal_plural, ds_lingua) values ('QUARTO','Habitación', 'Habitaciones', 'ES');

insert into metalocal (tp_metalocal,ds_metalocal_singular,ds_metalocal_plural, ds_lingua) values ('MESA','Tabelle', 'Tabellen', 'DE');
insert into metalocal (tp_metalocal,ds_metalocal_singular,ds_metalocal_plural, ds_lingua) values ('QUARTO','Zimmer', 'Zimmer', 'DE');

CREATE TABLE metalocal
(
   id_metalocal int PRIMARY KEY NOT NULL AUTO_INCREMENT,
   tp_metalocal varchar(20),
   ds_metalocal_singular varchar(255),
   ds_metalocal_plural varchar(255),
   ds_lingua varchar(5)   
)
;

alter table empresa add column(tp_metalocal varchar(20));