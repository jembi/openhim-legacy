#/bin/bash
if [[ $# -lt 1 ]]; then
	echo "Usage: " $0 " <jks-file> <pem-file>"
	echo "	<jks-file> The KJS file to convert."
	echo "	<pem-file> The PEM file to output to."
else
	keytool -importkeystore -srckeystore $1 -destkeystore foo.p12 -srcstoretype jks -deststoretype pkcs12

	openssl pkcs12 -in foo.p12 -out $2

	rm foo.p12
fi
