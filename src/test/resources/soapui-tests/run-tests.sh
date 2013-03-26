#!/bin/bash
SOAPUI_TESTRUNNER="/home/ryan/Programs/soapUI-4.5.1/bin/testrunner.sh"
$SOAPUI_TESTRUNNER -ehttps://localhost:5000 -s"OpenHIM Interface TestSuite" -utest -ptest -r -f. -I OpenHIM-integration-tests.xml
