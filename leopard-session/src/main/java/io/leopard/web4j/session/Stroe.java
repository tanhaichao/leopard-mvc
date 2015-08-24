package io.leopard.web4j.session;

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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String set(String key, String value, int seconds) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Long del(String key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String toJson(Object obj) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> T toObject(String json, Class<T> clazz) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isEnable() {
		// TODO Auto-generated method stub
		return false;
	}

}
