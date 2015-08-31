package io.leopard.web.passport;

import org.springframework.beans.BeansException;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

public class LeopardHandlerMapping extends RequestMappingHandlerMapping {

	@Override
	public void afterPropertiesSet() {
		// new Exception("afterPropertiesSet:" + this).printStackTrace();
		super.afterPropertiesSet();
		System.out.println("LeopardHandlerMapping afterPropertiesSet:" + this);
		HandlerInterceptor[] interceptors = super.getAdaptedInterceptors();
		if (interceptors != null) {
			for (HandlerInterceptor interceptor : interceptors) {
				System.out.println("LeopardHandlerMapping afterPropertiesSet getAdaptedInterceptors:" + interceptor);
			}
		}
	}

	protected void initApplicationContext() throws BeansException {
		System.out.println("LeopardHandlerMapping initApplicationContext:" + this);
		// new Exception("initApplicationContext").printStackTrace();
		super.initApplicationContext();
	}

	@Override
	public void setInterceptors(Object[] interceptors) {
		super.setInterceptors(interceptors);
		System.out.println("LeopardHandlerMapping setInterceptors:" + this);
	}
}
