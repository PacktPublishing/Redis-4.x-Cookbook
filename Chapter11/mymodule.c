#include "redismodule.h"

static RedisModuleString *ListLPopRPush(RedisModuleKey *ListKey) {
  RedisModuleString *value = RedisModule_ListPop(ListKey, REDISMODULE_LIST_HEAD);
  RedisModule_ListPush(ListKey, REDISMODULE_LIST_TAIL, value);
  return value;
}

int MyModule_Zip(RedisModuleCtx *ctx, RedisModuleString **argv, int argc) {
  // MYMODULE.ZIP destination field_list value_list 
  if (argc != 4) {
    return RedisModule_WrongArity(ctx);
  }
  RedisModule_AutoMemory(ctx);
  
  // Open field/value list keys
  RedisModuleKey *fieldListKey = RedisModule_OpenKey(ctx, argv[2], REDISMODULE_READ | REDISMODULE_WRITE);
  RedisModuleKey *valueListKey = RedisModule_OpenKey(ctx, argv[3], REDISMODULE_READ | REDISMODULE_WRITE);
  if ((RedisModule_KeyType(fieldListKey) != REDISMODULE_KEYTYPE_LIST &&
      RedisModule_KeyType(fieldListKey) != REDISMODULE_KEYTYPE_EMPTY) ||
      (RedisModule_KeyType(valueListKey) != REDISMODULE_KEYTYPE_LIST &&
      RedisModule_KeyType(valueListKey) != REDISMODULE_KEYTYPE_EMPTY)) {
    return RedisModule_ReplyWithError(ctx, REDISMODULE_ERRORMSG_WRONGTYPE);
  }
  
  // Open destination key
  RedisModuleKey *destinationKey = RedisModule_OpenKey(ctx, argv[1], REDISMODULE_WRITE);
  RedisModule_DeleteKey(destinationKey);
  
  //Get length of lists
  size_t fieldListLen = RedisModule_ValueLength(fieldListKey);
  size_t valueListLen = RedisModule_ValueLength(valueListKey);
  
  if (fieldListLen == 0 || valueListLen == 0) {
    RedisModule_ReplyWithLongLong(ctx, 0L);
    return REDISMODULE_OK;
  }
  
  size_t fCount = 0;
  size_t vCount = 0;
  while (fCount < fieldListLen && vCount < valueListLen) {
    //Pop from left and push back to right
    RedisModuleString *key = ListLPopRPush(fieldListKey);
    RedisModuleString *value = ListLPopRPush(valueListKey);
    //Set hash
    RedisModule_HashSet(destinationKey, REDISMODULE_HASH_NONE, key, value, NULL);
    fCount++;
    vCount++;
  }
  
  while (fCount++ < fieldListLen) {
    ListLPopRPush(fieldListKey);
  }
  
  while (vCount++ < valueListLen) {
    ListLPopRPush(valueListKey);
  }
  
  //Get hash length
  RedisModuleCallReply *hlenReply = RedisModule_Call(ctx, "HLEN", "s", argv[1]);
  if (hlenReply == NULL) {
    return RedisModule_ReplyWithError(ctx, "Failed to call HLEN");
  } else if (RedisModule_CallReplyType(hlenReply) == REDISMODULE_REPLY_ERROR) {
    RedisModule_ReplyWithCallReply(ctx, hlenReply);
    return REDISMODULE_ERR;
  }
  
  RedisModule_ReplyWithLongLong(ctx, RedisModule_CallReplyInteger(hlenReply));
  
  RedisModule_ReplicateVerbatim(ctx);
  
  return REDISMODULE_OK;
}

int RedisModule_OnLoad(RedisModuleCtx *ctx, RedisModuleString **argv, int argc) {
  if (RedisModule_Init(ctx, "mymodule", 1, REDISMODULE_APIVER_1) == REDISMODULE_ERR) {
    return REDISMODULE_ERR;
  }

  if (RedisModule_CreateCommand(ctx, "mymodule.zip", MyModule_Zip, "write deny-oom no-cluster", 
    1, 1, 1) == REDISMODULE_ERR) {
    return REDISMODULE_ERR;
  }

  return REDISMODULE_OK;
}
