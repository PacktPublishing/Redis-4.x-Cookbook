#!/bin/bash
DATAFILE="msgpack.data"

rm $DATAFILE >/dev/null  2>&1

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
	echo "user:00000${i} {\"name\":\"${NAMEOPTION[$namerand]}\",\"sex\":\"${SEXOPTION[$sexrand]}\",\"resigter_time\":`date +%s`,\"nation\":\"${NATIONOPTION[$nationrand]}\"}" >> $DATAFILE
    sleep 0.00000${timerand}
done

unix2dos $DATAFILE
