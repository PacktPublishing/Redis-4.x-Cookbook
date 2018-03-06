#!/bin/bash
set -e

MASTERIP=${6}
MASTERPORT=${7}
SLAVEIP=${4}
SLAVEPORT=${5}
MYIP=192.168.0.31 # Set the correct IP address for all hosts

#if master
if [ ${MASTERIP} = ${MYIP} ]; then
    #Disable RDB
    /redis/bin/redis-cli -h ${MYIP} -p ${MASTERPORT} config set save ""

    exit 0

#or slave
else
    /redis/bin/redis-cli -h ${MYIP} -p ${SLAVEPORT} config set save "900 1 300 10 60 10000"
    exit 0
fi

exit 1

