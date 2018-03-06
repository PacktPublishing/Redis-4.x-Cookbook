--EVAL 'this script' 1 some-key '{"some": "json"}'
local key = KEYS[1];
local partitionkey = KEYS[2];
local partitionvalue = ARGV[1];
local mvalue = cmsgpack.pack(cjson.decode(partitionvalue));
return redis.call('HSET', key, partitionkey,mvalue);
