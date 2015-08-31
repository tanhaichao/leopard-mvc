package io.leopard.web.frequency;

import io.leopard.redis.Redis;

import javax.servlet.http.HttpServletRequest;

public class StoreRedisImpl implements IStore {

	private static Redis redis;

	public static void setRedis(Redis redis) {
		StoreRedisImpl.redis = redis;
	}

	@Override
	public Object getPassport(HttpServletRequest request) {
		return request.getAttribute("passport");
	}

	@Override
	public boolean set(String key) {
		Long result = redis.setnx(key, "");
		return (result > 0);
	}

	@Override
	public void expire(String key, int seconds) {
		redis.expire(key, seconds);
	}

}
