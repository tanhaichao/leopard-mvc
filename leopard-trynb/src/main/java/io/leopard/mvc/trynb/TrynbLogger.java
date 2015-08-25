package io.leopard.mvc.trynb;

import javax.servlet.http.HttpServletRequest;

public interface TrynbLogger {

	void error(HttpServletRequest request, String uri, Exception exception);

	void warn(HttpServletRequest request, String uri, Exception exception);

	void info(HttpServletRequest request, String uri, Exception exception);

	void debug(HttpServletRequest request, String uri, Exception exception);

}
