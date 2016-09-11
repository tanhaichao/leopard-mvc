package io.leopard.mvc.trynb;

import javax.servlet.http.HttpServletRequest;

public interface TrynbLogger {

	/**
	 * 输出error级别日志
	 * 
	 * @param request
	 * @param uri
	 * @param exception
	 */
	void error(HttpServletRequest request, String uri, Exception exception);

	/**
	 * 输出warn级别日志
	 * 
	 * @param request
	 * @param uri
	 * @param exception
	 */
	void warn(HttpServletRequest request, String uri, Exception exception);

	/**
	 * 输出info级别日志
	 * 
	 * @param request
	 * @param uri
	 * @param exception
	 */
	void info(HttpServletRequest request, String uri, Exception exception);

	/**
	 * 输出debug级别日志
	 * 
	 * @param request
	 * @param uri
	 * @param exception
	 */
	void debug(HttpServletRequest request, String uri, Exception exception);

}
