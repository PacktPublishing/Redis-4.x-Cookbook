from __future__ import print_function
import redis

# Create connection to localhost Redis
client = redis.StrictRedis(host="localhost", port=6379)

# Create a pipeline
pipeline = client.pipeline()

# Add commands to pipeline
pipeline.set("mykey", "myvalue")
pipeline.sadd("myset", "value1", "value2")
pipeline.get("mykey")
pipeline.scard("myset")

#Send commands
response = pipeline.execute()
print(response)





