package io.leopard.web.xparam;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ValueConstants;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.annotation.AbstractNamedValueMethodArgumentResolver;

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
		System.err.println("UnderlineHandlerMethodArgumentResolver supportsParameter:" + name);
		for (char ch : name.toCharArray()) {
			if (Character.isUpperCase(ch)) {
				// 有大写就返回true.
				return true;
			}
		}
		return false;
	}

	@Override
	protected NamedValueInfo createNamedValueInfo(MethodParameter parameter) {
		RequestParam annotation = parameter.getParameterAnnotation(RequestParam.class);
		return (annotation != null) ? new RequestParamNamedValueInfo(annotation) : new RequestParamNamedValueInfo();
	}

	@Override
	protected Object resolveName(String name, MethodParameter parameter, NativeWebRequest request) throws Exception {
		HttpServletRequest req = (HttpServletRequest) request.getNativeRequest();

		String underlineName = camelToUnderline(name);
		System.err.println("UnderlineHandlerMethodArgumentResolver resolveName name:" + name + " underlineName:" + underlineName);

		String value = req.getParameter(underlineName);
		return value;
	}

	@Override
	protected void handleMissingValue(String name, MethodParameter parameter) throws ServletException {

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

	private static class RequestParamNamedValueInfo extends NamedValueInfo {

		public RequestParamNamedValueInfo() {
			super("", false, ValueConstants.DEFAULT_NONE);
		}

		public RequestParamNamedValueInfo(RequestParam annotation) {
			super(annotation.value(), annotation.required(), annotation.defaultValue());
		}
	}

}
