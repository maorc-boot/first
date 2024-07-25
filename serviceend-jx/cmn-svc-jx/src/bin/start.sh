#!/bin/sh
rm -f pid
echo Start cmn-svc.jar ..............
echo SPRING_PROFILES_ACTIVE=$SPRING_PROFILES_ACTIVE
java -Dloader.path=.,conf,lib -Xms512m -Xmx512m  -jar cmn-svc-*.jar >/dev/null 2>&1 &
echo $! > pid
echo Start cmn-svc Success!