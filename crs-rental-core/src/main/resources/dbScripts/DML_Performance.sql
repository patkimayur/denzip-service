set schema 'crs_schema';

insert into listing_type (listing_type_name)
values
('Rental'),
('Lease'),
('Sale');

----------------------------------------------------------
insert into listing_property_type (property_type_name)
values
('Villa'),
('Apartment'),
('Row House'),
('Builder Floor'),
('Plot');

------------------------------------------------
insert into listing_furnishing_type (furnishing_type_name)
values
('Fully Furnished'),
('Semi Furnished'),
('Non Furnished');

-----------------------------------------------------
insert into listing_transaction_mode (transaction_mode_name)
values
('Cash'),
('Cheque'),
('Online Transfers'),
('No Preference');

--------------------------------------------------------
insert into listing_bedroom_count (bedroom_count_name)
values
('1 BHK'),
('2 BHK'),
('3 BHK'),
('4+ BHK');

----Below is related to Auth and Security and needs to be optimized------

INSERT INTO authority(
	id, name)
	VALUES (1, 'ROLE_USER');

	INSERT INTO authority(
	id, name)
	VALUES (2, 'ROLE_ADMIN');

INSERT INTO OAUTH_CLIENT_DETAILS(CLIENT_ID, RESOURCE_IDS, CLIENT_SECRET, SCOPE, AUTHORIZED_GRANT_TYPES, AUTHORITIES, ACCESS_TOKEN_VALIDITY, REFRESH_TOKEN_VALIDITY)
 VALUES ('rw-client', 'resource-server-rest-api',
 /*'crs-rw-ro7&*/'$2a$04$qsKQe7YF7Lbp0P2G1B71lurZYA3Hu23gdnnU5G1eMd5etfCD512Ha',
 'read,write', 'password,refresh_token', 'ROLE_ADMIN', 600, 2592000);

--UPDATE crs_schema.authority
--	SET name='ROLE_USER'
--	WHERE id='1';

--	UPDATE crs_schema.authority
--	SET name='ROLE_ADMIN'
--	WHERE id='2';
--
--	ALTER TYPE crs_schema.schedule_status
--    ADD VALUE 'ACTIVATED' AFTER 'REJECTED';
--
--ALTER TYPE crs_schema.schedule_status
--    ADD VALUE 'DEACTIVATED' AFTER 'ACTIVATED';

--    UPDATE crs_schema.oauth_client_details
--	SET refresh_token_validity=2592000;

INSERT INTO authority(
	id, name)
	VALUES (3, 'ROLE_BROKER');


-----------10 May 2019--------
update listing_type set listing_type_name = 'Rent' where listing_type_name = 'Rental';
delete from listing_type where listing_type_name = 'Lease';

----------------------20 May-------------------------------------------
update listing_property_type set property_type_name = 'Flat' where property_type_name = 'Builder Floor';
update listing_property_type set property_type_name = 'Independent House' where property_type_name = 'Row House';

------------25 May---------------
insert into listing_bedroom_count (bedroom_count_name)
values ('1 RK');

commit;
	

