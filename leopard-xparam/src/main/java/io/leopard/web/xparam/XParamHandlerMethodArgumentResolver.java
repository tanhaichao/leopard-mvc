package io.leopard.web.xparam;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ValueConstants;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.annotation.AbstractNamedValueMethodArgumentResolver;

/**
 * 页面特殊参数.
 * 
 * @author 阿海
 *
 */
public class XParamHandlerMethodArgumentResolver extends AbstractNamedValueMethodArgumentResolver implements BeanFactoryAware {
	// TODO ahai 这里有必要使用线程安全的Map吗？
	private static final Map<String, XParam> data = new HashMap<String, XParam>();

	// public static void registerPageParameter(PageParameter page) {
	// data.put(page.getKey(), page);
	// }

	@Override
	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		ListableBeanFactory factory = (ListableBeanFactory) beanFactory;
		Map<String, XParam> map = factory.getBeansOfType(XParam.class);
		for (Entry<String, XParam> entry : map.entrySet()) {
			XParam xparam = entry.getValue();
			// System.err.println("xparam.getKey() " + entry.getKey() + ":" + xparam.getKey());
			data.put(xparam.getKey(), xparam);
		}
	}

	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		String name = parameter.getParameterName();
		boolean isSpecialName = data.containsKey(name);
		return isSpecialName;
	}

	@Override
	protected NamedValueInfo createNamedValueInfo(MethodParameter parameter) {
		RequestParam annotation = parameter.getParameterAnnotation(RequestParam.class);
		return (annotation != null) ? new RequestParamNamedValueInfo(annotation) : new RequestParamNamedValueInfo();
	}

	@Override
	protected Object resolveName(String name, MethodParameter parameter, NativeWebRequest request) throws Exception {
		XParam xparam = data.get(name);
		if (xparam == null) {
			throw new IllegalArgumentException("未知参数名称[" + name + "].");
		}
		return xparam.getValue((HttpServletRequest) request.getNativeRequest(), parameter);
	}

	@Override
	protected void handleMissingValue(String name, MethodParameter parameter) throws ServletException {

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
