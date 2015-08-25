package io.leopard.web.session;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 内存实现，紧测试用.
 * 
 * @author 阿海
 *
 */
public class StroeMemoryImpl implements IStroe {

	private Map<String, String> data = new ConcurrentHashMap<String, String>(10);

	@Override
	public boolean isEnable() {
		return true;
	}

	@Override
	public String get(String key) {
		System.err.println("StroeMemoryImpl:" + key);
		return data.get(key);
	}

	@Override
	public String set(String key, String value, int seconds) {
		data.put(key, value);
		return value;
	}

	@Override
	public Long del(String key) {
		data.remove(key);
		return 1L;
	}

}
