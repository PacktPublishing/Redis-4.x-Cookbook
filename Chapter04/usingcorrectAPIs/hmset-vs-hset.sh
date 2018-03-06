#!/bin/bash
HSETFILE="hset.cmd"
HMSETFILE="hmset.cmd"

rm $HSETFILE $HMSETFILE >/dev/null  2>&1

NAMEOPTION[0]="Jack"
NAMEOPTION[1]="MIKE"
NAMEOPTION[2]="Mary"
SEXOPTION[0]="m"
SEXOPTION[1]="f"
NATIONOPTION[0]="us"
NATIONOPTION[1]="cn"
NATIONOPTION[2]="uk"


printf "hmset \"user\" " >> $HMSETFILE
for i in `seq -f "%010g" 1 100000`
do
    namerand=$[ $RANDOM % 3 ]
    sexrand=$[ $RANDOM % 2 ]
    timerand=$[ $RANDOM % 30 ]
    nationrand=$[ $RANDOM % 3 ]
    echo "hset \"user\" ${i} '{\"name\":\"${NAMEOPTION[$namerand]}\",\"sex\":\"${SEXOPTION[$sexrand]}\",\"resigter_time\":`date +%s%N`,\"nation\":\"${NATIONOPTION[$nationrand]}\"}'" >> $HSETFILE
    printf " ${i} '{\"name\":\"${NAMEOPTION[$namerand]}\",\"sex\":\"${SEXOPTION[$sexrand]}\",\"resigter_time\":`date +%s%N`,\"nation\":\"${NATIONOPTION[$nationrand]}\"}' " >> $HMSETFILE
    sleep 0.00000${timerand}
done

unix2dos $HSETFILE
unix2dos $HMSETFILE

time cat $HSETFILE |/redis/bin/redis-cli
sleep 10
time cat $HMSETFILE |/redis/bin/redis-cli
