#!/bin/sh
rm -f pid
echo Start auth-svc.jar ..............
echo SPRING_PROFILES_ACTIVE=$SPRING_PROFILES_ACTIVE
java -Dloader.path=.,conf,lib  -Xms512m -Xmx512m  -jar auth-svc-*.jar >/dev/null 2>&1 &
echo $! > pid
echo Start auth-svc Success!