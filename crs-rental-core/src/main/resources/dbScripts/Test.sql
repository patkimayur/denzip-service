set schema 'crs_schema_performance';

alter table listing add column listing_virtual_presence boolean default false;

delete from razorpay_successful_order;
delete from listing_user_razorpay_order;

select * from razorpay_successful_order;

select * from crs_user order by created_time desc;

select bedroom_count_name from listing_bedroom_count;

select * from crs_user where user_mobile not ilike '% %';
select * from listing where created_by_user_id = 'a8353139-2b86-4453-a698-a10b114e6f0e' order by created_time desc;
--delete from apartment_amenity;
select * from broker_org_details;
select * from broker_org_user_mapping;


select * from apartment order by created_time desc;

select * from listing where apartment_id is not null order by created_time desc;

update crs_user set user_mobile = CONCAT('91',' ',user_mobile) where user_mobile not ilike '% %';
update crs_user set user_mobile = '1 7205611182' where user_mobile = '+1 7205611182';

select broker_org_details.broker_org_id, broker_org_name, broker_org_address, broker_org_mobile, broker_org_email
from broker_org_details, broker_org_user_mapping, crs_user
where crs_user.user_id = 'a8353139-2b86-4453-a698-a10b114e6f0e'
and crs_user.user_id = broker_org_user_mapping.user_id
and broker_org_user_mapping.broker_org_id = broker_org_details.broker_org_id;

insert into broker_org_user_mapping values('5d965882-7f73-472d-a2c5-eaf3c6d1c182', 'a8353139-2b86-4453-a698-a10b114e6f0e');

select * from listing where created_time > now() - interval '7 days'
and listing_active_ind = true
order by created_time desc;

insert into apartment_amenity (apartment_id) select apartment_id from apartment;

insert into listing_amenity (listing_id) select listing_id from listing on conflict (listing_id) do nothing;




select * from apartment_amenity;

select * from listing_amenity;

select row_to_json(apartment_amenity.*) as apartment_amenities from apartment_amenity limit 1;

SELECT json_object_agg (column_name,false) as apartment_amenities  FROM information_schema.columns WHERE  table_name  = 'apartment_amenity';


select row_to_json(listing_amenity.*) as listing_amenities from listing_amenity limit 1;

SELECT json_object_agg (column_name,false) as listing_amenities  FROM information_schema.columns WHERE  table_name  = 'listing_amenity';
select * from crs_user where user_id = '78d95a17-a0af-4edd-a8f3-8a8ced7aa840';
select * from apartment order by created_time desc;
select * from listing_amenity  order by created_time desc;

select * from listing where listing_id in ('df03a6e8-53ea-48a6-ae10-3448befb37b7','115c5312-54ca-483a-be8d-59f6dd3e3a8d'); --'a2799fd5-351b-4ef9-b9d6-520e2e5d4ae3';

select * from tag;
select * from listing_tag_mapping;
select * from user_tag_mapping;
select * from listing_notification_status;
select * from user_notification_status;

select * from listing_user_schedule_mapping where schedule_status = 'PROSPECTIVE';
delete from listing_user_schedule_mapping where schedule_status = 'PROSPECTIVE';
select * from listing_user_schedule_mapping where user_id = '78d95a17-a0af-4edd-a8f3-8a8ced7aa840';

select * from user_tag_mapping where user_id = '78d95a17-a0af-4edd-a8f3-8a8ced7aa840';
select * from crs_user where user_id = '78d95a17-a0af-4edd-a8f3-8a8ced7aa840'

select * from listing_tag_mapping where listing_id = '115c5312-54ca-483a-be8d-59f6dd3e3a8d';

update listing_notification_status set notification_sent = false;

select crs_user.user_id, crs_user.user_name, crs_user.user_mobile, crs_user.user_email,
array_agg(listing_tag_mapping.listing_id) as listing_ids
from listing_notification_status, listing_tag_mapping, user_tag_mapping, crs_user
where
listing_notification_status.notification_sent = false
and listing_notification_status.listing_id = listing_tag_mapping.listing_id
and listing_tag_mapping.tag_id = user_tag_mapping.tag_id
and user_tag_mapping.user_id = crs_user.user_id
group by crs_user.user_id;

select crs_user.user_id, crs_user.user_name, crs_user.user_mobile, crs_user.user_email,
array_agg(listing_tag_mapping.listing_id) as listing_ids
from listing_notification_status,
listing_tag_mapping left join user_tag_mapping on listing_tag_mapping.tag_id = user_tag_mapping.tag_id
left join crs_user on user_tag_mapping.user_id = crs_user.user_id
where
listing_notification_status.notification_sent = false
and listing_notification_status.listing_id = listing_tag_mapping.listing_id
group by crs_user.user_id;

select broker_org_details.broker_org_id, broker_org_details.broker_org_name, 
broker_org_details.broker_org_mobile, broker_org_details.broker_org_email,
array_agg(tag.tag_name) as tag_names, array_agg(user_tag_mapping.user_id) as user_ids
from user_notification_status, user_tag_mapping, broker_org_tag_mapping, broker_org_details, tag
where user_notification_status.notification_sent = false
and user_notification_status.user_id = user_tag_mapping.user_id
and user_tag_mapping.tag_id = broker_org_tag_mapping.tag_id
and broker_org_tag_mapping.broker_org_id = broker_org_details.broker_org_id
and broker_org_tag_mapping.tag_id = tag.tag_id
group by broker_org_details.broker_org_id;

insert into broker_org_details (broker_org_name, broker_org_address, broker_org_mobile, broker_org_email)
values (
'Broker Org2', 'Broker Org2 Address', '9740244634', 'yogeshj.jain@gmail.com');

select * from broker_org_details;

SELECT unnest(enum_range(NULL::schedule_status));  


insert into broker_org_tag_mapping (broker_org_id, tag_id)
values 
('2bef96ad-e741-4aa6-9e10-c4254bf69965', '72c3cd18-f1b9-4287-90c8-6c9a84dc46d7'),
 ('2bef96ad-e741-4aa6-9e10-c4254bf69965', 'ea576b22-d1f6-4db7-a64b-c1493c545898'),
 ('2bef96ad-e741-4aa6-9e10-c4254bf69965', '2e492f50-6f1b-45c2-8ab7-4eb80e5bf053');
 
 select * from broker_org_tag_mapping;


select * from listing where listing_active_ind = true order by created_time desc;
update listing set listing_active_ind = true, last_activation_time=now() where listing_id = '1300244e-a939-40de-ad39-752a37b7faf5';

update listing set listing_active_ind = false where listing_id = '1300244e-a939-40de-ad39-752a37b7faf5';
update listing set listing_active_ind = false where listing_id = '6760b1d7-e171-4382-8b4f-e26638d318a9';

update listing_user_schedule_mapping set schedule_status = 'SYSTEM_DEACTIVATED' where listing_id = '1300244e-a939-40de-ad39-752a37b7faf5';
update listing_user_schedule_mapping set schedule_status = 'SYSTEM_DEACTIVATED' where listing_id = '6760b1d7-e171-4382-8b4f-e26638d318a9';



update listing set listing_active_ind = true where listing_id = '8c4864dd-a27f-4142-bafa-7cc508022ec1';
update listing_user_schedule_mapping set schedule_status = 'ACTIVATED' where listing_id = '8c4864dd-a27f-4142-bafa-7cc508022ec1';
select * from listing_user_schedule_mapping where listing_id = '8c4864dd-a27f-4142-bafa-7cc508022ec1';

select * from listing, listing_user_schedule_mapping where listing_active_ind =false 
and listing.created_time < now() - interval '30 days'
and listing.listing_id = listing_user_schedule_mapping.listing_id
and listing.user_id = listing_user_schedule_mapping.user_id
order by listing.created_time desc;

select * from listing_user_schedule_mapping;
select * from deactivated_owner_listing_details;

with updated as (
update listing set listing_active_ind = false where listing_active_ind =true 
and last_activation_time < now() - interval '30 days' returning listing_id, user_id)
update listing_user_schedule_mapping set schedule_status = 'SYSTEM_DEACTIVATED'
from updated, crs_user
where listing_user_schedule_mapping.listing_id = updated.listing_id 
and listing_user_schedule_mapping.user_id = updated.user_id
and listing_user_schedule_mapping.user_id = crs_user.user_id
returning listing_user_schedule_mapping.listing_id, listing_user_schedule_mapping.user_id,
crs_user.user_mobile, crs_user.user_email;

with selected as (
select listing_id, user_id from listing where listing_active_ind =true 
and last_activation_time < now() - interval '20 days')
update listing_user_schedule_mapping set schedule_status = 'SYSTEM_PENDING_DEACTIVATION'
from selected, crs_user
where listing_user_schedule_mapping.listing_id = selected.listing_id 
and listing_user_schedule_mapping.user_id = selected.user_id 
and listing_user_schedule_mapping.user_id = crs_user.user_id
returning listing_user_schedule_mapping.listing_id, listing_user_schedule_mapping.user_id,
crs_user.user_mobile, crs_user.user_email;



select * from listing_locality_data order by created_time desc;
select * from listing_amenity where listing_id = '4f82a6bc-a08b-470c-9373-4d2a9da3176e';


--delete from listing where listing_id = 'e0fc6463-961e-4789-bbb2-44cc76421cd8';
--delete from listing_amenity where listing_id = '312d1783-edaa-474f-952a-300098ca8e64';


2019-03-15 22:31:47.307201+05:30
2019-03-21 18:30:28.715017+00

select * from authority;
select * from crs_user;
select * from oauth_client_details;
select * from oauth_client_token;
select * from oauth_access_token;
select * from oauth_approvals;
select * from oauth_code;
select * from oauth_refresh_token;
select * from listing order by created_time desc;

update listing set created_by_user_id = 'aed9a2ae-f20d-4450-bfd4-e5b0e9635f2e';
ALTER TABLE listing ALTER COLUMN created_by_user_id SET NOT NULL;
update crs_user set authorities = '{3}' where user_mobile = '9837285284';
update crs_user set authorities = '{2}' where user_mobile = '8197289699';



with updated as
(update listing set (listing_title, listing_area, last_modified_time) = ('updated listing', 600, now())
where listing_id = 'b60518f3-67a4-4831-af74-53bbd9807493' returning listing_id) update listing_amenity
set (geyser, last_modified_time) = (select true, now() from updated)
where listing_id = (select updated.listing_id  from updated)
returning listing_id;

select * from listing order by created_time desc;

update listing set listing_title = '3 BHK Row House In Mahadevpura' where listing_id = 'b95fe97a-83f5-4068-9e9e-b20de98c810e';
update listing set listing_latitude = 12.9580667 where listing_id = '8737aeeb-c39f-42f2-b021-6e01b7c08e56';


select * from listing_transaction_mode;
select l.* from listing l, listing_transaction_mode ltm
where ltm.transaction_mode_name = 'No Preference'
and ltm.transaction_mode_id = l.transaction_mode_id;

select * from listing where listing_id = 'b5a5c93a-c851-45d8-b7dc-1fecaa0a0efc';

update listing set transaction_mode_id = 'ef834bc0-3a96-47cf-b1bf-9dfe9f5f3935' 
where listing_id = '93d2100a-ad4f-4f21-ba4f-20b829430ba4';

select * from listing where transaction_mode_id = 'ef834bc0-3a96-47cf-b1bf-9dfe9f5f3935';

select * from user_request_callback;

select * from listing_user_schedule_mapping lusm where listing_id = '8c01abec-e1eb-4195-b16a-76c5851de026';

CREATE TABLE if not exists crs_schema_performance.deactivated_owner_listing_details
(
    owner_id UUID NOT NULL REFERENCES crs_schema_performance.crs_user (user_id),
    listing_id UUID NOT NULL REFERENCES crs_schema_performance.listing (listing_id),
    tenant_user_id UUID REFERENCES crs_schema_performance.crs_user (user_id),
    cashback_status TEXT NOT NULL,
    deactivation_type TEXT NOT NULL,
    created_time TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
    CONSTRAINT deactivated_owner_listing_pk PRIMARY KEY (owner_id, listing_id)
);

select * from deactivated_owner_listing_details;

select locality_data from listing_locality_data where listing_id = 'ead5c4c0-7930-443d-829b-8d40c169cfa4';

select user_id, authorities, user_secret, user_name, user_mobile, user_email, user_acceptance_doc, user_govt_id_no, user_govt_id_image, user_external_ind, 
user_pref_comm_mode, user_pref_comm_freq, user_pref_visit_slot from crs_user where user_mobile= '8197289699';

alter table listing alter column listing_images type TEXT;

select bedroom_count_name from listing_bedroom_count;

create table if not exists test_apartment (
apartment_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
apartment_name TEXT NOT NULL,
apartment_latitude NUMERIC NOT NULL,
apartment_longitude NUMERIC NOT Null,
created_time TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL
);

insert into test_apartment (apartment_name, apartment_latitude, apartment_longitude) values ('Alpine Eco', 12.973675, 77.700803);

select * from test_apartment;

select * from oauth_client_details;
update oauth_client_details set refresh_token_validity = 2592000;

CREATE INDEX listing_long_idx ON listing (listing_longitude);



select l.listing_id, l.listing_title, l.listing_address, 
--lt.listing_type_name, 
listing_property_type.property_type_name, 
l.listing_images, l.listing_latitude, l.listing_longitude,
l.listing_area, l.listing_value, l.listing_deposit, l.listing_active_ind, 
--listing_furnishing_type.furnishing_type_name, listing_transaction_mode.transaction_mode_name,
l.non_veg_allowed, l.bachelor_allowed
 , (select ARRAY(SELECT authority.name FROM authority, crs_user where l.created_by_user_id = crs_user.user_id and authority.id = ANY(crs_user.authorities) )) as created_user_authorities
--, 
--listing_bedroom_count.bedroom_count_name 
--,
--row_to_json(la.*) as listing_amenties,
--a.apartment_id, a.apartment_name, row_to_json(aa.*) as apartment_amenities,
--(select count(lusm.tenant_user_id) from listing_user_schedule_mapping lusm where l.listing_id = lusm.listing_id) as user_interested_count  
from listing l 
,
--LEFT JOIN apartment a ON l.apartment_id = a.apartment_id 
--LEFT JOIN apartment_amenity aa on a.apartment_id = aa.apartment_id,
--listing_amenity la, 
--listing_type lt, 
listing_property_type
--, 
--listing_furnishing_type, listing_transaction_mode, listing_bedroom_count  
where 
 l.created_by_user_id = 'a8353139-2b86-4453-a698-a10b114e6f0e'
 --l.listing_latitude between 
--11.954001365340858 and 11.990176234659142 
--2.973675 and 3.973675
--and l.listing_longitude 
--between 
--76.67592726233295 and 76.71280073766695 
--67.700803 and 67.770803
--and listing_bedroom_count.bedroom_count_name in ('4+ BHK')
and  
--l.listing_type_id = lt.listing_type_id and 
l.property_type_id = listing_property_type.property_type_id 
--and 
--l.furnishing_type_id = listing_furnishing_type.furnishing_type_id and 
--l.transaction_mode_id = listing_transaction_mode.transaction_mode_id and 
--l.bedroom_count_id = listing_bedroom_count.bedroom_count_id 
--and l.listing_id = la.listing_id 
 --and l.created_by_user_id = crs_user.user_id
 --and authority.id = ANY(crs_user.authorities);
;

--latitude"2.973675","112.973675"
--longitude"67.700803","177.700803"
select min(listing_latitude) from listing;

select * from crs_schema.listing_locality_data;

--{
  "categories" : [ {
    "name" : "HOSPITAL",
    "resources" : [ {
      "name" : "My Dental World",
      "latitude" : 12.9728322,
      "longitude" : 77.7016297,
      "distance" : "0.2 km",
      "duration" : "1 min"
    }, {
      "name" : "Kamadhenu Swasthya Kendra Speach and Hearing Clinic",
      "latitude" : 12.9729167,
      "longitude" : 77.69839619999999,
      "distance" : "2.4 km",
      "duration" : "7 mins"
    }, {
      "name" : "Rainbow Children’s Hospital",
      "latitude" : 12.9754132,
      "longitude" : 77.69785019999999,
      "distance" : "3.8 km",
      "duration" : "9 mins"
    }, {
      "name" : "ದೊಡ್ಡನೆಕ್ಕುಂದಿ",
      "latitude" : 12.9771994,
      "longitude" : 77.70189409999999,
      "distance" : "3.9 km",
      "duration" : "11 mins"
    }, {
      "name" : "sri channigaraya swamy medicals",
      "latitude" : 12.973251,
      "longitude" : 77.696277,
      "distance" : "2.7 km",
      "duration" : "9 mins"
    } ]
  }, {
    "name" : "GROCERY",
    "resources" : [ {
      "name" : "Gateway Super Bazaar",
      "latitude" : 12.9729655,
      "longitude" : 77.7011394,
      "distance" : "71 m",
      "duration" : "1 min"
    }, {
      "name" : "New Plaza Super Market",
      "latitude" : 12.9721342,
      "longitude" : 77.6988097,
      "distance" : "2.3 km",
      "duration" : "6 mins"
    }, {
      "name" : "Big Basket Head Office",
      "latitude" : 12.9745013,
      "longitude" : 77.706845,
      "distance" : "0.9 km",
      "duration" : "3 mins"
    }, {
      "name" : "Chinnappanhalli Main Road",
      "latitude" : 12.9723443,
      "longitude" : 77.7070592,
      "distance" : "1.7 km",
      "duration" : "6 mins"
    }, {
      "name" : "Happy Mart",
      "latitude" : 12.9723156,
      "longitude" : 77.70714439999999,
      "distance" : "1.7 km",
      "duration" : "6 mins"
    } ]
  }, {
    "name" : "RESTAURANT",
    "resources" : [ {
      "name" : "HashTag Kitchen",
      "latitude" : 12.9735544,
      "longitude" : 77.701555,
      "distance" : "0.2 km",
      "duration" : "2 mins"
    }, {
      "name" : "Kolkata Kathi Rolls",
      "latitude" : 12.9728834,
      "longitude" : 77.70113359999999,
      "distance" : "81 m",
      "duration" : "1 min"
    }, {
      "name" : "SRI GANESH FRUIT JUICE CENTRE",
      "latitude" : 12.972982,
      "longitude" : 77.701509,
      "distance" : "0.2 km",
      "duration" : "1 min"
    }, {
      "name" : "BBQ Ride India",
      "latitude" : 12.9726609,
      "longitude" : 77.70086669999999,
      "distance" : "0.1 km",
      "duration" : "1 min"
    }, {
      "name" : "Ambur Hot Dum Biryani",
      "latitude" : 12.9728671,
      "longitude" : 77.70144499999999,
      "distance" : "0.1 km",
      "duration" : "1 min"
    } ]
  } ]
}
 
 update apartment set apartment_images = '[ {
  "defaultImage" : "images/apartment/9f20c3ab-1dcc-4b68-a63d-202ecfdc4f73/PkRkzD.jpeg",
  "imageSet" : "images/apartment/9f20c3ab-1dcc-4b68-a63d-202ecfdc4f73/PkRkzD_400.jpeg 400w, images/apartment/9f20c3ab-1dcc-4b68-a63d-202ecfdc4f73/PkRkzD_600.jpeg 600w, images/apartment/9f20c3ab-1dcc-4b68-a63d-202ecfdc4f73/PkRkzD_800.jpeg 800w, images/apartment/9f20c3ab-1dcc-4b68-a63d-202ecfdc4f73/PkRkzD_1000.jpeg 1000w, images/apartment/9f20c3ab-1dcc-4b68-a63d-202ecfdc4f73/PkRkzD_1200.jpeg 1200w",
  "imageName" : "house1"
}, {
  "defaultImage" : "images/apartment/9f20c3ab-1dcc-4b68-a63d-202ecfdc4f73/eIAUiD.jpeg",
  "imageSet" : "images/apartment/9f20c3ab-1dcc-4b68-a63d-202ecfdc4f73/eIAUiD_400.jpeg 400w, images/apartment/9f20c3ab-1dcc-4b68-a63d-202ecfdc4f73/eIAUiD_600.jpeg 600w, images/apartment/9f20c3ab-1dcc-4b68-a63d-202ecfdc4f73/eIAUiD_800.jpeg 800w, images/apartment/9f20c3ab-1dcc-4b68-a63d-202ecfdc4f73/eIAUiD_1000.jpeg 1000w, images/apartment/9f20c3ab-1dcc-4b68-a63d-202ecfdc4f73/eIAUiD_1200.jpeg 1200w",
  "imageName" : "house2"
}, {
  "defaultImage" : "images/apartment/9f20c3ab-1dcc-4b68-a63d-202ecfdc4f73/Tp0ePO.jpeg",
  "imageSet" : "images/apartment/9f20c3ab-1dcc-4b68-a63d-202ecfdc4f73/Tp0ePO_400.jpeg 400w, images/apartment/9f20c3ab-1dcc-4b68-a63d-202ecfdc4f73/Tp0ePO_600.jpeg 600w, images/apartment/9f20c3ab-1dcc-4b68-a63d-202ecfdc4f73/Tp0ePO_800.jpeg 800w, images/apartment/9f20c3ab-1dcc-4b68-a63d-202ecfdc4f73/Tp0ePO_1000.jpeg 1000w, images/apartment/9f20c3ab-1dcc-4b68-a63d-202ecfdc4f73/Tp0ePO_1200.jpeg 1200w",
  "imageName" : "house3"
}, {
  "defaultImage" : "images/apartment/9f20c3ab-1dcc-4b68-a63d-202ecfdc4f73/weGSnM.jpeg",
  "imageSet" : "images/apartment/9f20c3ab-1dcc-4b68-a63d-202ecfdc4f73/weGSnM_400.jpeg 400w, images/apartment/9f20c3ab-1dcc-4b68-a63d-202ecfdc4f73/weGSnM_600.jpeg 600w, images/apartment/9f20c3ab-1dcc-4b68-a63d-202ecfdc4f73/weGSnM_800.jpeg 800w, images/apartment/9f20c3ab-1dcc-4b68-a63d-202ecfdc4f73/weGSnM_1000.jpeg 1000w, images/apartment/9f20c3ab-1dcc-4b68-a63d-202ecfdc4f73/weGSnM_1200.jpeg 1200w",
  "imageName" : "house4"
} ]' where apartment_images is null;

select * from listing;
update listing set listing_images = '[
{
"defaultImage" : "images/house1.jpeg",
"imageSet" : "images/house1_400.jpeg 400w, images/house1_600.jpeg 600w, images/house1_800.jpeg 800w, images/house1_1000.jpeg 1000w, images/house1_1200.jpeg 1200w"
},
{
"defaultImage" : "images/house2.jpeg",
"imageSet" : "images/house2_400.jpeg 400w, images/house2_600.jpeg 600w, images/house2_800.jpeg 800w, images/house2_1000.jpeg 1000w, images/house2_1200.jpeg 1200w"
},
{
"defaultImage" : "images/house3.jpeg",
"imageSet" : "images/house3_400.jpeg 400w, images/house3_600.jpeg 600w, images/house3_800.jpeg 800w, images/house3_1000.jpeg 1000w, images/house3_1200.jpeg 1200w"
},
{
"defaultImage" : "images/house4.jpeg",
"imageSet" : "images/house4_400.jpeg 400w, images/house4_600.jpeg 600w, images/house4_800.jpeg 800w, images/house4_1000.jpeg 1000w, images/house4_1200.jpeg 1200w"
}
]';



truncate  listing cascade;

select * from listing_locality_data where listing_id = '0f6bed37-01d8-4a2a-ad22-abf713bb7e0d';

insert into listing_locality_data
	select listing_id,
	'{
  "categories" : [ {
    "name" : "HOSPITAL",
    "resources" : [ {
      "name" : "My Dental World",
      "latitude" : 12.9728322,
      "longitude" : 77.7016297,
      "distance" : "0.2 km",
      "duration" : "1 min"
    }, {
      "name" : "Kamadhenu Swasthya Kendra Speach and Hearing Clinic",
      "latitude" : 12.9729167,
      "longitude" : 77.69839619999999,
      "distance" : "2.4 km",
      "duration" : "7 mins"
    }, {
      "name" : "Rainbow Children’s Hospital",
      "latitude" : 12.9754132,
      "longitude" : 77.69785019999999,
      "distance" : "3.8 km",
      "duration" : "9 mins"
    }, {
      "name" : "ದೊಡ್ಡನೆಕ್ಕುಂದಿ",
      "latitude" : 12.9771994,
      "longitude" : 77.70189409999999,
      "distance" : "3.9 km",
      "duration" : "11 mins"
    }, {
      "name" : "sri channigaraya swamy medicals",
      "latitude" : 12.973251,
      "longitude" : 77.696277,
      "distance" : "2.7 km",
      "duration" : "9 mins"
    } ]
  }, {
    "name" : "GROCERY",
    "resources" : [ {
      "name" : "Gateway Super Bazaar",
      "latitude" : 12.9729655,
      "longitude" : 77.7011394,
      "distance" : "71 m",
      "duration" : "1 min"
    }, {
      "name" : "New Plaza Super Market",
      "latitude" : 12.9721342,
      "longitude" : 77.6988097,
      "distance" : "2.3 km",
      "duration" : "6 mins"
    }, {
      "name" : "Big Basket Head Office",
      "latitude" : 12.9745013,
      "longitude" : 77.706845,
      "distance" : "0.9 km",
      "duration" : "3 mins"
    }, {
      "name" : "Chinnappanhalli Main Road",
      "latitude" : 12.9723443,
      "longitude" : 77.7070592,
      "distance" : "1.7 km",
      "duration" : "6 mins"
    }, {
      "name" : "Happy Mart",
      "latitude" : 12.9723156,
      "longitude" : 77.70714439999999,
      "distance" : "1.7 km",
      "duration" : "6 mins"
    } ]
  }, {
    "name" : "RESTAURANT",
    "resources" : [ {
      "name" : "HashTag Kitchen",
      "latitude" : 12.9735544,
      "longitude" : 77.701555,
      "distance" : "0.2 km",
      "duration" : "2 mins"
    }, {
      "name" : "Kolkata Kathi Rolls",
      "latitude" : 12.9728834,
      "longitude" : 77.70113359999999,
      "distance" : "81 m",
      "duration" : "1 min"
    }, {
      "name" : "SRI GANESH FRUIT JUICE CENTRE",
      "latitude" : 12.972982,
      "longitude" : 77.701509,
      "distance" : "0.2 km",
      "duration" : "1 min"
    }, {
      "name" : "BBQ Ride India",
      "latitude" : 12.9726609,
      "longitude" : 77.70086669999999,
      "distance" : "0.1 km",
      "duration" : "1 min"
    }, {
      "name" : "Ambur Hot Dum Biryani",
      "latitude" : 12.9728671,
      "longitude" : 77.70144499999999,
      "distance" : "0.1 km",
      "duration" : "1 min"
    } ]
  } ]
}
' from listing;
			