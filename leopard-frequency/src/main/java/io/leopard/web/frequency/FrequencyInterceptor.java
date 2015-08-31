package io.leopard.web.frequency;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.BeansException;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

/**
 * 访问频率限制过滤器.
 * 
 * @author 阿海
 * 
 */
// TODO ahai 在site项目必须实现BeanPostProcessor接口才能成功配置拦截器.
@Component
public class FrequencyInterceptor implements HandlerInterceptor, BeanFactoryAware, BeanPostProcessor {

	
	private FrequencyResolver frequencyResolver = new FrequencyResolver();
	private FrequencyChecker frequencyChecker = new FrequencyChecker();

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		Integer seconds = frequencyResolver.getSeconds(handler);
		System.out.println("FrequencyInterceptor preHandle:" + handler + " seconds:" + seconds);
		if (seconds == null || seconds <= 0) {
			return true;
		}

		frequencyChecker.check(request, seconds);
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

	}

	private boolean isHandlerMapping(BeanDefinition beanDefinition) {
		String beanClassName = beanDefinition.getBeanClassName();
		Class<?> clazz;
		try {
			clazz = Class.forName(beanClassName);
		}
		catch (ClassNotFoundException e) {
			return false;
		}
		if (RequestMappingHandlerMapping.class.isAssignableFrom(clazz)) {
			return true;
		}
		return false;
	}

	@Override
	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {

		ConfigurableListableBeanFactory factory = ((ConfigurableListableBeanFactory) beanFactory);
		for (String beanName : factory.getBeanDefinitionNames()) {
			BeanDefinition beanDefinition = factory.getBeanDefinition(beanName);
			if (isHandlerMapping(beanDefinition)) {
				MutablePropertyValues propertyValues = beanDefinition.getPropertyValues();
				System.out.println("FrequencyInterceptor postProcessBeanFactory BeanClassName:" + beanDefinition.getBeanClassName());
				propertyValues.addPropertyValue("interceptors", new Object[] { this });
			}
		}
	}

	@Override
	public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
		return bean;
	}

	@Override
	public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
		return bean;
	}
}
