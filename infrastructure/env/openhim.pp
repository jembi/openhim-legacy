$mysql_password = "Jembi#123"
$himuser_password = "Jembi#123"
#the location of the install files
$source_dir = "~"

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
	command => "mysqladmin -uroot password $mysql_password",
	require => Service["mysql"]
}

exec { "create-mysql-user":
	command => "mysql -uroot -p$mysql_password -e \"CREATE USER 'himuser'@'localhost' IDENTIFIED BY '$himuser_password';\"",
	require => Exec["mysqlpass"]
}

# Create the OpenHIM mysql db
exec { "create-openhim-db":
	unless => "mysql -uroot -p$mysql_password interoperability_layer",
	command => "mysql -uroot -p$mysql_password < create_database.sql",
	require => [ Service["mysql"], Exec["mysqlpass"] ],
}

exec { "mysql-user-privileges":
	command => "mysql -uroot -p$mysql_password -e \"GRANT ALL PRIVILEGES ON interoperability_layer.* TO 'himuser'@'localhost';\"",
	require => [ Exec["create-openhim-db"], Exec["create-openhim-db"] ]
}

## OpenLDAP ##


## Mule ESB ##

file { "fetch-mule-esb":
	command => "wget -P $source_dir/ http://dist.codehaus.org/mule/distributions/mule-standalone-3.4.0.tar.gz",
}

exec { "extract-mule-esb":
	command => "tar -xzvf $source_dir/mule-standalone-3.3.0.tar.gz",
	require => Exec["fetch-mule-esb"]
}

exec { "copy-HIM-mule-app":
	
}
