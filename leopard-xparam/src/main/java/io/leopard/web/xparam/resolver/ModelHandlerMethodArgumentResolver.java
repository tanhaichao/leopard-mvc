package io.leopard.web.xparam.resolver;

import java.lang.reflect.Field;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
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
public class ModelHandlerMethodArgumentResolver extends AbstractNamedValueMethodArgumentResolver {

	protected Log logger = LogFactory.getLog(this.getClass());

	@Value("${xparam.underline}")
	private String underline;

	@PostConstruct
	public void init() {
		enable = !"false".equals(underline);
	}

	private static boolean enable = true;

	public static boolean isEnable() {
		return enable;
	}

	public static void setEnable(boolean enable) {
		ModelHandlerMethodArgumentResolver.enable = enable;
	}

	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		Class<?> type = parameter.getParameterType();
		String className = type.getName();
		boolean supports = false;
		if (className.endsWith("VO") || className.endsWith("Form")) {
			supports = true;
		}
		logger.info("supportsParameter name:" + parameter.getParameterName() + " supports:" + supports);
		return supports;
	}

	@Override
	protected Object resolveName(String name, MethodParameter parameter, NativeWebRequest request) throws Exception {
		HttpServletRequest req = (HttpServletRequest) request.getNativeRequest();
		Class<?> clazz = parameter.getParameterType();

		Object bean = clazz.newInstance();
		for (Field field : clazz.getDeclaredFields()) {
			String underlineName = UnderlineHandlerMethodArgumentResolver.camelToUnderline(name);
			logger.info("resolveName name:" + name + " underlineName:" + underlineName);
			String value = req.getParameter(underlineName);

			field.setAccessible(true);
			field.set(bean, value);
		}

		return bean;
	}

}
