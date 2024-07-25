#!/bin/sh
rm -f pid
echo Start plan-svc-jx.jar ..............
java -Dloader.path=lib/cxf-rt-ws-policy-3.2.1.jar,lib/cxf-core-3.2.1.jar,.,classes,config,lib -Xms512m -Xmx512m -jar plan-svc-jx-*.jar >/dev/null 2>&1 &
echo $! > pid
echo Start Success!