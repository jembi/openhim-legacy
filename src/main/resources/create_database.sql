create database interoperability_layer;

use interoperability_layer;

create table status (
	id int PRIMARY KEY NOT NULL AUTO_INCREMENT,
	description text
) CHARSET=UTF8;

insert into status values ('1', 'Processing');
insert into status values ('2', 'Completed');
insert into status values ('3', 'Error');

create table transaction_log (
	id int PRIMARY KEY NOT NULL AUTO_INCREMENT,
	uuid varchar(36) NOT NULL,
	path text NOT NULL,
	request_params text,
	body text,
	http_method text NOT NULL,
	resp_status int,
	resp_body text,
	recieved_timestamp datetime NOT NULL,
	responded_timestamp datetime,
	authorized_username text,
	error_description text,
	error_stacktrace text,
	status int NOT NULL,
	CONSTRAINT `Status of the transaction` FOREIGN KEY (`status`) REFERENCES `status` (`id`)
) CHARSET=UTF8;
