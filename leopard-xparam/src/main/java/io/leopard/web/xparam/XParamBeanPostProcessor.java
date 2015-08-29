package io.leopard.web.xparam;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

@Component
public class XParamBeanPostProcessor implements BeanPostProcessor, BeanFactoryAware {
	private BeanFactory beanFactory;

	@Override
	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		this.beanFactory = beanFactory;
	}

	@Override
	public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
		if (bean instanceof RequestMappingHandlerAdapter) {
			this.registerHandlerMethodArgumentResolver((RequestMappingHandlerAdapter) bean);
		}
		return bean;
	}

	/**
	 * 注册HandlerMethodArgumentResolver.
	 * 
	 * @param adapter
	 */
	private void registerHandlerMethodArgumentResolver(RequestMappingHandlerAdapter adapter) {
		List<HandlerMethodArgumentResolver> customArgumentResolvers = adapter.getCustomArgumentResolvers();
		if (customArgumentResolvers == null) {
			customArgumentResolvers = new ArrayList<HandlerMethodArgumentResolver>();
			adapter.setCustomArgumentResolvers(customArgumentResolvers);
		}
		XParamHandlerMethodArgumentResolver argumentResolver = beanFactory.getBean(XParamHandlerMethodArgumentResolver.class);
		customArgumentResolvers.add(argumentResolver);
	}

	@Override
	public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
		return bean;
	}

}
