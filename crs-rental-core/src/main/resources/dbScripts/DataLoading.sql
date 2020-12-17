set schema 'crs_schema';

--select count(listing_id) from listing;

insert into apartment (apartment_name, apartment_latitude, apartment_longitude) values ('Alpine Eco', 12.973675, 77.700803);
insert into apartment (apartment_name, apartment_latitude, apartment_longitude) values ('Rohan Vasanta', 12.958563, 77.706851);
insert into apartment (apartment_name, apartment_latitude, apartment_longitude) values ('Knights Bridge', 12.967944, 77.716309);
insert into apartment (apartment_name, apartment_latitude, apartment_longitude) values ('Sri Padmavati Apt', 12.954198, 77.648895);
insert into apartment (apartment_name, apartment_latitude, apartment_longitude) values ('Shobha Indraprasth', 12.992059, 77.555999);

----------------------------------------------------------
insert into apartment_amenity (apartment_id,
powerbackup, lift, security, playarea, clubhouse, stp, wtp, visitorparking, swimmingpool, gym, lawntennis, snooker, tabletennis, basketball)
values (
(select apartment_id from apartment where apartment_name = 'Alpine Eco'),
true, true, true, true, true, true, true, false, true, true, true, true, true, true
);

insert into apartment_amenity (apartment_id,
powerbackup, lift, security, playarea, clubhouse, stp, wtp, visitorparking, swimmingpool, gym, lawntennis, snooker, tabletennis, basketball)
values (
(select apartment_id from apartment where apartment_name = 'Rohan Vasanta'),
true, true, true, true, true, true, true, true, true, true, true, true, true, true
);

insert into apartment_amenity (apartment_id,
powerbackup, lift, security, playarea, clubhouse, stp, wtp, visitorparking, swimmingpool, gym, lawntennis, snooker, tabletennis, basketball)
values (
(select apartment_id from apartment where apartment_name = 'Knights Bridge'),
true, true, true, true, true, false, false, true, true, true, true, false, true, true
);

insert into apartment_amenity (apartment_id,
powerbackup, lift, security, playarea, clubhouse, stp, wtp, visitorparking, swimmingpool, gym, lawntennis, snooker, tabletennis, basketball)
values (
(select apartment_id from apartment where apartment_name = 'Sri Padmavati Apt'),
false, true, true, false, false, false, false, false, false, false, false, false, false, false
);

insert into apartment_amenity (apartment_id,
powerbackup, lift, security, playarea, clubhouse, stp, wtp, visitorparking, swimmingpool, gym, lawntennis, snooker, tabletennis, basketball)
values (
(select apartment_id from apartment where apartment_name = 'Shobha Indraprasth'),
true, true, true, true, true, true, true, true, true, true, true, true, true, true
);
---------------------------------------------------------
insert into crs_user (user_name, user_mobile, user_email, user_govt_id_no, user_external_ind, user_pref_comm_mode, user_pref_comm_freq)
values
('Load Test Home Owner', '0123456789', null, null, 'Facebook', null, null);
---------------------------------------------------------
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
, '2 BHK Apartment In Alpine Eco, Doddanekundi', 'Alpine Eco, Doddanekundi, Near Rainbow Hospital, Outer Ring Road',
(select listing_type_id from listing_type where listing_type_name = 'Rental'),
(select property_type_id from listing_property_type where property_type_name ='Apartment'),
12.973675, 77.700803, 1150, 23000, 200000,
(select apartment_id from apartment where apartment_name = 'Alpine Eco'),
(select furnishing_type_id from listing_furnishing_type where furnishing_type_name = 'Fully Furnished'),
(select bedroom_count_id from listing_bedroom_count where bedroom_count_name = '2 BHK'),
(select transaction_mode_id from listing_transaction_mode where transaction_mode_name = 'Online Transfers'),
false,
true,
true) returning listing_id )
insert into listing_amenity (listing_id, fans, geyser, cupboards, chimney, parking, airconditioner, sofa, diningtable, centertable, television, internet, dth, refrigerator, washingmachine, bed)
select inserted.listing_id ,true, true, true, true, true, true, false, false, false, false, true, false, false, false, false from inserted;

--------------------------------------------------------------
do $$
begin
for r in 1..1000 loop

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
, (select CONCAT('Test Apartment 0 - 1K: ', r)), 'Alpine Eco, Doddanekundi, Near Rainbow Hospital, Outer Ring Road',
(select listing_type_id from listing_type where listing_type_name = 'Rental'),
(select property_type_id from listing_property_type where property_type_name ='Apartment'),
12.973675 + (.001*r), 77.700803 - (.001*r), 1150, 73000-r, 700000-(10*r),
(select apartment_id from apartment where apartment_name = 'Alpine Eco'),
--null,
(select furnishing_type_id from listing_furnishing_type where furnishing_type_name = 'Non Furnished'),
(select bedroom_count_id from listing_bedroom_count where bedroom_count_name = '1 BHK'),
(select transaction_mode_id from listing_transaction_mode where transaction_mode_name = 'Cash'),
false,
true,
true) returning listing_id )
insert into listing_amenity (listing_id, fans, geyser, cupboards, chimney, parking, airconditioner, sofa, diningtable, centertable, television, internet, dth, refrigerator, washingmachine, bed)
select inserted.listing_id ,true, true, true, true, true, true, false, false, false, false, true, false, false, false, false from inserted;

end loop;
end;
$$;
--------------------------------------------------------------------
do $$
begin
for r in 1..1000 loop

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
, (select CONCAT('Test Apartment 1K - 2K: ', r)), 'Alpine Eco, Doddanekundi, Near Rainbow Hospital, Outer Ring Road',
(select listing_type_id from listing_type where listing_type_name = 'Rental'),
(select property_type_id from listing_property_type where property_type_name ='Apartment'),
12.973675 - (.001*r), 77.700803 + (.001*r), 1450, 100000-r, 500000-(10*r),
(select apartment_id from apartment where apartment_name = 'Alpine Eco'),
--null,
(select furnishing_type_id from listing_furnishing_type where furnishing_type_name = 'Fully Furnished'),
(select bedroom_count_id from listing_bedroom_count where bedroom_count_name = '2 BHK'),
(select transaction_mode_id from listing_transaction_mode where transaction_mode_name = 'Online Transfers'),
false,
true,
true) returning listing_id )
insert into listing_amenity (listing_id, fans, geyser, cupboards, chimney, parking, airconditioner, sofa, diningtable, centertable, television, internet, dth, refrigerator, washingmachine, bed)
select inserted.listing_id ,true, true, true, true, true, true, false, false, false, false, true, false, false, false, false from inserted;

end loop;
end;
$$;
----------------------------------------------------------	
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
"defaultImage" : "images/test_images/house1.jpeg",
"imageSet" : "images/test_images/house1_400.jpeg 400w, images/test_images/house1_600.jpeg 600w, images/test_images/house1_800.jpeg 800w, images/test_images/house1_1000.jpeg 1000w, images/test_images/house1_1200.jpeg 1200w",
"imageName" : "house1"
},
{
"defaultImage" : "images/test_images/house2.jpeg",
"imageSet" : "images/test_images/house2_400.jpeg 400w, images/test_images/house2_600.jpeg 600w, images/test_images/house2_800.jpeg 800w, images/test_images/house2_1000.jpeg 1000w, images/test_images/house2_1200.jpeg 1200w",
"imageName" : "house2"
},
{
"defaultImage" : "images/test_images/house3.jpeg",
"imageSet" : "images/test_images/house3_400.jpeg 400w, images/test_images/house3_600.jpeg 600w, images/test_images/house3_800.jpeg 800w, images/test_images/house3_1000.jpeg 1000w, images/test_images/house3_1200.jpeg 1200w",
"imageName" : "house3"
},
{
"defaultImage" : "images/test_images/house4.jpeg",
"imageSet" : "images/test_images/house4_400.jpeg 400w, images/test_images/house4_600.jpeg 600w, images/test_images/house4_800.jpeg 800w, images/test_images/house4_1000.jpeg 1000w, images/test_images/house4_1200.jpeg 1200w",
"imageName" : "house4"
}
]';
----------------------------------------------------------------------
