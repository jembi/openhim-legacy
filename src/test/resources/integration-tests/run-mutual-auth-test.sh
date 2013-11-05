#/bin/bash
# The example truststore supplied in src/resources contains the curl-test-keystore cert and key
# The OpenHIM must be started with the property cert.client.auth=true
curl -E curl-test-keystore.pem:Jembi#123 -k -u test:test https://localhost:5000/test/protected
valid=$?

curl -E curl-test-invalid-keystore.pem:Jembi#123 -k -u test:test https://localhost:5000/test/protected
invalid=$?

if [ $valid = "0" -a $invalid != "0" ]; then
	echo "Tests passed!"
else
	echo "Tests failed."
fi