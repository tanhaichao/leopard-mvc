package io.leopard.web.frequency;

import javax.servlet.http.HttpServletRequest;

public interface IStore {

	Object getPassport(HttpServletRequest request);

	boolean set(String key);

	void expire(String key, int seconds);

}
