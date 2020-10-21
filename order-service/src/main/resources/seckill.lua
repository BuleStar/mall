--
-- Created by IntelliJ IDEA.
-- User: aya
-- Date: 2020/3/19
-- Time: 10:23 PM
-- To change this template use File | Settings | File Templates.
-- return -1:库存不足 0:用户重复购买 1:成功

local product_id = KEYS[1]
local user_id = ARGV[1]
local product_key = 'seckill:' .. product_id .. ':stock'

-- seckill:1:uids
local bought_users_key = 'seckill:' .. product_id .. ':uids'

-- 判断用户是否秒杀过
local is_in = redis.call('sismember',bought_users_key, user_id)

-- 如果user_id在秒杀过用户的名单里,则返回重复购买
if is_in > 0 then
	return 0
end

-- 获取商品当前库存
local stock = redis.call('get',product_key)

-- 如果库存<=0 ,则返回-1
if not stock or tonumber(stock) <=0 then
	return -1
end

-- 减库存,并且把用户的id添加进已购买用户set里
redis.call('decr',product_key)
redis.call('SADD',bought_users_key,user_id)
return 1




