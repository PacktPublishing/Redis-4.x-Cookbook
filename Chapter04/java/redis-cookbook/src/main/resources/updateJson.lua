local id = KEYS[1]
local data = ARGV[1]

local retJson = redis.call('get',  id)
local dataSource = cjson.decode(data)

if retJson == false then
    retJson = {}
else
    retJson = cjson.decode(retJson)
end


for k,v in pairs(dataSource) do
    retJson[k] = v
end
redis.call('set', id, cjson.encode(retJson))
return redis.call('get',id)