#!/bin/bash
cr=0
key=$1

rm ${1}.dumpfile
 
while true; do
    cr=`/redis/bin/redis-cli HSCAN user $cr MATCH '*' | {
        read a
        echo $a
        while read x; read y; do
            echo $x:$y >> ${1}.dumpfile
        done
    }`

    echo $cr
 
    if [ $cr == "0" ]; then
        break
    fi
done
