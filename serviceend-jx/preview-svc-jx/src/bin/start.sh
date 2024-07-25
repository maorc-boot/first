#!/bin/sh
rm -f pid
echo Start preview-svc.jar ..............
java -Xms1024m -Xmx1024m  -XX:+PrintGCTimeStamps -XX:+PrintGCDetails -Xloggc:/log/preview-svc-heap.log  -Dloader.path=.,classes,config,lib -jar preview-svc-*.jar >/dev/null 2>&1 &
echo $! > pid
echo Start Success!
