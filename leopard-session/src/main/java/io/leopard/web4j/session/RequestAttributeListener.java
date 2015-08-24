package io.leopard.web4j.session;

import javax.servlet.http.HttpServletRequest;

public interface RequestAttributeListener {

	void attributeGet(HttpServletRequest request, String name, Object value);

}
