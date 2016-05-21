package io.leopard.web.xparam;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.ServiceLoader;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.NativeWebRequest;

import io.leopard.web.xparam.api.UserinfoResolverImpl;

/**
 * 页面特殊参数.
 * 
 * @author 阿海
 *
 */
@Component
public class XParamHandlerMethodArgumentResolver extends AbstractNamedValueMethodArgumentResolver implements BeanFactoryAware {
	// TODO ahai 这里有必要使用线程安全的Map吗？
	private static final Map<String, XParam> data = new HashMap<String, XParam>();

	public XParamHandlerMethodArgumentResolver() {
		Iterator<XParam> iterator = ServiceLoader.load(XParam.class).iterator();
		while (iterator.hasNext()) {
			XParam xparam = iterator.next();
			data.put(xparam.getKey(), xparam);
		}

	}

	@Override
	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		UserinfoResolverImpl.setBeanFactory(beanFactory);

		ListableBeanFactory factory = (ListableBeanFactory) beanFactory;
		Map<String, XParam> map = factory.getBeansOfType(XParam.class);

		for (Entry<String, XParam> entry : map.entrySet()) {
			XParam xparam = entry.getValue();
			XParam old = data.get(xparam.getKey());
			if (old != null) {
				xparam.override(old);
			}
			data.put(xparam.getKey(), xparam);
		}
	}

	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		String name = parameter.getParameterName();
		boolean isSpecialName = data.containsKey(name);
		// System.out.println("name:" + name + " isSpecialName:" + isSpecialName);
		return isSpecialName;
	}

	@Override
	protected Object resolveName(String name, MethodParameter parameter, NativeWebRequest request) throws Exception {
		// System.err.println("resolveName name:" + name);
		XParam xparam = data.get(name);
		if (xparam == null) {
			throw new IllegalArgumentException("未知参数名称[" + name + "].");
		}
		return xparam.getValue((HttpServletRequest) request.getNativeRequest(), parameter);
	}

}
