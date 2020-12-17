#to login using root to psql shell
psql postgres

#create the user for prod db
CREATE ROLE crs_user WITH LOGIN PASSWORD 'crs_user';
CREATE ROLE crs_user_preprod WITH LOGIN PASSWORD 'crs_user_preprod';
ALTER USER crs_user_preprod VALID UNTIL 'infinity';


#allow him to create db
ALTER ROLE crs_user CREATEDB;

#to list all the users with their permission
\du

#quit
\q

#login as crs_user
psql postgres -U crs_user

#create db
CREATE DATABASE crs_db;

#grant all priviliges on this db to crs_user
GRANT ALL PRIVILEGES ON DATABASE crs_db TO crs_user;

#list all dbs to confirm db is created
\list

#connect to the db to create schema
\connect crs_db

#drop existing schema
drop schema crs_schema cascade;

#create schema for db
CREATE SCHEMA crs_schema;

# grant priveleges to crs_user_preprod on schema crs_schema_preprod
GRANT USAGE ON SCHEMA crs_schema TO crs_user_preprod;
grant usage on schema crs_schema_preprod to crs_user_preprod;
grant create on schema crs_schema_preprod to crs_user_preprod;
grant execute on all functions in schema crs_schema to public; -- this is crs_schema due to reason, it is not mistake
alter default privileges in schema crs_schema grant execute on functions to public;
--alter default privileges in schema crs_ grant usage on types to public;
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA crs_schema_preprod TO crs_user_preprod;

#Set Schema
set schema 'crs_schema';
-- set search_path to crs_schema_preprod,public,crs_schema;

ALTER DATABASE crs_db SET timezone= 'Asia/Kolkata';

--CREATE SCHEMA crs_schema_performance;
--set schema 'crs_schema_performance';

CREATE EXTENSION pgcrypto;
--CREATE EXTENSION pgcrypto with schema crs_schema_preprod;
--create type schedule_status AS ENUM ('SHORTLISTED','SCHEDULING_REQUESTED', 'SCHEDULED', 'VISITED', 'ACCEPTED', 'REJECTED');
CREATE TYPE crs_schema.schedule_status AS ENUM
    ('SHORTLISTED', 'SCHEDULING_REQUESTED', 'SCHEDULED', 'VISITED', 'ACCEPTED', 'REJECTED', 'ACTIVATED', 'DEACTIVATED', 'PENDING_ACTIVATION');

--CREATE TYPE crs_schema_preprod.schedule_status AS ENUM ('SHORTLISTED', 'SCHEDULING_REQUESTED', 'SCHEDULED', 'VISITED', 'ACCEPTED', 'REJECTED', 'ACTIVATED', 'DEACTIVATED', 'PENDING_ACTIVATION');


create table if not exists apartment (
apartment_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
apartment_name TEXT UNIQUE NOT NULL,
apartment_latitude NUMERIC NOT NULL,
apartment_longitude NUMERIC NOT Null,
created_time TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL
);

create table if not exists apartment_amenity (
apartment_id UUID NOT NULL REFERENCES apartment(apartment_id) PRIMARY KEY,
powerbackup BOOLEAN NOT NULL DEFAULT FALSE,
lift BOOLEAN NOT NULL DEFAULT FALSE,
security BOOLEAN NOT NULL DEFAULT FALSE,
playarea BOOLEAN NOT NULL DEFAULT FALSE,
clubhouse BOOLEAN NOT NULL DEFAULT FALSE,
stp BOOLEAN NOT NULL DEFAULT FALSE,
wtp BOOLEAN NOT NULL DEFAULT FALSE,
visitorparking BOOLEAN NOT NULL DEFAULT FALSE,
swimmingpool BOOLEAN NOT NULL DEFAULT FALSE,
gym BOOLEAN NOT NULL DEFAULT FALSE,
lawntennis BOOLEAN NOT NULL DEFAULT FALSE,
snooker BOOLEAN NOT NULL DEFAULT FALSE,
tabletennis BOOLEAN NOT NULL DEFAULT FALSE,
basketball BOOLEAN NOT NULL DEFAULT FALSE,
created_time TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL
);

create table if not exists listing_type (
listing_type_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
listing_type_name TEXT NOT NULL --Rental, Lease, Sale
);

create table if not exists listing_property_type (
property_type_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
property_type_name TEXT NOT NULL --Individual House, Apartment, Row House, Builder Floor
);
CREATE INDEX listing_property_type_name_idx ON listing_property_type (property_type_name);

create table if not exists listing_furnishing_type (
furnishing_type_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
furnishing_type_name TEXT NOT NULL --Fully Furnished, Semi Furnished, Not Furnished
);
CREATE INDEX listing_furnishing_type_name_idx ON listing_furnishing_type (furnishing_type_name);


create table if not exists listing_transaction_mode (
transaction_mode_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
transaction_mode_name TEXT NOT NULL --Cash, Cheque, Online Transfers, No Preference
);
CREATE INDEX listing_transaction_mode_name_idx ON listing_transaction_mode (transaction_mode_name);


create table if not exists listing_bedroom_count (
bedroom_count_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
bedroom_count_name TEXT NOT NULL --1 BHK, 2 BHK, 3 BHK, 4+ BHK
);
CREATE INDEX listing_bedroom_count_name_idx ON listing_bedroom_count (bedroom_count_name);

create table if not exists crs_user (
user_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
user_name TEXT NOT NULL,
user_mobile TEXT UNIQUE NOT NULL,
user_email TEXT,
user_acceptance_doc BYTEA,
user_govt_id_no TEXT,
user_govt_id_image BYTEA,
user_pref_comm_mode TEXT [], --Phone, SMS, WhatsApp
user_pref_comm_freq TEXT, --Daily, Weekly, Monthly
user_pref_visit_slot json, --Saturday 10-12, Sunday 1-3 etc...
authorities integer[] NOT NULL DEFAULT array[1]::integer[],
user_secret BYTEA,
created_time TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL
);
CREATE INDEX crs_user_mobile_idx ON crs_user (user_mobile);

create table if not exists listing (
listing_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
user_id UUID NOT NULL REFERENCES crs_user (user_id),
listing_title TEXT NOT NULL,
listing_address TEXT NOT NULL,
listing_type_id UUID NOT NULL REFERENCES listing_type (listing_type_id),
property_type_id UUID NOT NULL REFERENCES listing_property_type (property_type_id),
listing_images TEXT, --This will be like array of 'images/<listing_id>/<image_id>' updated once images are uploaded
listing_latitude NUMERIC NOT NULL,
listing_longitude NUMERIC NOT NULL,
listing_area INTEGER NOT NULL,
listing_value NUMERIC(12,2) NOT NULL,
listing_deposit NUMERIC(12,2), --Can be null for sale properties
apartment_id UUID REFERENCES apartment (apartment_id), --Can be null for non-apartment listings
furnishing_type_id UUID NOT NULL REFERENCES listing_furnishing_type (furnishing_type_id),
bedroom_count_id UUID NOT NULL REFERENCES listing_bedroom_count(bedroom_count_id),
transaction_mode_id UUID NOT NULL REFERENCES listing_transaction_mode (transaction_mode_id),
non_veg_allowed BOOLEAN NOT NULL DEFAULT TRUE,
bachelor_allowed BOOLEAN NOT NULL DEFAULT TRUE,
listing_active_ind BOOLEAN NOT NULL DEFAULT FALSE,
created_time TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL
);
CREATE INDEX listing_lat_idx ON listing (listing_latitude);
CREATE INDEX listing_long_idx ON listing (listing_longitude);
CREATE INDEX listing_value_idx ON listing (listing_value);
-- Create more indexes if required later


create table if not exists listing_amenity (
listing_id UUID NOT NULL REFERENCES listing(listing_id) PRIMARY KEY,
fans BOOLEAN NOT NULL DEFAULT FALSE,
geyser BOOLEAN NOT NULL DEFAULT FALSE,
cupboards BOOLEAN NOT NULL DEFAULT FALSE,
chimney BOOLEAN NOT NULL DEFAULT FALSE,
parking BOOLEAN NOT NULL DEFAULT FALSE,
airconditioner BOOLEAN NOT NULL DEFAULT FALSE,
sofa BOOLEAN NOT NULL DEFAULT FALSE,
diningtable BOOLEAN NOT NULL DEFAULT FALSE,
centertable BOOLEAN NOT NULL DEFAULT FALSE,
television BOOLEAN NOT NULL DEFAULT FALSE,
internet BOOLEAN NOT NULL DEFAULT FALSE,
dth BOOLEAN NOT NULL DEFAULT FALSE,
refrigerator BOOLEAN NOT NULL DEFAULT FALSE,
washingmachine BOOLEAN NOT NULL DEFAULT FALSE,
bed BOOLEAN NOT NULL DEFAULT FALSE,
created_time TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL
);


create table if not exists scheduled_visits (
owner_user_id UUID NOT NULL REFERENCES crs_user (user_id),
tenant_user_id UUID NOT NULL REFERENCES crs_user (user_id),
listing_id UUID NOT NULL REFERENCES listing (listing_id),
scheduled_slot TEXT NOT NULL,
owner_feedback TEXT,
tenant_feedback TEXT,
probable_outcome TEXT,
PRIMARY KEY (owner_user_id, tenant_user_id, listing_id),
created_time TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL
);

create table if not exists listing_user_schedule_mapping (
listing_id UUID NOT NULL REFERENCES listing (listing_id),
user_id UUID NOT NULL REFERENCES crs_user (user_id),
schedule_status schedule_status NOT NULL,
PRIMARY KEY (listing_id, user_id),
created_time TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL
);
CREATE INDEX listing_user_schedule_status_idx ON listing_user_schedule_mapping (schedule_status);



--Below table can be combined with Listing Table if performance issues are seen
create table if not exists listing_locality_data (
listing_id UUID NOT NULL REFERENCES listing (listing_id),
locality_data TEXT NOT NULL, --this will be used to storeListingImages the enitre data as json
PRIMARY KEY (listing_id),
created_time TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL
);


-- Below tables are for Auth and security. Need to be analyzed for performance---

CREATE TABLE OAUTH_CLIENT_DETAILS (
  CLIENT_ID VARCHAR(255) PRIMARY KEY,
  RESOURCE_IDS VARCHAR(255),
  CLIENT_SECRET VARCHAR(255),
  SCOPE VARCHAR(255),
  AUTHORIZED_GRANT_TYPES VARCHAR(255),
  WEB_SERVER_REDIRECT_URI VARCHAR(255),
  AUTHORITIES VARCHAR(255),
  ACCESS_TOKEN_VALIDITY INTEGER,
  REFRESH_TOKEN_VALIDITY INTEGER,
  ADDITIONAL_INFORMATION VARCHAR(4096),
  AUTOAPPROVE VARCHAR(255),
  created_time TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL
);

CREATE TABLE OAUTH_CLIENT_TOKEN (
  TOKEN_ID VARCHAR(255),
  TOKEN BYTEA,
  AUTHENTICATION_ID VARCHAR(255) PRIMARY KEY,
  USER_NAME VARCHAR(255),
  CLIENT_ID VARCHAR(255),
  created_time TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL
);

CREATE TABLE OAUTH_ACCESS_TOKEN (
  TOKEN_ID VARCHAR(255),
  TOKEN BYTEA,
  AUTHENTICATION_ID VARCHAR(255) PRIMARY KEY,
  USER_NAME VARCHAR(255),
  CLIENT_ID VARCHAR(255),
  AUTHENTICATION BYTEA,
  REFRESH_TOKEN VARCHAR(255),
  created_time TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL
);

CREATE TABLE OAUTH_REFRESH_TOKEN (
  TOKEN_ID VARCHAR(255),
  TOKEN BYTEA,
  AUTHENTICATION BYTEA,
  created_time TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL
);

CREATE TABLE OAUTH_CODE (
  CODE VARCHAR(255),
  AUTHENTICATION BYTEA,
  created_time TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL
);

CREATE TABLE OAUTH_APPROVALS (
  USERID VARCHAR(255),
  CLIENTID VARCHAR(255),
  SCOPE VARCHAR(255),
  STATUS VARCHAR(10),
  EXPIRESAT TIMESTAMP,
  LASTMODIFIEDAT TIMESTAMP
);

CREATE TABLE AUTHORITY (
   ID  BIGSERIAL NOT NULL,
   NAME VARCHAR(255),
   PRIMARY KEY (ID),
   created_time TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
   CONSTRAINT AUTHORITY_NAME UNIQUE (NAME)
);

--ALTER TABLE IF EXISTS AUTHORITY ADD CONSTRAINT AUTHORITY_NAME UNIQUE (NAME);

-- crs_user already has it
--ALTER TABLE crs_user ADD COLUMN authorities integer[] NOT NULL DEFAULT array[1]::integer[];
	
-- crs_user already has it
--ALTER TABLE crs_user ADD COLUMN user_secret BYTEA;

-- Table: deactivated_owner_listing_details

-- DROP TABLE crs_schema_performance.deactivated_owner_listing_details;

CREATE TABLE if not exists deactivated_owner_listing_details
(
    owner_id UUID NOT NULL REFERENCES crs_user (user_id),
    listing_id UUID NOT NULL REFERENCES listing (listing_id),
    tenant_user_id UUID REFERENCES crs_user (user_id),
    cashback_status TEXT NOT NULL,
    deactivation_type TEXT NOT NULL,
    created_time TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
    CONSTRAINT deactivated_owner_listing_pk PRIMARY KEY (owner_id, listing_id)
);

--ALTER TABLE crs_schema_performance.deactivated_owner_listing_details
 --   OWNER to crs_user;

--GRANT ALL ON TABLE crs_schema_performance.deactivated_owner_listing_details TO crs_user WITH GRANT OPTION;


create table if not exists user_request_callback (
	phone_no TEXT NOT NULL,
	contacted_user BOOLEAN DEFAULT FALSE,
	created_time TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL
);

ALTER TABLE crs_user ADD COLUMN user_facebook_id text;

ALTER TABLE crs_user ADD COLUMN user_google_id text;

ALTER TABLE apartment ADD COLUMN apartment_images TEXT;

ALTER TABLE listing ADD COLUMN last_modified_time TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL;

ALTER TABLE listing_amenity ADD COLUMN last_modified_time TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL;

ALTER TABLE apartment ADD COLUMN last_modified_time TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL;

ALTER TABLE apartment_amenity ADD COLUMN last_modified_time TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL;

ALTER TABLE crs_user ADD CONSTRAINT USER_MOBILE UNIQUE (user_mobile);

alter table crs_user drop column user_external_ind;


ALTER TABLE listing_user_schedule_mapping
    ADD COLUMN schedule_itinerary_status text DEFAULT 'NOT_CREATED';

ALTER TABLE listing_user_schedule_mapping
    ADD COLUMN potential_itinerary json;

ALTER TABLE listing_user_schedule_mapping
    ADD COLUMN finalized_itinerary json;

	CREATE MATERIALIZED VIEW tenant_listing_schedule
AS
SELECT l.user_id as owner_id,lusm.listing_id, lusm.user_id as tenant_id, lusm.schedule_status,
lusm.schedule_itinerary_status, lusm.potential_itinerary, lusm.finalized_itinerary,
cu.user_pref_visit_slot as tenant_pref_visit_slot,
(SELECT u.user_pref_visit_slot from crs_user u where u.user_id = l.user_id) as owner_pref_visit_slot
	FROM listing_user_schedule_mapping lusm
	LEFT JOIN crs_user cu on lusm.user_id = cu.user_id
	LEFT JOIN listing l on lusm.listing_id = l.listing_id
	where lusm.schedule_status='SCHEDULING_REQUESTED' and lusm.schedule_itinerary_status NOT IN ('CREATED')
	WITH NO DATA;

	CREATE UNIQUE INDEX tenant_listing_owner ON tenant_listing_schedule (owner_id, listing_id, tenant_id);

	ALTER TABLE tenant_listing_schedule
  OWNER TO crs_user;

	REFRESH MATERIALIZED VIEW tenant_listing_schedule;

	CREATE TABLE crs_audit
(
    audit_id uuid DEFAULT gen_random_uuid(),
    input json,
    result text,
    audit_type text,
    created_time timestamp with time zone NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT crs_audit_pkey PRIMARY KEY (audit_id)
);

ALTER TABLE crs_audit
    OWNER to crs_user;

    ALTER TABLE listing_user_schedule_mapping DROP COLUMN finalized_itinerary CASCADE;

ALTER TABLE listing_user_schedule_mapping
    ADD COLUMN finalized_itinerary text;


    CREATE MATERIALIZED VIEW tenant_listing_schedule
AS
SELECT l.user_id as owner_id,lusm.listing_id, lusm.user_id as tenant_id, lusm.schedule_status,
lusm.schedule_itinerary_status, lusm.potential_itinerary, lusm.finalized_itinerary,
cu.user_pref_visit_slot as tenant_pref_visit_slot,
(SELECT u.user_pref_visit_slot from crs_user u where u.user_id = l.user_id) as owner_pref_visit_slot
	FROM listing_user_schedule_mapping lusm
	LEFT JOIN crs_user cu on lusm.user_id = cu.user_id
	LEFT JOIN listing l on lusm.listing_id = l.listing_id
	where lusm.schedule_status='SCHEDULING_REQUESTED' and lusm.schedule_itinerary_status NOT IN ('CREATED')
	WITH NO DATA;

	CREATE UNIQUE INDEX tenant_listing_owner ON tenant_listing_schedule (owner_id, listing_id, tenant_id);

	ALTER TABLE tenant_listing_schedule
  OWNER TO crs_user;

	REFRESH MATERIALIZED VIEW tenant_listing_schedule;

alter table listing add column created_by_user_id UUID NOT NULL REFERENCES crs_user (user_id);
alter table listing add column last_activation_time TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL;

ALTER TYPE schedule_status ADD VALUE 'SYSTEM_PENDING_DEACTIVATION' AFTER 'PENDING_ACTIVATION';
ALTER TYPE schedule_status ADD VALUE 'SYSTEM_DEACTIVATED' AFTER 'SYSTEM_PENDING_DEACTIVATION';

----3 May 2019----
create table if not exists tag (
tag_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
tag_name TEXT UNIQUE NOT NULL,
created_time TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL
);

create table if not exists listing_tag_mapping (
listing_id UUID NOT NULL REFERENCES listing (listing_id),
tag_id UUID NOT NULL REFERENCES tag (tag_id),
PRIMARY KEY (listing_id),
created_time TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL
);

create table if not exists user_tag_mapping (
user_id UUID NOT NULL REFERENCES crs_user (user_id),
tag_id UUID NOT NULL REFERENCES tag (tag_id),
PRIMARY KEY (user_id, tag_id),
created_time TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL
);

create table if not exists listing_notification_status (
listing_id UUID NOT NULL REFERENCES listing (listing_id),
notification_sent BOOLEAN NOT NULL DEFAULT FALSE,
created_time TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
PRIMARY KEY (listing_id)
);

create table if not exists user_notification_status (
user_id UUID NOT NULL REFERENCES crs_user (user_id),
notification_sent BOOLEAN NOT NULL DEFAULT FALSE,
created_time TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
PRIMARY KEY (user_id)
);

create table if not exists broker_org_details (
broker_org_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
broker_org_name TEXT NOT NULL,
broker_org_address TEXT,
broker_org_mobile TEXT UNIQUE NOT NULL,
broker_org_email TEXT,
created_time TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL
);

create table if not exists broker_org_tag_mapping (
broker_org_id UUID NOT NULL REFERENCES broker_org_details (broker_org_id),
tag_id UUID NOT NULL REFERENCES tag (tag_id),
PRIMARY KEY (broker_org_id, tag_id),
created_time TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL
);

ALTER TYPE schedule_status ADD VALUE 'PROSPECTIVE' AFTER 'SYSTEM_DEACTIVATED';

----------------13 May----------------------------------------
ALTER TABLE listing ADD COLUMN listing_possession_by TEXT;

create table if not exists broker_org_user_mapping (
broker_org_id UUID NOT NULL REFERENCES broker_org_details (broker_org_id),
user_id UUID NOT NULL REFERENCES crs_user (user_id),
PRIMARY KEY (broker_org_id, user_id),
created_time TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL
);

ALTER TABLE listing ADD COLUMN broker_listing_owner_mobile TEXT;
alter table listing_user_schedule_mapping add column last_modified_time TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL;

alter table listing add column listing_virtual_presence boolean default false;

create table if not exists listing_user_razorpay_order (
listing_id UUID NOT NULL REFERENCES listing (listing_id),
user_id UUID NOT NULL REFERENCES crs_user (user_id),
razorpay_order_id text UNIQUE NOT NULL,
PRIMARY KEY (listing_id, user_id),
created_time TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL
);

create table if not exists razorpay_successful_order (
razorpay_order_id text NOT NULL REFERENCES listing_user_razorpay_order(razorpay_order_id),
created_time TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
refund_requested boolean NOT NULL default false,
refund_requested_time TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
PRIMARY KEY (razorpay_order_id)
);

