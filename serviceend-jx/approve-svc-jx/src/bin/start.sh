#!/bin/sh
rm -f pid
echo Start approve-svc.jar ..............
java -Dloader.path=.,classes,config,lib -Xms512m -Xmx512m -jar approve-svc-*.jar >/dev/null 2>&1 &
echo $! > pid
echo Start Success!
