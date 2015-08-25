package io.leopard.web.session;

public class Stroe implements IStroe {

	private static final IStroe instance = null;

	public static IStroe getInstance() {
		return instance;
	}

	// private Redis redis;

	/**
	 * 根据session id获取session key.
	 * 
	 * @param sid
	 * @return
	 */
	protected String getSessionKey(String sid) {
		// AssertUtil.assertNotEmpty(sid, "sessionId怎么会为空?");
		if (sid == null || sid.length() == 0) {
			throw new IllegalArgumentException("sessionId怎么会为空?");
		}
		return "sid:" + sid;
	}

	// /**
	// * 根据session id获取session对象.
	// *
	// * @param sid
	// * @return
	// */
	// @Override
	// public Map<String, Object> getSession(String sid) {
	// String key = this.getSessionKey(sid);
	// String json = this.redis.get(key);
	// Map<String, Object> session = Json.toObject(json, Map.class);
	// return session;
	// }

	// /**
	// * 保存Session对象.
	// *
	// * @param sid
	// * id
	// * @param session
	// * map对象
	// * @param seconds
	// * 保存时间
	// */
	//
	// @Override
	// public void saveSession(String sid, Map<String, Object> session, int seconds) {
	// String key = this.getSessionKey(sid);
	// String json = Json.toJson(session);
	// this.redis.set(key, json, seconds);
	// }

	// /**
	// * 移除session对象.
	// *
	// * @param sid
	// */
	// @Override
	// public void removeSession(String sid) {
	// String key = this.getSessionKey(sid);
	// this.redis.del(key);
	// }

	@Override
	public String get(String key) {
		throw new UnsupportedOperationException("Not Impl.");
	}

	@Override
	public String set(String key, String value, int seconds) {
		throw new UnsupportedOperationException("Not Impl.");
	}

	@Override
	public Long del(String key) {
		throw new UnsupportedOperationException("Not Impl.");
	}

	// @Override
	// public String toJson(Object obj) {
	// throw new UnsupportedOperationException("Not Impl.");
	// }
	//
	// @Override
	// public <T> T toObject(String json, Class<T> clazz) {
	// throw new UnsupportedOperationException("Not Impl.");
	// }

	@Override
	public boolean isEnable() {
		throw new UnsupportedOperationException("Not Impl.");
	}

}
