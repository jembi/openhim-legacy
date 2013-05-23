# Config - Please set proper passwords!

$mysql_password = "Jembi#123"
$himuser_password = "Jembi#123"

# the location of the install files
$source_dir = "/home/jembi"

$openldap_base_dn = "dc=moh,dc=gov,dc=rw"
$openldap_root_dn = "cn=admin,dc=moh,dc=gov,dc=rw"
# use the slappasswd utility to generate
$openldap_rootpw = "{SSHA}yrobOdiwUl0JOU8toZeFE/C5EsCyQfEz"
$ldap_pass = "Jembi#123"
$ldap_ldif = "ldap-auth-pre-prod.ldif"

####

# defaults for Exec
Exec {
	    path => ["/bin", "/sbin", "/usr/bin", "/usr/sbin", "/usr/local/bin", "/usr/local/sbin"],
		user => 'root',
}

# Make sure package index is updated (when referenced by require)
exec { "apt-get update":
    command => "apt-get update",
    user => "root",
}

## Java ##

package { "openjdk-7-jre":
	ensure => latest,
	require => Exec['apt-get update'];
}

## MySQL ##

package { "mysql-server":
	ensure => latest,
	require => Exec['apt-get update'];
}

service { "mysql":
	enable => true,
	ensure => running,
	require => Package["mysql-server"],
}

#Set MySQL root password
exec { "mysqlpass":
	unless => "mysqladmin -uroot -p$mysql_password status",
	command => "mysqladmin -uroot password $mysql_password",
	require => Service["mysql"]
}

exec { "create-mysql-user":
	unless => "mysqladmin -uhimuser -p$himuser_password status",
	command => "mysql -uroot -p$mysql_password -e \"CREATE USER 'himuser'@'localhost' IDENTIFIED BY '$himuser_password';\"",
	require => Exec["mysqlpass"]
}

# Create the OpenHIM mysql db
exec { "create-openhim-db":
	cwd => "$source_dir",
	unless => "mysql -uroot -p$mysql_password interoperability_layer",
	command => "mysql -uroot -p$mysql_password < create_database.sql",
	require => [ Service["mysql"], Exec["mysqlpass"] ],
}

exec { "mysql-user-privileges":
	unless => "mysqladmin -uhimuser -p$himuser_password status",
	command => "mysql -uroot -p$mysql_password -e \"GRANT ALL PRIVILEGES ON interoperability_layer.* TO 'himuser'@'localhost';\"",
	require => [ Exec["create-openhim-db"], Exec["create-openhim-db"] ]
}

## OpenLDAP ##

include openldap

package { "ldap-utils":
	ensure => latest,
	require => [Exec['apt-get update'], Class['openldap']],
}

exec { "setup-ldap-ldif":
	cwd => "$source_dir",
	command => "ldapadd -c -x -D $openldap_root_dn -w $ldap_pass -f $ldap_ldif",
	require => Package['ldap-utils']
}

## Mule ESB ##

exec { "fetch-mule-esb":
	command => "wget -P $source_dir/ http://dist.codehaus.org/mule/distributions/mule-standalone-3.4.0.tar.gz",
	creates => "$source_dir/mule-standalone-3.4.0.tar.gz",
	timeout => 0,
}

exec { "extract-mule-esb":
	cwd => "/opt",
	command => "tar -zxf $source_dir/mule-standalone-3.4.0.tar.gz",
	require => Exec["fetch-mule-esb"],
	creates => "/opt/mule-standalone-3.4.0",
}

exec { "copy-HIM-mule-app":
	cwd => "/opt/mule-standalone-3.4.0/apps",
	command => "cp $source_dir/openhim-0.1.0-SNAPSHOT.zip ./",
	creates => "/opt/mule-standalone-3.4.0/apps/openhim-0.1.0-SNAPSHOT.zip",
	require => Exec["extract-mule-esb"],
}
