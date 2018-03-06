from __future__ import print_function
import redis

# Create connection to localhost Redis
client = redis.StrictRedis(host="localhost", port=6379)

# String Operations
restaurant = "Extreme Pizza"
client.set(restaurant, "300 Broadway, New York, NY")
client.append(restaurant, " 10011")
address = client.get("Extreme Pizza")
print("Address for " + restaurant + " is: " + address)

# List operations
listKey = "favorite_restaurants"
client.lpush(listKey, "PF Chang's", "Olive Garden")
client.rpush(listKey, "Outback Steakhouse", "Red Lobster")
favoriteRestaurants = client.lrange(listKey, 0, -1)
print("Favorite Restaurants: ", favoriteRestaurants)



