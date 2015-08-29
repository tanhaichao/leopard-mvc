package io.leopard.web.passport;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 通行证(用户)登录验证接口
 * 
 * @author 阿海
 *
 */
public interface PassportValidate {

	/**
	 * 获取当前登录的用户信息(Leopard会自动将返回值存入session作缓存).
	 * 
	 * @return 通行证或uid
	 */
	Object validate(HttpServletRequest request, HttpServletResponse response);

	/**
	 * 显示登陆框.
	 * 
	 * @param request
	 * @param response
	 * 
	 * @return 已实现登陆框返回true，未实现返回false
	 */
	boolean showLoginBox(HttpServletRequest request, HttpServletResponse response);

	/**
	 * 登陆验证.
	 * 
	 * @param request
	 * @param response
	 * @return 未实现返回null，已实现跳转返回true，未跳转返回false
	 */
	Boolean login(HttpServletRequest request, HttpServletResponse response);

}
