#!/bin/sh
rm -f pid
echo Start eval-svc-jx.jar ..............
java -Dloader.path=.,classes,config,lib -Xms512m -Xmx512m -jar eval-svc-jx*.jar >/dev/null 2>&1 &
echo $! > pid
echo Start Success!
