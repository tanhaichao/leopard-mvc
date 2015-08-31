package io.leopard.web.frequency;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.BeansException;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.PropertyValue;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.annotation.Autowired;
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
	@Autowired
	private FrequencyLei frequencyLei;

	private Map<Integer, Integer> data = new HashMap<Integer, Integer>();

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		int hashCode = handler.hashCode();
		Integer seconds = data.get(hashCode);
		if (seconds == null) {
			return true;
		}
		Object account = this.getAccount(request);
		if (account == null) {
			return true;
		}
		// String requestUri = RequestUtil.getRequestContextUri(request);
		String requestUri = request.getRequestURI();// 包含ContextPath也没有问题
		frequencyLei.request(account.toString(), requestUri, seconds);
		return true;
	}

	protected Object getAccount(HttpServletRequest request) {
		// TODO ahai 根据Controller方法中使用的session参数进行判断?
		return request.getAttribute("account");
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

	private void registerInterceptors(BeanDefinition beanDefinition) {
		MutablePropertyValues propertyValues = beanDefinition.getPropertyValues();
		System.out.println("FrequencyInterceptor postProcessBeanFactory BeanClassName:" + beanDefinition.getBeanClassName());
		propertyValues.addPropertyValue("interceptors", new Object[] { this });
		PropertyValue interceptors = propertyValues.getPropertyValue("interceptors");
		Object[] values = (Object[]) interceptors.getValue();
		for (Object value : values) {
			System.out.println("FrequencyInterceptor postProcessBeanFactory  PropertyValue:" + value);
		}
	}

	@Override
	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		ConfigurableListableBeanFactory factory = ((ConfigurableListableBeanFactory) beanFactory);
		for (String beanName : factory.getBeanDefinitionNames()) {
			BeanDefinition beanDefinition = factory.getBeanDefinition(beanName);
			if (isHandlerMapping(beanDefinition)) {
				System.out.println("FrequencyInterceptor setBeanFactory source:" + beanDefinition.getSource());
				this.registerInterceptors(beanDefinition);
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
