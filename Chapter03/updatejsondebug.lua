local id = KEYS[1]
local data = ARGV[1]
redis.debug(id)
redis.debug(data)

local retJson = redis.call('get',  id)
local dataSource = cjson.decode(data)

if retJson == false then
  retJson = {}
else
  retJson = cjson.decode(retJson)
end
redis.breakpoint()

for k,v in pairs(dataSource) do
  retJson[k] = v
end

redis.log(redis.LOG_WARNING,cjson.encode(retJson))
redis.call('set', id, cjson.encode(retJson))

return redis.call('get',id)

