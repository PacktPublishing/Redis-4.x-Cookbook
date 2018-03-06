#!/bin/bash
DATAFILE="hash.data"

echo -n "hmset bighash " > $DATAFILE
for i in `seq -f "%010g" 1 30000`
do
    echo -n "$[ $RANDOM % 30000] $[ $RANDOM % 30000] " >> $DATAFILE
done

unix2dos $DATAFILE
cat $DATAFILE | /redis/bin/redis-cli
