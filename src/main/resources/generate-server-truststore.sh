#!/bin/bash
if [ $# -lt 1 ]
then
	echo "Usage: " $0  " <client-certificate> ..."
	echo "	<client-certificate> is a client certificate, you may supply multiple of these at a time."
else
	echo "Importing $# certificates..."
	for cert in $*
	do
		keytool -import -file $cert -alias $cert -keystore truststore.jks
	done
fi
