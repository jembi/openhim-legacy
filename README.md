OpenHIM
=======

OpenHIM is an open source Health Information Mediator built to facilitate interoperability between disparate Health Information Systems.

It was developed for the Rwandan Health Infromation Exchange however, it was designed and intended to be used in re-used other settings. For more details about how the OpenHIM is used in the Rwandan Health Information exchange, see https://jembiprojects.jira.com/wiki/pages/viewpage.action?pageId=10289633

Setup
=====

The HIM is developed as a Mule ESB application so we need MuleStudio to work with the project. You can download it here: http://www.mulesoft.org/download-mule-esb-community-edition

Next, you will want to import this git repo into MuleStudio. There is some good documentation on how to accomblish this on the Mule wiki: http://www.mulesoft.org/documentation/display/MULE3STUDIO/Using+Git+with+Studio

Once, you have the application imported you will need to configure 2 things for the HIM to function.

1.	a MySQL database - this is used to store and log transactions that flow through the OpenHIM
2.	an OpenLDAP server - this is used for client authentication with the OpenHIM

Database setup
--------------

1.	Install MySQL for your platform. In Ubuntu you can do this as follows: `$apt-get install mysql-server`
2.	Locate the create_database.sql script in the HIM built in src/main/resources.
3.	Execute this sql script using mysql and it will create the database required for the RHEA HIM `$mysql -u root -p"<root_pwd>" < create_database.sql`
4.	Create a new user for the database and grant access to them. Log into the mysql console: `$mysql -u -p"<root_pwd>"`
5.	Then run: `grant all on interoperability_layer.* to 'himuser'@localhost identified by '<user_pwd>';`
6.	Locate the my.properties file. Edit this file to contain the details of a mysql user (created above) that can read and write to the created database.

OpenLDAP setup
--------------

See https://help.ubuntu.com/12.04/serverguide/openldap-server.html for the source of alot of this material.

1.	edit /etc/hosts and replace the domain name with one that will give you the suffix you desire `$cp hosts hosts.bak`
2.	edit hosts file and change the 127.0.1.1 line to: "127.0.1.1          <hostname>.moh.gov.rw           <hostname>" Note: <hostname> must be the hostname of your machine, run the following command to find it out: `$hostname`
3.	Install OpenLDAP `$sudo apt-get install slapd ldap-utils`
4.	revert /etc/hosts back to normal `$cp hosts.bak hosts`
5.	Test if the ldap server was setup and the default datbase was created correctly. Run the following command: `$ldapsearch -x -LLL -H ldap:/// -b dc=moh,dc=gov,dc=rw dn` You should see the following 2 line:
	dn: dc=moh,dc=gov,dc=rw
	dn: cn=admin,dc=moh,dc=gov,dc=rw
	If you see this the database was setup correctly!
6.	Now we need to add some user to the LDAP server to do this you need to import a .ldif file. A pre-production ldif file comes packaged with the HIM source code. To import the LDIF file run: `$ldapadd -c -x -D cn=admin,dc=moh,dc=gov,dc=rw -W -f ldap-auth-pre-prod.ldif`
7.	To do a test search of the ldap database run: `$ldapsearch -x -LLL -b dc=moh,dc=gov,dc=rw 'uid=test'`. If the result of the search shows up positive you are set to go!

Note: when creating your own ldap database of users have to be in OrganizationalUnit called himusers

How to change a users password: $ldappasswd -D "cn=admin,dc=moh,dc=gov,dc=rw" -W -S "uid=test,ou=himusers,dc=moh,dc=gov,dc=rw"

How to run the application
--------------------------

1.	In the openhim Mule application locate the mule-project.xml file.
2.	Right click on and it and choose 'run as', then choose 'Mule Application'.
3.	The app will start up and run in the eclipse console, hey presto!

Running the Mock Services
-------------------------

Ok, so the OpenHIM doesn't do much without services to orchestration. The current OpenHIM is setup to work with the services in the Rwandan Health Information Exchange. However, we have also created some mock services that the OpenHIM setup to use by default. These can be found here: https://github.com/jembi/rhea-mock-services. These services are also built as a mule app an can be imported and run the same way.

Testing the RHEA HIM
--------------------

To test the RHEA HIM implementation you can use SOAP UI (http://www.soapui.org/)

A SOAP UI project that is setup for testing the API calls for the HIM can be found here: https://github.com/jembi/rhea-api-tests
