#!/bin/bash

BEGIN=0
TOPNUM=10
TMPFILE="/tmp/scan.out"
RESULTFILE="result.out"

> $RESULTFILE

/redis/bin/redis-cli --raw SCAN $BEGIN > $TMPFILE

while true
do
    for key in `sed '1d' $TMPFILE`
    do
        echo -n $key" " >> $RESULTFILE
	/redis/bin/redis-cli --raw DEBUG OBJECT $key | awk '{print $NF}' | cut -d":" -f 2 >> $RESULTFILE
    done
    
    CURSOR=`head -n1 $TMPFILE`
  
    if [[ $CURSOR -eq $BEGIN ]] 
    then
	echo "Scan ended!"
	echo "The Top $TOPNUM key in your Redis is"
	cat $RESULTFILE  | sort -nrk2  | head -n$TOPNUM
        exit 
    fi
    /redis/bin/redis-cli --raw SCAN $CURSOR > $TMPFILE
done
