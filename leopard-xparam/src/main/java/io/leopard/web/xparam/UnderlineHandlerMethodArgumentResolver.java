package io.leopard.web.xparam;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.NativeWebRequest;

/**
 * 下划线参数名称解析.
 * 
 * @author 阿海
 *
 */
@Component
public class UnderlineHandlerMethodArgumentResolver extends AbstractNamedValueMethodArgumentResolver {

	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		String name = parameter.getParameterName();
		// System.err.println("UnderlineHandlerMethodArgumentResolver supportsParameter:" + name);
		if (StringUtils.isEmpty(name)) {
			return false;
		}
		// TODO ahai 这里要判断model就返回false?
		// TODO 重启的时候name会为null?
		for (char ch : name.toCharArray()) {
			if (Character.isUpperCase(ch)) {
				// 有大写就返回true.
				return true;
			}
		}
		return false;
	}

	@Override
	protected Object resolveName(String name, MethodParameter parameter, NativeWebRequest request) throws Exception {
		HttpServletRequest req = (HttpServletRequest) request.getNativeRequest();

		String underlineName = camelToUnderline(name);
		// System.err.println("UnderlineHandlerMethodArgumentResolver resolveName name:" + name + " underlineName:" + underlineName);

		String value = req.getParameter(underlineName);
		return value;
	}

	/**
	 * 将驼峰式命名的字符串转换为下划线方式.
	 */
	public static String camelToUnderline(String param) {
		if (param == null || param.length() == 0) {
			return param;
		}
		int len = param.length();
		StringBuilder sb = new StringBuilder(len);
		for (int i = 0; i < len; i++) {
			char c = param.charAt(i);
			if (Character.isUpperCase(c)) {
				sb.append('_');
				sb.append(Character.toLowerCase(c));
			}
			else {
				sb.append(c);
			}
		}
		return sb.toString();
	}

}
