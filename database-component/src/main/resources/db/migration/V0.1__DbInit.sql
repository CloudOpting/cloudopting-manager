CREATE SEQUENCE application_media_media_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE application_media_media_id_seq OWNER TO postgres;

--
-- TOC entry 198 (class 1259 OID 16538)
-- Name: application_media; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE application_media (
    id bigint DEFAULT nextval('application_media_media_id_seq'::regclass) NOT NULL,
    application_id bigint,
    media_content character varying(256)[] NOT NULL
);


ALTER TABLE application_media OWNER TO postgres;

--
-- TOC entry 2529 (class 0 OID 0)
-- Dependencies: 198
-- Name: TABLE application_media; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON TABLE application_media IS 'Table that will hold all the media content that can be used by the Catalogue frontend to present the application.
On developer choice the media could also be placed in the file system (so that is better retrieved) and here will be saved just the path.';


--
-- TOC entry 233 (class 1259 OID 17397)
-- Name: application_size_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE application_size_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE application_size_id_seq OWNER TO postgres;

--
-- TOC entry 234 (class 1259 OID 17399)
-- Name: application_size; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE application_size (
    id bigint DEFAULT nextval('application_size_id_seq'::regclass) NOT NULL,
    size character varying(100) NOT NULL
);


ALTER TABLE application_size OWNER TO postgres;

--
-- TOC entry 199 (class 1259 OID 16545)
-- Name: applications_application_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE applications_application_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE applications_application_id_seq OWNER TO postgres;

--
-- TOC entry 200 (class 1259 OID 16547)
-- Name: applications; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE applications (
    id bigint DEFAULT nextval('applications_application_id_seq'::regclass) NOT NULL,
    application_name character varying(50) NOT NULL,
    application_description text NOT NULL,
    application_tosca_template text,
    application_version character varying(10) NOT NULL,
    status_id bigint NOT NULL,
    organization_id bigint,
    application_tosca_name character varying(10),
    application_process bigint,
    short_description text,
    application_subscriber_mail text,
    application_sp_mail text,
    application_is_tryable boolean,
    terms text,
    service_price character varying(100),
    platform_price character varying(100),
    size_id bigint
);


ALTER TABLE applications OWNER TO postgres;

--
-- TOC entry 2530 (class 0 OID 0)
-- Dependencies: 200
-- Name: COLUMN applications.application_tosca_name; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN applications.application_tosca_name IS 'This is the name used in the storage and has to be the same as the one used in the tosca file.
This will be checked and validated';


--
-- TOC entry 201 (class 1259 OID 16554)
-- Name: applications_tags; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE applications_tags (
    application_id bigint NOT NULL,
    tag_id bigint NOT NULL
);


ALTER TABLE applications_tags OWNER TO postgres;

--
-- TOC entry 202 (class 1259 OID 16557)
-- Name: cloudaccounts_cloudaccount_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE cloudaccounts_cloudaccount_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE cloudaccounts_cloudaccount_id_seq OWNER TO postgres;

--
-- TOC entry 203 (class 1259 OID 16559)
-- Name: cloud_accounts; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE cloud_accounts (
    id bigint DEFAULT nextval('cloudaccounts_cloudaccount_id_seq'::regclass) NOT NULL,
    name character varying(50) NOT NULL,
    api_key character varying(100) NOT NULL,
    secret_key character varying(100) NOT NULL,
    endpoint character varying(100) NOT NULL,
    provider_id bigint NOT NULL,
    organization_id bigint NOT NULL
);


ALTER TABLE cloud_accounts OWNER TO postgres;

--
-- TOC entry 204 (class 1259 OID 16563)
-- Name: contact_contact_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE contact_contact_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE contact_contact_id_seq OWNER TO postgres;

--
-- TOC entry 205 (class 1259 OID 16565)
-- Name: contact; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE contact (
    id bigint DEFAULT nextval('contact_contact_id_seq'::regclass) NOT NULL,
    name character varying(100) NOT NULL,
    email character varying(100) NOT NULL,
    phone character varying(20),
    message character varying(1000),
    company_name character varying(100)
);


ALTER TABLE contact OWNER TO postgres;

--
-- TOC entry 231 (class 1259 OID 17260)
-- Name: customization_status_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE customization_status_id_seq
    START WITH 100
    INCREMENT BY 1
    MINVALUE 100
    NO MAXVALUE
    CACHE 1;


ALTER TABLE customization_status_id_seq OWNER TO postgres;

--
-- TOC entry 232 (class 1259 OID 17262)
-- Name: customization_status; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE customization_status (
    id bigint DEFAULT nextval('customization_status_id_seq'::regclass) NOT NULL,
    status character varying(50) NOT NULL
);


ALTER TABLE customization_status OWNER TO postgres;

--
-- TOC entry 206 (class 1259 OID 16572)
-- Name: customizations_customization_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE customizations_customization_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE customizations_customization_id_seq OWNER TO postgres;

--
-- TOC entry 207 (class 1259 OID 16574)
-- Name: customizations; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE customizations (
    id bigint DEFAULT nextval('customizations_customization_id_seq'::regclass) NOT NULL,
    application_id bigint NOT NULL,
    customization_tosca_file text NOT NULL,
    customization_creation date NOT NULL,
    customization_activation date NOT NULL,
    customization_decommission date NOT NULL,
    status_id bigint NOT NULL,
    process_id character varying(64),
    customer_organization_id bigint,
    cloud_account_id bigint,
    customization_form_value text,
    pay_service boolean,
    pay_platform boolean,
    is_trial boolean,
    trial_end_date date
);


ALTER TABLE customizations OWNER TO postgres;

--
-- TOC entry 208 (class 1259 OID 16581)
-- Name: hibernate_sequence; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE hibernate_sequence
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE hibernate_sequence OWNER TO postgres;

--
-- TOC entry 229 (class 1259 OID 17160)
-- Name: monitoring_info_elastic_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE monitoring_info_elastic_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE monitoring_info_elastic_id_seq OWNER TO postgres;

--
-- TOC entry 230 (class 1259 OID 17162)
-- Name: monitoring_info_elastic; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE monitoring_info_elastic (
    id bigint DEFAULT nextval('monitoring_info_elastic_id_seq'::regclass) NOT NULL,
    customization_id bigint NOT NULL,
    container character varying(1000),
    condition character varying(1000),
    fields character varying(1000),
    type character varying(1000),
    description text,
    title character varying(1000),
    pagination bigint
);


ALTER TABLE monitoring_info_elastic OWNER TO postgres;

--
-- TOC entry 209 (class 1259 OID 16583)
-- Name: organizationstatus_organizationstatus_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE organizationstatus_organizationstatus_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE organizationstatus_organizationstatus_id_seq OWNER TO postgres;

--
-- TOC entry 210 (class 1259 OID 16585)
-- Name: organization_status; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE organization_status (
    id bigint DEFAULT nextval('organizationstatus_organizationstatus_id_seq'::regclass) NOT NULL,
    status character varying(20) NOT NULL
);


ALTER TABLE organization_status OWNER TO postgres;

--
-- TOC entry 211 (class 1259 OID 16589)
-- Name: organizationtypes_organizationtype_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE organizationtypes_organizationtype_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE organizationtypes_organizationtype_id_seq OWNER TO postgres;

--
-- TOC entry 212 (class 1259 OID 16591)
-- Name: organization_types; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE organization_types (
    id bigint DEFAULT nextval('organizationtypes_organizationtype_id_seq'::regclass) NOT NULL,
    types character varying(20) NOT NULL
);


ALTER TABLE organization_types OWNER TO postgres;

--
-- TOC entry 213 (class 1259 OID 16595)
-- Name: organizations_organization_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE organizations_organization_id_seq
    START WITH 10
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;


ALTER TABLE organizations_organization_id_seq OWNER TO postgres;

--
-- TOC entry 214 (class 1259 OID 16597)
-- Name: organizations; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE organizations (
    id bigint DEFAULT nextval('organizations_organization_id_seq'::regclass) NOT NULL,
    organization_creation date NOT NULL,
    organization_activation date,
    organization_decommission date,
    status_id bigint NOT NULL,
    organization_type bigint NOT NULL,
    description character varying(500) NOT NULL,
    organization_key character varying(300),
    organization_name character varying(300),
    email character varying(100),
    contact_representative character varying(300),
    contact_phone character varying(100)
);


ALTER TABLE organizations OWNER TO postgres;

--
-- TOC entry 215 (class 1259 OID 16604)
-- Name: providers_provider_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE providers_provider_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE providers_provider_id_seq OWNER TO postgres;

--
-- TOC entry 216 (class 1259 OID 16606)
-- Name: providers; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE providers (
    id bigint DEFAULT nextval('providers_provider_id_seq'::regclass) NOT NULL,
    provider character varying(20) NOT NULL
);


ALTER TABLE providers OWNER TO postgres;

--
-- TOC entry 217 (class 1259 OID 16610)
-- Name: status_status_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE status_status_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE status_status_id_seq OWNER TO postgres;

--
-- TOC entry 218 (class 1259 OID 16612)
-- Name: status; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE status (
    id bigint DEFAULT nextval('status_status_id_seq'::regclass) NOT NULL,
    status character varying(20) NOT NULL
);


ALTER TABLE status OWNER TO postgres;

--
-- TOC entry 219 (class 1259 OID 16616)
-- Name: t_authority; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE t_authority (
    name character varying(50) NOT NULL
);


ALTER TABLE t_authority OWNER TO postgres;

--
-- TOC entry 220 (class 1259 OID 16619)
-- Name: t_persistent_audit_event_event_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE t_persistent_audit_event_event_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE t_persistent_audit_event_event_id_seq OWNER TO postgres;

--
-- TOC entry 221 (class 1259 OID 16621)
-- Name: t_persistent_audit_event; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE t_persistent_audit_event (
    event_id bigint DEFAULT nextval('t_persistent_audit_event_event_id_seq'::regclass) NOT NULL,
    principal character varying(255) NOT NULL,
    event_date timestamp without time zone NOT NULL,
    event_type character varying(255) NOT NULL
);


ALTER TABLE t_persistent_audit_event OWNER TO postgres;

--
-- TOC entry 222 (class 1259 OID 16628)
-- Name: t_persistent_audit_event_data; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE t_persistent_audit_event_data (
    event_id bigint NOT NULL,
    name character varying(255) NOT NULL,
    value character varying(255) NOT NULL
);


ALTER TABLE t_persistent_audit_event_data OWNER TO postgres;

--
-- TOC entry 223 (class 1259 OID 16634)
-- Name: t_persistent_token; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE t_persistent_token (
    series character varying(255) NOT NULL,
    user_id bigint NOT NULL,
    token_value character varying(255) NOT NULL,
    token_date date NOT NULL,
    ip_address character varying(39) NOT NULL,
    user_agent character varying(255) NOT NULL
);


ALTER TABLE t_persistent_token OWNER TO postgres;

--
-- TOC entry 224 (class 1259 OID 16640)
-- Name: t_user_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE t_user_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE t_user_id_seq OWNER TO postgres;

--
-- TOC entry 225 (class 1259 OID 16642)
-- Name: t_user; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE t_user (
    id bigint DEFAULT nextval('t_user_id_seq'::regclass) NOT NULL,
    login character varying(50) NOT NULL,
    password character varying(100) NOT NULL,
    first_name character varying(50) NOT NULL,
    last_name character varying(50) NOT NULL,
    email character varying(100) NOT NULL,
    activated boolean NOT NULL,
    lang_key character varying(5) NOT NULL,
    activation_key character varying(20) NOT NULL,
    created_by character varying(50) NOT NULL,
    created_date timestamp without time zone NOT NULL,
    last_modified_by character varying(50) NOT NULL,
    last_modified_date timestamp without time zone NOT NULL,
    organization_id bigint,
    reset_date date,
    reset_key character(20)
);


ALTER TABLE t_user OWNER TO postgres;

--
-- TOC entry 226 (class 1259 OID 16649)
-- Name: t_user_authority; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE t_user_authority (
    user_id bigint NOT NULL,
    authority_name character varying(50) NOT NULL
);


ALTER TABLE t_user_authority OWNER TO postgres;

--
-- TOC entry 227 (class 1259 OID 16652)
-- Name: tags_tag_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE tags_tag_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE tags_tag_id_seq OWNER TO postgres;

--
-- TOC entry 228 (class 1259 OID 16654)
-- Name: tags; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE tags (
    id bigint DEFAULT nextval('tags_tag_id_seq'::regclass) NOT NULL,
    tag character varying(20) NOT NULL
);


ALTER TABLE tags OWNER TO postgres;

--
-- TOC entry 2518 (class 2613 OID 17137)
-- Name: 17137; Type: BLOB; Schema: -; Owner: postgres
--

SELECT pg_catalog.lo_create('17137');


ALTER LARGE OBJECT 17137 OWNER TO postgres;




--
-- TOC entry 2532 (class 0 OID 0)
-- Dependencies: 197
-- Name: application_media_media_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('application_media_media_id_seq', 1, false);


--
-- TOC entry 2517 (class 0 OID 17399)
-- Dependencies: 234
-- Data for Name: application_size; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO application_size (id, size) VALUES (1, 'Small');
INSERT INTO application_size (id, size) VALUES (2, 'Medium');
INSERT INTO application_size (id, size) VALUES (3, 'Large');
INSERT INTO application_size (id, size) VALUES (4, 'Extra large');


--
-- TOC entry 2533 (class 0 OID 0)
-- Dependencies: 233
-- Name: application_size_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('application_size_id_seq', 4, true);


--
-- TOC entry 2483 (class 0 OID 16547)
-- Dependencies: 200
-- Data for Name: applications; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO applications (id, application_name, application_description, application_tosca_template, application_version, status_id, organization_id, application_tosca_name, application_process, short_description, application_subscriber_mail, application_sp_mail, application_is_tryable, terms, service_price, platform_price, size_id) VALUES (3366, 'Hello', 'Hello', NULL, '1', 105, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO applications (id, application_name, application_description, application_tosca_template, application_version, status_id, organization_id, application_tosca_name, application_process, short_description, application_subscriber_mail, application_sp_mail, application_is_tryable, terms, service_price, platform_price, size_id) VALUES (3367, 'Wordpress', 'bleh', NULL, '1', 3, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO applications (id, application_name, application_description, application_tosca_template, application_version, status_id, organization_id, application_tosca_name, application_process, short_description, application_subscriber_mail, application_sp_mail, application_is_tryable, terms, service_price, platform_price, size_id) VALUES (3372, 'hello', 'hello', NULL, '1', 105, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO applications (id, application_name, application_description, application_tosca_template, application_version, status_id, organization_id, application_tosca_name, application_process, short_description, application_subscriber_mail, application_sp_mail, application_is_tryable, terms, service_price, platform_price, size_id) VALUES (3541, 'service test 01', 'hfalksjhflka d hflkajsdhfjhflaskj f hlsak hflksahf lksjadhflkajshf hlkasjhf', NULL, '1', 105, NULL, 'tosca66776', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO applications (id, application_name, application_description, application_tosca_template, application_version, status_id, organization_id, application_tosca_name, application_process, short_description, application_subscriber_mail, application_sp_mail, application_is_tryable, terms, service_price, platform_price, size_id) VALUES (233, 'ASIA', 'ASIA-GUIA (Applied Integrated Systems Support) is a corporative database that accumulates every data related to Facilities and Events in Barcelona and its Metropolitan Area.

The main purpose is to centralize the information about these Facilities and Events into an only system that provides users with quality information updated by the different information and public attention channels that the Barcelona City Council has.', NULL, '2', 2, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO applications (id, application_name, application_description, application_tosca_template, application_version, status_id, organization_id, application_tosca_name, application_process, short_description, application_subscriber_mail, application_sp_mail, application_is_tryable, terms, service_price, platform_price, size_id) VALUES (195, 'Next2Me', 'NextToMe is an application offered by the Barcelona City Council that allows the citizen to find services and events occurring around him. The objective is to develop a mobile solution that looks for nearby services and public facilities, allowing the user to filter them by category of the service/event or to look for a specific one. The application provides a list of services and facilities, geo-location of the user and a map of the services and facilities.
Agenda and NextToMe applications are going to be migrated to the CloudOpting platform under the same experiment #3, since these two applications are sharing the same technological approach.', NULL, '3', 3, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO applications (id, application_name, application_description, application_tosca_template, application_version, status_id, organization_id, application_tosca_name, application_process, short_description, application_subscriber_mail, application_sp_mail, application_is_tryable, terms, service_price, platform_price, size_id) VALUES (236, 'MIB', 'MIB is a service that....', NULL, '1', 1, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO applications (id, application_name, application_description, application_tosca_template, application_version, status_id, organization_id, application_tosca_name, application_process, short_description, application_subscriber_mail, application_sp_mail, application_is_tryable, terms, service_price, platform_price, size_id) VALUES (1, 'Clearò', 'Clearò is a shared web portal, multi-agency, delivered in the cloud, designed in accordance with standard models and patterns imposed by the Italian national laws and rules about Transparency, that respects guidelines defined by the National Authority Anti Corruption (ANAC).

Clearò can be integrated easily into the own corporate website, and is customizable according the availability of contents. It is able to handle even the aspects of storage and updating in a synergistic way with the existing information systems supporting the organization and activities of the institutions (Financial, administrative acts, Personnel Management…).', 'csisp/Clearo.czar', '3', 3, NULL, NULL, NULL, NULL, 'Thank you for subscribing to <#if serviceName?has_content>${serviceName}</#if>.
You will be contacte', 'Dear <#if serviceOrganization?has_content>${serviceOrganization}</#if>,
the service <#if serviceName', NULL, NULL, NULL, NULL, NULL);
INSERT INTO applications (id, application_name, application_description, application_tosca_template, application_version, status_id, organization_id, application_tosca_name, application_process, short_description, application_subscriber_mail, application_sp_mail, application_is_tryable, terms, service_price, platform_price, size_id) VALUES (199, 'MobileID', 'The mobile digital-identity system is a cryptographic authentication service performed through a digital-identity application for citizens, downloaded to a mobile telephone device with access to the Internet under the owner’s control, in accordance with the current legislation. The system enables the user, among other things, to communicate with services of the administration and third parties that use this digital-identity authentication system.', NULL, '2', 2, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO applications (id, application_name, application_description, application_tosca_template, application_version, status_id, organization_id, application_tosca_name, application_process, short_description, application_subscriber_mail, application_sp_mail, application_is_tryable, terms, service_price, platform_price, size_id) VALUES (239, 'Energy Consumption - Corby', 'The service transforms a home (or business premises) into a smart space where the power, lighting and heating can be controlled from a smartphone or tablet. In addition, electricity use and pre-set options can be monitored to manage lighting or powered devices to meet the space requirements or reduce energy consumption.', NULL, '1', 1, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO applications (id, application_name, application_description, application_tosca_template, application_version, status_id, organization_id, application_tosca_name, application_process, short_description, application_subscriber_mail, application_sp_mail, application_is_tryable, terms, service_price, platform_price, size_id) VALUES (243, 'BusPortal - Corby', 'The Northamptonshire Bus Portal is an online website designed to help users plan their journey more easily. The service will be developed taking into account Northamptonshire’s ambition of accelerating modal shift which will result in greater public transport use. Expanding information on public services to an online platform is also part of Northamptonshire Transportation Plan of 2012. The service will be developed by TeamNet using Northamptonshire County Council data.', NULL, '3', 3, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO applications (id, application_name, application_description, application_tosca_template, application_version, status_id, organization_id, application_tosca_name, application_process, short_description, application_subscriber_mail, application_sp_mail, application_is_tryable, terms, service_price, platform_price, size_id) VALUES (182, 'Agenda', 'City Agenda is a service offered by Barcelona City Council that aims to increase the citizen participation by keeping them updated with the schedule of the events. The objective is to develop a mobile solution that allows the users to look for cultural and leisure events –by theme– happening in Barcelona. The application provides a list of events occurring within an established distance, geo-location of the user, map of events, search tool for events and possibility of highlighting events.', NULL, '3', 3, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO applications (id, application_name, application_description, application_tosca_template, application_version, status_id, organization_id, application_tosca_name, application_process, short_description, application_subscriber_mail, application_sp_mail, application_is_tryable, terms, service_price, platform_price, size_id) VALUES (181, 'FixThis', 'Fix This is an application set to ease the interaction among the citizens and the city council. The objective is to develop a mobile solution where citizens can report geo-located incidences with their smartphones, which are automatically sent to the incidents systems of the Public Administration for reporting and then take corrective actions. The application provides a list of possible incidences, geo-location of the user, record of incidences sent by the user and status of them.', NULL, '2', 2, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO applications (id, application_name, application_description, application_tosca_template, application_version, status_id, organization_id, application_tosca_name, application_process, short_description, application_subscriber_mail, application_sp_mail, application_is_tryable, terms, service_price, platform_price, size_id) VALUES (242, 'Transportation System - Corby', 'Transportation System - Corby is a service that allows to...', NULL, '2', 2, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO applications (id, application_name, application_description, application_tosca_template, application_version, status_id, organization_id, application_tosca_name, application_process, short_description, application_subscriber_mail, application_sp_mail, application_is_tryable, terms, service_price, platform_price, size_id) VALUES (716, 'TestApp', 'Test application description', NULL, '1', 1, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO applications (id, application_name, application_description, application_tosca_template, application_version, status_id, organization_id, application_tosca_name, application_process, short_description, application_subscriber_mail, application_sp_mail, application_is_tryable, terms, service_price, platform_price, size_id) VALUES (4056, 'aClearo', '                                                <p>Clearo description<br/></p>                                                <h2 class="service-summary-heading">Features</h2>                                                <ul class="service-summary-features-and-benefits">                                                    <li>Feature 1</li>                                                </ul>                                                <h2 class="service-summary-heading">Benefits</h2>                                                <ul class="service-summary-features-and-benefits">                                                    <li>Benefit 1</li>                                                </ul>                                                <h2 class="service-summary-heading">Support</h2>                                                <h2 class="service-summary-heading">Price</h2>                                                <h2 class="service-summary-heading">Contact</h2>                                                <p>Name Surname</p>                                                <p>phone</p>                                                <p>mail</p>                                            ', 'csi/clearo/template/tosca.zip', '1', 3, 3929, 'clearo', NULL, NULL, 'Thank you for subscribing to <#if serviceName?has_content>${serviceName}</#if>.
You will be contacte', 'Dear <#if serviceOrganization?has_content>${serviceOrganization}</#if>,
the service', NULL, NULL, NULL, NULL, NULL);
INSERT INTO applications (id, application_name, application_description, application_tosca_template, application_version, status_id, organization_id, application_tosca_name, application_process, short_description, application_subscriber_mail, application_sp_mail, application_is_tryable, terms, service_price, platform_price, size_id) VALUES (4516, 'ProvaFixDeployLogo', '                                                <p>Fix deploy logo win 10</p>                                                <h5 class="service-summary-heading">Features</h5>                                                <ul class="service-summary-features-and-benefits">                                                    <li>[Place your features here]</li>                                                    <li>[Place your features here]</li>                                                </ul>                                                <h5 class="service-summary-heading">Benefits</h5>                                                <ul class="service-summary-features-and-benefits">                                                    <li>[Place your benefits here]</li>                                                    <li>[Place your benefits here]</li>                                                </ul>                                                <h5 class="service-summary-heading">Support</h5>                                                <h5 class="service-summary-heading">Price</h5>                                                <h5 class="service-summary-heading">Contact</h5>                                                <ul class="service-summary-features-and-benefits">                                                    <li>Name, Surname: [Place your name here]</li>                                                    <li>Phone number: [Place your phone number here]</li>                                                    <li>E-mail: [Place your e-mail here]</li>                                                </ul>                                                                                            ', 'organization_key/Prova2/template/l-uomo-tigre-17.jpg', '1', 3, 1, 'Prova2', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);

--
-- TOC entry 2534 (class 0 OID 0)
-- Dependencies: 199
-- Name: applications_application_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('applications_application_id_seq', 4, true);

--
-- TOC entry 2486 (class 0 OID 16559)
-- Dependencies: 203
-- Data for Name: cloud_accounts; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO cloud_accounts (id, name, api_key, secret_key, endpoint, provider_id, organization_id) VALUES (1, 'CSI-Cloud', 'OOmWvl5fK_-WA1efI9-xyk8_mD-ZruqPDcNCQl1MqC6D8s3enzRssRRXnLwMhko93BfSBcIC3M8ZuPOsMCeSRQ', 'vkWj7MV9z0PIV6FEQyoqzB46CJ4fOziqSaCTyT1gCSa-FXsygKuePsJ__e2gJjEsug0k9Pt7WVDPWWIcEZ0wjA', 'https://cs1.cloudopen.csipiemonte.it:8443/client/api', 1, 1);


--
-- TOC entry 2535 (class 0 OID 0)
-- Dependencies: 202
-- Name: cloudaccounts_cloudaccount_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('cloudaccounts_cloudaccount_id_seq', 1, false);

--
-- TOC entry 2536 (class 0 OID 0)
-- Dependencies: 204
-- Name: contact_contact_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('contact_contact_id_seq', 1, false);


--
-- TOC entry 2515 (class 0 OID 17262)
-- Dependencies: 232
-- Data for Name: customization_status; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO customization_status (id, status) VALUES (100, 'Requested');
INSERT INTO customization_status (id, status) VALUES (101, 'Running');
INSERT INTO customization_status (id, status) VALUES (102, 'Stopped');
INSERT INTO customization_status (id, status) VALUES (103, 'To Delete');
INSERT INTO customization_status (id, status) VALUES (104, 'Deleted');


--
-- TOC entry 2537 (class 0 OID 0)
-- Dependencies: 231
-- Name: customization_status_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('customization_status_id_seq', 104, true);



--
-- TOC entry 2538 (class 0 OID 0)
-- Dependencies: 206
-- Name: customizations_customization_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('customizations_customization_id_seq', 10, true);


--
-- TOC entry 2539 (class 0 OID 0)
-- Dependencies: 208
-- Name: hibernate_sequence; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('hibernate_sequence', 4624, true);


--
-- TOC entry 2513 (class 0 OID 17162)
-- Dependencies: 230
-- Data for Name: monitoring_info_elastic; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO monitoring_info_elastic (id, customization_id, container, condition, fields, type, description, title, pagination) VALUES (1, 4067, '66.249.78.191', '/html', 'path', NULL, 'what this chart means', 'chart title', 50);
INSERT INTO monitoring_info_elastic (id, customization_id, container, condition, fields, type, description, title, pagination) VALUES (2, 4067, '66.249.78.191', '/html', 'path', NULL, 'second chart', 'second', 50);


--
-- TOC entry 2540 (class 0 OID 0)
-- Dependencies: 229
-- Name: monitoring_info_elastic_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('monitoring_info_elastic_id_seq', 1, true);


--
-- TOC entry 2493 (class 0 OID 16585)
-- Dependencies: 210
-- Data for Name: organization_status; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO organization_status (id, status) VALUES (1, 'Pending');
INSERT INTO organization_status (id, status) VALUES (2, 'Validated');
INSERT INTO organization_status (id, status) VALUES (3, 'Retired');


--
-- TOC entry 2495 (class 0 OID 16591)
-- Dependencies: 212
-- Data for Name: organization_types; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO organization_types (id, types) VALUES (1, 'Type 1');
INSERT INTO organization_types (id, types) VALUES (2, 'Type 2');
INSERT INTO organization_types (id, types) VALUES (3, 'Type 3');


--
-- TOC entry 2497 (class 0 OID 16597)
-- Dependencies: 214
-- Data for Name: organizations; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO organizations (id, organization_creation, organization_activation, organization_decommission, status_id, organization_type, description, organization_key, organization_name, email, contact_representative, contact_phone) VALUES (4034, '2016-01-26', '2016-01-26', NULL, 1, 1, 'SP', 'SP', 'SP', NULL, NULL, NULL);
INSERT INTO organizations (id, organization_creation, organization_activation, organization_decommission, status_id, organization_type, description, organization_key, organization_name, email, contact_representative, contact_phone) VALUES (3929, '2016-01-26', '2016-01-28', NULL, 1, 1, 'Consorzio per il sistema informativo della Regione Piemonte', 'csi', 'CSI-Piemonte', 'luca.gioppo@csi.it', 'Anna Cavallo', '+390113168713');
INSERT INTO organizations (id, organization_creation, organization_activation, organization_decommission, status_id, organization_type, description, organization_key, organization_name, email, contact_representative, contact_phone) VALUES (4176, '2016-01-28', '2016-01-28', NULL, 1, 1, 'Testing organization for subscribers', 'goth', 'Gotham City', 'luca.gioppo@csi.it', 'Bruce Wayne', '+4100000000');
INSERT INTO organizations (id, organization_creation, organization_activation, organization_decommission, status_id, organization_type, description, organization_key, organization_name, email, contact_representative, contact_phone) VALUES (4198, '2016-01-28', '2016-01-28', NULL, 1, 1, 'SC TeamNet International SA', 'TEA', 'Teamnet', NULL, NULL, NULL);
INSERT INTO organizations (id, organization_creation, organization_activation, organization_decommission, status_id, organization_type, description, organization_key, organization_name, email, contact_representative, contact_phone) VALUES (4197, '2016-01-28', '2016-01-28', NULL, 1, 1, 'Institut Municipal Informàtica (Barcelona)', 'IMI', 'IMI', NULL, NULL, NULL);
INSERT INTO organizations (id, organization_creation, organization_activation, organization_decommission, status_id, organization_type, description, organization_key, organization_name, email, contact_representative, contact_phone) VALUES (4199, '2016-01-28', '2016-01-28', NULL, 1, 1, 'Wellness Telecom', 'WT', 'Wellness Telecom', NULL, NULL, NULL);
INSERT INTO organizations (id, organization_creation, organization_activation, organization_decommission, status_id, organization_type, description, organization_key, organization_name, email, contact_representative, contact_phone) VALUES (4203, '2016-01-28', '2016-01-28', NULL, 1, 1, 'Atos WordLine', 'ATOSWL', 'Atos WordLine', NULL, NULL, NULL);
INSERT INTO organizations (id, organization_creation, organization_activation, organization_decommission, status_id, organization_type, description, organization_key, organization_name, email, contact_representative, contact_phone) VALUES (4204, '2016-01-28', '2016-01-28', NULL, 1, 1, 'SDG', 'SDG', 'SDG', NULL, NULL, NULL);
INSERT INTO organizations (id, organization_creation, organization_activation, organization_decommission, status_id, organization_type, description, organization_key, organization_name, email, contact_representative, contact_phone) VALUES (4205, '2016-01-28', '2016-01-28', NULL, 1, 1, 'Smart Partnership', 'SP', 'Smart Partnership', NULL, NULL, NULL);
INSERT INTO organizations (id, organization_creation, organization_activation, organization_decommission, status_id, organization_type, description, organization_key, organization_name, email, contact_representative, contact_phone) VALUES (4206, '2016-01-28', '2016-01-28', NULL, 1, 1, 'Profesia SRL', 'PRO', 'Profesia', NULL, NULL, NULL);
INSERT INTO organizations (id, organization_creation, organization_activation, organization_decommission, status_id, organization_type, description, organization_key, organization_name, email, contact_representative, contact_phone) VALUES (4207, '2016-01-28', '2016-01-28', NULL, 1, 1, 'Electric Corby', 'COR', 'Electric Corby', NULL, NULL, NULL);
INSERT INTO organizations (id, organization_creation, organization_activation, organization_decommission, status_id, organization_type, description, organization_key, organization_name, email, contact_representative, contact_phone) VALUES (4208, '2016-01-28', '2016-01-28', NULL, 1, 1, 'Netport Science Park AB', 'NET', 'Netport', NULL, NULL, NULL);
INSERT INTO organizations (id, organization_creation, organization_activation, organization_decommission, status_id, organization_type, description, organization_key, organization_name, email, contact_representative, contact_phone) VALUES (1, '2015-02-15', '2015-02-15', '2015-02-15', 1, 1, 'organization de', 'organization_key', 'organization_name', 'luca.gioppo@csi.it', 'Luca Gioppo', NULL);


--
-- TOC entry 2541 (class 0 OID 0)
-- Dependencies: 213
-- Name: organizations_organization_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('organizations_organization_id_seq', 0, true);


--
-- TOC entry 2542 (class 0 OID 0)
-- Dependencies: 209
-- Name: organizationstatus_organizationstatus_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('organizationstatus_organizationstatus_id_seq', 3, true);


--
-- TOC entry 2543 (class 0 OID 0)
-- Dependencies: 211
-- Name: organizationtypes_organizationtype_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('organizationtypes_organizationtype_id_seq', 1, false);


--
-- TOC entry 2499 (class 0 OID 16606)
-- Dependencies: 216
-- Data for Name: providers; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO providers (id, provider) VALUES (1, 'cloudstack');


--
-- TOC entry 2544 (class 0 OID 0)
-- Dependencies: 215
-- Name: providers_provider_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('providers_provider_id_seq', 1, false);


--
-- TOC entry 2501 (class 0 OID 16612)
-- Dependencies: 218
-- Data for Name: status; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO status (id, status) VALUES (1, 'Uploaded');
INSERT INTO status (id, status) VALUES (2, 'For Testing');
INSERT INTO status (id, status) VALUES (3, 'Published');
INSERT INTO status (id, status) VALUES (101, 'Running');
INSERT INTO status (id, status) VALUES (102, 'Stopped');
INSERT INTO status (id, status) VALUES (103, 'To Delete');
INSERT INTO status (id, status) VALUES (104, 'Deleted');
INSERT INTO status (id, status) VALUES (100, 'Requested');
INSERT INTO status (id, status) VALUES (90, 'In Deployment');
INSERT INTO status (id, status) VALUES (105, 'Draft');


--
-- TOC entry 2545 (class 0 OID 0)
-- Dependencies: 217
-- Name: status_status_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('status_status_id_seq', 1, false);


--
-- TOC entry 2502 (class 0 OID 16616)
-- Dependencies: 219
-- Data for Name: t_authority; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO t_authority (name) VALUES ('ROLE_ADMIN');
INSERT INTO t_authority (name) VALUES ('ROLE_USER');
INSERT INTO t_authority (name) VALUES ('ROLE_SUBSCRIBER');
INSERT INTO t_authority (name) VALUES ('ROLE_PUBLISHER');
INSERT INTO t_authority (name) VALUES ('ROLE_OPERATOR');


--
-- TOC entry 2546 (class 0 OID 0)
-- Dependencies: 220
-- Name: t_persistent_audit_event_event_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('t_persistent_audit_event_event_id_seq', 1, false);


--
-- TOC entry 2506 (class 0 OID 16634)
-- Dependencies: 223
-- Data for Name: t_persistent_token; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO t_persistent_token (series, user_id, token_value, token_date, ip_address, user_agent) VALUES ('vD+UoEjDB8FX5L4RDaLzOw==', 3, 'AV5uhkGoYdDGauDqV7EGog==', '2016-01-20', '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/47.0.2526.111 Safari/537.36');
INSERT INTO t_persistent_token (series, user_id, token_value, token_date, ip_address, user_agent) VALUES ('5IZch0McspWm1Ky+RnNYJg==', 3, 'KLU3H7nSSRNE7MKo0ZNN4g==', '2016-01-20', '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/47.0.2526.111 Safari/537.36');
INSERT INTO t_persistent_token (series, user_id, token_value, token_date, ip_address, user_agent) VALUES ('CEWUkMONPOsxBrDTjpGOCw==', 3, '+NVO2+V++Q0GQGgGS8GKPA==', '2016-01-21', '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/47.0.2526.111 Safari/537.36');
INSERT INTO t_persistent_token (series, user_id, token_value, token_date, ip_address, user_agent) VALUES ('1+DLKns/c2z4O3oGYqa5Hw==', 3, 'pbmXKEs7OtcAAmlW1DPbJA==', '2016-01-22', '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/47.0.2526.111 Safari/537.36');
INSERT INTO t_persistent_token (series, user_id, token_value, token_date, ip_address, user_agent) VALUES ('vakjY934DOg4bSgElYX+Dw==', 3, 'bgaCGjp+7gOYLKgmg8kzTg==', '2016-01-22', '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/47.0.2526.111 Safari/537.36');
INSERT INTO t_persistent_token (series, user_id, token_value, token_date, ip_address, user_agent) VALUES ('VJFfNejp341gMvfsJRbPHg==', 3, 'Cs2UEEWgz/H48O9adi94iA==', '2016-01-22', '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10.11; rv:43.0) Gecko/20100101 Firefox/43.0');
INSERT INTO t_persistent_token (series, user_id, token_value, token_date, ip_address, user_agent) VALUES ('QsVWEuUlClTMgSHmurzQig==', 3, 'FvmHpu3XW80LnbPfMQM2eg==', '2016-01-22', '10.0.2.2', 'Mozilla/5.0 (X11; Linux x86_64; rv:38.0) Gecko/20100101 Firefox/38.0');
INSERT INTO t_persistent_token (series, user_id, token_value, token_date, ip_address, user_agent) VALUES ('b8tHRSII2A3D6qAxur0COg==', 3, 'XhNNKcGYeBE3Xst3XzJKkQ==', '2016-01-25', '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/47.0.2526.111 Safari/537.36');
INSERT INTO t_persistent_token (series, user_id, token_value, token_date, ip_address, user_agent) VALUES ('n2icDqzjorKGjciZshwXbA==', 3, 'WhoggETe6CBIb9PphQm2ig==', '2016-01-25', '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/47.0.2526.111 Safari/537.36');
INSERT INTO t_persistent_token (series, user_id, token_value, token_date, ip_address, user_agent) VALUES ('yOWmSAr7lNCW+UQX5uxJ2w==', 3, 'BrgCOYiHA9wZflwKz49HFA==', '2016-01-25', '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/47.0.2526.111 Safari/537.36');
INSERT INTO t_persistent_token (series, user_id, token_value, token_date, ip_address, user_agent) VALUES ('Puzwac3Hoq0pdlmMAj12Mg==', 3, 'eb2WpL2zfL48hAAb1Q8Phw==', '2016-01-25', '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/47.0.2526.111 Safari/537.36');
INSERT INTO t_persistent_token (series, user_id, token_value, token_date, ip_address, user_agent) VALUES ('E9mXFJoucOlbrwGjI8VPcA==', 3, 'euzcjWjVixcC4jemhYSL7w==', '2016-01-25', '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/47.0.2526.111 Safari/537.36');
INSERT INTO t_persistent_token (series, user_id, token_value, token_date, ip_address, user_agent) VALUES ('9Tr8/qgMaNTNDrts7kRH/w==', 5, 'WnkXNqygc7+61Y3pH3wKxw==', '2016-01-25', '10.0.2.2', 'Mozilla/5.0 (X11; Linux x86_64; rv:38.0) Gecko/20100101 Firefox/38.0');
INSERT INTO t_persistent_token (series, user_id, token_value, token_date, ip_address, user_agent) VALUES ('06m7XVLRiR8OnzxzCsUnvQ==', 3, '25eWRxX0DnV89o16YvMelg==', '2016-01-25', '10.0.2.2', 'Mozilla/5.0 (X11; Linux x86_64; rv:38.0) Gecko/20100101 Firefox/38.0');
INSERT INTO t_persistent_token (series, user_id, token_value, token_date, ip_address, user_agent) VALUES ('QXGvJC13qQnFN1IqWWwnZQ==', 3, 'xJBXiV+zHgeDW0vpH+AG6Q==', '2016-01-25', '10.0.2.2', 'Mozilla/5.0 (X11; Linux x86_64; rv:38.0) Gecko/20100101 Firefox/38.0');
INSERT INTO t_persistent_token (series, user_id, token_value, token_date, ip_address, user_agent) VALUES ('Qi0FOq7SSb4SutJBZj5+fA==', 3, 'AROFeZZLWqLPl5AJz2oppA==', '2016-01-25', '10.0.2.2', 'Mozilla/5.0 (X11; Linux x86_64; rv:38.0) Gecko/20100101 Firefox/38.0');
INSERT INTO t_persistent_token (series, user_id, token_value, token_date, ip_address, user_agent) VALUES ('JoU2JY9EyIv0ZqYc6DflvQ==', 3, '8x/eQp3WNNO3leEy+6/06g==', '2016-01-25', '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/47.0.2526.111 Safari/537.36');
INSERT INTO t_persistent_token (series, user_id, token_value, token_date, ip_address, user_agent) VALUES ('Eo9S9PnLaQyDU+RQsMiKDQ==', 3, 'cxz/60IzWFBOHIzvgmT9cg==', '2016-01-25', '10.0.2.2', 'Mozilla/5.0 (X11; Linux x86_64; rv:38.0) Gecko/20100101 Firefox/38.0');
INSERT INTO t_persistent_token (series, user_id, token_value, token_date, ip_address, user_agent) VALUES ('HfA45XUf/m1Z/+t2f0xUzw==', 3, '62ex89mrpoxIWZoJkp4Zgg==', '2016-01-25', '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/47.0.2526.111 Safari/537.36');
INSERT INTO t_persistent_token (series, user_id, token_value, token_date, ip_address, user_agent) VALUES ('nU1AMSa8Xaskm0V1P9wLQg==', 3, 'aP1y/F1JLzS05wzaEEqRZw==', '2016-01-25', '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/47.0.2526.111 Safari/537.36');
INSERT INTO t_persistent_token (series, user_id, token_value, token_date, ip_address, user_agent) VALUES ('U8hZAINrRohqmizVg5CKSA==', 3, 'JqlQUrjUvirdAGug2Ha/Rg==', '2016-01-25', '10.0.2.2', 'Mozilla/5.0 (X11; Linux x86_64; rv:38.0) Gecko/20100101 Firefox/38.0');
INSERT INTO t_persistent_token (series, user_id, token_value, token_date, ip_address, user_agent) VALUES ('EPLi8HbFRZjypl6l0xGgZQ==', 3, 'WtDbpnP/zHsp0GrRRHgwzw==', '2016-01-25', '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/47.0.2526.111 Safari/537.36');
INSERT INTO t_persistent_token (series, user_id, token_value, token_date, ip_address, user_agent) VALUES ('AXDP4gevadEmaToewdbv0Q==', 3, '7KPCpwNkZaNL82WTfllP5g==', '2016-01-25', '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/47.0.2526.111 Safari/537.36');
INSERT INTO t_persistent_token (series, user_id, token_value, token_date, ip_address, user_agent) VALUES ('CXRekRY93ESC3E2NaHFaLw==', 3, 'fHOEbif4lt3G5JUGMnTC8A==', '2016-01-25', '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/47.0.2526.111 Safari/537.36');
INSERT INTO t_persistent_token (series, user_id, token_value, token_date, ip_address, user_agent) VALUES ('mOzLu5HEO14Z21l+PI6URw==', 3, 'un/E+MVStYSwhajD8nM0sQ==', '2016-01-26', '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/47.0.2526.111 Safari/537.36');
INSERT INTO t_persistent_token (series, user_id, token_value, token_date, ip_address, user_agent) VALUES ('kqxf09TZwkdRdTMkaAXquw==', 3, '1a3YR+4e4O+SqU3EjKda3w==', '2016-01-26', '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/47.0.2526.111 Safari/537.36');
INSERT INTO t_persistent_token (series, user_id, token_value, token_date, ip_address, user_agent) VALUES ('52JqDfQnVRXS8dkzRTxKyA==', 3930, 'OMhizQ8QkLNynSvtZZBjAg==', '2016-01-26', '10.0.2.2', 'Mozilla/5.0 (X11; Linux x86_64; rv:38.0) Gecko/20100101 Firefox/38.0');
INSERT INTO t_persistent_token (series, user_id, token_value, token_date, ip_address, user_agent) VALUES ('xxa3akCmf6RKnSejVMoXcg==', 3, 'BTHioaWKBv9zNnAhwSuGSA==', '2016-01-26', '10.0.2.2', 'Mozilla/5.0 (X11; Linux x86_64; rv:38.0) Gecko/20100101 Firefox/38.0');
INSERT INTO t_persistent_token (series, user_id, token_value, token_date, ip_address, user_agent) VALUES ('kE0yed/fleDUkNQuckFvfQ==', 3, 'xhXlj06IhBsKEcIzrPzmzQ==', '2016-01-26', '10.0.2.2', 'Mozilla/5.0 (X11; Linux x86_64; rv:38.0) Gecko/20100101 Firefox/38.0');
INSERT INTO t_persistent_token (series, user_id, token_value, token_date, ip_address, user_agent) VALUES ('psJZ9V9zCaDUyG0i+hzX8w==', 3, 'w8G5By5P0CZ2VFx6IM54AA==', '2016-01-26', '10.0.2.2', 'Mozilla/5.0 (X11; Linux x86_64; rv:38.0) Gecko/20100101 Firefox/38.0');
INSERT INTO t_persistent_token (series, user_id, token_value, token_date, ip_address, user_agent) VALUES ('su8m7pYzVkxRLFl/2B+zGA==', 3, 'UNidwMPQAJ+rfu/CfS2lSA==', '2016-01-26', '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/47.0.2526.111 Safari/537.36');
INSERT INTO t_persistent_token (series, user_id, token_value, token_date, ip_address, user_agent) VALUES ('9q96pz6sY5F9Z9PSe4kEPQ==', 3, 'K40ozSMGfKV1zslXaTLIQg==', '2016-01-26', '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/47.0.2526.111 Safari/537.36');
INSERT INTO t_persistent_token (series, user_id, token_value, token_date, ip_address, user_agent) VALUES ('at865diqBluxZMUGa3ZKeA==', 3, '419qEAW3pU7EQL2GHOkblA==', '2016-01-26', '10.0.2.2', 'Mozilla/5.0 (Windows NT 5.1; rv:43.0) Gecko/20100101 Firefox/43.0');
INSERT INTO t_persistent_token (series, user_id, token_value, token_date, ip_address, user_agent) VALUES ('eWSU3uk3c5Kd/lP0/dmylw==', 3, 'QTpezWokQr/9SGE9u4Pkuw==', '2016-01-26', '10.0.2.2', 'Mozilla/5.0 (X11; Linux x86_64; rv:38.0) Gecko/20100101 Firefox/38.0');
INSERT INTO t_persistent_token (series, user_id, token_value, token_date, ip_address, user_agent) VALUES ('NRoxvaL7qwp2CLvxBgSWfQ==', 3, 'i8E0kdsVPNVWabks/bh39Q==', '2016-01-26', '10.0.2.2', 'Mozilla/5.0 (Windows NT 5.1; rv:43.0) Gecko/20100101 Firefox/43.0');
INSERT INTO t_persistent_token (series, user_id, token_value, token_date, ip_address, user_agent) VALUES ('MQ0xd76gl90N5b8f420uSA==', 3, '+X5i07KBelwV6HyvgLsySg==', '2016-01-26', '10.0.2.2', 'Mozilla/5.0 (Windows NT 5.1; rv:43.0) Gecko/20100101 Firefox/43.0');
INSERT INTO t_persistent_token (series, user_id, token_value, token_date, ip_address, user_agent) VALUES ('RyOV23VuGyEjHj1VA2n4ag==', 3, 'H2IxYTkUimJGvcuGeY6+2A==', '2016-01-27', '10.0.2.2', 'Mozilla/5.0 (Windows NT 5.1; rv:43.0) Gecko/20100101 Firefox/43.0');
INSERT INTO t_persistent_token (series, user_id, token_value, token_date, ip_address, user_agent) VALUES ('MhvV6FATOP2503hyWpiJYQ==', 3, '12WHNv7G9JR8ppA1rAytsA==', '2016-01-26', '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/47.0.2526.111 Safari/537.36');
INSERT INTO t_persistent_token (series, user_id, token_value, token_date, ip_address, user_agent) VALUES ('FWe1Rc9ZV8Nw2HL2KyzBkw==', 3, 'XjcxqQWfBCbJeZqGyNUnWw==', '2016-01-26', '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/47.0.2526.111 Safari/537.36');
INSERT INTO t_persistent_token (series, user_id, token_value, token_date, ip_address, user_agent) VALUES ('vWkZ4tbt9YfwxBwxB7X80Q==', 3, 'RusvXGizd2jyk87MhlqW3g==', '2016-01-26', '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/47.0.2526.111 Safari/537.36');
INSERT INTO t_persistent_token (series, user_id, token_value, token_date, ip_address, user_agent) VALUES ('RI7CXGT/UFvcgYFegJ/x0w==', 3, 'XrdiZ7BO4pOlTq2Xi3DjCA==', '2016-01-26', '10.0.2.2', 'Mozilla/5.0 (X11; Linux x86_64; rv:38.0) Gecko/20100101 Firefox/38.0');
INSERT INTO t_persistent_token (series, user_id, token_value, token_date, ip_address, user_agent) VALUES ('0xekgkdJ662MP935O/jAOQ==', 3, 'R6LsIOch28Tj3LRCyXW27w==', '2016-01-27', '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/47.0.2526.111 Safari/537.36');
INSERT INTO t_persistent_token (series, user_id, token_value, token_date, ip_address, user_agent) VALUES ('q3/5kNHES4FYnEUB8EOfFA==', 3, 'B75gNZ/s6SHmkbIQM7raFQ==', '2016-01-27', '10.0.2.2', 'Mozilla/5.0 (Windows NT 5.1; rv:43.0) Gecko/20100101 Firefox/43.0');
INSERT INTO t_persistent_token (series, user_id, token_value, token_date, ip_address, user_agent) VALUES ('B99wTohmDykT6G7W8id1BA==', 3, 'AG2PZ74R/sX4KwArv0RVcw==', '2016-01-27', '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/47.0.2526.111 Safari/537.36');
INSERT INTO t_persistent_token (series, user_id, token_value, token_date, ip_address, user_agent) VALUES ('OQbVHT1PT9g+BiPaevyODg==', 3, 'KV1WVx0F9rKHQ8FX9RcGRA==', '2016-01-27', '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/42.0.2311.135 Safari/537.36');
INSERT INTO t_persistent_token (series, user_id, token_value, token_date, ip_address, user_agent) VALUES ('t7vj3AKI0V2ettjQZLM0BA==', 3, 'd/3+eFnBclsM8YE6S/iYBQ==', '2016-01-28', '10.0.2.2', 'Mozilla/5.0 (Windows NT 5.1; rv:44.0) Gecko/20100101 Firefox/44.0');
INSERT INTO t_persistent_token (series, user_id, token_value, token_date, ip_address, user_agent) VALUES ('Uqik94fDoaTxzQfmgRdH8g==', 3, 'mh/6dnIh67w3hSPAgqQ/0A==', '2016-01-28', '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/47.0.2526.111 Safari/537.36');
INSERT INTO t_persistent_token (series, user_id, token_value, token_date, ip_address, user_agent) VALUES ('EuFIH9ptgS6u+0dItHHN7Q==', 3, '1aj6k2kOUJYlaTdgxHbfiQ==', '2016-02-01', '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_10_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/48.0.2564.97 Safari/537.36');
INSERT INTO t_persistent_token (series, user_id, token_value, token_date, ip_address, user_agent) VALUES ('s8N2icUHTJmG5hmcK+2MQg==', 3, 'GwMIOqFNcSrZrqFGvtLkVw==', '2016-02-01', '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_10_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/48.0.2564.97 Safari/537.36');


--
-- TOC entry 2508 (class 0 OID 16642)
-- Dependencies: 225
-- Data for Name: t_user; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO t_user (id, login, password, first_name, last_name, email, activated, lang_key, activation_key, created_by, created_date, last_modified_by, last_modified_date, organization_id, reset_date, reset_key) VALUES (1, 'system', '$2a$10$mE.qmcV0mFU5NcKh73TZx.z4ueI/.bDWbj0T1BYyqP481kGGarKLG', 'system', 'System', 'syste@syste.com', true, 'en', '1234', 'system', '2015-02-15 14:12:41.67', 'admin', '2015-02-15 00:00:00', NULL, NULL, NULL);
INSERT INTO t_user (id, login, password, first_name, last_name, email, activated, lang_key, activation_key, created_by, created_date, last_modified_by, last_modified_date, organization_id, reset_date, reset_key) VALUES (4, 'operator', '$2a$10$gSAhZrxMllrbgj/kkK9UceBPpChGWJA7SYIb1Mqo.n5aNLq1/oRrC', 'Operator', 'Operator', 'operator@cloudopting.eu', true, 'en', '55555', 'admin', '2015-02-15 00:00:00', 'admin', '2015-02-15 00:00:00', NULL, NULL, NULL);
INSERT INTO t_user (id, login, password, first_name, last_name, email, activated, lang_key, activation_key, created_by, created_date, last_modified_by, last_modified_date, organization_id, reset_date, reset_key) VALUES (3, 'admin', '$2a$10$gSAhZrxMllrbgj/kkK9UceBPpChGWJA7SYIb1Mqo.n5aNLq1/oRrC', 'Admin', 'Administrator', 'admin@admin.com', true, 'en', '55555', 'admin', '2015-02-15 00:00:00', 'admin', '2015-09-21 13:34:25.883', 1, NULL, NULL);
INSERT INTO t_user (id, login, password, first_name, last_name, email, activated, lang_key, activation_key, created_by, created_date, last_modified_by, last_modified_date, organization_id, reset_date, reset_key) VALUES (2808, 'xrierape', '$2a$10$2J3Swo2eMyYL4YM.g6IIlOohjDunN0Nee4QzvGhLwDAC3e5t5hjFS', 'Xavier', 'Riera Pérez', 'xrierape@gmail.com', true, 'en', '94479004834454765443', 'anonymousUser', '2015-09-23 09:07:18.103', 'anonymousUser', '2015-09-23 09:07:18.103', NULL, NULL, NULL);
INSERT INTO t_user (id, login, password, first_name, last_name, email, activated, lang_key, activation_key, created_by, created_date, last_modified_by, last_modified_date, organization_id, reset_date, reset_key) VALUES (5, 'subscriber', '$2a$10$gSAhZrxMllrbgj/kkK9UceBPpChGWJA7SYIb1Mqo.n5aNLq1/oRrC', 'Subscriber', 'Subscriber', 'subscriber@cloudopting.eu', true, 'en', '21640000158412619719', 'admin', '2015-02-15 00:00:00', 'admin', '2016-01-25 08:50:10.86', 1, NULL, NULL);
INSERT INTO t_user (id, login, password, first_name, last_name, email, activated, lang_key, activation_key, created_by, created_date, last_modified_by, last_modified_date, organization_id, reset_date, reset_key) VALUES (4224, 'atos', '$2a$10$NS.a4jxZ/O1xAKxiw95HZer633OGnyP2XBtZAWnkyzsg8oTbf6KSu', 'atos', 'atos', 'atos@atos', true, 'es', '66584456996728323974', 'admin', '2016-01-28 11:16:20.707', 'admin', '2016-01-28 11:16:21.064', 4203, NULL, NULL);
INSERT INTO t_user (id, login, password, first_name, last_name, email, activated, lang_key, activation_key, created_by, created_date, last_modified_by, last_modified_date, organization_id, reset_date, reset_key) VALUES (4225, 'atossub', '$2a$10$85Sr5K1pqWY/euXgVciPg.YzJ4neVk67Y.MuHzHa1/EXGO8CbAEk.', 'atossub', 'atossub', 'atossub@atossub', true, 'es', '71753378132134513717', 'admin', '2016-01-28 11:17:09.676', 'admin', '2016-01-28 11:17:10.016', 4203, NULL, NULL);
INSERT INTO t_user (id, login, password, first_name, last_name, email, activated, lang_key, activation_key, created_by, created_date, last_modified_by, last_modified_date, organization_id, reset_date, reset_key) VALUES (4226, 'sdg', '$2a$10$IZvOH4m29kn795YpzMUyou3YQp5Q1j0X8QHLMTvUZFjK5QnRPwCuW', 'sdg', 'sdg', 'sdg@sdg', true, 'es', '14044400908802023069', 'admin', '2016-01-28 11:18:06.216', 'admin', '2016-01-28 11:18:06.555', 4204, NULL, NULL);
INSERT INTO t_user (id, login, password, first_name, last_name, email, activated, lang_key, activation_key, created_by, created_date, last_modified_by, last_modified_date, organization_id, reset_date, reset_key) VALUES (4227, 'sdgsub', '$2a$10$.IrhCxiOVXPAVD.FZ7qoOejR5OVjMtg6As7izBSuI7/lwoZnBWHim', 'sdgsub', 'sdgsub', 'sdgsub@sdgsub', true, 'es', '66428671708232022851', 'admin', '2016-01-28 11:18:38.255', 'admin', '2016-01-28 11:18:38.594', 4204, NULL, NULL);
INSERT INTO t_user (id, login, password, first_name, last_name, email, activated, lang_key, activation_key, created_by, created_date, last_modified_by, last_modified_date, organization_id, reset_date, reset_key) VALUES (3930, 'gioppoluca', '$2a$10$9fS/5N7.4s7ye4k50kFeU.cqdHjH3JzptLg7f3ZUlJ1J/2dmU3mHK', 'Luca', 'Gioppo', 'gioppo@csi.it', true, 'en', '64648802047098836422', 'admin', '2016-01-26 08:14:04.936', 'admin', '2016-01-27 08:10:21.551', 3929, NULL, NULL);
INSERT INTO t_user (id, login, password, first_name, last_name, email, activated, lang_key, activation_key, created_by, created_date, last_modified_by, last_modified_date, organization_id, reset_date, reset_key) VALUES (6, 'publisher', '$2a$10$gSAhZrxMllrbgj/kkK9UceBPpChGWJA7SYIb1Mqo.n5aNLq1/oRrC', 'Publisher', 'Publisher', 'publisher@cloudopting.eu', true, 'en', '55555', 'admin', '2015-02-15 00:00:00', 'admin', '2015-02-15 00:00:00', 1, NULL, NULL);
INSERT INTO t_user (id, login, password, first_name, last_name, email, activated, lang_key, activation_key, created_by, created_date, last_modified_by, last_modified_date, organization_id, reset_date, reset_key) VALUES (4177, 'batman', '$2a$10$2A8a8r7PdZ2sW6lIZAR.AeOeBMsFePoxM5NphTBBjmNNglxdkAe7y', 'Batman', 'DarkKnight', 'luca.gioppo@csi.it', true, 'en', '08924129517987390505', 'admin', '2016-01-28 10:20:22.946', 'admin', '2016-01-28 10:20:23.115', 4176, NULL, NULL);
INSERT INTO t_user (id, login, password, first_name, last_name, email, activated, lang_key, activation_key, created_by, created_date, last_modified_by, last_modified_date, organization_id, reset_date, reset_key) VALUES (4228, 'sp', '$2a$10$AtOllByr8uEryci74VPSBetSFawYPAjyYL3b87T2GVa.IIu82wmp.', 'sp', 'sp', 'sp@sp', true, 'es', '70398601152922291681', 'admin', '2016-01-28 11:19:13.498', 'admin', '2016-01-28 11:19:13.852', 4205, NULL, NULL);
INSERT INTO t_user (id, login, password, first_name, last_name, email, activated, lang_key, activation_key, created_by, created_date, last_modified_by, last_modified_date, organization_id, reset_date, reset_key) VALUES (4209, 'imi', '$2a$10$DcktY8ZUN57KNk.ihu1JreFded9PQZxUNXaeOlPgws2Jobbdzhtrq', 'IMI', 'IMI', 'imi@imi', true, 'es', '16664986804693461923', 'admin', '2016-01-28 10:58:48.439', 'admin', '2016-01-28 10:58:48.848', 4197, NULL, NULL);
INSERT INTO t_user (id, login, password, first_name, last_name, email, activated, lang_key, activation_key, created_by, created_date, last_modified_by, last_modified_date, organization_id, reset_date, reset_key) VALUES (3841, 'xevi', '$2a$10$cJ3Pe1ijHszRtA/jZDD9iuSYi13RWRmUKp9Wqd2yWGtDRpqmkCsaO', 'Xavier', 'Cases Camats', 'xeviscc@gmail.com', true, 'en', '79495699300130759288', 'admin', '2016-01-25 12:14:59.826', 'admin', '2016-01-26 10:50:42.001', 1, NULL, NULL);
INSERT INTO t_user (id, login, password, first_name, last_name, email, activated, lang_key, activation_key, created_by, created_date, last_modified_by, last_modified_date, organization_id, reset_date, reset_key) VALUES (4219, 'imisub', '$2a$10$SNx.Q1uyFpm/tbKQFIyvC.s..aAl.L/N3ixEEs5lfimCJlPigqqim', 'imisub', 'imisub', 'imisub@imi', true, 'es', '70487600179142301179', 'admin', '2016-01-28 11:08:25.022', 'admin', '2016-01-28 11:08:25.416', 4197, NULL, NULL);
INSERT INTO t_user (id, login, password, first_name, last_name, email, activated, lang_key, activation_key, created_by, created_date, last_modified_by, last_modified_date, organization_id, reset_date, reset_key) VALUES (4220, 'tea', '$2a$10$zNK4ChT2a4aizOt2z.JnReNEL21TeuIW7CnuR/Onyv/.CCqLWfkpS', 'tea', 'tea', 'tea@tea', true, 'en', '48065543069572603654', 'admin', '2016-01-28 11:13:08.132', 'admin', '2016-01-28 11:13:08.472', 4198, NULL, NULL);
INSERT INTO t_user (id, login, password, first_name, last_name, email, activated, lang_key, activation_key, created_by, created_date, last_modified_by, last_modified_date, organization_id, reset_date, reset_key) VALUES (4221, 'teasub', '$2a$10$tVh66niw9CZJeWeUlIxHveXhonl2pzQvgi2CMFL1b5WI51IVSRqvO', 'teasub', 'teasub', 'teasub@teasub', true, 'en', '66345853996746447264', 'admin', '2016-01-28 11:13:47.438', 'admin', '2016-01-28 11:13:47.781', 4198, NULL, NULL);
INSERT INTO t_user (id, login, password, first_name, last_name, email, activated, lang_key, activation_key, created_by, created_date, last_modified_by, last_modified_date, organization_id, reset_date, reset_key) VALUES (4222, 'wt', '$2a$10$n.3ejHTZ7TaoTDueSKr1/.o6u2VaPrFH6B7oZpVgvmq1PdnNcg9xS', 'wt', 'wt', 'wt@wt', true, 'es', '28787352189585816823', 'admin', '2016-01-28 11:14:40.541', 'admin', '2016-01-28 11:14:40.882', 4199, NULL, NULL);
INSERT INTO t_user (id, login, password, first_name, last_name, email, activated, lang_key, activation_key, created_by, created_date, last_modified_by, last_modified_date, organization_id, reset_date, reset_key) VALUES (4223, 'wtsub', '$2a$10$YElGsp2jMZkadZ0OOyDoJu3/G6Akb6nI9nnr8y7ZRc28LBU/AedOC', 'wtsub', 'wtsub', 'wtsub@stsub', true, 'es', '09661787364519288906', 'admin', '2016-01-28 11:15:30.075', 'admin', '2016-01-28 11:15:30.416', 4199, NULL, NULL);
INSERT INTO t_user (id, login, password, first_name, last_name, email, activated, lang_key, activation_key, created_by, created_date, last_modified_by, last_modified_date, organization_id, reset_date, reset_key) VALUES (4229, 'spsub', '$2a$10$Kv58CYCnvM2Ea..fZQ19FOtIyy.ny2O5D1o.KUBRPrrPEBHxyGTSC', 'spsub', 'spsub', 'spsub@spsub', true, 'es', '89294477505746669494', 'admin', '2016-01-28 11:19:46.123', 'admin', '2016-01-28 11:19:46.554', 4205, NULL, NULL);
INSERT INTO t_user (id, login, password, first_name, last_name, email, activated, lang_key, activation_key, created_by, created_date, last_modified_by, last_modified_date, organization_id, reset_date, reset_key) VALUES (4230, 'pro', '$2a$10$oAFqGK0D7w7WbtoviYrWL.5DKBuUAySDchrpoSisFpMGcDk03dxnu', 'pro', 'pro', 'guido.spadotto@profesia.it', true, 'it', '34760273132049155931', 'admin', '2016-01-28 11:20:36.491', 'admin', '2016-01-28 11:20:36.849', 4206, NULL, NULL);
INSERT INTO t_user (id, login, password, first_name, last_name, email, activated, lang_key, activation_key, created_by, created_date, last_modified_by, last_modified_date, organization_id, reset_date, reset_key) VALUES (4231, 'prosub', '$2a$10$kELVmJH7VaYQhBoMUAYkdO7R4En1Xlq1kHyGyHchUGgqomgV0lZ1G', 'prosub', 'prosub', 'prosub@prosub', true, 'it', '86694160555412434687', 'admin', '2016-01-28 11:24:09.55', 'admin', '2016-01-28 11:24:09.9', 4206, NULL, NULL);
INSERT INTO t_user (id, login, password, first_name, last_name, email, activated, lang_key, activation_key, created_by, created_date, last_modified_by, last_modified_date, organization_id, reset_date, reset_key) VALUES (4233, 'cor', '$2a$10$oaG5ov4yNOYsC5JGhvCRzuVKWIUnkpFOy5JUNm3lc9/ab8Z06QbeO', 'cor', 'cor', 'cor@cor', true, 'en', '61527725684125697751', 'admin', '2016-01-28 11:24:54.486', 'admin', '2016-01-28 11:24:54.954', 4207, NULL, NULL);
INSERT INTO t_user (id, login, password, first_name, last_name, email, activated, lang_key, activation_key, created_by, created_date, last_modified_by, last_modified_date, organization_id, reset_date, reset_key) VALUES (4234, 'corsub', '$2a$10$8t265HL4uC9t8CWjFlpFaOkj8882gtPZQvlnGjTCEpqJDyLjbMeM.', 'corsub', 'corsub', 'corsub@corsub', true, 'en', '30125710938047895572', 'admin', '2016-01-28 11:25:29.17', 'admin', '2016-01-28 11:25:29.549', 4207, NULL, NULL);
INSERT INTO t_user (id, login, password, first_name, last_name, email, activated, lang_key, activation_key, created_by, created_date, last_modified_by, last_modified_date, organization_id, reset_date, reset_key) VALUES (4235, 'net', '$2a$10$9U9rzZT.ibjJRZRsr4AOfuyt4/ehG.zvdj8N502zIpTdHaAIiMnz6', 'net', 'net', 'net@net', true, 'en', '39639451946334542429', 'admin', '2016-01-28 11:26:00.799', 'admin', '2016-01-28 11:26:01.409', 4208, NULL, NULL);
INSERT INTO t_user (id, login, password, first_name, last_name, email, activated, lang_key, activation_key, created_by, created_date, last_modified_by, last_modified_date, organization_id, reset_date, reset_key) VALUES (4236, 'netsub', '$2a$10$I/slw6SrTMPAwFtcbvZrp.jxkwqjjjEiBBMRQOUAuIOlTG3LSpNl.', 'netsub', 'netsub', 'netsub@netsub', true, 'en', '46822539615638081970', 'admin', '2016-01-28 11:26:31.049', 'admin', '2016-01-28 11:26:31.39', 4208, NULL, NULL);
INSERT INTO t_user (id, login, password, first_name, last_name, email, activated, lang_key, activation_key, created_by, created_date, last_modified_by, last_modified_date, organization_id, reset_date, reset_key) VALUES (4250, 'a', '$2a$10$/Q6B8hokebqD7MDKJLELie1prcEUcE54z6s3FLGK7xuqKuORZPtfi', 'b', 'Cases Camats', 'asxeviscc@gmail.com', true, 'en', '16870557188820091615', 'admin', '2016-01-28 12:02:59.024', 'admin', '2016-01-28 12:02:59.348', 1, NULL, NULL);
INSERT INTO t_user (id, login, password, first_name, last_name, email, activated, lang_key, activation_key, created_by, created_date, last_modified_by, last_modified_date, organization_id, reset_date, reset_key) VALUES (4367, 'cpavel', '$2a$10$8RTYnRRsXFhLugA.WJZZK.ySJxn3rdSp2vG3LKO.bOc9X12e1oOYG', 'ciprian', 'pavel', 'ciprian.pavel@teamnet.ro', true, 'en', '09513228075759540515', 'anonymousUser', '2016-01-29 07:30:18.034', 'admin', '2016-01-29 07:31:13.655', 4198, NULL, NULL);


--
-- TOC entry 2509 (class 0 OID 16649)
-- Dependencies: 226
-- Data for Name: t_user_authority; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO t_user_authority (user_id, authority_name) VALUES (3, 'ROLE_ADMIN');
INSERT INTO t_user_authority (user_id, authority_name) VALUES (3, 'ROLE_USER');
INSERT INTO t_user_authority (user_id, authority_name) VALUES (4, 'ROLE_OPERATOR');
INSERT INTO t_user_authority (user_id, authority_name) VALUES (5, 'ROLE_SUBSCRIBER');
INSERT INTO t_user_authority (user_id, authority_name) VALUES (6, 'ROLE_PUBLISHER');
INSERT INTO t_user_authority (user_id, authority_name) VALUES (2808, 'ROLE_SUBSCRIBER');
INSERT INTO t_user_authority (user_id, authority_name) VALUES (5, 'ROLE_USER');
INSERT INTO t_user_authority (user_id, authority_name) VALUES (4, 'ROLE_USER');
INSERT INTO t_user_authority (user_id, authority_name) VALUES (6, 'ROLE_USER');
INSERT INTO t_user_authority (user_id, authority_name) VALUES (2808, 'ROLE_USER');
INSERT INTO t_user_authority (user_id, authority_name) VALUES (3841, 'ROLE_SUBSCRIBER');
INSERT INTO t_user_authority (user_id, authority_name) VALUES (3841, 'ROLE_USER');
INSERT INTO t_user_authority (user_id, authority_name) VALUES (3930, 'ROLE_USER');
INSERT INTO t_user_authority (user_id, authority_name) VALUES (3930, 'ROLE_PUBLISHER');
INSERT INTO t_user_authority (user_id, authority_name) VALUES (4177, 'ROLE_SUBSCRIBER');
INSERT INTO t_user_authority (user_id, authority_name) VALUES (4177, 'ROLE_USER');
INSERT INTO t_user_authority (user_id, authority_name) VALUES (4209, 'ROLE_OPERATOR');
INSERT INTO t_user_authority (user_id, authority_name) VALUES (4209, 'ROLE_USER');
INSERT INTO t_user_authority (user_id, authority_name) VALUES (4209, 'ROLE_ADMIN');
INSERT INTO t_user_authority (user_id, authority_name) VALUES (4219, 'ROLE_SUBSCRIBER');
INSERT INTO t_user_authority (user_id, authority_name) VALUES (4219, 'ROLE_USER');
INSERT INTO t_user_authority (user_id, authority_name) VALUES (4219, 'ROLE_ADMIN');
INSERT INTO t_user_authority (user_id, authority_name) VALUES (4220, 'ROLE_USER');
INSERT INTO t_user_authority (user_id, authority_name) VALUES (4220, 'ROLE_ADMIN');
INSERT INTO t_user_authority (user_id, authority_name) VALUES (4221, 'ROLE_SUBSCRIBER');
INSERT INTO t_user_authority (user_id, authority_name) VALUES (4221, 'ROLE_USER');
INSERT INTO t_user_authority (user_id, authority_name) VALUES (4222, 'ROLE_USER');
INSERT INTO t_user_authority (user_id, authority_name) VALUES (4222, 'ROLE_ADMIN');
INSERT INTO t_user_authority (user_id, authority_name) VALUES (4223, 'ROLE_SUBSCRIBER');
INSERT INTO t_user_authority (user_id, authority_name) VALUES (4223, 'ROLE_USER');
INSERT INTO t_user_authority (user_id, authority_name) VALUES (4224, 'ROLE_USER');
INSERT INTO t_user_authority (user_id, authority_name) VALUES (4224, 'ROLE_ADMIN');
INSERT INTO t_user_authority (user_id, authority_name) VALUES (4225, 'ROLE_SUBSCRIBER');
INSERT INTO t_user_authority (user_id, authority_name) VALUES (4225, 'ROLE_USER');
INSERT INTO t_user_authority (user_id, authority_name) VALUES (4226, 'ROLE_USER');
INSERT INTO t_user_authority (user_id, authority_name) VALUES (4226, 'ROLE_ADMIN');
INSERT INTO t_user_authority (user_id, authority_name) VALUES (4227, 'ROLE_SUBSCRIBER');
INSERT INTO t_user_authority (user_id, authority_name) VALUES (4227, 'ROLE_USER');
INSERT INTO t_user_authority (user_id, authority_name) VALUES (4228, 'ROLE_USER');
INSERT INTO t_user_authority (user_id, authority_name) VALUES (4228, 'ROLE_ADMIN');
INSERT INTO t_user_authority (user_id, authority_name) VALUES (4229, 'ROLE_SUBSCRIBER');
INSERT INTO t_user_authority (user_id, authority_name) VALUES (4229, 'ROLE_USER');
INSERT INTO t_user_authority (user_id, authority_name) VALUES (4230, 'ROLE_USER');
INSERT INTO t_user_authority (user_id, authority_name) VALUES (4230, 'ROLE_ADMIN');
INSERT INTO t_user_authority (user_id, authority_name) VALUES (4231, 'ROLE_SUBSCRIBER');
INSERT INTO t_user_authority (user_id, authority_name) VALUES (4231, 'ROLE_USER');
INSERT INTO t_user_authority (user_id, authority_name) VALUES (4233, 'ROLE_USER');
INSERT INTO t_user_authority (user_id, authority_name) VALUES (4233, 'ROLE_ADMIN');
INSERT INTO t_user_authority (user_id, authority_name) VALUES (4234, 'ROLE_SUBSCRIBER');
INSERT INTO t_user_authority (user_id, authority_name) VALUES (4234, 'ROLE_USER');
INSERT INTO t_user_authority (user_id, authority_name) VALUES (4235, 'ROLE_USER');
INSERT INTO t_user_authority (user_id, authority_name) VALUES (4235, 'ROLE_ADMIN');
INSERT INTO t_user_authority (user_id, authority_name) VALUES (4236, 'ROLE_SUBSCRIBER');
INSERT INTO t_user_authority (user_id, authority_name) VALUES (4236, 'ROLE_USER');
INSERT INTO t_user_authority (user_id, authority_name) VALUES (4250, 'ROLE_USER');
INSERT INTO t_user_authority (user_id, authority_name) VALUES (4250, 'ROLE_ADMIN');
INSERT INTO t_user_authority (user_id, authority_name) VALUES (4367, 'ROLE_SUBSCRIBER');
INSERT INTO t_user_authority (user_id, authority_name) VALUES (4367, 'ROLE_USER');


--
-- TOC entry 2547 (class 0 OID 0)
-- Dependencies: 224
-- Name: t_user_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('t_user_id_seq', 1, false);


--
-- TOC entry 2511 (class 0 OID 16654)
-- Dependencies: 228
-- Data for Name: tags; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 2548 (class 0 OID 0)
-- Dependencies: 227
-- Name: tags_tag_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('tags_tag_id_seq', 1, false);


--
-- TOC entry 2519 (class 0 OID 0)
-- Data for Name: BLOBS; Type: BLOBS; Schema: -; Owner: 
--

SET search_path = pg_catalog;

BEGIN;

SELECT pg_catalog.lo_open('17137', 131072);
SELECT pg_catalog.lowrite(0, '\x7b22646270617373223a2261646d696e222c2270617373776f7264223a2261646d696e646231222c227365727665726e616d65223a227777772e7765622e6974222c22706f7374677265735f70617373776f7264223a2261646d696e6462227d');
SELECT pg_catalog.lo_close(0);

COMMIT;

SET search_path = public, pg_catalog;


--
-- TOC entry 2258 (class 2606 OID 16752)
-- Name: aplpications_tags_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY applications_tags
    ADD CONSTRAINT aplpications_tags_pkey PRIMARY KEY (application_id, tag_id);


--
-- TOC entry 2305 (class 2606 OID 17404)
-- Name: application_size_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY application_size
    ADD CONSTRAINT application_size_pkey PRIMARY KEY (id);


--
-- TOC entry 2256 (class 2606 OID 16754)
-- Name: applications_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY applications
    ADD CONSTRAINT applications_pkey PRIMARY KEY (id);


--
-- TOC entry 2260 (class 2606 OID 16756)
-- Name: cloudaccounts_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY cloud_accounts
    ADD CONSTRAINT cloudaccounts_pkey PRIMARY KEY (id);


--
-- TOC entry 2262 (class 2606 OID 16758)
-- Name: contact_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY contact
    ADD CONSTRAINT contact_pkey PRIMARY KEY (id);


--
-- TOC entry 2303 (class 2606 OID 17267)
-- Name: customization_status_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY customization_status
    ADD CONSTRAINT customization_status_pkey PRIMARY KEY (id);


--
-- TOC entry 2264 (class 2606 OID 16760)
-- Name: customizations_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY customizations
    ADD CONSTRAINT customizations_pkey PRIMARY KEY (id);


--
-- TOC entry 2301 (class 2606 OID 17170)
-- Name: mon_elastic_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY monitoring_info_elastic
    ADD CONSTRAINT mon_elastic_pkey PRIMARY KEY (id);


--
-- TOC entry 2266 (class 2606 OID 16762)
-- Name: organization_status_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY organization_status
    ADD CONSTRAINT organization_status_pkey PRIMARY KEY (id);


--
-- TOC entry 2272 (class 2606 OID 16764)
-- Name: organizations_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY organizations
    ADD CONSTRAINT organizations_pkey PRIMARY KEY (id);


--
-- TOC entry 2269 (class 2606 OID 16766)
-- Name: organizationtypes_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY organization_types
    ADD CONSTRAINT organizationtypes_pkey PRIMARY KEY (id);


--
-- TOC entry 2254 (class 2606 OID 16768)
-- Name: pk_application_media; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY application_media
    ADD CONSTRAINT pk_application_media PRIMARY KEY (id);


--
-- TOC entry 2280 (class 2606 OID 16770)
-- Name: pk_t_authority; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY t_authority
    ADD CONSTRAINT pk_t_authority PRIMARY KEY (name);


--
-- TOC entry 2283 (class 2606 OID 16772)
-- Name: pk_t_persistent_audit_event; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY t_persistent_audit_event
    ADD CONSTRAINT pk_t_persistent_audit_event PRIMARY KEY (event_id);


--
-- TOC entry 2288 (class 2606 OID 16774)
-- Name: pk_t_persistent_token; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY t_persistent_token
    ADD CONSTRAINT pk_t_persistent_token PRIMARY KEY (series);


--
-- TOC entry 2292 (class 2606 OID 16776)
-- Name: pk_t_user; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY t_user
    ADD CONSTRAINT pk_t_user PRIMARY KEY (id);


--
-- TOC entry 2274 (class 2606 OID 16778)
-- Name: providers_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY providers
    ADD CONSTRAINT providers_pkey PRIMARY KEY (id);


--
-- TOC entry 2277 (class 2606 OID 16780)
-- Name: status_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY status
    ADD CONSTRAINT status_pkey PRIMARY KEY (id);


--
-- TOC entry 2286 (class 2606 OID 16782)
-- Name: t_persistent_audit_event_data_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY t_persistent_audit_event_data
    ADD CONSTRAINT t_persistent_audit_event_data_pkey PRIMARY KEY (event_id, name);


--
-- TOC entry 2296 (class 2606 OID 16784)
-- Name: t_user_authority_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY t_user_authority
    ADD CONSTRAINT t_user_authority_pkey PRIMARY KEY (user_id, authority_name);


--
-- TOC entry 2298 (class 2606 OID 16786)
-- Name: tags_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY tags
    ADD CONSTRAINT tags_pkey PRIMARY KEY (id);


--
-- TOC entry 2252 (class 1259 OID 16832)
-- Name: application_media_application_i; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE UNIQUE INDEX application_media_application_i ON application_media USING btree (application_id);


--
-- TOC entry 2281 (class 1259 OID 16833)
-- Name: idx_persistent_audit_event; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX idx_persistent_audit_event ON t_persistent_audit_event USING btree (principal, event_date);


--
-- TOC entry 2284 (class 1259 OID 16834)
-- Name: idx_persistent_audit_event_data; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX idx_persistent_audit_event_data ON t_persistent_audit_event_data USING btree (event_id);


--
-- TOC entry 2289 (class 1259 OID 16835)
-- Name: idx_user_email; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE UNIQUE INDEX idx_user_email ON t_user USING btree (email);


--
-- TOC entry 2290 (class 1259 OID 16836)
-- Name: idx_user_login; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE UNIQUE INDEX idx_user_login ON t_user USING btree (login);


--
-- TOC entry 2267 (class 1259 OID 16837)
-- Name: organizationstatus_pkey; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE UNIQUE INDEX organizationstatus_pkey ON organization_status USING btree (id);


--
-- TOC entry 2270 (class 1259 OID 16838)
-- Name: organizationtypes_types_key; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE UNIQUE INDEX organizationtypes_types_key ON organization_types USING btree (types);


--
-- TOC entry 2275 (class 1259 OID 16839)
-- Name: providers_provider_key; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE UNIQUE INDEX providers_provider_key ON providers USING btree (provider);


--
-- TOC entry 2278 (class 1259 OID 16840)
-- Name: status_status_key; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE UNIQUE INDEX status_status_key ON status USING btree (status);


--
-- TOC entry 2293 (class 1259 OID 16841)
-- Name: t_user_email_key; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE UNIQUE INDEX t_user_email_key ON t_user USING btree (email);


--
-- TOC entry 2294 (class 1259 OID 16842)
-- Name: t_user_login_key; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE UNIQUE INDEX t_user_login_key ON t_user USING btree (login);


--
-- TOC entry 2299 (class 1259 OID 16843)
-- Name: tags_tag_key; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE UNIQUE INDEX tags_tag_key ON tags USING btree (tag);


--
-- TOC entry 2327 (class 2606 OID 16949)
-- Name: application_media_application_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY application_media
    ADD CONSTRAINT application_media_application_id_fkey FOREIGN KEY (application_id) REFERENCES applications(id) ON UPDATE RESTRICT ON DELETE RESTRICT;


--
-- TOC entry 2328 (class 2606 OID 16954)
-- Name: applications_status_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY applications
    ADD CONSTRAINT applications_status_id_fkey FOREIGN KEY (status_id) REFERENCES status(id) ON UPDATE RESTRICT ON DELETE RESTRICT;


--
-- TOC entry 2331 (class 2606 OID 16959)
-- Name: fk_app_tag_references_app; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY applications_tags
    ADD CONSTRAINT fk_app_tag_references_app FOREIGN KEY (application_id) REFERENCES applications(id);


--
-- TOC entry 2332 (class 2606 OID 16964)
-- Name: fk_app_tag_references_tag; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY applications_tags
    ADD CONSTRAINT fk_app_tag_references_tag FOREIGN KEY (tag_id) REFERENCES tags(id);


--
-- TOC entry 2329 (class 2606 OID 16969)
-- Name: fk_applic_reference_organ; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY applications
    ADD CONSTRAINT fk_applic_reference_organ FOREIGN KEY (organization_id) REFERENCES organizations(id);


--
-- TOC entry 2330 (class 2606 OID 17405)
-- Name: fk_application_size; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY applications
    ADD CONSTRAINT fk_application_size FOREIGN KEY (size_id) REFERENCES application_size(id);


--
-- TOC entry 2343 (class 2606 OID 16974)
-- Name: fk_authority_name; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY t_user_authority
    ADD CONSTRAINT fk_authority_name FOREIGN KEY (authority_name) REFERENCES t_authority(name);


--
-- TOC entry 2333 (class 2606 OID 16979)
-- Name: fk_clouacc_reference_org; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY cloud_accounts
    ADD CONSTRAINT fk_clouacc_reference_org FOREIGN KEY (organization_id) REFERENCES organizations(id);


--
-- TOC entry 2334 (class 2606 OID 16984)
-- Name: fk_clouacc_reference_provider; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY cloud_accounts
    ADD CONSTRAINT fk_clouacc_reference_provider FOREIGN KEY (provider_id) REFERENCES providers(id);


--
-- TOC entry 2335 (class 2606 OID 16989)
-- Name: fk_cloud_account; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY customizations
    ADD CONSTRAINT fk_cloud_account FOREIGN KEY (cloud_account_id) REFERENCES cloud_accounts(id);


--
-- TOC entry 2337 (class 2606 OID 17268)
-- Name: fk_custom_custom_status; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY customizations
    ADD CONSTRAINT fk_custom_custom_status FOREIGN KEY (status_id) REFERENCES customization_status(id);


--
-- TOC entry 2336 (class 2606 OID 16994)
-- Name: fk_custom_reference_org; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY customizations
    ADD CONSTRAINT fk_custom_reference_org FOREIGN KEY (customer_organization_id) REFERENCES organizations(id);


--
-- TOC entry 2340 (class 2606 OID 16999)
-- Name: fk_event_persistent_audit_event_data; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY t_persistent_audit_event_data
    ADD CONSTRAINT fk_event_persistent_audit_event_data FOREIGN KEY (event_id) REFERENCES t_persistent_audit_event(event_id);


--
-- TOC entry 2345 (class 2606 OID 17171)
-- Name: fk_monelastic_customization; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY monitoring_info_elastic
    ADD CONSTRAINT fk_monelastic_customization FOREIGN KEY (customization_id) REFERENCES customizations(id);


--
-- TOC entry 2338 (class 2606 OID 17004)
-- Name: fk_org_org_status; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY organizations
    ADD CONSTRAINT fk_org_org_status FOREIGN KEY (status_id) REFERENCES organization_status(id);


--
-- TOC entry 2339 (class 2606 OID 17009)
-- Name: fk_organ_reference_organtypes; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY organizations
    ADD CONSTRAINT fk_organ_reference_organtypes FOREIGN KEY (organization_type) REFERENCES organization_types(id);


--
-- TOC entry 2342 (class 2606 OID 17014)
-- Name: fk_organiz_reference_organiz; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY t_user
    ADD CONSTRAINT fk_organiz_reference_organiz FOREIGN KEY (organization_id) REFERENCES organizations(id);


--
-- TOC entry 2344 (class 2606 OID 17019)
-- Name: fk_user_id; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY t_user_authority
    ADD CONSTRAINT fk_user_id FOREIGN KEY (user_id) REFERENCES t_user(id);


--
-- TOC entry 2341 (class 2606 OID 17024)
-- Name: fk_user_persistent_token; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY t_persistent_token
    ADD CONSTRAINT fk_user_persistent_token FOREIGN KEY (user_id) REFERENCES t_user(id);


alter table customizations add CONSTRAINT fk_custom_application FOREIGN KEY (application_id)
      REFERENCES applications (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;
      
-- Completed on 2016-02-02 15:24:40

--
-- PostgreSQL database dump complete
--

