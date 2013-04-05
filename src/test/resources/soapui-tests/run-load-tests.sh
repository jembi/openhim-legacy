#!/bin/bash
LOADUI_HOME="/home/ryan/Programs/loadUI-2.1.1/"

DIR=`pwd`
cd $LOADUI_HOME
./loadUI-cmd.sh -p $DIR/OpenHIM-load-tests.xml -L 10:0:0 -r $DIR -F PDF -l -s -z
