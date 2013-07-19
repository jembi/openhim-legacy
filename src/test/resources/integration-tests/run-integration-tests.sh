#!/bin/bash
# This must be set to the path in which your soap ui instance is installed
SOAPUI_TESTRUNNER="/home/ryan/Programs/soapUI-4.5.1/bin/testrunner.sh"

# Launch standalone wiremock for mocked services
java -jar wiremock-1.33-standalone.jar --verbose &
WireMockPID=$!
sleep 2s

# Launch soap ui integration testing suite
$SOAPUI_TESTRUNNER -ehttps://localhost:5000 -s"OpenHIM Interface TestSuite" -utest -ptest -r -f. -I OpenHIM-integration-tests.xml

# Kill wiremock dead
kill -HUP $WireMockPID