
insert into apartment (apartment_name, apartment_latitude, apartment_longitude) values ('Alpine Eco', 12.973675, 77.700803);
insert into apartment (apartment_name, apartment_latitude, apartment_longitude) values ('Rohan Vasanta', 12.958563, 77.706851);
insert into apartment (apartment_name, apartment_latitude, apartment_longitude) values ('Knights Bridge', 12.967944, 77.716309);
insert into apartment (apartment_name, apartment_latitude, apartment_longitude) values ('Sri Padmavati Apt', 12.954198, 77.648895);
insert into apartment (apartment_name, apartment_latitude, apartment_longitude) values ('Shobha Indraprasth', 12.992059, 77.555999);

----------------------------------------------------------
insert into apartment_amenity (apartment_id,
power_backup, lift, security, play_area, club_house, stp, wtp, visitor_parking, swimming_pool, gym, lawn_tennis, snooker, table_tennis, basket_ball)
values (
(select apartment_id from apartment where apartment_name = 'Alpine Eco'),
true, true, true, true, true, true, true, false, true, true, true, true, true, true
);

insert into apartment_amenity (apartment_id,
power_backup, lift, security, play_area, club_house, stp, wtp, visitor_parking, swimming_pool, gym, lawn_tennis, snooker, table_tennis, basket_ball)
values (
(select apartment_id from apartment where apartment_name = 'Rohan Vasanta'),
true, true, true, true, true, true, true, true, true, true, true, true, true, true
);

insert into apartment_amenity (apartment_id,
power_backup, lift, security, play_area, club_house, stp, wtp, visitor_parking, swimming_pool, gym, lawn_tennis, snooker, table_tennis, basket_ball)
values (
(select apartment_id from apartment where apartment_name = 'Knights Bridge'),
true, true, true, true, true, false, false, true, true, true, true, false, true, true
);

insert into apartment_amenity (apartment_id,
power_backup, lift, security, play_area, club_house, stp, wtp, visitor_parking, swimming_pool, gym, lawn_tennis, snooker, table_tennis, basket_ball)
values (
(select apartment_id from apartment where apartment_name = 'Sri Padmavati Apt'),
false, true, true, false, false, false, false, false, false, false, false, false, false, false
);

insert into apartment_amenity (apartment_id,
power_backup, lift, security, play_area, club_house, stp, wtp, visitor_parking, swimming_pool, gym, lawn_tennis, snooker, table_tennis, basket_ball)
values (
(select apartment_id from apartment where apartment_name = 'Shobha Indraprasth'),
true, true, true, true, true, true, true, true, true, true, true, true, true, true
);

-------------------------------------------------------
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
--insert into listing_food_preference (food_preference_name)
--values
--('Veg'),
--('Non-Veg'),
--('No Preference');

---------------------------------------------------------
--insert into listing_tenant_preference (tenant_preference_name)
--values
--('Family'),
--('Bachelor'),
--('No Preference');

---------------------------------------------------------
insert into listing_bedroom_count (bedroom_count_name)
values
('1 BHK'),
('2 BHK'),
('3 BHK'),
('4+ BHK');
---------------------------------------------------------
insert into crs_user (user_name, user_mobile, user_email, user_govt_id_no, user_external_ind, user_pref_comm_mode, user_pref_comm_freq)
values
('Yogesh Jain', '8197289699', 'yogeshj.jain@gmail.com', 'AGHJK6578L', 'Google', ARRAY ['WhatsApp', 'Sms', 'Phone'], 'Daily'),
('Rajul Jain', '9740244634', 'abc@gmail.com', 'AGHJK6578L', null, ARRAY ['Phone'], 'Monthly'),
('Mayur Patki', '0123456789', null, null, 'Facebook', null, null);

----------------------------------------
with inserted as (
insert into listing (user_id,
listing_title, listing_description,
listing_type_id,
property_type_id, listing_image_location, listing_latitude, listing_longitude, listing_area, listing_value, listing_deposit,
apartment_id,
furnishing_type_id,
bedroom_count_id,
transaction_mode_id,
non_veg_allowed,
bachelor_allowed,
listing_active_ind)
values
(
(select user_id from crs_user where user_name = 'Yogesh Jain')
, '2 BHK Apartment', 'Nice Apartment',
(select listing_type_id from listing_type where listing_type_name = 'Rental'),
(select property_type_id from listing_property_type where property_type_name ='Apartment'),
'imageLoc', 12.973675, 77.700803, 1150, 23000, 200000,
(select apartment_id from apartment where apartment_name = 'Alpine Eco'),
(select furnishing_type_id from listing_furnishing_type where furnishing_type_name = 'Fully Furnished'),
(select bedroom_count_id from listing_bedroom_count where bedroom_count_name = '2 BHK'),
(select transaction_mode_id from listing_transaction_mode where transaction_mode_name = 'Online Transfers'),
false,
true,
true) returning listing_id )
insert into listing_amenity (listing_id, fans, geyser, cupboards, chimney, parking, air_conditioner, sofa, dining_table, center_table, television, internet, dth, refrigerator, washing_machine, bed)
select inserted.listing_id ,true, true, true, true, true, true, false, false, false, false, true, false, false, false, false from inserted;

with inserted as (
insert into listing (user_id,
listing_title, listing_description,
listing_type_id,
property_type_id, listing_image_location, listing_latitude, listing_longitude, listing_area, listing_value, listing_deposit,
apartment_id,
furnishing_type_id,
bedroom_count_id,
transaction_mode_id,
non_veg_allowed,
bachelor_allowed,
listing_active_ind)
values
(
(select user_id from crs_user where user_name = 'Rajul Jain')
, '1 BHK Apartment', 'Awesome Apartment',
(select listing_type_id from listing_type where listing_type_name = 'Rental'),
(select property_type_id from listing_property_type where property_type_name ='Villa'),
'imageLoc', 12.973675, 77.709803, 1150, 23000, 200000,
(select apartment_id from apartment where apartment_name = ''),
(select furnishing_type_id from listing_furnishing_type where furnishing_type_name = 'Semi Furnished'),
(select bedroom_count_id from listing_bedroom_count where bedroom_count_name = '1 BHK'),
(select transaction_mode_id from listing_transaction_mode where transaction_mode_name = 'Cash'),
true,
true,
true) returning listing_id)
insert into listing_amenity (listing_id, fans, geyser, cupboards, chimney, parking, air_conditioner, sofa, dining_table, center_table, television, internet, dth, refrigerator, washing_machine, bed)
select inserted.listing_id ,true, true, true, true, true, true, false, true, false, false, true, false, false, false, false from inserted;


with inserted as (
insert into listing (user_id,
listing_title, listing_description,
listing_type_id,
property_type_id, listing_image_location, listing_latitude, listing_longitude, listing_area, listing_value, listing_deposit,
apartment_id,
furnishing_type_id,
bedroom_count_id,
transaction_mode_id,
non_veg_allowed,
bachelor_allowed,
listing_active_ind)
values
(
(select user_id from crs_user where user_name = 'Mayur Patki')
, '5 BHK Apartment', 'Big Apartment',
(select listing_type_id from listing_type where listing_type_name = 'Rental'),
(select property_type_id from listing_property_type where property_type_name ='Builder Floor'),
'imageLoc', 12.992059, 77.555999, 3000, 10000, 800000,
(select apartment_id from apartment where apartment_name = 'Shobha Indraprasth'),
(select furnishing_type_id from listing_furnishing_type where furnishing_type_name = 'Non Furnished'),
(select bedroom_count_id from listing_bedroom_count where bedroom_count_name = '4+ BHK'),
(select transaction_mode_id from listing_transaction_mode where transaction_mode_name = 'Cheque'),
false,
false,
true) returning listing_id)
insert into listing_amenity (listing_id, fans, geyser, cupboards, chimney, parking, air_conditioner, sofa, dining_table, center_table, television, internet, dth, refrigerator, washing_machine, bed)
select inserted.listing_id,true, false, true, true, true, true, false, false, false, false, true, true, false, true, true from inserted;



with inserted as (
insert into listing (user_id,
listing_title, listing_description,
listing_type_id,
property_type_id, listing_image_location, listing_latitude, listing_longitude, listing_area, listing_value, listing_deposit,
apartment_id,
furnishing_type_id,
bedroom_count_id,
transaction_mode_id,
non_veg_allowed,
bachelor_allowed,
listing_active_ind)
values
(
(select user_id from crs_user where user_name = 'Yogesh Jain')
, '4 BHK Apartment', 'Pretty Apartment',
(select listing_type_id from listing_type where listing_type_name = 'Rental'),
(select property_type_id from listing_property_type where property_type_name ='Apartment'),
'imageLoc', 12.958563, 77.706851, 2500, 50000, 350000,
(select apartment_id from apartment where apartment_name = 'Rohan Vasanta'),
(select furnishing_type_id from listing_furnishing_type where furnishing_type_name = 'Non Furnished'),
(select bedroom_count_id from listing_bedroom_count where bedroom_count_name = '4+ BHK'),
(select transaction_mode_id from listing_transaction_mode where transaction_mode_name = 'Cheque'),
true,
true,
true) returning listing_id)
insert into listing_amenity (listing_id, fans, geyser, cupboards, chimney, parking, air_conditioner, sofa, dining_table, center_table, television, internet, dth, refrigerator, washing_machine, bed)
select inserted.listing_id,true, true, false, true, true, true, false, true, false, true, true, false, false, false, true from inserted;


with inserted as (
insert into listing (user_id,
listing_title, listing_description,
listing_type_id,
property_type_id, listing_image_location, listing_latitude, listing_longitude, listing_area, listing_value, listing_deposit,
apartment_id,
furnishing_type_id,
bedroom_count_id,
transaction_mode_id,
non_veg_allowed,
bachelor_allowed,
listing_active_ind)
values
(
(select user_id from crs_user where user_name = 'Yogesh Jain')
, '1 BHK Apartment', 'Small Apartment',
(select listing_type_id from listing_type where listing_type_name = 'Rental'),
(select property_type_id from listing_property_type where property_type_name ='Apartment'),
'imageLoc', 12.954198, 77.648895, 600, 8000, 50000,
(select apartment_id from apartment where apartment_name = 'Sri Padmavati Apt'),
(select furnishing_type_id from listing_furnishing_type where furnishing_type_name = 'Non Furnished'),
(select bedroom_count_id from listing_bedroom_count where bedroom_count_name = '1 BHK'),
(select transaction_mode_id from listing_transaction_mode where transaction_mode_name = 'Cheque'),
true,
false,
true) returning listing_id)
insert into listing_amenity (listing_id, fans, geyser, cupboards, chimney, parking, air_conditioner, sofa, dining_table, center_table, television, internet, dth, refrigerator, washing_machine, bed)
select inserted.listing_id,true, true, true, true, true, false, false, true, false, false, true, false, false, true, false from inserted;


with inserted as (
insert into listing (user_id,
listing_title, listing_description,
listing_type_id,
property_type_id, listing_image_location, listing_latitude, listing_longitude, listing_area, listing_value, listing_deposit,
apartment_id,
furnishing_type_id,
bedroom_count_id,
transaction_mode_id,
non_veg_allowed,
bachelor_allowed,
listing_active_ind)
values
(
(select user_id from crs_user where user_name = 'Yogesh Jain')
, '6 BHK Apartment', 'Huge Apartment',
(select listing_type_id from listing_type where listing_type_name = 'Rental'),
(select property_type_id from listing_property_type where property_type_name ='Apartment'),
'imageLoc', 12.967944, 77.716309, 4000, 80000, 550000,
(select apartment_id from apartment where apartment_name = 'Knights Bridge'),
(select furnishing_type_id from listing_furnishing_type where furnishing_type_name = 'Semi Furnished'),
(select bedroom_count_id from listing_bedroom_count where bedroom_count_name = '4+ BHK'),
(select transaction_mode_id from listing_transaction_mode where transaction_mode_name = 'Cash'),
false,
true,
true) returning listing_id)
insert into listing_amenity (listing_id, fans, geyser, cupboards, chimney, parking, air_conditioner, sofa, dining_table, center_table, television, internet, dth, refrigerator, washing_machine, bed)
select inserted.listing_id,true, true, true, false, true, true, false, false, false, false, true, true, false, false, true from inserted;


--------------------------------------------------------------------
--insert into listing_amenity (listing_id, fans, geyser, cupboards, chimney, parking, air_conditioner, sofa, dining_table, center_table, television, internet, dth, refrigerator, washing_machine, bed)
--values
--(18,true, true, true, true, true, true, false, false, false, false, true, false, false, false, false),
--(19,true, true, true, true, true, true, false, true, false, false, true, false, false, false, false),
--(20,true, false, true, true, true, true, false, false, false, false, true, true, false, true, false),
--(21,true, true, false, true, true, true, false, true, false, true, true, false, false, false, true),
--(22,true, true, true, true, true, false, false, true, false, false, true, false, false, true, false),
--(23,true, true, true, false, true, true, false, false, false, false, true, true, false, false, true);

-------------------------------------------------------
--insert into scheduled_visits
--values
--(5,1,18, 'Saturday - 11', false, null, null, null),
--(2,1,19, 'Wednesday - 3', false, null, null, null),
--(1,2,20, 'Thursday - 8', true, 'Like Tenant', 'Not like house', 'Getting cheaper at other places');


-- for now set one image to display it on ui, change this to folder name later and add logic to obtain files from folder
UPDATE crs_schema.listing set listing_image_location = '/Users/YJ/CRS/crs-rental-ui-jainyogesh-new/src/assets/images';


-- INSERT INTO OAUTH_CLIENT_DETAILS(CLIENT_ID, RESOURCE_IDS, CLIENT_SECRET, SCOPE, AUTHORIZED_GRANT_TYPES, AUTHORITIES, ACCESS_TOKEN_VALIDITY, REFRESH_TOKEN_VALIDITY)
-- VALUES ('spring-security-oauth2-read-write-client', 'resource-server-rest-api',
 /*spring-security-oauth2-read-write-client-password1234*/'$2a$04$soeOR.QFmClXeFIrhJVLWOQxfHjsJLSpWrU1iGxcMGdu.a5hvfY4W',
 -- 'read,write', 'password,authorization_code,refresh_token,implicit', 'USER', 10800, 2592000);

-- password is hashed with BCrypt (4 rounds).

INSERT INTO authority(
	id, name)
	VALUES (1, 'CRUD');

	INSERT INTO authority(
	id, name)
	VALUES (2, 'R');

INSERT INTO OAUTH_CLIENT_DETAILS(CLIENT_ID, RESOURCE_IDS, CLIENT_SECRET, SCOPE, AUTHORIZED_GRANT_TYPES, AUTHORITIES, ACCESS_TOKEN_VALIDITY, REFRESH_TOKEN_VALIDITY)
 VALUES ('rw-client', 'resource-server-rest-api',
 /*'crs-rw-ro7&*/'$2a$04$qsKQe7YF7Lbp0P2G1B71lurZYA3Hu23gdnnU5G1eMd5etfCD512Ha',
 'read,write', 'password,refresh_token', 'ROLE_ADMIN', 60, 86400);

