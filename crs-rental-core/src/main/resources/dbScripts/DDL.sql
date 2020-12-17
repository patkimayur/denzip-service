#to login using root to psql shell
psql postgres

#create the user for prod db
CREATE ROLE crs_user WITH LOGIN PASSWORD 'crs_user';

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

#Set Schema
set schema 'crs_schema';

create type external_ind AS ENUM ('Google', 'Facebook');
create type schedule_status AS ENUM ('SHORTLISTED','SCHEDULING_REQUESTED', 'SCHEDULED', 'VISITED', 'ACCEPTED', 'REJECTED');


create table if not exists apartment (
apartment_id BIGSERIAL PRIMARY KEY,
apartment_name TEXT NOT NULL,
apartment_latitude NUMERIC NOT NULL,
apartment_longitude NUMERIC NOT Null
);

create table if not exists apartment_amenity (
apartment_id BIGINT NOT NULL REFERENCES apartment(apartment_id) PRIMARY KEY,
power_backup BOOLEAN NOT NULL DEFAULT FALSE,
lift BOOLEAN NOT NULL DEFAULT FALSE,
security BOOLEAN NOT NULL DEFAULT FALSE,
play_area BOOLEAN NOT NULL DEFAULT FALSE,
club_house BOOLEAN NOT NULL DEFAULT FALSE,
stp BOOLEAN NOT NULL DEFAULT FALSE,
wtp BOOLEAN NOT NULL DEFAULT FALSE,
visitor_parking BOOLEAN NOT NULL DEFAULT FALSE,
swimming_pool BOOLEAN NOT NULL DEFAULT FALSE,
gym BOOLEAN NOT NULL DEFAULT FALSE,
lawn_tennis BOOLEAN NOT NULL DEFAULT FALSE,
snooker BOOLEAN NOT NULL DEFAULT FALSE,
table_tennis BOOLEAN NOT NULL DEFAULT FALSE,
basket_ball BOOLEAN NOT NULL DEFAULT FALSE
);

create table if not exists listing_type (
listing_type_id SERIAL PRIMARY KEY,
listing_type_name TEXT NOT NULL --Rental, Lease, Sale
);

create table if not exists listing_property_type (
property_type_id SERIAL PRIMARY KEY,
property_type_name TEXT NOT NULL --Individual House, Apartment, Row House, Builder Floor
);

create table if not exists listing_furnishing_type (
furnishing_type_id SERIAL PRIMARY KEY,
furnishing_type_name TEXT NOT NULL --Fully Furnished, Semi Furnished, Not Furnished
);

create table if not exists listing_transaction_mode (
transaction_mode_id SERIAL PRIMARY KEY,
transaction_mode_name TEXT NOT NULL --Cash, Cheque, Online Transfers, No Preference
);

--create table if not exists listing_food_preference (
--food_preference_id SERIAL PRIMARY KEY,
--food_preference_name TEXT NOT NULL --Veg, Non-Veg, No Preference
--);
--
--create table if not exists listing_tenant_preference (
--tenant_preference_id SERIAL PRIMARY KEY,
--tenant_preference_name TEXT NOT NULL --Family, Bachelor, No Preference
--);

create table if not exists listing_bedroom_count (
bedroom_count_id SERIAL PRIMARY KEY,
bedroom_count_name TEXT NOT NULL --1 BHK, 2 BHK, 3 BHK, 4+ BHK
);

create table if not exists crs_user (
user_id BIGSERIAL PRIMARY KEY,
user_name TEXT NOT NULL,
user_mobile TEXT NOT NULL,
user_email TEXT,
user_acceptance_doc BYTEA,
user_govt_id_no TEXT,
user_govt_id_image BYTEA,
user_external_ind TEXT, --Internal defined Type, Google & Facebook
user_pref_comm_mode TEXT [], --Phone, SMS, WhatsApp
user_pref_comm_freq TEXT, --Daily, Weekly, Monthly
user_pref_visit_slot json --Saturday 10-12, Sunday 1-3 etc...
--, user_seen_listing_id BIGINT [] ELEMENT REFERENCES listing(listing_id),
--user_shortlisted_listing_id BIGINT [] ELEMENT REFERENCES listing(listing_id),
--user_schedule_request_listing_id BIGINT [] ELEMENT REFERENCES listing(listing_id)
);

create table if not exists listing (
listing_id BIGSERIAL PRIMARY KEY,
user_id BIGINT NOT NULL REFERENCES crs_user (user_id),
listing_title TEXT NOT NULL,
listing_description TEXT NOT NULL, --This can be changed to JSON once we lock down POJO
listing_type_id INTEGER NOT NULL REFERENCES listing_type (listing_type_id),
property_type_id INTEGER NOT NULL REFERENCES listing_property_type (property_type_id),
listing_image_location TEXT NOT NULL, --Postgre provides image loc, to explore
listing_latitude NUMERIC NOT NULL,
listing_longitude NUMERIC NOT NULL,
listing_area INTEGER NOT NULL,
listing_value NUMERIC(12,2) NOT NULL,
listing_deposit NUMERIC(12,2), --Can be null for sale properties
apartment_id BIGINT REFERENCES apartment (apartment_id), --Can be null for non-apartment listings
furnishing_type_id INTEGER NOT NULL REFERENCES listing_furnishing_type (furnishing_type_id),
bedroom_count_id INTEGER NOT NULL REFERENCES listing_bedroom_count(bedroom_count_id),
transaction_mode_id INTEGER NOT NULL REFERENCES listing_transaction_mode (transaction_mode_id),
non_veg_allowed BOOLEAN NOT NULL DEFAULT TRUE,
bachelor_allowed BOOLEAN NOT NULL DEFAULT TRUE,
listing_active_ind BOOLEAN NOT NULL DEFAULT TRUE
);

create table if not exists listing_amenity (
listing_id BIGINT NOT NULL REFERENCES listing(listing_id) PRIMARY KEY,
fans BOOLEAN NOT NULL DEFAULT FALSE,
geyser BOOLEAN NOT NULL DEFAULT FALSE,
cupboards BOOLEAN NOT NULL DEFAULT FALSE,
chimney BOOLEAN NOT NULL DEFAULT FALSE,
parking BOOLEAN NOT NULL DEFAULT FALSE,
air_conditioner BOOLEAN NOT NULL DEFAULT FALSE,
sofa BOOLEAN NOT NULL DEFAULT FALSE,
dining_table BOOLEAN NOT NULL DEFAULT FALSE,
center_table BOOLEAN NOT NULL DEFAULT FALSE,
television BOOLEAN NOT NULL DEFAULT FALSE,
internet BOOLEAN NOT NULL DEFAULT FALSE,
dth BOOLEAN NOT NULL DEFAULT FALSE,
refrigerator BOOLEAN NOT NULL DEFAULT FALSE,
washing_machine BOOLEAN NOT NULL DEFAULT FALSE,
bed BOOLEAN NOT NULL DEFAULT FALSE
);

create table if not exists scheduled_visits (
owner_user_id BIGINT NOT NULL REFERENCES crs_user (user_id),
tenant_user_id BIGINT NOT NULL REFERENCES crs_user (user_id),
listing_id BIGINT NOT NULL REFERENCES listing (listing_id),
scheduled_slot TEXT NOT NULL,
visited BOOLEAN NOT NULL DEFAULT FALSE,
owner_feedback TEXT,
tenant_feedback TEXT,
probable_outcome TEXT,
PRIMARY KEY (owner_user_id, tenant_user_id)
);

--create table if not exists listing_user_shortlist_mapping (
--listing_id BIGINT NOT NULL REFERENCES listing (listing_id),
--user_id BIGINT NOT NULL REFERENCES crs_user (user_id),
--PRIMARY KEY (listing_id, user_id)
--);

create table if not exists listing_user_schedule_mapping (
listing_id BIGINT NOT NULL REFERENCES listing (listing_id),
user_id BIGINT NOT NULL REFERENCES crs_user (user_id),
schedule_status schedule_status NOT NULL,
PRIMARY KEY (listing_id, user_id)
);

create table if not exists listing_locality_data (
listing_id BIGINT NOT NULL REFERENCES listing (listing_id),
locality_data TEXT NOT NULL, --this will be used to storeListingImages the enitre data as json
PRIMARY KEY (listing_id)
);

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
  AUTOAPPROVE VARCHAR(255)
);

CREATE TABLE OAUTH_CLIENT_TOKEN (
  TOKEN_ID VARCHAR(255),
  TOKEN BYTEA,
  AUTHENTICATION_ID VARCHAR(255) PRIMARY KEY,
  USER_NAME VARCHAR(255),
  CLIENT_ID VARCHAR(255)
);

CREATE TABLE OAUTH_ACCESS_TOKEN (
  TOKEN_ID VARCHAR(255),
  TOKEN BYTEA,
  AUTHENTICATION_ID VARCHAR(255) PRIMARY KEY,
  USER_NAME VARCHAR(255),
  CLIENT_ID VARCHAR(255),
  AUTHENTICATION BYTEA,
  REFRESH_TOKEN VARCHAR(255)
);

CREATE TABLE OAUTH_REFRESH_TOKEN (
  TOKEN_ID VARCHAR(255),
  TOKEN BYTEA,
  AUTHENTICATION BYTEA
);

CREATE TABLE OAUTH_CODE (
  CODE VARCHAR(255),
  AUTHENTICATION BYTEA
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
   PRIMARY KEY (ID)
);

ALTER TABLE IF EXISTS AUTHORITY ADD CONSTRAINT AUTHORITY_NAME UNIQUE (NAME);

ALTER TABLE crs_user
    ADD COLUMN authorities integer[] NOT NULL DEFAULT array[1]::integer[];



#quit
\q