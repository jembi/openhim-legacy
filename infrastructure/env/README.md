OpenHIM Puppet Script
=====================

Stand-alone script usage:
*	Copy the following files to your server:
	```
	create_database.sql
	ldap-auth-pre-prod.ldif
	```
	and a copy of the OpenHIM build:
	```
	openhim-0.1.0-SNAPSHOT.zip
	```
*	Install puppet: `sudo apt-get install puppet`
*	Install the example openldap puppet module (https://github.com/example42/puppet-modules/tree/master/openldap)
	In option is to extract `openldap.zip` into `/etc/puppet/modules/`
*	Set the passwords in `openhim.pp`
*	`sudo puppet apply openhim.pp`
