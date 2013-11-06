#!/bin/bash
if [ $# -lt 1 ]
then
	echo "Usage: " $0  " <hostname>"
	echo "	<hostname> must be the hostname of the server. It will be used as the common name in the generated certificate"
else
	keytool -genkey -keystore keystore2.jks -dname "CN=$1"
fi
