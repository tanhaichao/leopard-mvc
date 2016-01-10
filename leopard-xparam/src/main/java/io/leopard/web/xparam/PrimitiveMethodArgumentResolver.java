package io.leopard.web.xparam;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ValueConstants;

/**
 * 基本数据类型
 * 
 * @author ahai
 *
 */
public class PrimitiveMethodArgumentResolver extends org.springframework.web.method.annotation.RequestParamMethodArgumentResolver {

	public PrimitiveMethodArgumentResolver() {
		super(false);
	}

	// @Override
	// public boolean supportsParameter(MethodParameter parameter) {
	// boolean supports = super.supportsParameter(parameter);
	// new Exception("supportsParameter:" + parameter.getParameterName() + " supports:" + supports).printStackTrace();
	// return supports;
	// }

	@Override
	protected NamedValueInfo createNamedValueInfo(MethodParameter parameter) {
		if (true) {
			return super.createNamedValueInfo(parameter);
		}
		RequestParam ann = parameter.getParameterAnnotation(RequestParam.class);
		if (ann != null) {
			return new RequestParamNamedValueInfo(ann);
		}
		// ValueConstants.DEFAULT_NONE
		Class<?> clazz = parameter.getParameterType();
		String defaultValue;
		if (clazz.isPrimitive()) {
			if (clazz.equals(long.class)) {
				new Exception("long " + parameter.getParameterName()).printStackTrace();
				// defaultValue = "0";
				defaultValue = ValueConstants.DEFAULT_NONE;
			}
			else if (clazz.equals(int.class)) {
				defaultValue = "0";
			}
			else {
				defaultValue = ValueConstants.DEFAULT_NONE;
			}
		}
		else {
			defaultValue = ValueConstants.DEFAULT_NONE;
		}
		return new RequestParamNamedValueInfo(defaultValue);
	}

	private static class RequestParamNamedValueInfo extends NamedValueInfo {

		public RequestParamNamedValueInfo(String defaultValue) {
			super("", false, defaultValue);
		}

		public RequestParamNamedValueInfo(RequestParam annotation) {
			super(annotation.name(), annotation.required(), annotation.defaultValue());
		}
	}
}
