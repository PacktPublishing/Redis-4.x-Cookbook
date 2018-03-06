from __future__ import print_function
import redis

# Create connection to localhost Redis
client = redis.StrictRedis(host="localhost", port=6379)

user = "users:id:992452"
client.set(user, '{"name": "Tina", "sex": "female", "grade": "A"}')

# Read the lua scripts from file
with open("updateJson.lua") as f:
  lua = f.read()

  #Create Redis Script instance
  updateJson = client.register_script(lua)

  #Invoke lua script using the script instance
  updateJson(keys=[user], args=['{"grade": "C"}'])

  print(client.get(user))





