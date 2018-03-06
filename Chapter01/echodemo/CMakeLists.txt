# vim: set ai expandtab:

cmake_minimum_required(VERSION 3.5 FATAL_ERROR)

project(redis VERSION 4.0)

find_package(Threads)

get_filename_component(REDIS_ROOT "${CMAKE_CURRENT_SOURCE_DIR}" ABSOLUTE)

add_executable(my-redis-server
    src/adlist.c
    src/adlist.h
    src/ae.c
    src/ae.h
    src/anet.c
    src/anet.h
    src/aof.c
    src/asciilogo.h
    src/bio.c
    src/bio.h
    src/bitops.c
    src/blocked.c
    src/cluster.c
    src/cluster.h
    src/childinfo.c
    src/config.c
    src/config.h
    src/crc16.c
    src/crc64.c
    src/crc64.h
    src/db.c
    src/debug.c
    src/defrag.c
    src/dict.c
    src/dict.h
    src/endianconv.c
    src/endianconv.h
    src/expire.c
    src/evict.c
    src/fmacros.h
    src/geo.c
    src/geo.h
    src/geohash_helper.c
    src/geohash.c
    src/help.h
    src/hyperloglog.c
    src/intset.c
    src/intset.h
    src/latency.c
    src/lazyfree.c
    src/latency.h
    src/lzf.h
    src/lzfP.h
    src/lzf_c.c
    src/lzf_d.c
    src/memtest.c
    src/multi.c
    src/module.c
    src/networking.c
    src/notify.c
    src/object.c
    src/pqsort.c
    src/pqsort.h
    src/pubsub.c
    src/quicklist.c
    src/quicklist.h
    src/rand.c
    src/rand.h
    src/rax.c
    src/rax.h
    src/rdb.c
    src/rdb.h
    src/redis-check-rdb.c
    src/redis-check-aof.c
    src/redisassert.h
    src/release.c
    src/release.h
    src/replication.c
    src/rio.c
    src/rio.h
    src/scripting.c
    src/sds.c
    src/sds.h
    src/sdsalloc.h
    src/sentinel.c
    src/server.c
    src/server.h
    src/setproctitle.c
    src/sha1.c
    src/sha1.h
    src/siphash.c
    src/slowlog.c
    src/slowlog.h
    src/solarisfixes.h
    src/sort.c
    src/sparkline.c
    src/sparkline.h
    src/syncio.c
    src/t_hash.c
    src/t_list.c
    src/t_set.c
    src/t_string.c
    src/t_zset.c
    src/testhelp.h
    src/util.c
    src/util.h
    src/version.h
    src/ziplist.c
    src/ziplist.h
    src/zipmap.c
    src/zipmap.h
    src/zmalloc.c
    src/zmalloc.h
)

set_property(TARGET my-redis-server PROPERTY C_STANDARD 99)
set_property(TARGET my-redis-server PROPERTY CXX_STANDARD 11)
set_property(TARGET my-redis-server PROPERTY CXX_STANDARD_REQUIRED ON)


target_include_directories(my-redis-server
    PRIVATE ${REDIS_ROOT}/deps/hiredis
    PRIVATE ${REDIS_ROOT}/deps/linenoise
    PRIVATE ${REDIS_ROOT}/deps/lua/src
)

# now create an imported static target
add_library(luaa STATIC IMPORTED)
# Import target "scintilla" for configuration ""
set_property(TARGET luaa APPEND PROPERTY IMPORTED_CONFIGURATIONS NOCONFIG)
set_target_properties(luaa PROPERTIES IMPORTED_LOCATION_NOCONFIG "${REDIS_ROOT}/deps/lua/src/liblua.a")

target_link_libraries(my-redis-server
    PRIVATE m
    PRIVATE luaa
    PRIVATE ${REDIS_ROOT}/deps/lua/src/liblua.a
    PRIVATE ${REDIS_ROOT}/deps/linenoise/linenoise.o
    PRIVATE ${REDIS_ROOT}/deps/hiredis/libhiredis.a
	${CMAKE_THREAD_LIBS_INIT}
    ${CMAKE_DL_LIBS}
)


add_library(ae src/anet.c src/ae.c src/zmalloc.c)

#For Echo Client/Server Demo
add_executable(echo-server echodemo/echo-server.c echodemo/echo.c echodemo/echo.h)
add_executable(echo-client echodemo/echo-client.c echodemo/echo.c echodemo/echo.h)
target_link_libraries(echo-server ae)
target_link_libraries(echo-client ae)
