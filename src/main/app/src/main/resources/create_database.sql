create database interoperability_layer;

use interoperability_layer;

create table inbound_messages (id int PRIMARY KEY NOT NULL AUTO_INCREMENT, payload text, timestamp datetime);
