package io.leopard.web.xparam;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.NativeWebRequest;

/**
 * List<?>参数解析.
 * 
 * @author 阿海
 *
 */
@Component
public class ParamListHandlerMethodArgumentResolver extends AbstractNamedValueMethodArgumentResolver {

	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		Class<?> type = parameter.getParameterType();
		if (!type.equals(List.class)) {
			return false;
		}
		String name = parameter.getParameterName();
		boolean support = name.endsWith("List");
		System.err.println("ParamListHandlerMethodArgumentResolver supportsParameter name:" + name + " support:" + support + " type:" + type.getName());
		return support;
	}

	@Override
	protected Object resolveName(String name, MethodParameter parameter, NativeWebRequest request) throws Exception {
		HttpServletRequest req = (HttpServletRequest) request.getNativeRequest();
		name = name.replaceFirst("List$", "");

		String[] values = req.getParameterValues(name);
		// String value = req.getParameter(name);
		return values;
	}

}
