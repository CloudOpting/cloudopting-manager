/*==============================================================*/
/* DBMS name:      MySQL 5.0                                    */
/* Created on:     08/02/2015 02:25:23                          */
/*==============================================================*/


drop table if exists APPLICATIONS;

drop table if exists APPLICATION_MEDIA;

drop table if exists AUTHORITIES;

drop table if exists CUSTOMIZATIONS;

drop table if exists STATUS;

drop table if exists USERS;

/*==============================================================*/
/* Table: APPLICATIONS                                          */
/*==============================================================*/
create table APPLICATIONS
(
   ID                   bigint not null,
   SERVICE_PROVIDER_ID  bigint,
   APPLICATION_NAME     varchar(50),
   APPLICATION_DESCRIPTION text,
   APPLICATION_TOSCA_TEMPLATE longtext,
   APPLICATION_VERSION  int,
   STATUS_ID            bigint,
   primary key (ID)
);

/*==============================================================*/
/* Table: APPLICATION_MEDIA                                     */
/*==============================================================*/
create table APPLICATION_MEDIA
(
   ID                   bigint not null,
   MEDIA_CONTENT        blob,
   APPLICATION_ID       bigint,
   primary key (ID)
);

/*==============================================================*/
/* Table: AUTHORITIES                                           */
/*==============================================================*/
create table AUTHORITIES
(
   USERNAME             varchar(50),
   AUTHORITY            varchar(50),
   primary key (AUTHORITY)
);

/*==============================================================*/
/* Table: CUSTOMIZATIONS                                        */
/*==============================================================*/
create table CUSTOMIZATIONS
(
   ID                   bigint not null,
   APPLICATION_ID       bigint,
   CUSTOMER_ID          bigint,
   CUSTOMIZATION_TOSCA_FILE longtext,
   CUSTOMIZATION_CREATION date,
   CUSTOMIZATION_ACTIVATION date,
   CUSTOMIZATION_DECOMMISSION date,
   STATUS_ID            bigint,
   primary key (ID)
);

/*==============================================================*/
/* Table: STATUS                                                */
/*==============================================================*/
create table STATUS
(
   ID                   bigint not null,
   STATUS               varchar(20),
   primary key (ID)
);

/*==============================================================*/
/* Table: USERS                                                 */
/*==============================================================*/
create table USERS
(
   USERNAME             varchar(50) not null,
   PASSWORD             varchar(50),
   ENABLED              boolean,
   primary key (USERNAME)
);



alter table APPLICATIONS add constraint FK_REFERENCE_1 foreign key (STATUS_ID)
      references STATUS (ID) on delete restrict on update restrict;

alter table APPLICATION_MEDIA add constraint FK_REFERENCE_2 foreign key (APPLICATION_ID)
      references APPLICATIONS (ID) on delete restrict on update restrict;

alter table AUTHORITIES add constraint FK_REFERENCE_5 foreign key (USERNAME)
      references USERS (USERNAME) on delete restrict on update restrict;

alter table CUSTOMIZATIONS add constraint FK_REFERENCE_3 foreign key (APPLICATION_ID)
      references APPLICATIONS (ID) on delete restrict on update restrict;

alter table CUSTOMIZATIONS add constraint FK_REFERENCE_4 foreign key (STATUS_ID)
      references STATUS (ID) on delete restrict on update restrict;