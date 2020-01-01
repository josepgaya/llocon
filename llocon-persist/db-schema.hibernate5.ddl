create sequence hibernate_sequence start with 1 increment by 1;

create table connexio (
   id bigint not null,
    created_by varchar(64) not null,
    created_date timestamp not null,
    lastmod_by varchar(64),
    lastmod_date timestamp,
    version bigint not null,
    contrasenya varchar(64) not null,
    nom varchar(100) not null,
    proveidor integer not null,
    usuari varchar(64) not null,
    primary key (id)
);

create table factura (
   id bigint not null,
    created_by varchar(64) not null,
    created_date timestamp not null,
    lastmod_by varchar(64),
    lastmod_date timestamp,
    version bigint not null,
    data timestamp not null,
    estat integer not null,
    import numeric not null,
    numero varchar(64) not null,
    subministrament_id bigint not null,
    primary key (id)
);

create table lloguer (
   id bigint not null,
    created_by varchar(64) not null,
    created_date timestamp not null,
    lastmod_by varchar(64),
    lastmod_date timestamp,
    version bigint not null,
    adressa varchar(200) not null,
    codi varchar(10) not null,
    import_pend numeric,
    nom varchar(100) not null,
    primary key (id)
);

create table submin (
   id bigint not null,
    created_by varchar(64) not null,
    created_date timestamp not null,
    lastmod_by varchar(64),
    lastmod_date timestamp,
    version bigint not null,
    contracte_num varchar(64) not null,
    darrera_act timestamp,
    producte integer not null,
    connexio_id bigint not null,
    lloguer_id bigint not null,
    primary key (id)
);

create table usuari (
   id bigint not null,
    created_by varchar(64) not null,
    created_date timestamp not null,
    lastmod_by varchar(64),
    lastmod_date timestamp,
    version bigint not null,
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
   usuari_id bigint not null,
    rol varchar(10)
);

alter table usuari 
   add constraint usuari_codi_uk unique (codi);

alter table usuari 
   add constraint usuari_email_uk unique (email);

alter table factura 
   add constraint fk_submin_factura 
   foreign key (subministrament_id) 
   references submin;

alter table submin 
   add constraint fk_submin_connexio 
   foreign key (connexio_id) 
   references connexio;

alter table submin 
   add constraint fk_submin_lloguer 
   foreign key (lloguer_id) 
   references lloguer;

alter table usuari_authority 
   add constraint usuaut_usuari_fk 
   foreign key (usuari_id) 
   references usuari;
