#!/bin/bash
DATAFILE="mr.data"

PLENGTH=3

rm $DATAFILE >/dev/null  2>&1

/redis/bin/redis-cli FLUSHALL

NAMEOPTION[0]="Jack"
NAMEOPTION[1]="MIKE"
NAMEOPTION[2]="Mary"
SEXOPTION[0]="m"
SEXOPTION[1]="f"
NATIONOPTION[0]="us"
NATIONOPTION[1]="cn"
NATIONOPTION[2]="uk"

for i in `seq -f "%010g" 1 10000`
do
    namerand=$[ $RANDOM % 3 ]
    sexrand=$[ $RANDOM % 2 ]
    timerand=$[ $RANDOM % 30 ]
    nationrand=$[ $RANDOM % 3 ]
    blancerand=$[ $RANDOM % 100 ]
    LENGTH=`echo ${#i}`
    LENGTHCUT=`echo $((LENGTH-PLENGTH))`
    LENGTHEND=`echo $((LENGTHCUT+1))`
    VALUE1=`echo $i | cut -c1-${LENGTHCUT}`
    VALUE2=`echo $i | cut -c${LENGTHEND}-${LENGTH}`

    echo "hset \"user:${VALUE1}\" $VALUE2 '{\"name\":\"${NAMEOPTION[$namerand]}\",\"sex\":\"${SEXOPTION[$sexrand]}\",\"rtime\":`date +%s%N`,\"nation\":\"${NATIONOPTION[$nationrand]}\",\"balance\":${blancerand}}'" >> $DATAFILE
    sleep 0.00000${timerand}
done

unix2dos $DATAFILE
cat $DATAFILE | /redis/bin/redis-cli --pipe
rm $DATAFILE
