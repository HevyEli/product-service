#!/bin/bash

export LOG_FILE="$HOSTNAME"
export APP_NAME="PRODUCT-SVC"
echo "log file name patter is $LOG_FILE"

#run java application
if [ ! -e ./app.jar ]; then
    echo "ERROR app.jar doesn't exist, can't start $APP_NAME application. EXITING bye!"
    exit 1
fi
exec java $JAVA_OPT -jar app.jar -Dspring.profiles.active=$SPRING_PROFILES_ACTIVE --spring.config.location="file:/app/config/" --spring.config.name="application"