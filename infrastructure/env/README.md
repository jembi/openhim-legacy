OpenHIM Puppet Script
=====================

Stand-alone script usage:
*	Copy the following files to your server:
	```
	create_database.sql

	update_database_*.sql

	ldap-auth-pre-prod.ldif
	```
	
	located in `src/main/resources`, as well as a copy of the OpenHIM build:
	
	```
	openhim-0.1.0-SNAPSHOT.zip
	```
*	Install puppet: `sudo apt-get install puppet`
*	Install the example openldap puppet module (https://github.com/example42/puppet-modules/tree/master/openldap)
	*	On older versions of puppet, one option would be to extract `openldap.zip` into `/etc/puppet/modules/`
*	Set the passwords in `openhim.pp`
	*	The passwords set here correspond to passwords in `HIM-core.properties`
	*	The `$openldap_rootpw` is a hash of whatever is chosen for `$ldap_pass`. The `slappasswd` utility can be used to generate this hash (`slappasswd -s {password}`).
*	`sudo puppet apply openhim.pp`
