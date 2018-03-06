--EVAL 'this script' 1 some-key '{"some": "json"}'
local key = KEYS[1];
local value = ARGV[1];
local mvalue = cmsgpack.pack(cjson.decode(value));
return redis.call('SET', key, mvalue);
