#!/bin/bash
SOAPUI_TESTRUNNER="/home/ryan/Programs/soapUI-4.5.1/bin/testrunner.sh"
#LOADUI_HOME="/home/ryan/Programs/loadUI-2.1.1/"

$SOAPUI_TESTRUNNER -ehttps://localhost:5000 -s"OpenHIM Interface TestSuite" -utest -ptest -r -f. -I OpenHIM-integration-tests.xml

#DIR=`pwd`
#cd $LOADUI_HOME
#./loadUI-cmd.sh -p $DIR/OpenHIM-load-tests.xml -L 60:0:0 -r $DIR -F PDF
