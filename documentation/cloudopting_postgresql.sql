/*==============================================================*/
/* DBMS name:      PostgreSQL 8                                 */
/* Created on:     15/02/2015 18:18:00                          */
/*==============================================================*/
/*database cloudopting is needed by the application
if another database is available then the application.yml should be configured so*/
CREATE DATABASE cloudopting
  WITH OWNER = postgres
       ENCODING = 'UTF8'
       TABLESPACE = pg_default
       LC_COLLATE = 'English_United States.1252'
       LC_CTYPE = 'English_United States.1252'
       CONNECTION LIMIT = -1;

drop index application_media_application_i;

drop table application_media;

drop index applications_status_id_key;

drop table applications;

drop index customizations_status_id_key;

drop index customizations_customer_id_key;

drop index customizations_application_id_k;

drop table customizations;

drop index status_status_key;

drop table status;

drop table t_authority;

drop index idx_persistent_audit_event;

drop table t_persistent_audit_event;

drop index idx_persistent_audit_event_data;

drop table t_persistent_audit_event_data;

drop table t_persistent_token;

drop index t_user_login_key;

drop index t_user_email_key;

drop index idx_user_login;

drop index idx_user_email;

drop table t_user;

drop table t_user_authority;

drop table settings;

drop sequence application_media_media_id_seq;

drop sequence applications_application_id_seq;

drop sequence customizations_customization_id_seq;

drop sequence hibernate_sequence;

drop sequence status_status_id_seq;

drop sequence t_persistent_audit_event_event_id_seq;

drop sequence t_user_id_seq;

create sequence application_media_media_id_seq;

create sequence applications_application_id_seq;

create sequence customizations_customization_id_seq;

create sequence hibernate_sequence;

create sequence status_status_id_seq;

create sequence t_persistent_audit_event_event_id_seq;

create sequence t_user_id_seq;

/*==============================================================*/
/* Table: application_media                                     */
/*==============================================================*/
create table application_media (
   id                   INT8                 not null default nextval('application_media_media_id_seq'::regclass),
   media_content        bytea                null,
   application_id       INT8                 null,
   constraint PK_APPLICATION_MEDIA primary key (id),
   constraint application_media_pkey unique (id)
)
without oids;

comment on table application_media is
'Table that will hold all the media content that can be used by the Catalogue frontend to present the application.
On developer choice the media could also be placed in the file system (so that is better retrieved) and here will be saved just the path.';

-- set table ownership
alter table application_media owner to postgres
;
/*==============================================================*/
/* Index: application_media_application_i                       */
/*==============================================================*/
create unique index application_media_application_i on application_media using btree (
application_id
);

/*==============================================================*/
/* Table: applications                                          */
/*==============================================================*/
create table applications (
   id                   INT8                 not null default nextval('applications_application_id_seq'::regclass),
   application_name     varchar(50)          not null,
   application_description text                 not null,
   application_tosca_template xml                  not null,
   application_version  varchar(10)          not null,
   status_id            INT8                 not null,
   user_id              int8                 null default nextval('t_user_id_seq'::regclass),
   constraint applications_pkey primary key (id)
)
without oids;

-- set table ownership
alter table applications owner to postgres
;
/*==============================================================*/
/* Index: applications_status_id_key                            */
/*==============================================================*/
create unique index applications_status_id_key on applications using btree (
status_id
);

/*==============================================================*/
/* Table: customizations                                        */
/*==============================================================*/
create table customizations (
   id                   INT8                 not null default nextval('customizations_customization_id_seq'::regclass),
   application_id       INT8                 not null,
   customization_tosca_file xml                  not null,
   customization_creation date                 not null,
   customization_activation date                 not null,
   customization_decommission date                 not null,
   status_id            INT8                 not null,
   username             varchar(15)          not null,
   constraint customizations_pkey primary key (id)
)
without oids;

-- set table ownership
alter table customizations owner to postgres
;
/*==============================================================*/
/* Index: customizations_application_id_k                       */
/*==============================================================*/
create unique index customizations_application_id_k on customizations using btree (
application_id
);

/*==============================================================*/
/* Index: customizations_customer_id_key                        */
/*==============================================================*/
create unique index customizations_customer_id_key on customizations using btree (
username
);

/*==============================================================*/
/* Index: customizations_status_id_key                          */
/*==============================================================*/
create unique index customizations_status_id_key on customizations using btree (
status_id
);

/*==============================================================*/
/* Table: status                                                */
/*==============================================================*/
create table status (
   id                   INT8                 not null default nextval('status_status_id_seq'::regclass),
   status               varchar(20)          not null,
   constraint status_pkey primary key (id)
)
without oids;

-- set table ownership
alter table status owner to postgres
;
/*==============================================================*/
/* Index: status_status_key                                     */
/*==============================================================*/
create unique index status_status_key on status using btree (
status
);

/*==============================================================*/
/* Table: t_authority                                           */
/*==============================================================*/
create table t_authority (
   name                 varchar(50)          not null,
   constraint pk_t_authority primary key (name)
)
without oids;

-- set table ownership
alter table t_authority owner to postgres
;
/*==============================================================*/
/* Table: t_persistent_audit_event                              */
/*==============================================================*/
create table t_persistent_audit_event (
   event_id             int8                 not null default nextval('t_persistent_audit_event_event_id_seq'::regclass),
   principal            varchar(255)         not null,
   event_date           timestamp            not null,
   event_type           varchar(255)         not null,
   constraint pk_t_persistent_audit_event primary key (event_id)
)
without oids;

-- set table ownership
alter table t_persistent_audit_event owner to postgres
;
/*==============================================================*/
/* Index: idx_persistent_audit_event                            */
/*==============================================================*/
create  index idx_persistent_audit_event on t_persistent_audit_event using btree (
principal,
event_date
);

/*==============================================================*/
/* Table: t_persistent_audit_event_data                         */
/*==============================================================*/
create table t_persistent_audit_event_data (
   event_id             int8                 not null,
   name                 varchar(255)         not null,
   value                varchar(255)         not null,
   constraint t_persistent_audit_event_data_pkey primary key (event_id, name)
)
without oids;

-- set table ownership
alter table t_persistent_audit_event_data owner to postgres
;
/*==============================================================*/
/* Index: idx_persistent_audit_event_data                       */
/*==============================================================*/
create  index idx_persistent_audit_event_data on t_persistent_audit_event_data using btree (
event_id
);

/*==============================================================*/
/* Table: t_persistent_token                                    */
/*==============================================================*/
create table t_persistent_token (
   series               varchar(255)         not null,
   user_id              int8                 not null,
   token_value          varchar(255)         not null,
   token_date           date                 not null,
   ip_address           varchar(39)          not null,
   user_agent           varchar(255)         not null,
   constraint pk_t_persistent_token primary key (series)
)
without oids;

-- set table ownership
alter table t_persistent_token owner to postgres
;
/*==============================================================*/
/* Table: t_user                                                */
/*==============================================================*/
create table t_user (
   id                   int8                 not null default nextval('t_user_id_seq'::regclass),
   login                varchar(50)          not null,
   password             varchar(100)         not null,
   first_name           varchar(50)          not null,
   last_name            varchar(50)          not null,
   email                varchar(100)         not null,
   activated            bool                 not null,
   lang_key             varchar(5)           not null,
   activation_key       varchar(20)          not null,
   created_by           varchar(50)          not null,
   created_date         timestamp            not null,
   last_modified_by     varchar(50)          not null,
   last_modified_date   timestamp            not null,
   constraint pk_t_user primary key (id)
)
without oids;

-- set table ownership
alter table t_user owner to postgres
;
/*==============================================================*/
/* Index: idx_user_email                                        */
/*==============================================================*/
create unique index idx_user_email on t_user using btree (
email
);

/*==============================================================*/
/* Index: idx_user_login                                        */
/*==============================================================*/
create unique index idx_user_login on t_user using btree (
login
);

/*==============================================================*/
/* Index: t_user_email_key                                      */
/*==============================================================*/
create unique index t_user_email_key on t_user using btree (
email
);

/*==============================================================*/
/* Index: t_user_login_key                                      */
/*==============================================================*/
create unique index t_user_login_key on t_user using btree (
login
);

/*==============================================================*/
/* Table: t_user_authority                                      */
/*==============================================================*/
create table t_user_authority (
   user_id              int8                 not null,
   authority_name       varchar(50)          not null,
   constraint t_user_authority_pkey primary key (user_id, authority_name)
)
without oids;

-- set table ownership
alter table t_user_authority owner to postgres
;
alter table application_media
   add constraint application_media_application_id_fkey foreign key (application_id)
      references applications (id)
      on delete restrict on update restrict;

alter table applications
   add constraint FK_APPLICAT_REFERENCE_T_USER foreign key (user_id)
      references t_user (id)
      on delete restrict on update restrict;

alter table applications
   add constraint applications_status_id_fkey foreign key (status_id)
      references status (id)
      on delete restrict on update restrict;

alter table customizations
   add constraint customizations_application_id_fkey foreign key (application_id)
      references applications (id)
      on delete restrict on update restrict;

alter table customizations
   add constraint customizations_status_id_fkey foreign key (status_id)
      references status (id)
      on delete restrict on update restrict;

alter table t_persistent_audit_event_data
   add constraint FK_event_persistent_audit_event_data foreign key (event_id)
      references t_persistent_audit_event (event_id);

alter table t_persistent_token
   add constraint fk_user_persistent_token foreign key (user_id)
      references t_user (id);

alter table t_user_authority
   add constraint fk_authority_name foreign key (authority_name)
      references t_authority (name);

alter table t_user_authority
   add constraint fk_user_id foreign key (user_id)
      references t_user (id);



INSERT INTO t_authority(
            name)
    VALUES ('ROLE_ADMIN');
	
INSERT INTO t_authority(
            name)
    VALUES ('ROLE_USER');
	
INSERT INTO t_user(
            id, login, password, first_name, last_name, email, activated, 
            lang_key, activation_key, created_by, created_date, last_modified_by, 
            last_modified_date)
    VALUES (1, 'admin', '$2a$10$gSAhZrxMllrbgj/kkK9UceBPpChGWJA7SYIb1Mqo.n5aNLq1/oRrC', 'Admin', 'Administrator', 'admin@admin.com', TRUE, 
            'en', '55555', 'admin', '2015-02-15 00:00:00', 'admin', 
            '2015-02-15 00:00:00');
            
INSERT INTO t_user_authority(
            user_id, authority_name)
    VALUES (1, 'ROLE_ADMIN');

CREATE TABLE settings
(
  key text NOT NULL, -- key of the setting
  value text, -- value of the setting
  description text, -- description of the setting
  CONSTRAINT pk_setting UNIQUE (key)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE settings
  OWNER TO postgres;
COMMENT ON TABLE settings
  IS 'table that will hold in format key/value various settings of the system';
COMMENT ON COLUMN settings.key IS 'key of the setting';
COMMENT ON COLUMN settings.value IS 'value of the setting';
COMMENT ON COLUMN settings.description IS 'description of the setting';

	