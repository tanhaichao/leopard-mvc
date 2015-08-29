package io.leopard.web.passport;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;

public class RequestMappingHandlerMapping extends org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping {
	@Override
	protected void initApplicationContext(ApplicationContext context) throws BeansException {
		System.out.println("RequestMappingHandlerMapping initApplicationContext:" + context);
		System.out.println("RequestMappingHandlerMapping initApplicationContext:" + this);
		super.initApplicationContext(context);
	}
}
