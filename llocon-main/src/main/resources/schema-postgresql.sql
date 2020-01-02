create sequence hibernate_sequence start 1 increment 1;

create table connexio (
   id int8 not null,
    created_by varchar(64) not null,
    created_date timestamp not null,
    lastmod_by varchar(64),
    lastmod_date timestamp,
    version int8 not null,
    contrasenya varchar(64) not null,
    nom varchar(100) not null,
    proveidor int4 not null,
    usuari varchar(64) not null,
    primary key (id)
);

create table factura (
   id int8 not null,
    created_by varchar(64) not null,
    created_date timestamp not null,
    lastmod_by varchar(64),
    lastmod_date timestamp,
    version int8 not null,
    data timestamp not null,
    estat int4 not null,
    import numeric(19, 2) not null,
    numero varchar(64) not null,
    subministrament_id int8 not null,
    primary key (id)
);

create table lloguer (
   id int8 not null,
    created_by varchar(64) not null,
    created_date timestamp not null,
    lastmod_by varchar(64),
    lastmod_date timestamp,
    version int8 not null,
    adressa varchar(200) not null,
    codi varchar(10) not null,
    import_pend numeric(19, 2),
    nom varchar(100) not null,
    primary key (id)
);

create table submin (
   id int8 not null,
    created_by varchar(64) not null,
    created_date timestamp not null,
    lastmod_by varchar(64),
    lastmod_date timestamp,
    version int8 not null,
    contracte_num varchar(64) not null,
    darrera_act timestamp,
    producte int4 not null,
    connexio_id int8 not null,
    lloguer_id int8 not null,
    primary key (id)
);

create table usuari (
   id int8 not null,
    created_by varchar(64) not null,
    created_date timestamp not null,
    lastmod_by varchar(64),
    lastmod_date timestamp,
    version int8 not null,
    actiu boolean not null,
    codi varchar(64) not null,
    contrasenya varchar(255),
    email varchar(100) not null,
    imatge_url varchar(255),
    llinatges varchar(100) not null,
    nom varchar(100) not null,
    validat boolean not null,
    primary key (id)
);

create table usuari_authority (
   usuari_id int8 not null,
    rol varchar(10)
);

alter table if exists usuari 
   add constraint usuari_codi_uk unique (codi);

alter table if exists usuari 
   add constraint usuari_email_uk unique (email);

alter table if exists factura 
   add constraint fk_submin_factura 
   foreign key (subministrament_id) 
   references submin;

alter table if exists submin 
   add constraint fk_submin_connexio 
   foreign key (connexio_id) 
   references connexio;

alter table if exists submin 
   add constraint fk_submin_lloguer 
   foreign key (lloguer_id) 
   references lloguer;

alter table if exists usuari_authority 
   add constraint usuaut_usuari_fk 
   foreign key (usuari_id) 
   references usuari;
