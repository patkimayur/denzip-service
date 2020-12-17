#!/bin/bash

APP_HOME={{APP_HOME}}
JAVA_HOME={{JAVA_HOME}}
JVM_ARGS="-server -Xms512m -Xmx512m -XX:+PrintGCDetails -XX:+PrintGCDateStamps -Xloggc:$APP_HOME/logs/denzip-gc.log -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=$APP_HOME/logs/"

CMD="$JAVA_HOME/bin/java $JVM_ARGS -Dloader.path=$APP_HOME/crs-rental-config -jar $APP_HOME/crs-rental-service/crs-rental-uber.jar"
echo $CMD
$CMD
