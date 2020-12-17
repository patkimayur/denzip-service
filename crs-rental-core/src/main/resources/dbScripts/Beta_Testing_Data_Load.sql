--Mahadevpura, Bangalore
--Doddanekundi, Bangalore
--Karthik Nagar, Bangalore
--Whitefield, Bangalore
--Brookfield, Bangalore
--ITPL, Bangalore
--Kundalhalli, Bangalore
--Marathahalli, Bangalore
--Kadubeesnahalli, Bangalore
--Panthur, Bangalore
--Bellandur, Bangalore
--Sarjapur, Bangalore
--
--1 BHK - Apartment - Semi Furnished - Cheque
--2 BHK - Apartment - Fully Furnished - Online Transfers
--3 BHK - Row House - Non Furnished - Cash
--4 BHK - Villa - Semi Furnished - No Preference


set schema 'crs_schema';

with inserted as (
insert into apartment (apartment_name, apartment_latitude, apartment_longitude) values ('Durga Rainbow', 12.988732,77.6876913)
returning apartment_id)
insert into apartment_amenity (apartment_id,
powerbackup, lift, security, playarea, clubhouse, stp, wtp, visitorparking, swimmingpool, gym, lawntennis, snooker, tabletennis, basketball)
select inserted.apartment_id, true, true, true, true, true, true, true, false, true, true, true, true, true, true from inserted
; --mahadevpura

with inserted as (
insert into apartment (apartment_name, apartment_latitude, apartment_longitude) values ('Alpine Eco', 12.973675, 77.700803) returning apartment_id)
insert into apartment_amenity (apartment_id,
powerbackup, lift, security, playarea, clubhouse, stp, wtp, visitorparking, swimmingpool, gym, lawntennis, snooker, tabletennis, basketball)
select inserted.apartment_id, true, true, true, true, true, true, true, false, true, true, true, true, true, true from inserted
; --doddanekundi

with inserted as (
insert into apartment (apartment_name, apartment_latitude, apartment_longitude) values ('Bren Unity Apartments', 12.9711705,77.7058543) returning apartment_id)
insert into apartment_amenity (apartment_id,
powerbackup, lift, security, playarea, clubhouse, stp, wtp, visitorparking, swimmingpool, gym, lawntennis, snooker, tabletennis, basketball)
select inserted.apartment_id, true, true, true, true, true, true, true, false, true, true, true, true, true, true from inserted
; --karthik nagar

with inserted as (
insert into apartment (apartment_name, apartment_latitude, apartment_longitude) values ('SVS Silver Woods', 12.9570667,77.7384723) returning apartment_id)
insert into apartment_amenity (apartment_id,
powerbackup, lift, security, playarea, clubhouse, stp, wtp, visitorparking, swimmingpool, gym, lawntennis, snooker, tabletennis, basketball)
select inserted.apartment_id, true, true, true, true, true, true, true, false, true, true, true, true, true, true from inserted
; --whitefield

with inserted as (
insert into apartment (apartment_name, apartment_latitude, apartment_longitude) values ('Knights Bridge', 12.967944, 77.716309) returning apartment_id)
insert into apartment_amenity (apartment_id,
powerbackup, lift, security, playarea, clubhouse, stp, wtp, visitorparking, swimmingpool, gym, lawntennis, snooker, tabletennis, basketball)
select inserted.apartment_id, true, true, true, true, true, true, true, false, true, true, true, true, true, true from inserted
; --brookfield

with inserted as (
insert into apartment (apartment_name, apartment_latitude, apartment_longitude) values ('Prestige Shantiniketan', 12.9897602,77.7309759) returning apartment_id)
insert into apartment_amenity (apartment_id,
powerbackup, lift, security, playarea, clubhouse, stp, wtp, visitorparking, swimmingpool, gym, lawntennis, snooker, tabletennis, basketball)
select inserted.apartment_id, true, true, true, true, true, true, true, false, true, true, true, true, true, true from inserted
; --ITPL

with inserted as (
insert into apartment (apartment_name, apartment_latitude, apartment_longitude) values ('Rohan Vasanta', 12.958563, 77.706851) returning apartment_id)
insert into apartment_amenity (apartment_id,
powerbackup, lift, security, playarea, clubhouse, stp, wtp, visitorparking, swimmingpool, gym, lawntennis, snooker, tabletennis, basketball)
select inserted.apartment_id, true, true, true, true, true, true, true, false, true, true, true, true, true, true from inserted
; --kundalhalli

with inserted as (
insert into apartment (apartment_name, apartment_latitude, apartment_longitude) values ('Ittina Akaya', 12.9534626,77.6977583) returning apartment_id)
insert into apartment_amenity (apartment_id,
powerbackup, lift, security, playarea, clubhouse, stp, wtp, visitorparking, swimmingpool, gym, lawntennis, snooker, tabletennis, basketball)
select inserted.apartment_id, true, true, true, true, true, true, true, false, true, true, true, true, true, true from inserted
; --marathalli


with inserted as (
insert into apartment (apartment_name, apartment_latitude, apartment_longitude) values ('Butterfly Apartment', 12.9384397,77.6970864) returning apartment_id)
insert into apartment_amenity (apartment_id,
powerbackup, lift, security, playarea, clubhouse, stp, wtp, visitorparking, swimmingpool, gym, lawntennis, snooker, tabletennis, basketball)
select inserted.apartment_id, true, true, true, true, true, true, true, false, true, true, true, true, true, true from inserted
; --kadubessnahalli

with inserted as (
insert into apartment (apartment_name, apartment_latitude, apartment_longitude) values ('Vandana Apartment', 12.9364609,77.7021243) returning apartment_id)
insert into apartment_amenity (apartment_id,
powerbackup, lift, security, playarea, clubhouse, stp, wtp, visitorparking, swimmingpool, gym, lawntennis, snooker, tabletennis, basketball)
select inserted.apartment_id, true, true, true, true, true, true, true, false, true, true, true, true, true, true from inserted
; --Panthur

with inserted as (
insert into apartment (apartment_name, apartment_latitude, apartment_longitude) values ('SLS Splendor', 12.9239338,77.6780734) returning apartment_id)
insert into apartment_amenity (apartment_id,
powerbackup, lift, security, playarea, clubhouse, stp, wtp, visitorparking, swimmingpool, gym, lawntennis, snooker, tabletennis, basketball)
select inserted.apartment_id, true, true, true, true, true, true, true, false, true, true, true, true, true, true from inserted
; --Bellandur

with inserted as (
insert into apartment (apartment_name, apartment_latitude, apartment_longitude) values ('Salarpuria Sattva Senorita', 12.9058677,77.6834147) returning apartment_id)
insert into apartment_amenity (apartment_id,
powerbackup, lift, security, playarea, clubhouse, stp, wtp, visitorparking, swimmingpool, gym, lawntennis, snooker, tabletennis, basketball)
select inserted.apartment_id, true, true, true, true, true, true, true, false, true, true, true, true, true, true from inserted
; --Sarjapur Road

-------------------------------1 BHK------------------------------------------------------------------------------------

with inserted as (
insert into listing (user_id,
listing_title, listing_address,
listing_type_id,
property_type_id, listing_latitude, listing_longitude, listing_area, listing_value, listing_deposit,
apartment_id,
furnishing_type_id,
bedroom_count_id,
transaction_mode_id,
non_veg_allowed,
bachelor_allowed,
listing_active_ind)
values
(
(select user_id from crs_user where user_name = 'Load Test Home Owner')
, '1 BHK Apartment In Durga Rainbow, Mahadevpura',
'Second Floor, Durga Rainbow, Outer Ring Road, Near Hyundai Showroom,
2 Balconies, West Facing Main Door,
Vetrified Tiles Flooring, Borewell Water,
Maintainence Rs 1000 Per Month Excluding Rent,
Move In Charges Rs 500',
(select listing_type_id from listing_type where listing_type_name = 'Rental'),
(select property_type_id from listing_property_type where property_type_name ='Apartment'),
12.988732, 77.6876913, 650, 15000, 150000,
(select apartment_id from apartment where apartment_name = 'Durga Rainbow'),
(select furnishing_type_id from listing_furnishing_type where furnishing_type_name = 'Semi Furnished'),
(select bedroom_count_id from listing_bedroom_count where bedroom_count_name = '1 BHK'),
(select transaction_mode_id from listing_transaction_mode where transaction_mode_name = 'Cheque'),
false,
true,
true) returning listing_id )
insert into listing_amenity (listing_id, fans, geyser, cupboards, chimney, parking, airconditioner, sofa, diningtable, centertable, television, internet, dth, refrigerator, washingmachine, bed)
select inserted.listing_id ,true, true, true, false, true, false, false, false, false, false, false, false, false, false, false from inserted;


with inserted as (
insert into listing (user_id,
listing_title, listing_address,
listing_type_id,
property_type_id, listing_latitude, listing_longitude, listing_area, listing_value, listing_deposit,
apartment_id,
furnishing_type_id,
bedroom_count_id,
transaction_mode_id,
non_veg_allowed,
bachelor_allowed,
listing_active_ind)
values
(
(select user_id from crs_user where user_name = 'Load Test Home Owner')
, '1 BHK Apartment In Alpine Eco, Doddanekundi',
'Second Floor, Alpine Eco, Outer Ring Road, Near Rainbow Hospital,
2 Balconies, West Facing Main Door,
Vetrified Tiles Flooring, Borewell Water,
Maintainence Rs 1000 Per Month Excluding Rent,
Move In Charges Rs 500',
(select listing_type_id from listing_type where listing_type_name = 'Rental'),
(select property_type_id from listing_property_type where property_type_name ='Apartment'),
12.973675, 77.700803, 650, 15000, 150000,
(select apartment_id from apartment where apartment_name = 'Alpine Eco' limit 1),
(select furnishing_type_id from listing_furnishing_type where furnishing_type_name = 'Semi Furnished'),
(select bedroom_count_id from listing_bedroom_count where bedroom_count_name = '1 BHK'),
(select transaction_mode_id from listing_transaction_mode where transaction_mode_name = 'Cheque'),
false,
true,
true) returning listing_id )
insert into listing_amenity (listing_id, fans, geyser, cupboards, chimney, parking, airconditioner, sofa, diningtable, centertable, television, internet, dth, refrigerator, washingmachine, bed)
select inserted.listing_id ,true, true, true, false, true, false, false, false, false, false, false, false, false, false, false from inserted;


with inserted as (
insert into listing (user_id,
listing_title, listing_address,
listing_type_id,
property_type_id, listing_latitude, listing_longitude, listing_area, listing_value, listing_deposit,
apartment_id,
furnishing_type_id,
bedroom_count_id,
transaction_mode_id,
non_veg_allowed,
bachelor_allowed,
listing_active_ind)
values
(
(select user_id from crs_user where user_name = 'Load Test Home Owner')
, '1 BHK Apartment In Bren Unity Apartments, Karthik Nagar',
'Second Floor, Bren Unity, Karthik Nagar, Outer Ring Road, Near Axis Bank,
2 Balconies, West Facing Main Door,
Vetrified Tiles Flooring, Borewell Water,
Maintainence Rs 1000 Per Month Excluding Rent,
Move In Charges Rs 500',
(select listing_type_id from listing_type where listing_type_name = 'Rental'),
(select property_type_id from listing_property_type where property_type_name ='Apartment'),
12.9711705,77.7058543, 650, 15000, 150000,
(select apartment_id from apartment where apartment_name = 'Bren Unity Apartments'),
(select furnishing_type_id from listing_furnishing_type where furnishing_type_name = 'Semi Furnished'),
(select bedroom_count_id from listing_bedroom_count where bedroom_count_name = '1 BHK'),
(select transaction_mode_id from listing_transaction_mode where transaction_mode_name = 'Cheque'),
false,
true,
true) returning listing_id )
insert into listing_amenity (listing_id, fans, geyser, cupboards, chimney, parking, airconditioner, sofa, diningtable, centertable, television, internet, dth, refrigerator, washingmachine, bed)
select inserted.listing_id ,true, true, true, false, true, false, false, false, false, false, false, false, false, false, false from inserted;


with inserted as (
insert into listing (user_id,
listing_title, listing_address,
listing_type_id,
property_type_id, listing_latitude, listing_longitude, listing_area, listing_value, listing_deposit,
apartment_id,
furnishing_type_id,
bedroom_count_id,
transaction_mode_id,
non_veg_allowed,
bachelor_allowed,
listing_active_ind)
values
(
(select user_id from crs_user where user_name = 'Load Test Home Owner')
, '1 BHK Apartment In SVS Silver Woods, Whitefield',
'Second Floor, SVS Silver Woods, Whitefile, Near More Store,
2 Balconies, West Facing Main Door,
Vetrified Tiles Flooring, Borewell Water,
Maintainence Rs 1000 Per Month Excluding Rent,
Move In Charges Rs 500',
(select listing_type_id from listing_type where listing_type_name = 'Rental'),
(select property_type_id from listing_property_type where property_type_name ='Apartment'),
12.9570667,77.7384723, 650, 15000, 150000,
(select apartment_id from apartment where apartment_name = 'SVS Silver Woods'),
(select furnishing_type_id from listing_furnishing_type where furnishing_type_name = 'Semi Furnished'),
(select bedroom_count_id from listing_bedroom_count where bedroom_count_name = '1 BHK'),
(select transaction_mode_id from listing_transaction_mode where transaction_mode_name = 'Cheque'),
false,
true,
true) returning listing_id )
insert into listing_amenity (listing_id, fans, geyser, cupboards, chimney, parking, airconditioner, sofa, diningtable, centertable, television, internet, dth, refrigerator, washingmachine, bed)
select inserted.listing_id ,true, true, true, false, true, false, false, false, false, false, false, false, false, false, false from inserted;


with inserted as (
insert into listing (user_id,
listing_title, listing_address,
listing_type_id,
property_type_id, listing_latitude, listing_longitude, listing_area, listing_value, listing_deposit,
apartment_id,
furnishing_type_id,
bedroom_count_id,
transaction_mode_id,
non_veg_allowed,
bachelor_allowed,
listing_active_ind)
values
(
(select user_id from crs_user where user_name = 'Load Test Home Owner')
, '1 BHK Apartment In Knights Bridge, Brookfield',
'Second Floor, Knights Bridge, Brookfield, Near McDonalds,
2 Balconies, West Facing Main Door,
Vetrified Tiles Flooring, Borewell Water,
Maintainence Rs 1000 Per Month Excluding Rent,
Move In Charges Rs 500',
(select listing_type_id from listing_type where listing_type_name = 'Rental'),
(select property_type_id from listing_property_type where property_type_name ='Apartment'),
12.967944, 77.716309, 650, 15000, 150000,
(select apartment_id from apartment where apartment_name = 'Knights Bridge' limit 1),
(select furnishing_type_id from listing_furnishing_type where furnishing_type_name = 'Semi Furnished'),
(select bedroom_count_id from listing_bedroom_count where bedroom_count_name = '1 BHK'),
(select transaction_mode_id from listing_transaction_mode where transaction_mode_name = 'Cheque'),
false,
true,
true) returning listing_id )
insert into listing_amenity (listing_id, fans, geyser, cupboards, chimney, parking, airconditioner, sofa, diningtable, centertable, television, internet, dth, refrigerator, washingmachine, bed)
select inserted.listing_id ,true, true, true, false, true, false, false, false, false, false, false, false, false, false, false from inserted;


with inserted as (
insert into listing (user_id,
listing_title, listing_address,
listing_type_id,
property_type_id, listing_latitude, listing_longitude, listing_area, listing_value, listing_deposit,
apartment_id,
furnishing_type_id,
bedroom_count_id,
transaction_mode_id,
non_veg_allowed,
bachelor_allowed,
listing_active_ind)
values
(
(select user_id from crs_user where user_name = 'Load Test Home Owner')
, '1 BHK Apartment In Prestige Shantiniketan, Whitefield',
'Second Floor, Prestige Shantiniketan, Whitefield, In Front of ITPL,
2 Balconies, West Facing Main Door,
Vetrified Tiles Flooring, Borewell Water,
Maintainence Rs 1000 Per Month Excluding Rent,
Move In Charges Rs 500',
(select listing_type_id from listing_type where listing_type_name = 'Rental'),
(select property_type_id from listing_property_type where property_type_name ='Apartment'),
12.9897602,77.7309759, 650, 15000, 150000,
(select apartment_id from apartment where apartment_name = 'Prestige Shantiniketan'),
(select furnishing_type_id from listing_furnishing_type where furnishing_type_name = 'Semi Furnished'),
(select bedroom_count_id from listing_bedroom_count where bedroom_count_name = '1 BHK'),
(select transaction_mode_id from listing_transaction_mode where transaction_mode_name = 'Cheque'),
false,
true,
true) returning listing_id )
insert into listing_amenity (listing_id, fans, geyser, cupboards, chimney, parking, airconditioner, sofa, diningtable, centertable, television, internet, dth, refrigerator, washingmachine, bed)
select inserted.listing_id ,true, true, true, false, true, false, false, false, false, false, false, false, false, false, false from inserted;


with inserted as (
insert into listing (user_id,
listing_title, listing_address,
listing_type_id,
property_type_id, listing_latitude, listing_longitude, listing_area, listing_value, listing_deposit,
apartment_id,
furnishing_type_id,
bedroom_count_id,
transaction_mode_id,
non_veg_allowed,
bachelor_allowed,
listing_active_ind)
values
(
(select user_id from crs_user where user_name = 'Load Test Home Owner')
, '1 BHK Apartment In Rohan Vasanta, Kundalhalli',
'Second Floor, Rohan Vasanta, Kundalhalli, Near Bridge,
2 Balconies, West Facing Main Door,
Vetrified Tiles Flooring, Borewell Water,
Maintainence Rs 1000 Per Month Excluding Rent,
Move In Charges Rs 500',
(select listing_type_id from listing_type where listing_type_name = 'Rental'),
(select property_type_id from listing_property_type where property_type_name ='Apartment'),
12.958563, 77.706851, 650, 15000, 150000,
(select apartment_id from apartment where apartment_name = 'Rohan Vasanta' limit 1),
(select furnishing_type_id from listing_furnishing_type where furnishing_type_name = 'Semi Furnished'),
(select bedroom_count_id from listing_bedroom_count where bedroom_count_name = '1 BHK'),
(select transaction_mode_id from listing_transaction_mode where transaction_mode_name = 'Cheque'),
false,
true,
true) returning listing_id )
insert into listing_amenity (listing_id, fans, geyser, cupboards, chimney, parking, airconditioner, sofa, diningtable, centertable, television, internet, dth, refrigerator, washingmachine, bed)
select inserted.listing_id ,true, true, true, false, true, false, false, false, false, false, false, false, false, false, false from inserted;


with inserted as (
insert into listing (user_id,
listing_title, listing_address,
listing_type_id,
property_type_id, listing_latitude, listing_longitude, listing_area, listing_value, listing_deposit,
apartment_id,
furnishing_type_id,
bedroom_count_id,
transaction_mode_id,
non_veg_allowed,
bachelor_allowed,
listing_active_ind)
values
(
(select user_id from crs_user where user_name = 'Load Test Home Owner')
, '1 BHK Apartment In Ittina Akaya, Marathalli',
'Second Floor, Ittina Akaya, Marathalli, Near HomeTown,
2 Balconies, West Facing Main Door,
Vetrified Tiles Flooring, Borewell Water,
Maintainence Rs 1000 Per Month Excluding Rent,
Move In Charges Rs 500',
(select listing_type_id from listing_type where listing_type_name = 'Rental'),
(select property_type_id from listing_property_type where property_type_name ='Apartment'),
12.9534626,77.6977583, 650, 15000, 150000,
(select apartment_id from apartment where apartment_name = 'Ittina Akaya'),
(select furnishing_type_id from listing_furnishing_type where furnishing_type_name = 'Semi Furnished'),
(select bedroom_count_id from listing_bedroom_count where bedroom_count_name = '1 BHK'),
(select transaction_mode_id from listing_transaction_mode where transaction_mode_name = 'Cheque'),
false,
true,
true) returning listing_id )
insert into listing_amenity (listing_id, fans, geyser, cupboards, chimney, parking, airconditioner, sofa, diningtable, centertable, television, internet, dth, refrigerator, washingmachine, bed)
select inserted.listing_id ,true, true, true, false, true, false, false, false, false, false, false, false, false, false, false from inserted;


with inserted as (
insert into listing (user_id,
listing_title, listing_address,
listing_type_id,
property_type_id, listing_latitude, listing_longitude, listing_area, listing_value, listing_deposit,
apartment_id,
furnishing_type_id,
bedroom_count_id,
transaction_mode_id,
non_veg_allowed,
bachelor_allowed,
listing_active_ind)
values
(
(select user_id from crs_user where user_name = 'Load Test Home Owner')
, '1 BHK Apartment In Butterfly Apartment, Kadubessnahalli',
'Second Floor, Butterfly Apartment, Kadubessnahalli, Near Main Road,
2 Balconies, West Facing Main Door,
Vetrified Tiles Flooring, Borewell Water,
Maintainence Rs 1000 Per Month Excluding Rent,
Move In Charges Rs 500',
(select listing_type_id from listing_type where listing_type_name = 'Rental'),
(select property_type_id from listing_property_type where property_type_name ='Apartment'),
12.9384397,77.6970864, 650, 15000, 150000,
(select apartment_id from apartment where apartment_name = 'Butterfly Apartment'),
(select furnishing_type_id from listing_furnishing_type where furnishing_type_name = 'Semi Furnished'),
(select bedroom_count_id from listing_bedroom_count where bedroom_count_name = '1 BHK'),
(select transaction_mode_id from listing_transaction_mode where transaction_mode_name = 'Cheque'),
false,
true,
true) returning listing_id )
insert into listing_amenity (listing_id, fans, geyser, cupboards, chimney, parking, airconditioner, sofa, diningtable, centertable, television, internet, dth, refrigerator, washingmachine, bed)
select inserted.listing_id ,true, true, true, false, true, false, false, false, false, false, false, false, false, false, false from inserted;


with inserted as (
insert into listing (user_id,
listing_title, listing_address,
listing_type_id,
property_type_id, listing_latitude, listing_longitude, listing_area, listing_value, listing_deposit,
apartment_id,
furnishing_type_id,
bedroom_count_id,
transaction_mode_id,
non_veg_allowed,
bachelor_allowed,
listing_active_ind)
values
(
(select user_id from crs_user where user_name = 'Load Test Home Owner')
, '1 BHK Apartment In Vandana Apartment, Panthur',
'Second Floor, Vandana Apartment, Panthur, Near Railway Station,
2 Balconies, West Facing Main Door,
Vetrified Tiles Flooring, Borewell Water,
Maintainence Rs 1000 Per Month Excluding Rent,
Move In Charges Rs 500',
(select listing_type_id from listing_type where listing_type_name = 'Rental'),
(select property_type_id from listing_property_type where property_type_name ='Apartment'),
12.9364609,77.7021243, 650, 15000, 150000,
(select apartment_id from apartment where apartment_name = 'Vandana Apartment'),
(select furnishing_type_id from listing_furnishing_type where furnishing_type_name = 'Semi Furnished'),
(select bedroom_count_id from listing_bedroom_count where bedroom_count_name = '1 BHK'),
(select transaction_mode_id from listing_transaction_mode where transaction_mode_name = 'Cheque'),
false,
true,
true) returning listing_id )
insert into listing_amenity (listing_id, fans, geyser, cupboards, chimney, parking, airconditioner, sofa, diningtable, centertable, television, internet, dth, refrigerator, washingmachine, bed)
select inserted.listing_id ,true, true, true, false, true, false, false, false, false, false, false, false, false, false, false from inserted;


with inserted as (
insert into listing (user_id,
listing_title, listing_address,
listing_type_id,
property_type_id, listing_latitude, listing_longitude, listing_area, listing_value, listing_deposit,
apartment_id,
furnishing_type_id,
bedroom_count_id,
transaction_mode_id,
non_veg_allowed,
bachelor_allowed,
listing_active_ind)
values
(
(select user_id from crs_user where user_name = 'Load Test Home Owner')
, '1 BHK Apartment In SLS Splendor, Bellandur',
'Second Floor, SLS Splendor, Bellandur, Near Bangalore Central Mall,
2 Balconies, West Facing Main Door,
Vetrified Tiles Flooring, Borewell Water,
Maintainence Rs 1000 Per Month Excluding Rent,
Move In Charges Rs 500',
(select listing_type_id from listing_type where listing_type_name = 'Rental'),
(select property_type_id from listing_property_type where property_type_name ='Apartment'),
12.9239338,77.6780734, 650, 15000, 150000,
(select apartment_id from apartment where apartment_name = 'SLS Splendor'),
(select furnishing_type_id from listing_furnishing_type where furnishing_type_name = 'Semi Furnished'),
(select bedroom_count_id from listing_bedroom_count where bedroom_count_name = '1 BHK'),
(select transaction_mode_id from listing_transaction_mode where transaction_mode_name = 'Cheque'),
false,
true,
true) returning listing_id )
insert into listing_amenity (listing_id, fans, geyser, cupboards, chimney, parking, airconditioner, sofa, diningtable, centertable, television, internet, dth, refrigerator, washingmachine, bed)
select inserted.listing_id ,true, true, true, false, true, false, false, false, false, false, false, false, false, false, false from inserted;


with inserted as (
insert into listing (user_id,
listing_title, listing_address,
listing_type_id,
property_type_id, listing_latitude, listing_longitude, listing_area, listing_value, listing_deposit,
apartment_id,
furnishing_type_id,
bedroom_count_id,
transaction_mode_id,
non_veg_allowed,
bachelor_allowed,
listing_active_ind)
values
(
(select user_id from crs_user where user_name = 'Load Test Home Owner')
, '1 BHK Apartment In Salarpuria Sattva Senorita, Sarjapur Road',
'Second Floor, Salarpuria Sattva Senorita, Sarjapur Road, Near Flyover,
2 Balconies, West Facing Main Door,
Vetrified Tiles Flooring, Borewell Water,
Maintainence Rs 1000 Per Month Excluding Rent,
Move In Charges Rs 500',
(select listing_type_id from listing_type where listing_type_name = 'Rental'),
(select property_type_id from listing_property_type where property_type_name ='Apartment'),
12.9058677,77.6834147, 650, 15000, 150000,
(select apartment_id from apartment where apartment_name = 'Salarpuria Sattva Senorita'),
(select furnishing_type_id from listing_furnishing_type where furnishing_type_name = 'Semi Furnished'),
(select bedroom_count_id from listing_bedroom_count where bedroom_count_name = '1 BHK'),
(select transaction_mode_id from listing_transaction_mode where transaction_mode_name = 'Cheque'),
false,
true,
true) returning listing_id )
insert into listing_amenity (listing_id, fans, geyser, cupboards, chimney, parking, airconditioner, sofa, diningtable, centertable, television, internet, dth, refrigerator, washingmachine, bed)
select inserted.listing_id ,true, true, true, false, true, false, false, false, false, false, false, false, false, false, false from inserted;


-------------------------------2 BHK------------------------------------------------------------------------------------

with inserted as (
insert into listing (user_id,
listing_title, listing_address,
listing_type_id,
property_type_id, listing_latitude, listing_longitude, listing_area, listing_value, listing_deposit,
apartment_id,
furnishing_type_id,
bedroom_count_id,
transaction_mode_id,
non_veg_allowed,
bachelor_allowed,
listing_active_ind)
values
(
(select user_id from crs_user where user_name = 'Load Test Home Owner')
, '2 BHK Apartment In Durga Rainbow, Mahadevpura',
'Second Floor, Durga Rainbow, Outer Ring Road, Near Hyundai Showroom,
2 Balconies, West Facing Main Door,
Vetrified Tiles Flooring, Borewell Water,
Maintainence Rs 1000 Per Month Excluding Rent,
Move In Charges Rs 500',
(select listing_type_id from listing_type where listing_type_name = 'Rental'),
(select property_type_id from listing_property_type where property_type_name ='Apartment'),
12.988732, 77.6876913, 1200, 25000, 200000,
(select apartment_id from apartment where apartment_name = 'Durga Rainbow'),
(select furnishing_type_id from listing_furnishing_type where furnishing_type_name = 'Fully Furnished'),
(select bedroom_count_id from listing_bedroom_count where bedroom_count_name = '2 BHK'),
(select transaction_mode_id from listing_transaction_mode where transaction_mode_name = 'Online Transfers'),
false,
true,
true) returning listing_id )
insert into listing_amenity (listing_id, fans, geyser, cupboards, chimney, parking, airconditioner, sofa, diningtable, centertable, television, internet, dth, refrigerator, washingmachine, bed)
select inserted.listing_id ,true, true, true, false, true, false, false, false, false, false, false, false, false, false, false from inserted;


with inserted as (
insert into listing (user_id,
listing_title, listing_address,
listing_type_id,
property_type_id, listing_latitude, listing_longitude, listing_area, listing_value, listing_deposit,
apartment_id,
furnishing_type_id,
bedroom_count_id,
transaction_mode_id,
non_veg_allowed,
bachelor_allowed,
listing_active_ind)
values
(
(select user_id from crs_user where user_name = 'Load Test Home Owner')
, '2 BHK Apartment In Alpine Eco, Doddanekundi',
'Second Floor, Alpine Eco, Outer Ring Road, Near Rainbow Hospital,
2 Balconies, West Facing Main Door,
Vetrified Tiles Flooring, Borewell Water,
Maintainence Rs 1000 Per Month Excluding Rent,
Move In Charges Rs 500',
(select listing_type_id from listing_type where listing_type_name = 'Rental'),
(select property_type_id from listing_property_type where property_type_name ='Apartment'),
12.973675, 77.700803, 1200, 25000, 200000,
(select apartment_id from apartment where apartment_name = 'Alpine Eco' limit 1),
(select furnishing_type_id from listing_furnishing_type where furnishing_type_name = 'Fully Furnished'),
(select bedroom_count_id from listing_bedroom_count where bedroom_count_name = '2 BHK'),
(select transaction_mode_id from listing_transaction_mode where transaction_mode_name = 'Online Transfers'),
false,
true,
true) returning listing_id )
insert into listing_amenity (listing_id, fans, geyser, cupboards, chimney, parking, airconditioner, sofa, diningtable, centertable, television, internet, dth, refrigerator, washingmachine, bed)
select inserted.listing_id ,true, true, true, false, true, false, false, false, false, false, false, false, false, false, false from inserted;


with inserted as (
insert into listing (user_id,
listing_title, listing_address,
listing_type_id,
property_type_id, listing_latitude, listing_longitude, listing_area, listing_value, listing_deposit,
apartment_id,
furnishing_type_id,
bedroom_count_id,
transaction_mode_id,
non_veg_allowed,
bachelor_allowed,
listing_active_ind)
values
(
(select user_id from crs_user where user_name = 'Load Test Home Owner')
, '2 BHK Apartment In Bren Unity Apartments, Karthik Nagar',
'Second Floor, Bren Unity, Karthik Nagar, Outer Ring Road, Near Axis Bank,
2 Balconies, West Facing Main Door,
Vetrified Tiles Flooring, Borewell Water,
Maintainence Rs 1000 Per Month Excluding Rent,
Move In Charges Rs 500',
(select listing_type_id from listing_type where listing_type_name = 'Rental'),
(select property_type_id from listing_property_type where property_type_name ='Apartment'),
12.9711705,77.7058543, 1200, 25000, 200000,
(select apartment_id from apartment where apartment_name = 'Bren Unity Apartments'),
(select furnishing_type_id from listing_furnishing_type where furnishing_type_name = 'Fully Furnished'),
(select bedroom_count_id from listing_bedroom_count where bedroom_count_name = '2 BHK'),
(select transaction_mode_id from listing_transaction_mode where transaction_mode_name = 'Online Transfers'),
false,
true,
true) returning listing_id )
insert into listing_amenity (listing_id, fans, geyser, cupboards, chimney, parking, airconditioner, sofa, diningtable, centertable, television, internet, dth, refrigerator, washingmachine, bed)
select inserted.listing_id ,true, true, true, false, true, false, false, false, false, false, false, false, false, false, false from inserted;


with inserted as (
insert into listing (user_id,
listing_title, listing_address,
listing_type_id,
property_type_id, listing_latitude, listing_longitude, listing_area, listing_value, listing_deposit,
apartment_id,
furnishing_type_id,
bedroom_count_id,
transaction_mode_id,
non_veg_allowed,
bachelor_allowed,
listing_active_ind)
values
(
(select user_id from crs_user where user_name = 'Load Test Home Owner')
, '2 BHK Apartment In SVS Silver Woods, Whitefield',
'Second Floor, SVS Silver Woods, Whitefile, Near More Store,
2 Balconies, West Facing Main Door,
Vetrified Tiles Flooring, Borewell Water,
Maintainence Rs 1000 Per Month Excluding Rent,
Move In Charges Rs 500',
(select listing_type_id from listing_type where listing_type_name = 'Rental'),
(select property_type_id from listing_property_type where property_type_name ='Apartment'),
12.9570667,77.7384723, 1200, 25000, 200000,
(select apartment_id from apartment where apartment_name = 'SVS Silver Woods'),
(select furnishing_type_id from listing_furnishing_type where furnishing_type_name = 'Fully Furnished'),
(select bedroom_count_id from listing_bedroom_count where bedroom_count_name = '2 BHK'),
(select transaction_mode_id from listing_transaction_mode where transaction_mode_name = 'Online Transfers'),
false,
true,
true) returning listing_id )
insert into listing_amenity (listing_id, fans, geyser, cupboards, chimney, parking, airconditioner, sofa, diningtable, centertable, television, internet, dth, refrigerator, washingmachine, bed)
select inserted.listing_id ,true, true, true, false, true, false, false, false, false, false, false, false, false, false, false from inserted;


with inserted as (
insert into listing (user_id,
listing_title, listing_address,
listing_type_id,
property_type_id, listing_latitude, listing_longitude, listing_area, listing_value, listing_deposit,
apartment_id,
furnishing_type_id,
bedroom_count_id,
transaction_mode_id,
non_veg_allowed,
bachelor_allowed,
listing_active_ind)
values
(
(select user_id from crs_user where user_name = 'Load Test Home Owner')
, '2 BHK Apartment In Knights Bridge, Brookfield',
'Second Floor, Knights Bridge, Brookfield, Near McDonalds,
2 Balconies, West Facing Main Door,
Vetrified Tiles Flooring, Borewell Water,
Maintainence Rs 1000 Per Month Excluding Rent,
Move In Charges Rs 500',
(select listing_type_id from listing_type where listing_type_name = 'Rental'),
(select property_type_id from listing_property_type where property_type_name ='Apartment'),
12.967944, 77.716309, 1200, 25000, 200000,
(select apartment_id from apartment where apartment_name = 'Knights Bridge' limit 1),
(select furnishing_type_id from listing_furnishing_type where furnishing_type_name = 'Fully Furnished'),
(select bedroom_count_id from listing_bedroom_count where bedroom_count_name = '2 BHK'),
(select transaction_mode_id from listing_transaction_mode where transaction_mode_name = 'Online Transfers'),
false,
true,
true) returning listing_id )
insert into listing_amenity (listing_id, fans, geyser, cupboards, chimney, parking, airconditioner, sofa, diningtable, centertable, television, internet, dth, refrigerator, washingmachine, bed)
select inserted.listing_id ,true, true, true, false, true, false, false, false, false, false, false, false, false, false, false from inserted;


with inserted as (
insert into listing (user_id,
listing_title, listing_address,
listing_type_id,
property_type_id, listing_latitude, listing_longitude, listing_area, listing_value, listing_deposit,
apartment_id,
furnishing_type_id,
bedroom_count_id,
transaction_mode_id,
non_veg_allowed,
bachelor_allowed,
listing_active_ind)
values
(
(select user_id from crs_user where user_name = 'Load Test Home Owner')
, '2 BHK Apartment In Prestige Shantiniketan, Whitefield',
'Second Floor, Prestige Shantiniketan, Whitefield, In Front of ITPL,
2 Balconies, West Facing Main Door,
Vetrified Tiles Flooring, Borewell Water,
Maintainence Rs 1000 Per Month Excluding Rent,
Move In Charges Rs 500',
(select listing_type_id from listing_type where listing_type_name = 'Rental'),
(select property_type_id from listing_property_type where property_type_name ='Apartment'),
12.9897602,77.7309759, 1200, 25000, 200000,
(select apartment_id from apartment where apartment_name = 'Prestige Shantiniketan'),
(select furnishing_type_id from listing_furnishing_type where furnishing_type_name = 'Fully Furnished'),
(select bedroom_count_id from listing_bedroom_count where bedroom_count_name = '2 BHK'),
(select transaction_mode_id from listing_transaction_mode where transaction_mode_name = 'Online Transfers'),
false,
true,
true) returning listing_id )
insert into listing_amenity (listing_id, fans, geyser, cupboards, chimney, parking, airconditioner, sofa, diningtable, centertable, television, internet, dth, refrigerator, washingmachine, bed)
select inserted.listing_id ,true, true, true, false, true, false, false, false, false, false, false, false, false, false, false from inserted;


with inserted as (
insert into listing (user_id,
listing_title, listing_address,
listing_type_id,
property_type_id, listing_latitude, listing_longitude, listing_area, listing_value, listing_deposit,
apartment_id,
furnishing_type_id,
bedroom_count_id,
transaction_mode_id,
non_veg_allowed,
bachelor_allowed,
listing_active_ind)
values
(
(select user_id from crs_user where user_name = 'Load Test Home Owner')
, '2 BHK Apartment In Rohan Vasanta, Kundalhalli',
'Second Floor, Rohan Vasanta, Kundalhalli, Near Bridge,
2 Balconies, West Facing Main Door,
Vetrified Tiles Flooring, Borewell Water,
Maintainence Rs 1000 Per Month Excluding Rent,
Move In Charges Rs 500',
(select listing_type_id from listing_type where listing_type_name = 'Rental'),
(select property_type_id from listing_property_type where property_type_name ='Apartment'),
12.958563, 77.706851, 1200, 25000, 200000,
(select apartment_id from apartment where apartment_name = 'Rohan Vasanta' limit 1),
(select furnishing_type_id from listing_furnishing_type where furnishing_type_name = 'Fully Furnished'),
(select bedroom_count_id from listing_bedroom_count where bedroom_count_name = '2 BHK'),
(select transaction_mode_id from listing_transaction_mode where transaction_mode_name = 'Online Transfers'),
false,
true,
true) returning listing_id )
insert into listing_amenity (listing_id, fans, geyser, cupboards, chimney, parking, airconditioner, sofa, diningtable, centertable, television, internet, dth, refrigerator, washingmachine, bed)
select inserted.listing_id ,true, true, true, false, true, false, false, false, false, false, false, false, false, false, false from inserted;


with inserted as (
insert into listing (user_id,
listing_title, listing_address,
listing_type_id,
property_type_id, listing_latitude, listing_longitude, listing_area, listing_value, listing_deposit,
apartment_id,
furnishing_type_id,
bedroom_count_id,
transaction_mode_id,
non_veg_allowed,
bachelor_allowed,
listing_active_ind)
values
(
(select user_id from crs_user where user_name = 'Load Test Home Owner')
, '2 BHK Apartment In Ittina Akaya, Marathalli',
'Second Floor, Ittina Akaya, Marathalli, Near HomeTown,
2 Balconies, West Facing Main Door,
Vetrified Tiles Flooring, Borewell Water,
Maintainence Rs 1000 Per Month Excluding Rent,
Move In Charges Rs 500',
(select listing_type_id from listing_type where listing_type_name = 'Rental'),
(select property_type_id from listing_property_type where property_type_name ='Apartment'),
12.9534626,77.6977583, 1200, 25000, 200000,
(select apartment_id from apartment where apartment_name = 'Ittina Akaya'),
(select furnishing_type_id from listing_furnishing_type where furnishing_type_name = 'Fully Furnished'),
(select bedroom_count_id from listing_bedroom_count where bedroom_count_name = '2 BHK'),
(select transaction_mode_id from listing_transaction_mode where transaction_mode_name = 'Online Transfers'),
false,
true,
true) returning listing_id )
insert into listing_amenity (listing_id, fans, geyser, cupboards, chimney, parking, airconditioner, sofa, diningtable, centertable, television, internet, dth, refrigerator, washingmachine, bed)
select inserted.listing_id ,true, true, true, false, true, false, false, false, false, false, false, false, false, false, false from inserted;


with inserted as (
insert into listing (user_id,
listing_title, listing_address,
listing_type_id,
property_type_id, listing_latitude, listing_longitude, listing_area, listing_value, listing_deposit,
apartment_id,
furnishing_type_id,
bedroom_count_id,
transaction_mode_id,
non_veg_allowed,
bachelor_allowed,
listing_active_ind)
values
(
(select user_id from crs_user where user_name = 'Load Test Home Owner')
, '2 BHK Apartment In Butterfly Apartment, Kadubessnahalli',
'Second Floor, Butterfly Apartment, Kadubessnahalli, Near Main Road,
2 Balconies, West Facing Main Door,
Vetrified Tiles Flooring, Borewell Water,
Maintainence Rs 1000 Per Month Excluding Rent,
Move In Charges Rs 500',
(select listing_type_id from listing_type where listing_type_name = 'Rental'),
(select property_type_id from listing_property_type where property_type_name ='Apartment'),
12.9384397,77.6970864, 1200, 25000, 200000,
(select apartment_id from apartment where apartment_name = 'Butterfly Apartment'),
(select furnishing_type_id from listing_furnishing_type where furnishing_type_name = 'Fully Furnished'),
(select bedroom_count_id from listing_bedroom_count where bedroom_count_name = '2 BHK'),
(select transaction_mode_id from listing_transaction_mode where transaction_mode_name = 'Online Transfers'),
false,
true,
true) returning listing_id )
insert into listing_amenity (listing_id, fans, geyser, cupboards, chimney, parking, airconditioner, sofa, diningtable, centertable, television, internet, dth, refrigerator, washingmachine, bed)
select inserted.listing_id ,true, true, true, false, true, false, false, false, false, false, false, false, false, false, false from inserted;


with inserted as (
insert into listing (user_id,
listing_title, listing_address,
listing_type_id,
property_type_id, listing_latitude, listing_longitude, listing_area, listing_value, listing_deposit,
apartment_id,
furnishing_type_id,
bedroom_count_id,
transaction_mode_id,
non_veg_allowed,
bachelor_allowed,
listing_active_ind)
values
(
(select user_id from crs_user where user_name = 'Load Test Home Owner')
, '2 BHK Apartment In Vandana Apartment, Panthur',
'Second Floor, Vandana Apartment, Panthur, Near Railway Station,
2 Balconies, West Facing Main Door,
Vetrified Tiles Flooring, Borewell Water,
Maintainence Rs 1000 Per Month Excluding Rent,
Move In Charges Rs 500',
(select listing_type_id from listing_type where listing_type_name = 'Rental'),
(select property_type_id from listing_property_type where property_type_name ='Apartment'),
12.9364609,77.7021243, 1200, 25000, 200000,
(select apartment_id from apartment where apartment_name = 'Vandana Apartment'),
(select furnishing_type_id from listing_furnishing_type where furnishing_type_name = 'Fully Furnished'),
(select bedroom_count_id from listing_bedroom_count where bedroom_count_name = '2 BHK'),
(select transaction_mode_id from listing_transaction_mode where transaction_mode_name = 'Online Transfers'),
false,
true,
true) returning listing_id )
insert into listing_amenity (listing_id, fans, geyser, cupboards, chimney, parking, airconditioner, sofa, diningtable, centertable, television, internet, dth, refrigerator, washingmachine, bed)
select inserted.listing_id ,true, true, true, false, true, false, false, false, false, false, false, false, false, false, false from inserted;


with inserted as (
insert into listing (user_id,
listing_title, listing_address,
listing_type_id,
property_type_id, listing_latitude, listing_longitude, listing_area, listing_value, listing_deposit,
apartment_id,
furnishing_type_id,
bedroom_count_id,
transaction_mode_id,
non_veg_allowed,
bachelor_allowed,
listing_active_ind)
values
(
(select user_id from crs_user where user_name = 'Load Test Home Owner')
, '2 BHK Apartment In SLS Splendor, Bellandur',
'Second Floor, SLS Splendor, Bellandur, Near Bangalore Central Mall,
2 Balconies, West Facing Main Door,
Vetrified Tiles Flooring, Borewell Water,
Maintainence Rs 1000 Per Month Excluding Rent,
Move In Charges Rs 500',
(select listing_type_id from listing_type where listing_type_name = 'Rental'),
(select property_type_id from listing_property_type where property_type_name ='Apartment'),
12.9239338,77.6780734, 1200, 25000, 200000,
(select apartment_id from apartment where apartment_name = 'SLS Splendor'),
(select furnishing_type_id from listing_furnishing_type where furnishing_type_name = 'Fully Furnished'),
(select bedroom_count_id from listing_bedroom_count where bedroom_count_name = '2 BHK'),
(select transaction_mode_id from listing_transaction_mode where transaction_mode_name = 'Online Transfers'),
false,
true,
true) returning listing_id )
insert into listing_amenity (listing_id, fans, geyser, cupboards, chimney, parking, airconditioner, sofa, diningtable, centertable, television, internet, dth, refrigerator, washingmachine, bed)
select inserted.listing_id ,true, true, true, false, true, false, false, false, false, false, false, false, false, false, false from inserted;


with inserted as (
insert into listing (user_id,
listing_title, listing_address,
listing_type_id,
property_type_id, listing_latitude, listing_longitude, listing_area, listing_value, listing_deposit,
apartment_id,
furnishing_type_id,
bedroom_count_id,
transaction_mode_id,
non_veg_allowed,
bachelor_allowed,
listing_active_ind)
values
(
(select user_id from crs_user where user_name = 'Load Test Home Owner')
, '2 BHK Apartment In Salarpuria Sattva Senorita, Sarjapur Road',
'Second Floor, Salarpuria Sattva Senorita, Sarjapur Road, Near Flyover,
2 Balconies, West Facing Main Door,
Vetrified Tiles Flooring, Borewell Water,
Maintainence Rs 1000 Per Month Excluding Rent,
Move In Charges Rs 500',
(select listing_type_id from listing_type where listing_type_name = 'Rental'),
(select property_type_id from listing_property_type where property_type_name ='Apartment'),
12.9058677,77.6834147, 1200, 25000, 200000,
(select apartment_id from apartment where apartment_name = 'Salarpuria Sattva Senorita'),
(select furnishing_type_id from listing_furnishing_type where furnishing_type_name = 'Fully Furnished'),
(select bedroom_count_id from listing_bedroom_count where bedroom_count_name = '2 BHK'),
(select transaction_mode_id from listing_transaction_mode where transaction_mode_name = 'Online Transfers'),
false,
true,
true) returning listing_id )
insert into listing_amenity (listing_id, fans, geyser, cupboards, chimney, parking, airconditioner, sofa, diningtable, centertable, television, internet, dth, refrigerator, washingmachine, bed)
select inserted.listing_id ,true, true, true, false, true, false, false, false, false, false, false, false, false, false, false from inserted;

-------------------------------3 BHK------------------------------------------------------------------------------------

with inserted as (
insert into listing (user_id,
listing_title, listing_address,
listing_type_id,
property_type_id, listing_latitude, listing_longitude, listing_area, listing_value, listing_deposit,
apartment_id,
furnishing_type_id,
bedroom_count_id,
transaction_mode_id,
non_veg_allowed,
bachelor_allowed,
listing_active_ind)
values
(
(select user_id from crs_user where user_name = 'Load Test Home Owner')
, '3 BHK Row House In Mahadevpura',
'Second Floor, Durga Rainbow, Outer Ring Road, Near Hyundai Showroom,
2 Balconies, West Facing Main Door,
Vetrified Tiles Flooring, Borewell Water,
Maintainence Rs 1000 Per Month Excluding Rent,
Move In Charges Rs 500',
(select listing_type_id from listing_type where listing_type_name = 'Rental'),
(select property_type_id from listing_property_type where property_type_name ='Row House'),
12.989732, 77.6876913, 1800, 35000, 300000,
null,
(select furnishing_type_id from listing_furnishing_type where furnishing_type_name = 'Non Furnished'),
(select bedroom_count_id from listing_bedroom_count where bedroom_count_name = '3 BHK'),
(select transaction_mode_id from listing_transaction_mode where transaction_mode_name = 'Cash'),
false,
true,
true) returning listing_id )
insert into listing_amenity (listing_id, fans, geyser, cupboards, chimney, parking, airconditioner, sofa, diningtable, centertable, television, internet, dth, refrigerator, washingmachine, bed)
select inserted.listing_id ,true, true, true, false, true, false, false, false, false, false, false, false, false, false, false from inserted;


with inserted as (
insert into listing (user_id,
listing_title, listing_address,
listing_type_id,
property_type_id, listing_latitude, listing_longitude, listing_area, listing_value, listing_deposit,
apartment_id,
furnishing_type_id,
bedroom_count_id,
transaction_mode_id,
non_veg_allowed,
bachelor_allowed,
listing_active_ind)
values
(
(select user_id from crs_user where user_name = 'Load Test Home Owner')
, '3 BHK Row House In Doddanekundi',
'Second Floor, Alpine Eco, Outer Ring Road, Near Rainbow Hospital,
2 Balconies, West Facing Main Door,
Vetrified Tiles Flooring, Borewell Water,
Maintainence Rs 1000 Per Month Excluding Rent,
Move In Charges Rs 500',
(select listing_type_id from listing_type where listing_type_name = 'Rental'),
(select property_type_id from listing_property_type where property_type_name ='Row House'),
12.974675, 77.700803, 1800, 35000, 300000,
null,
(select furnishing_type_id from listing_furnishing_type where furnishing_type_name = 'Non Furnished'),
(select bedroom_count_id from listing_bedroom_count where bedroom_count_name = '3 BHK'),
(select transaction_mode_id from listing_transaction_mode where transaction_mode_name = 'Cash'),
false,
true,
true) returning listing_id )
insert into listing_amenity (listing_id, fans, geyser, cupboards, chimney, parking, airconditioner, sofa, diningtable, centertable, television, internet, dth, refrigerator, washingmachine, bed)
select inserted.listing_id ,true, true, true, false, true, false, false, false, false, false, false, false, false, false, false from inserted;


with inserted as (
insert into listing (user_id,
listing_title, listing_address,
listing_type_id,
property_type_id, listing_latitude, listing_longitude, listing_area, listing_value, listing_deposit,
apartment_id,
furnishing_type_id,
bedroom_count_id,
transaction_mode_id,
non_veg_allowed,
bachelor_allowed,
listing_active_ind)
values
(
(select user_id from crs_user where user_name = 'Load Test Home Owner')
, '3 BHK Row House In Karthik Nagar',
'Second Floor, Bren Unity, Karthik Nagar, Outer Ring Road, Near Axis Bank,
2 Balconies, West Facing Main Door,
Vetrified Tiles Flooring, Borewell Water,
Maintainence Rs 1000 Per Month Excluding Rent,
Move In Charges Rs 500',
(select listing_type_id from listing_type where listing_type_name = 'Rental'),
(select property_type_id from listing_property_type where property_type_name ='Row House'),
12.9721705,77.7058543, 1800, 35000, 300000,
null,
(select furnishing_type_id from listing_furnishing_type where furnishing_type_name = 'Non Furnished'),
(select bedroom_count_id from listing_bedroom_count where bedroom_count_name = '3 BHK'),
(select transaction_mode_id from listing_transaction_mode where transaction_mode_name = 'Cash'),
false,
true,
true) returning listing_id )
insert into listing_amenity (listing_id, fans, geyser, cupboards, chimney, parking, airconditioner, sofa, diningtable, centertable, television, internet, dth, refrigerator, washingmachine, bed)
select inserted.listing_id ,true, true, true, false, true, false, false, false, false, false, false, false, false, false, false from inserted;


with inserted as (
insert into listing (user_id,
listing_title, listing_address,
listing_type_id,
property_type_id, listing_latitude, listing_longitude, listing_area, listing_value, listing_deposit,
apartment_id,
furnishing_type_id,
bedroom_count_id,
transaction_mode_id,
non_veg_allowed,
bachelor_allowed,
listing_active_ind)
values
(
(select user_id from crs_user where user_name = 'Load Test Home Owner')
, '3 BHK Row House In Whitefield',
'Second Floor, SVS Silver Woods, Whitefile, Near More Store,
2 Balconies, West Facing Main Door,
Vetrified Tiles Flooring, Borewell Water,
Maintainence Rs 1000 Per Month Excluding Rent,
Move In Charges Rs 500',
(select listing_type_id from listing_type where listing_type_name = 'Rental'),
(select property_type_id from listing_property_type where property_type_name ='Row House'),
12.9580667,77.7384723, 1800, 35000, 300000,
null,
(select furnishing_type_id from listing_furnishing_type where furnishing_type_name = 'Non Furnished'),
(select bedroom_count_id from listing_bedroom_count where bedroom_count_name = '3 BHK'),
(select transaction_mode_id from listing_transaction_mode where transaction_mode_name = 'Cash'),
false,
true,
true) returning listing_id )
insert into listing_amenity (listing_id, fans, geyser, cupboards, chimney, parking, airconditioner, sofa, diningtable, centertable, television, internet, dth, refrigerator, washingmachine, bed)
select inserted.listing_id ,true, true, true, false, true, false, false, false, false, false, false, false, false, false, false from inserted;


with inserted as (
insert into listing (user_id,
listing_title, listing_address,
listing_type_id,
property_type_id, listing_latitude, listing_longitude, listing_area, listing_value, listing_deposit,
apartment_id,
furnishing_type_id,
bedroom_count_id,
transaction_mode_id,
non_veg_allowed,
bachelor_allowed,
listing_active_ind)
values
(
(select user_id from crs_user where user_name = 'Load Test Home Owner')
, '3 BHK Row House In Brookfield',
'Second Floor, Knights Bridge, Brookfield, Near McDonalds,
2 Balconies, West Facing Main Door,
Vetrified Tiles Flooring, Borewell Water,
Maintainence Rs 1000 Per Month Excluding Rent,
Move In Charges Rs 500',
(select listing_type_id from listing_type where listing_type_name = 'Rental'),
(select property_type_id from listing_property_type where property_type_name ='Row House'),
12.968944, 77.716309, 1800, 35000, 300000,
null,
(select furnishing_type_id from listing_furnishing_type where furnishing_type_name = 'Non Furnished'),
(select bedroom_count_id from listing_bedroom_count where bedroom_count_name = '3 BHK'),
(select transaction_mode_id from listing_transaction_mode where transaction_mode_name = 'Cash'),
false,
true,
true) returning listing_id )
insert into listing_amenity (listing_id, fans, geyser, cupboards, chimney, parking, airconditioner, sofa, diningtable, centertable, television, internet, dth, refrigerator, washingmachine, bed)
select inserted.listing_id ,true, true, true, false, true, false, false, false, false, false, false, false, false, false, false from inserted;


with inserted as (
insert into listing (user_id,
listing_title, listing_address,
listing_type_id,
property_type_id, listing_latitude, listing_longitude, listing_area, listing_value, listing_deposit,
apartment_id,
furnishing_type_id,
bedroom_count_id,
transaction_mode_id,
non_veg_allowed,
bachelor_allowed,
listing_active_ind)
values
(
(select user_id from crs_user where user_name = 'Load Test Home Owner')
, '3 BHK Row House In Whitefield',
'Second Floor, Prestige Shantiniketan, Whitefield, In Front of ITPL,
2 Balconies, West Facing Main Door,
Vetrified Tiles Flooring, Borewell Water,
Maintainence Rs 1000 Per Month Excluding Rent,
Move In Charges Rs 500',
(select listing_type_id from listing_type where listing_type_name = 'Rental'),
(select property_type_id from listing_property_type where property_type_name ='Row House'),
12.9907602,77.7309759, 1800, 35000, 300000,
null,
(select furnishing_type_id from listing_furnishing_type where furnishing_type_name = 'Non Furnished'),
(select bedroom_count_id from listing_bedroom_count where bedroom_count_name = '3 BHK'),
(select transaction_mode_id from listing_transaction_mode where transaction_mode_name = 'Cash'),
false,
true,
true) returning listing_id )
insert into listing_amenity (listing_id, fans, geyser, cupboards, chimney, parking, airconditioner, sofa, diningtable, centertable, television, internet, dth, refrigerator, washingmachine, bed)
select inserted.listing_id ,true, true, true, false, true, false, false, false, false, false, false, false, false, false, false from inserted;


with inserted as (
insert into listing (user_id,
listing_title, listing_address,
listing_type_id,
property_type_id, listing_latitude, listing_longitude, listing_area, listing_value, listing_deposit,
apartment_id,
furnishing_type_id,
bedroom_count_id,
transaction_mode_id,
non_veg_allowed,
bachelor_allowed,
listing_active_ind)
values
(
(select user_id from crs_user where user_name = 'Load Test Home Owner')
, '3 BHK Row House In Kundalhalli',
'Second Floor, Rohan Vasanta, Kundalhalli, Near Bridge,
2 Balconies, West Facing Main Door,
Vetrified Tiles Flooring, Borewell Water,
Maintainence Rs 1000 Per Month Excluding Rent,
Move In Charges Rs 500',
(select listing_type_id from listing_type where listing_type_name = 'Rental'),
(select property_type_id from listing_property_type where property_type_name ='Row House'),
12.959563, 77.706851, 1800, 35000, 300000,
null,
(select furnishing_type_id from listing_furnishing_type where furnishing_type_name = 'Non Furnished'),
(select bedroom_count_id from listing_bedroom_count where bedroom_count_name = '3 BHK'),
(select transaction_mode_id from listing_transaction_mode where transaction_mode_name = 'Cash'),
false,
true,
true) returning listing_id )
insert into listing_amenity (listing_id, fans, geyser, cupboards, chimney, parking, airconditioner, sofa, diningtable, centertable, television, internet, dth, refrigerator, washingmachine, bed)
select inserted.listing_id ,true, true, true, false, true, false, false, false, false, false, false, false, false, false, false from inserted;


with inserted as (
insert into listing (user_id,
listing_title, listing_address,
listing_type_id,
property_type_id, listing_latitude, listing_longitude, listing_area, listing_value, listing_deposit,
apartment_id,
furnishing_type_id,
bedroom_count_id,
transaction_mode_id,
non_veg_allowed,
bachelor_allowed,
listing_active_ind)
values
(
(select user_id from crs_user where user_name = 'Load Test Home Owner')
, '3 BHK Row House In Marathalli',
'Second Floor, Ittina Akaya, Marathalli, Near HomeTown,
2 Balconies, West Facing Main Door,
Vetrified Tiles Flooring, Borewell Water,
Maintainence Rs 1000 Per Month Excluding Rent,
Move In Charges Rs 500',
(select listing_type_id from listing_type where listing_type_name = 'Rental'),
(select property_type_id from listing_property_type where property_type_name ='Row House'),
12.9544626,77.6977583, 1800, 35000, 300000,
null,
(select furnishing_type_id from listing_furnishing_type where furnishing_type_name = 'Non Furnished'),
(select bedroom_count_id from listing_bedroom_count where bedroom_count_name = '3 BHK'),
(select transaction_mode_id from listing_transaction_mode where transaction_mode_name = 'Cash'),
false,
true,
true) returning listing_id )
insert into listing_amenity (listing_id, fans, geyser, cupboards, chimney, parking, airconditioner, sofa, diningtable, centertable, television, internet, dth, refrigerator, washingmachine, bed)
select inserted.listing_id ,true, true, true, false, true, false, false, false, false, false, false, false, false, false, false from inserted;


with inserted as (
insert into listing (user_id,
listing_title, listing_address,
listing_type_id,
property_type_id, listing_latitude, listing_longitude, listing_area, listing_value, listing_deposit,
apartment_id,
furnishing_type_id,
bedroom_count_id,
transaction_mode_id,
non_veg_allowed,
bachelor_allowed,
listing_active_ind)
values
(
(select user_id from crs_user where user_name = 'Load Test Home Owner')
, '3 BHK Row House In Kadubessnahalli',
'Second Floor, Butterfly Row House, Kadubessnahalli, Near Main Road,
2 Balconies, West Facing Main Door,
Vetrified Tiles Flooring, Borewell Water,
Maintainence Rs 1000 Per Month Excluding Rent,
Move In Charges Rs 500',
(select listing_type_id from listing_type where listing_type_name = 'Rental'),
(select property_type_id from listing_property_type where property_type_name ='Row House'),
12.9394397,77.6970864, 1800, 35000, 300000,
null,
(select furnishing_type_id from listing_furnishing_type where furnishing_type_name = 'Non Furnished'),
(select bedroom_count_id from listing_bedroom_count where bedroom_count_name = '3 BHK'),
(select transaction_mode_id from listing_transaction_mode where transaction_mode_name = 'Cash'),
false,
true,
true) returning listing_id )
insert into listing_amenity (listing_id, fans, geyser, cupboards, chimney, parking, airconditioner, sofa, diningtable, centertable, television, internet, dth, refrigerator, washingmachine, bed)
select inserted.listing_id ,true, true, true, false, true, false, false, false, false, false, false, false, false, false, false from inserted;


with inserted as (
insert into listing (user_id,
listing_title, listing_address,
listing_type_id,
property_type_id, listing_latitude, listing_longitude, listing_area, listing_value, listing_deposit,
apartment_id,
furnishing_type_id,
bedroom_count_id,
transaction_mode_id,
non_veg_allowed,
bachelor_allowed,
listing_active_ind)
values
(
(select user_id from crs_user where user_name = 'Load Test Home Owner')
, '3 BHK Row House In Panthur',
'Second Floor, Vandana Row House, Panthur, Near Railway Station,
2 Balconies, West Facing Main Door,
Vetrified Tiles Flooring, Borewell Water,
Maintainence Rs 1000 Per Month Excluding Rent,
Move In Charges Rs 500',
(select listing_type_id from listing_type where listing_type_name = 'Rental'),
(select property_type_id from listing_property_type where property_type_name ='Row House'),
12.9374609,77.7021243, 1800, 35000, 300000,
null,
(select furnishing_type_id from listing_furnishing_type where furnishing_type_name = 'Non Furnished'),
(select bedroom_count_id from listing_bedroom_count where bedroom_count_name = '3 BHK'),
(select transaction_mode_id from listing_transaction_mode where transaction_mode_name = 'Cash'),
false,
true,
true) returning listing_id )
insert into listing_amenity (listing_id, fans, geyser, cupboards, chimney, parking, airconditioner, sofa, diningtable, centertable, television, internet, dth, refrigerator, washingmachine, bed)
select inserted.listing_id ,true, true, true, false, true, false, false, false, false, false, false, false, false, false, false from inserted;


with inserted as (
insert into listing (user_id,
listing_title, listing_address,
listing_type_id,
property_type_id, listing_latitude, listing_longitude, listing_area, listing_value, listing_deposit,
apartment_id,
furnishing_type_id,
bedroom_count_id,
transaction_mode_id,
non_veg_allowed,
bachelor_allowed,
listing_active_ind)
values
(
(select user_id from crs_user where user_name = 'Load Test Home Owner')
, '3 BHK Row House In Bellandur',
'Second Floor, SLS Splendor, Bellandur, Near Bangalore Central Mall,
2 Balconies, West Facing Main Door,
Vetrified Tiles Flooring, Borewell Water,
Maintainence Rs 1000 Per Month Excluding Rent,
Move In Charges Rs 500',
(select listing_type_id from listing_type where listing_type_name = 'Rental'),
(select property_type_id from listing_property_type where property_type_name ='Row House'),
12.9249338,77.6780734, 1800, 35000, 300000,
null,
(select furnishing_type_id from listing_furnishing_type where furnishing_type_name = 'Non Furnished'),
(select bedroom_count_id from listing_bedroom_count where bedroom_count_name = '3 BHK'),
(select transaction_mode_id from listing_transaction_mode where transaction_mode_name = 'Cash'),
false,
true,
true) returning listing_id )
insert into listing_amenity (listing_id, fans, geyser, cupboards, chimney, parking, airconditioner, sofa, diningtable, centertable, television, internet, dth, refrigerator, washingmachine, bed)
select inserted.listing_id ,true, true, true, false, true, false, false, false, false, false, false, false, false, false, false from inserted;


with inserted as (
insert into listing (user_id,
listing_title, listing_address,
listing_type_id,
property_type_id, listing_latitude, listing_longitude, listing_area, listing_value, listing_deposit,
apartment_id,
furnishing_type_id,
bedroom_count_id,
transaction_mode_id,
non_veg_allowed,
bachelor_allowed,
listing_active_ind)
values
(
(select user_id from crs_user where user_name = 'Load Test Home Owner')
, '3 BHK Row House In Sarjapur Road',
'Second Floor, Salarpuria Sattva Senorita, Sarjapur Road, Near Flyover,
2 Balconies, West Facing Main Door,
Vetrified Tiles Flooring, Borewell Water,
Maintainence Rs 1000 Per Month Excluding Rent,
Move In Charges Rs 500',
(select listing_type_id from listing_type where listing_type_name = 'Rental'),
(select property_type_id from listing_property_type where property_type_name ='Row House'),
12.9068677,77.6834147, 1800, 35000, 300000,
null,
(select furnishing_type_id from listing_furnishing_type where furnishing_type_name = 'Non Furnished'),
(select bedroom_count_id from listing_bedroom_count where bedroom_count_name = '3 BHK'),
(select transaction_mode_id from listing_transaction_mode where transaction_mode_name = 'Cash'),
false,
true,
true) returning listing_id )
insert into listing_amenity (listing_id, fans, geyser, cupboards, chimney, parking, airconditioner, sofa, diningtable, centertable, television, internet, dth, refrigerator, washingmachine, bed)
select inserted.listing_id ,true, true, true, false, true, false, false, false, false, false, false, false, false, false, false from inserted;


-------------------------------4 BHK------------------------------------------------------------------------------------

with inserted as (
insert into listing (user_id,
listing_title, listing_address,
listing_type_id,
property_type_id, listing_latitude, listing_longitude, listing_area, listing_value, listing_deposit,
apartment_id,
furnishing_type_id,
bedroom_count_id,
transaction_mode_id,
non_veg_allowed,
bachelor_allowed,
listing_active_ind)
values
(
(select user_id from crs_user where user_name = 'Load Test Home Owner')
, '4 BHK Villa In Mahadevpura',
'Second Floor, Durga Rainbow, Outer Ring Road, Near Hyundai Showroom,
2 Balconies, West Facing Main Door,
Vetrified Tiles Flooring, Borewell Water,
Maintainence Rs 1000 Per Month Excluding Rent,
Move In Charges Rs 500',
(select listing_type_id from listing_type where listing_type_name = 'Rental'),
(select property_type_id from listing_property_type where property_type_name ='Row House'),
12.987732, 77.6876913, 2500, 40000, 400000,
null,
(select furnishing_type_id from listing_furnishing_type where furnishing_type_name = 'Semi Furnished'),
(select bedroom_count_id from listing_bedroom_count where bedroom_count_name = '4+ BHK'),
(select transaction_mode_id from listing_transaction_mode where transaction_mode_name = 'No Preference'),
false,
true,
true) returning listing_id )
insert into listing_amenity (listing_id, fans, geyser, cupboards, chimney, parking, airconditioner, sofa, diningtable, centertable, television, internet, dth, refrigerator, washingmachine, bed)
select inserted.listing_id ,true, true, true, false, true, false, false, false, false, false, false, false, false, false, false from inserted;


with inserted as (
insert into listing (user_id,
listing_title, listing_address,
listing_type_id,
property_type_id, listing_latitude, listing_longitude, listing_area, listing_value, listing_deposit,
apartment_id,
furnishing_type_id,
bedroom_count_id,
transaction_mode_id,
non_veg_allowed,
bachelor_allowed,
listing_active_ind)
values
(
(select user_id from crs_user where user_name = 'Load Test Home Owner')
, '4 BHK Villa In Doddanekundi',
'Second Floor, Alpine Eco, Outer Ring Road, Near Rainbow Hospital,
2 Balconies, West Facing Main Door,
Vetrified Tiles Flooring, Borewell Water,
Maintainence Rs 1000 Per Month Excluding Rent,
Move In Charges Rs 500',
(select listing_type_id from listing_type where listing_type_name = 'Rental'),
(select property_type_id from listing_property_type where property_type_name ='Villa'),
12.972675, 77.700803, 2500, 40000, 400000,
null,
(select furnishing_type_id from listing_furnishing_type where furnishing_type_name = 'Semi Furnished'),
(select bedroom_count_id from listing_bedroom_count where bedroom_count_name = '4+ BHK'),
(select transaction_mode_id from listing_transaction_mode where transaction_mode_name = 'No Preference'),
false,
true,
true) returning listing_id )
insert into listing_amenity (listing_id, fans, geyser, cupboards, chimney, parking, airconditioner, sofa, diningtable, centertable, television, internet, dth, refrigerator, washingmachine, bed)
select inserted.listing_id ,true, true, true, false, true, false, false, false, false, false, false, false, false, false, false from inserted;


with inserted as (
insert into listing (user_id,
listing_title, listing_address,
listing_type_id,
property_type_id, listing_latitude, listing_longitude, listing_area, listing_value, listing_deposit,
apartment_id,
furnishing_type_id,
bedroom_count_id,
transaction_mode_id,
non_veg_allowed,
bachelor_allowed,
listing_active_ind)
values
(
(select user_id from crs_user where user_name = 'Load Test Home Owner')
, '4 BHK Villa In Karthik Nagar',
'Second Floor, Bren Unity, Karthik Nagar, Outer Ring Road, Near Axis Bank,
2 Balconies, West Facing Main Door,
Vetrified Tiles Flooring, Borewell Water,
Maintainence Rs 1000 Per Month Excluding Rent,
Move In Charges Rs 500',
(select listing_type_id from listing_type where listing_type_name = 'Rental'),
(select property_type_id from listing_property_type where property_type_name ='Villa'),
12.9701705,77.7058543, 2500, 40000, 400000,
null,
(select furnishing_type_id from listing_furnishing_type where furnishing_type_name = 'Semi Furnished'),
(select bedroom_count_id from listing_bedroom_count where bedroom_count_name = '4+ BHK'),
(select transaction_mode_id from listing_transaction_mode where transaction_mode_name = 'No Preference'),
false,
true,
true) returning listing_id )
insert into listing_amenity (listing_id, fans, geyser, cupboards, chimney, parking, airconditioner, sofa, diningtable, centertable, television, internet, dth, refrigerator, washingmachine, bed)
select inserted.listing_id ,true, true, true, false, true, false, false, false, false, false, false, false, false, false, false from inserted;


with inserted as (
insert into listing (user_id,
listing_title, listing_address,
listing_type_id,
property_type_id, listing_latitude, listing_longitude, listing_area, listing_value, listing_deposit,
apartment_id,
furnishing_type_id,
bedroom_count_id,
transaction_mode_id,
non_veg_allowed,
bachelor_allowed,
listing_active_ind)
values
(
(select user_id from crs_user where user_name = 'Load Test Home Owner')
, '4 BHK Villa In Whitefield',
'Second Floor, SVS Silver Woods, Whitefile, Near More Store,
2 Balconies, West Facing Main Door,
Vetrified Tiles Flooring, Borewell Water,
Maintainence Rs 1000 Per Month Excluding Rent,
Move In Charges Rs 500',
(select listing_type_id from listing_type where listing_type_name = 'Rental'),
(select property_type_id from listing_property_type where property_type_name ='Villa'),
12.9560667,77.7384723, 2500, 40000, 400000,
null,
(select furnishing_type_id from listing_furnishing_type where furnishing_type_name = 'Semi Furnished'),
(select bedroom_count_id from listing_bedroom_count where bedroom_count_name = '4+ BHK'),
(select transaction_mode_id from listing_transaction_mode where transaction_mode_name = 'No Preference'),
false,
true,
true) returning listing_id )
insert into listing_amenity (listing_id, fans, geyser, cupboards, chimney, parking, airconditioner, sofa, diningtable, centertable, television, internet, dth, refrigerator, washingmachine, bed)
select inserted.listing_id ,true, true, true, false, true, false, false, false, false, false, false, false, false, false, false from inserted;


with inserted as (
insert into listing (user_id,
listing_title, listing_address,
listing_type_id,
property_type_id, listing_latitude, listing_longitude, listing_area, listing_value, listing_deposit,
apartment_id,
furnishing_type_id,
bedroom_count_id,
transaction_mode_id,
non_veg_allowed,
bachelor_allowed,
listing_active_ind)
values
(
(select user_id from crs_user where user_name = 'Load Test Home Owner')
, '4 BHK Villa In Brookfield',
'Second Floor, Knights Bridge, Brookfield, Near McDonalds,
2 Balconies, West Facing Main Door,
Vetrified Tiles Flooring, Borewell Water,
Maintainence Rs 1000 Per Month Excluding Rent,
Move In Charges Rs 500',
(select listing_type_id from listing_type where listing_type_name = 'Rental'),
(select property_type_id from listing_property_type where property_type_name ='Villa'),
12.966944, 77.716309, 2500, 40000, 400000,
null,
(select furnishing_type_id from listing_furnishing_type where furnishing_type_name = 'Semi Furnished'),
(select bedroom_count_id from listing_bedroom_count where bedroom_count_name = '4+ BHK'),
(select transaction_mode_id from listing_transaction_mode where transaction_mode_name = 'No Preference'),
false,
true,
true) returning listing_id )
insert into listing_amenity (listing_id, fans, geyser, cupboards, chimney, parking, airconditioner, sofa, diningtable, centertable, television, internet, dth, refrigerator, washingmachine, bed)
select inserted.listing_id ,true, true, true, false, true, false, false, false, false, false, false, false, false, false, false from inserted;


with inserted as (
insert into listing (user_id,
listing_title, listing_address,
listing_type_id,
property_type_id, listing_latitude, listing_longitude, listing_area, listing_value, listing_deposit,
apartment_id,
furnishing_type_id,
bedroom_count_id,
transaction_mode_id,
non_veg_allowed,
bachelor_allowed,
listing_active_ind)
values
(
(select user_id from crs_user where user_name = 'Load Test Home Owner')
, '4 BHK Villa In Whitefield',
'Second Floor, Prestige Shantiniketan, Whitefield, In Front of ITPL,
2 Balconies, West Facing Main Door,
Vetrified Tiles Flooring, Borewell Water,
Maintainence Rs 1000 Per Month Excluding Rent,
Move In Charges Rs 500',
(select listing_type_id from listing_type where listing_type_name = 'Rental'),
(select property_type_id from listing_property_type where property_type_name ='Villa'),
12.9887602,77.7309759, 2500, 40000, 400000,
null,
(select furnishing_type_id from listing_furnishing_type where furnishing_type_name = 'Semi Furnished'),
(select bedroom_count_id from listing_bedroom_count where bedroom_count_name = '4+ BHK'),
(select transaction_mode_id from listing_transaction_mode where transaction_mode_name = 'No Preference'),
false,
true,
true) returning listing_id )
insert into listing_amenity (listing_id, fans, geyser, cupboards, chimney, parking, airconditioner, sofa, diningtable, centertable, television, internet, dth, refrigerator, washingmachine, bed)
select inserted.listing_id ,true, true, true, false, true, false, false, false, false, false, false, false, false, false, false from inserted;


with inserted as (
insert into listing (user_id,
listing_title, listing_address,
listing_type_id,
property_type_id, listing_latitude, listing_longitude, listing_area, listing_value, listing_deposit,
apartment_id,
furnishing_type_id,
bedroom_count_id,
transaction_mode_id,
non_veg_allowed,
bachelor_allowed,
listing_active_ind)
values
(
(select user_id from crs_user where user_name = 'Load Test Home Owner')
, '4 BHK Villa In Kundalhalli',
'Second Floor, Rohan Vasanta, Kundalhalli, Near Bridge,
2 Balconies, West Facing Main Door,
Vetrified Tiles Flooring, Borewell Water,
Maintainence Rs 1000 Per Month Excluding Rent,
Move In Charges Rs 500',
(select listing_type_id from listing_type where listing_type_name = 'Rental'),
(select property_type_id from listing_property_type where property_type_name ='Villa'),
12.957563, 77.706851, 2500, 40000, 400000,
null,
(select furnishing_type_id from listing_furnishing_type where furnishing_type_name = 'Semi Furnished'),
(select bedroom_count_id from listing_bedroom_count where bedroom_count_name = '4+ BHK'),
(select transaction_mode_id from listing_transaction_mode where transaction_mode_name = 'No Preference'),
false,
true,
true) returning listing_id )
insert into listing_amenity (listing_id, fans, geyser, cupboards, chimney, parking, airconditioner, sofa, diningtable, centertable, television, internet, dth, refrigerator, washingmachine, bed)
select inserted.listing_id ,true, true, true, false, true, false, false, false, false, false, false, false, false, false, false from inserted;


with inserted as (
insert into listing (user_id,
listing_title, listing_address,
listing_type_id,
property_type_id, listing_latitude, listing_longitude, listing_area, listing_value, listing_deposit,
apartment_id,
furnishing_type_id,
bedroom_count_id,
transaction_mode_id,
non_veg_allowed,
bachelor_allowed,
listing_active_ind)
values
(
(select user_id from crs_user where user_name = 'Load Test Home Owner')
, '4 BHK Villa In Marathalli',
'Second Floor, Ittina Akaya, Marathalli, Near HomeTown,
2 Balconies, West Facing Main Door,
Vetrified Tiles Flooring, Borewell Water,
Maintainence Rs 1000 Per Month Excluding Rent,
Move In Charges Rs 500',
(select listing_type_id from listing_type where listing_type_name = 'Rental'),
(select property_type_id from listing_property_type where property_type_name ='Villa'),
12.9524626,77.6977583, 2500, 40000, 400000,
null,
(select furnishing_type_id from listing_furnishing_type where furnishing_type_name = 'Semi Furnished'),
(select bedroom_count_id from listing_bedroom_count where bedroom_count_name = '4+ BHK'),
(select transaction_mode_id from listing_transaction_mode where transaction_mode_name = 'No Preference'),
false,
true,
true) returning listing_id )
insert into listing_amenity (listing_id, fans, geyser, cupboards, chimney, parking, airconditioner, sofa, diningtable, centertable, television, internet, dth, refrigerator, washingmachine, bed)
select inserted.listing_id ,true, true, true, false, true, false, false, false, false, false, false, false, false, false, false from inserted;


with inserted as (
insert into listing (user_id,
listing_title, listing_address,
listing_type_id,
property_type_id, listing_latitude, listing_longitude, listing_area, listing_value, listing_deposit,
apartment_id,
furnishing_type_id,
bedroom_count_id,
transaction_mode_id,
non_veg_allowed,
bachelor_allowed,
listing_active_ind)
values
(
(select user_id from crs_user where user_name = 'Load Test Home Owner')
, '4 BHK Villa In Kadubessnahalli',
'Second Floor, Butterfly Villa, Kadubessnahalli, Near Main Road,
2 Balconies, West Facing Main Door,
Vetrified Tiles Flooring, Borewell Water,
Maintainence Rs 1000 Per Month Excluding Rent,
Move In Charges Rs 500',
(select listing_type_id from listing_type where listing_type_name = 'Rental'),
(select property_type_id from listing_property_type where property_type_name ='Villa'),
12.9374397,77.6970864, 2500, 40000, 400000,
null,
(select furnishing_type_id from listing_furnishing_type where furnishing_type_name = 'Semi Furnished'),
(select bedroom_count_id from listing_bedroom_count where bedroom_count_name = '4+ BHK'),
(select transaction_mode_id from listing_transaction_mode where transaction_mode_name = 'No Preference'),
false,
true,
true) returning listing_id )
insert into listing_amenity (listing_id, fans, geyser, cupboards, chimney, parking, airconditioner, sofa, diningtable, centertable, television, internet, dth, refrigerator, washingmachine, bed)
select inserted.listing_id ,true, true, true, false, true, false, false, false, false, false, false, false, false, false, false from inserted;


with inserted as (
insert into listing (user_id,
listing_title, listing_address,
listing_type_id,
property_type_id, listing_latitude, listing_longitude, listing_area, listing_value, listing_deposit,
apartment_id,
furnishing_type_id,
bedroom_count_id,
transaction_mode_id,
non_veg_allowed,
bachelor_allowed,
listing_active_ind)
values
(
(select user_id from crs_user where user_name = 'Load Test Home Owner')
, '4 BHK Villa In Panthur',
'Second Floor, Vandana Villa, Panthur, Near Railway Station,
2 Balconies, West Facing Main Door,
Vetrified Tiles Flooring, Borewell Water,
Maintainence Rs 1000 Per Month Excluding Rent,
Move In Charges Rs 500',
(select listing_type_id from listing_type where listing_type_name = 'Rental'),
(select property_type_id from listing_property_type where property_type_name ='Villa'),
12.9354609,77.7021243, 2500, 40000, 400000,
null,
(select furnishing_type_id from listing_furnishing_type where furnishing_type_name = 'Semi Furnished'),
(select bedroom_count_id from listing_bedroom_count where bedroom_count_name = '4+ BHK'),
(select transaction_mode_id from listing_transaction_mode where transaction_mode_name = 'No Preference'),
false,
true,
true) returning listing_id )
insert into listing_amenity (listing_id, fans, geyser, cupboards, chimney, parking, airconditioner, sofa, diningtable, centertable, television, internet, dth, refrigerator, washingmachine, bed)
select inserted.listing_id ,true, true, true, false, true, false, false, false, false, false, false, false, false, false, false from inserted;


with inserted as (
insert into listing (user_id,
listing_title, listing_address,
listing_type_id,
property_type_id, listing_latitude, listing_longitude, listing_area, listing_value, listing_deposit,
apartment_id,
furnishing_type_id,
bedroom_count_id,
transaction_mode_id,
non_veg_allowed,
bachelor_allowed,
listing_active_ind)
values
(
(select user_id from crs_user where user_name = 'Load Test Home Owner')
, '4 BHK Villa In Bellandur',
'Second Floor, SLS Splendor, Bellandur, Near Bangalore Central Mall,
2 Balconies, West Facing Main Door,
Vetrified Tiles Flooring, Borewell Water,
Maintainence Rs 1000 Per Month Excluding Rent,
Move In Charges Rs 500',
(select listing_type_id from listing_type where listing_type_name = 'Rental'),
(select property_type_id from listing_property_type where property_type_name ='Villa'),
12.9229338,77.6780734, 2500, 40000, 400000,
null,
(select furnishing_type_id from listing_furnishing_type where furnishing_type_name = 'Semi Furnished'),
(select bedroom_count_id from listing_bedroom_count where bedroom_count_name = '4+ BHK'),
(select transaction_mode_id from listing_transaction_mode where transaction_mode_name = 'No Preference'),
false,
true,
true) returning listing_id )
insert into listing_amenity (listing_id, fans, geyser, cupboards, chimney, parking, airconditioner, sofa, diningtable, centertable, television, internet, dth, refrigerator, washingmachine, bed)
select inserted.listing_id ,true, true, true, false, true, false, false, false, false, false, false, false, false, false, false from inserted;


with inserted as (
insert into listing (user_id,
listing_title, listing_address,
listing_type_id,
property_type_id, listing_latitude, listing_longitude, listing_area, listing_value, listing_deposit,
apartment_id,
furnishing_type_id,
bedroom_count_id,
transaction_mode_id,
non_veg_allowed,
bachelor_allowed,
listing_active_ind)
values
(
(select user_id from crs_user where user_name = 'Load Test Home Owner')
, '4 BHK Villa In Sarjapur Road',
'Second Floor, Salarpuria Sattva Senorita, Sarjapur Road, Near Flyover,
2 Balconies, West Facing Main Door,
Vetrified Tiles Flooring, Borewell Water,
Maintainence Rs 1000 Per Month Excluding Rent,
Move In Charges Rs 500',
(select listing_type_id from listing_type where listing_type_name = 'Rental'),
(select property_type_id from listing_property_type where property_type_name ='Villa'),
12.9048677,77.6834147, 2500, 40000, 400000,
null,
(select furnishing_type_id from listing_furnishing_type where furnishing_type_name = 'Semi Furnished'),
(select bedroom_count_id from listing_bedroom_count where bedroom_count_name = '4+ BHK'),
(select transaction_mode_id from listing_transaction_mode where transaction_mode_name = 'No Preference'),
false,
true,
true) returning listing_id )
insert into listing_amenity (listing_id, fans, geyser, cupboards, chimney, parking, airconditioner, sofa, diningtable, centertable, television, internet, dth, refrigerator, washingmachine, bed)
select inserted.listing_id ,true, true, true, false, true, false, false, false, false, false, false, false, false, false, false from inserted;


-----------------------------------------------------------------------------------------------------------------------------
insert into listing_locality_data
	select listing_id,
	'{
       "categories" : [ {
         "name" : "HOSPITAL",
         "resources" : [ {
           "name" : "Rainbow Children’s Hospital",
           "latitude" : 12.9754132,
           "longitude" : 77.69785019999999,
           "distance" : "4.0 km",
           "duration" : "10 mins"
         }, {
           "name" : "Sri SAI CLINIC",
           "latitude" : 12.9686612,
           "longitude" : 77.705522,
           "distance" : "2.3 km",
           "duration" : "8 mins"
         }, {
           "name" : "Birthright by Rainbow, Marathahalli - Bengaluru",
           "latitude" : 12.9752402,
           "longitude" : 77.6977325,
           "distance" : "4.0 km",
           "duration" : "10 mins"
         }, {
           "name" : "Kamadhenu Swasthya Kendra Speach and Hearing Clinic",
           "latitude" : 12.9729167,
           "longitude" : 77.69839619999999,
           "distance" : "2.6 km",
           "duration" : "7 mins"
         }, {
           "name" : "Dr Veerabhadra V Mallad ( Rainbow Chidren’s Hospital)",
           "latitude" : 12.9756056,
           "longitude" : 77.6974807,
           "distance" : "4.0 km",
           "duration" : "10 mins"
         } ]
       }, {
         "name" : "GROCERY",
         "resources" : [ {
           "name" : "Gateway Super Bazaar",
           "latitude" : 12.9729655,
           "longitude" : 77.7011394,
           "distance" : "0.3 km",
           "duration" : "2 mins"
         }, {
           "name" : "New Plaza Super Market",
           "latitude" : 12.9721342,
           "longitude" : 77.6988097,
           "distance" : "2.5 km",
           "duration" : "7 mins"
         }, {
           "name" : "Big Basket Head Office",
           "latitude" : 12.9745013,
           "longitude" : 77.706845,
           "distance" : "1.1 km",
           "duration" : "4 mins"
         }, {
           "name" : "Happy Mart",
           "latitude" : 12.9723156,
           "longitude" : 77.70714439999999,
           "distance" : "1.9 km",
           "duration" : "7 mins"
         }, {
           "name" : "Keerthanas Mega Bazaar",
           "latitude" : 12.9791,
           "longitude" : 77.695217,
           "distance" : "3.5 km",
           "duration" : "8 mins"
         } ]
       }, {
         "name" : "RESTAURANT",
         "resources" : [ {
           "name" : "Cafe Coffee Day",
           "latitude" : 12.9688886,
           "longitude" : 77.70083079999999,
           "distance" : "2.0 km",
           "duration" : "6 mins"
         }, {
           "name" : "Felicita Cafe",
           "latitude" : 12.979833,
           "longitude" : 77.69351499999999,
           "distance" : "3.6 km",
           "duration" : "9 mins"
         }, {
           "name" : "The Bawarchi",
           "latitude" : 12.969175,
           "longitude" : 77.701248,
           "distance" : "1.5 km",
           "duration" : "5 mins"
         }, {
           "name" : "Jalsa",
           "latitude" : 12.9775135,
           "longitude" : 77.6953771,
           "distance" : "3.2 km",
           "duration" : "8 mins"
         }, {
           "name" : "Meat & Eat",
           "latitude" : 12.9816524,
           "longitude" : 77.696777,
           "distance" : "4.1 km",
           "duration" : "11 mins"
         } ]
       } ]
     }' from listing;
---------------------------------------------------------------------------------
update listing set listing_images = '[
{
"defaultImage" : "images/listing/test_images/house1.jpeg",
"imageSet" : "images/listing/test_images/house1_400.jpeg 400w, images/listing/test_images/house1_600.jpeg 600w, images/listing/test_images/house1_800.jpeg 800w, images/listing/test_images/house1_1000.jpeg 1000w, images/listing/test_images/house1_1200.jpeg 1200w",
"imageName" : "house1"
},
{
"defaultImage" : "images/listing/test_images/house2.jpeg",
"imageSet" : "images/listing/test_images/house2_400.jpeg 400w, images/listing/test_images/house2_600.jpeg 600w, images/listing/test_images/house2_800.jpeg 800w, images/listing/test_images/house2_1000.jpeg 1000w, images/listing/test_images/house2_1200.jpeg 1200w",
"imageName" : "house2"
},
{
"defaultImage" : "images/listing/test_images/house3.jpeg",
"imageSet" : "images/listing/test_images/house3_400.jpeg 400w, images/listing/test_images/house3_600.jpeg 600w, images/listing/test_images/house3_800.jpeg 800w, images/listing/test_images/house3_1000.jpeg 1000w, images/listing/test_images/house3_1200.jpeg 1200w",
"imageName" : "house3"
},
{
"defaultImage" : "images/listing/test_images/house4.jpeg",
"imageSet" : "images/listing/test_images/house4_400.jpeg 400w, images/listing/test_images/house4_600.jpeg 600w, images/listing/test_images/house4_800.jpeg 800w, images/listing/test_images/house4_1000.jpeg 1000w, images/listing/test_images/house4_1200.jpeg 1200w",
"imageName" : "house4"
}
]';
----------------------------------------------------------------------
