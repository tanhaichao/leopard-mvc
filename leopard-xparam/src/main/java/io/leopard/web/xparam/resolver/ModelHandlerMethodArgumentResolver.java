package io.leopard.web.xparam.resolver;

import java.awt.List;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.Date;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.math.NumberUtils;
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

		if (className.endsWith("AddressVO")) {
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

			Class<?> type = field.getType();
			Object obj;
			if (List.class.equals(type)) {
				Class<?> subType = (Class<?>) ((ParameterizedType) field.getGenericType()).getActualTypeArguments()[0];
				name = name.replaceFirst("List$", "");
				name = UnderlineHandlerMethodArgumentResolver.camelToUnderline(name);
				String[] values = req.getParameterValues(name);
				obj = ParamListHandlerMethodArgumentResolver.toList(subType, values);
				// throw new IllegalArgumentException("还没有支持List.class解析.");
			}
			else {
				String underlineName = UnderlineHandlerMethodArgumentResolver.camelToUnderline(field.getName());
				logger.info("resolveName name:" + field.getName() + " underlineName:" + underlineName);
				String value = req.getParameter(underlineName);
				if (value == null) {
					continue;
				}
				obj = this.toObject(value, type);
			}

			field.setAccessible(true);
			field.set(bean, obj);
		}

		return bean;
	}

	protected Object toObject(String value, Class<?> type) {
		if (String.class.equals(type)) {
			return value;
		}
		if (boolean.class.equals(type)) {
			return "true".equals(value);
		}
		if (int.class.equals(type)) {
			return Integer.parseInt(value);
		}
		if (long.class.equals(type)) {
			return Long.parseLong(value);
		}
		if (float.class.equals(type)) {
			return Float.parseFloat(value);
		}
		if (double.class.equals(type)) {
			return Double.parseDouble(value);
		}
		if (Date.class.equals(type)) {
			long time = NumberUtils.toLong(value);
			if (time <= 0) {
				return null;
			}
			return new Date(time);
		}
		throw new IllegalArgumentException("未知数据类型[" + type.getName() + "].");
	}

}
