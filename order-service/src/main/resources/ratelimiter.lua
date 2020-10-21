--
-- Created by IntelliJ IDEA.
-- User: hongfei
-- Date: 2020/9/5
-- Time: 13:19
-- To change this template use File | Settings | File Templates.
--获取业务Id
local busKey = KEYS[1]

local limit = tonumber(ARGV[1])

local count = tonumber(redis.call('get',busKey) or "0")

if count + 1> limit then

    return false
else

    redis.call('incrby',busKey,1)
    redis.call('expire',busKey,1)
    return true
end