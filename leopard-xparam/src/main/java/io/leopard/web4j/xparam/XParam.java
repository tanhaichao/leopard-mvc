package io.leopard.web4j.xparam;

import javax.servlet.http.HttpServletRequest;

import org.springframework.core.MethodParameter;

/**
 * 页面特殊参数.
 * 
 * @author 阿海
 * 
 */
public interface XParam {

	/**
	 * 参数名称(区分大小写)，在spring初始化时使用.
	 * 
	 * @return
	 */
	String getKey();

	/**
	 * 获取参数值.
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	Object getValue(HttpServletRequest request, MethodParameter parameter);

}
