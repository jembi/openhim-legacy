#!/bin/bash
if [ $# -lt 1 ]
then
	echo "Usage: " $0  " <client-name> <hostname>"
	echo "	<client-name> is a text name for this client."
	echo "	<hostname> must be the hostname of the server. It will be used as the common name in the generated certificate"
else
	echo "Generating keystore..."
	keytool -genkey -keystore $1-keystore.jks -dname "CN=$2"
	echo "Exporting certificate from keystore..."
	keytool -exportcert -keystore $1-keystore.jks -file $1.cer
fi
