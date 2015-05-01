
/* Drop Tables */

DROP TABLE IF EXISTS customizations;
DROP TABLE IF EXISTS application_media;
DROP TABLE IF EXISTS Applications;
DROP TABLE IF EXISTS status;
DROP TABLE IF EXISTS users;




/* Create Tables */

CREATE TABLE Applications
(
	application_id serial NOT NULL UNIQUE,
	service_provider_id int NOT NULL UNIQUE,
	application_name varchar(50) NOT NULL,
	application_description text NOT NULL,
	application_tosca_template xml NOT NULL,
	application_version varchar(10) NOT NULL,
	status_id int NOT NULL UNIQUE,
	PRIMARY KEY (application_id)
) WITHOUT OIDS;


ALTER SEQUENCE Applications_application_id_SEQ INCREMENT 1 MINVALUE 1;


-- Table that will hold all the media content that can be used by the Catalogue frontend to present the application.
-- On developer choice the media could also be placed in the file system (so that is better retrieved) and here will be saved just the path.
CREATE TABLE application_media
(
	media_id serial NOT NULL UNIQUE,
	media_content bytea,
	application_id int NOT NULL UNIQUE,
	PRIMARY KEY (media_id)
) WITHOUT OIDS;


ALTER SEQUENCE application_media_media_id_SEQ INCREMENT 1 MINVALUE 1;


CREATE TABLE customizations
(
	customization_id serial NOT NULL UNIQUE,
	application_id int NOT NULL UNIQUE,
	customer_id int NOT NULL UNIQUE,
	customization_tosca_file xml NOT NULL,
	customization_creation date,
	customization_activation date,
	customization_decommission date,
	status_id int NOT NULL UNIQUE,
	PRIMARY KEY (customization_id)
) WITHOUT OIDS;


ALTER SEQUENCE customizations_customization_id_SEQ INCREMENT 1 MINVALUE 1;


CREATE TABLE status
(
	status_id serial NOT NULL UNIQUE,
	status varchar(20) NOT NULL UNIQUE,
	PRIMARY KEY (status_id)
) WITHOUT OIDS;


ALTER SEQUENCE status_status_id_SEQ INCREMENT 1 MINVALUE 1;


CREATE TABLE users
(
	user_id int NOT NULL UNIQUE,
	user_name varchar(15) NOT NULL UNIQUE,
	user_mail varchar(100) NOT NULL UNIQUE,
	user_password varchar(50) NOT NULL,
	registration_date date NOT NULL,
	PRIMARY KEY (user_id)
) WITHOUT OIDS;



/* Create Foreign Keys */

ALTER TABLE customizations
	ADD FOREIGN KEY (application_id)
	REFERENCES Applications (application_id)
	ON UPDATE RESTRICT
	ON DELETE RESTRICT
;


ALTER TABLE application_media
	ADD FOREIGN KEY (application_id)
	REFERENCES Applications (application_id)
	ON UPDATE RESTRICT
	ON DELETE RESTRICT
;


ALTER TABLE customizations
	ADD FOREIGN KEY (status_id)
	REFERENCES status (status_id)
	ON UPDATE RESTRICT
	ON DELETE RESTRICT
;


ALTER TABLE Applications
	ADD FOREIGN KEY (status_id)
	REFERENCES status (status_id)
	ON UPDATE RESTRICT
	ON DELETE RESTRICT
;


ALTER TABLE Applications
	ADD FOREIGN KEY (service_provider_id)
	REFERENCES users (user_id)
	ON UPDATE RESTRICT
	ON DELETE RESTRICT
;


ALTER TABLE customizations
	ADD FOREIGN KEY (customer_id)
	REFERENCES users (user_id)
	ON UPDATE RESTRICT
	ON DELETE RESTRICT
;



/* Comments */

COMMENT ON TABLE application_media IS 'Table that will hold all the media content that can be used by the Catalogue frontend to present the application.
On developer choice the media could also be placed in the file system (so that is better retrieved) and here will be saved just the path.';



