CREATE SEQUENCE customization_deploy_info_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
	
CREATE TABLE customization_deploy_info (
    id bigint DEFAULT nextval('customization_deploy_info_id_seq'::regclass) NOT NULL,
	customization_id bigint NOT NULL, 
	hostname character varying(1000),
	fqdn character varying(1000),
	internal_ip character varying(100),
	public_ip character varying(100)
);

ALTER TABLE ONLY customization_deploy_info
    ADD CONSTRAINT customization_deploy_info_pkey PRIMARY KEY (id);

ALTER TABLE ONLY customization_deploy_info
    ADD CONSTRAINT fk_deploy_info_customization FOREIGN KEY (customization_id) REFERENCES customizations(id);
	

CREATE SEQUENCE customization_zabbix_monitor_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
	
CREATE TABLE customization_zabbix_monitor (
    id bigint DEFAULT nextval('customization_zabbix_monitor_id_seq'::regclass) NOT NULL,
	customization_id bigint NOT NULL, 
	hostname character varying(1000),
	item text,
	history text,
	sortfield text,
	limit_ bigint,
	description text,
	chart_type text,
	unit text
);

ALTER TABLE ONLY customization_zabbix_monitor
    ADD CONSTRAINT customization_zabbix_pkey PRIMARY KEY (id);

ALTER TABLE ONLY customization_zabbix_monitor
    ADD CONSTRAINT fk_zabbix_monitor_customization FOREIGN KEY (customization_id) REFERENCES customizations(id);