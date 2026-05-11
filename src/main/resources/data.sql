-- USER ROLES
insert into USER_ROLE (name)
values ('USER');

insert into USER_ROLE (name)
values ('ADMIN');

-- INSERT TANK ROLES
insert into TANK_ROLE (name, img_path)
values ('Light tank', '/img/tankrole/light_tank.svg');

insert into TANK_ROLE (name, img_path)
values ('Medium tank', '/img/tankrole/medium_tank.svg');

insert into TANK_ROLE (name, img_path)
values ('Heavy tank', '/img/tankrole/heavy_tank.svg');

-- INSERT NATIONS
insert into NATION (name, img_path)
values ('USA', '/img/nation/usa.svg');

insert into NATION (name, img_path)
values ('Germany', '/img/nation/germany.svg');

insert into NATION (name, img_path)
values ('German Reich', '/img/nation/german_reich.svg');

insert into NATION (name, img_path)
values ('USSR', '/img/nation/ussr.svg');

insert into NATION (name, img_path)
values ('Great Britain', '/img/nation/uk.svg');

-- INSERT TANKS
insert into TANK (nation_id, tank_role_id, name, price, description, img_path, main_gun_calibre, armor_thickness, max_speed, crew_size)
values (
        (select id from NATION where name = 'German Reich'),
        (select id from TANK_ROLE where name = 'Heavy tank'),
        'Tiger II',
        210000,
        'The Panzerkampfwagen Tiger Ausf. B (Sd.Kfz. Index: Sd.Kfz. 182), also known as the Tiger II or informally the Königstiger (lit. "King Tiger"), was a German heavy tank developed in 1943 by Henschel to serve as a replacement for the Tiger I.',
        '/img/tank/tiger2.png',
        88,
        185,
        38,
        5
);

insert into TANK (nation_id, tank_role_id, name, price, description, img_path, main_gun_calibre, armor_thickness, max_speed, crew_size)
values (
           (select id from NATION where name = 'German Reich'),
           (select id from TANK_ROLE where name = 'Heavy tank'),
           'Tiger H1',
           105000,
           'The Panzerkampfwagen VI Ausführung H1 (Tiger H1) is the first (early-production) variant of the Tiger I heavy tank family, designed and built by Henschel and used by the German Army during World War II.',
           '/img/tank/tigerh1.png',
           88,
           100,
           45,
           5
       );

insert into TANK (nation_id, tank_role_id, name, price, description, img_path, main_gun_calibre, armor_thickness, max_speed, crew_size)
values (
           (select id from NATION where name = 'German Reich'),
           (select id from TANK_ROLE where name = 'Medium tank'),
           'Panther A',
           155000,
           'The Panzerkampfwagen V Ausführung A (Panther A) (Sd.Kfz. Index: Sd.Kfz. 171) is the second production variant of the iconic Panzerkampfwagen V Panther medium tank family.',
           '/img/tank/panther_a.png',
           75,
           100,
           46,
           5
       );

insert into TANK (nation_id, tank_role_id, name, price, description, img_path, main_gun_calibre, armor_thickness, max_speed, crew_size)
values (
           (select id from NATION where name = 'USA'),
           (select id from TANK_ROLE where name = 'Heavy tank'),
           'M4A3E2',
           105000,
           'The M4A3E2 Sherman - Assault Tank is an armoured modification of the M4A3, which is the fourth variant of the early-generation Medium Tank M4 (Sherman) family.',
           '/img/tank/sherman_jumbo.png',
           75,
           177,
           35,
           5
       );

insert into TANK (nation_id, tank_role_id, name, price, description, img_path, main_gun_calibre, armor_thickness, max_speed, crew_size)
values (
           (select id from NATION where name = 'Great Britain'),
           (select id from TANK_ROLE where name = 'Medium tank'),
           'Centurion Mk 3',
           210000,
           'The Centurion Mk 3 was the third variant of the Centurion medium tank family. The 84 mm Ordnance QF 20-pounder tank gun was installed in this variant, which provided significantly superior accuracy thanks to a newly developed two-plane fully automatic stabilization system (modified from the Centurion Mk 2).',
           '/img/tank/cent_mk3.png',
           84,
           152,
           35,
           4
       );