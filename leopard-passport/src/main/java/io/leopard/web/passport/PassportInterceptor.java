package io.leopard.web.passport;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.PropertyValue;
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
 * 检查是否已登录.
 * 
 * @author 阿海
 * 
 */
@Component
// TODO ahai 在site项目必须实现BeanPostProcessor接口才能成功配置拦截器.
public class PassportInterceptor implements HandlerInterceptor, BeanFactoryAware, BeanPostProcessor {
	protected Log logger = LogFactory.getLog(this.getClass());

	private PassportChecker passportChecker = new PassportCheckerImpl();
	private PassportValidate passportValidateLei = PassportValidateImpl.getInstance();

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		boolean isNeedCheckLogin = passportChecker.isNeedCheckLogin(request, handler);
		System.out.println("PassportInterceptor preHandle:" + isNeedCheckLogin + " " + handler);
		if (!isNeedCheckLogin) {
			return true;
		}

		Object account = passportValidateLei.validate(request, response);
		if (account == null) {
			passportValidateLei.showLoginBox(request, response);
			return false;
		}
		// FrequencyInterceptor.setAccount(request, account);
		// request.setAttribute("account", account);
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

	private void registerInterceptors(BeanDefinition beanDefinition) {
		MutablePropertyValues propertyValues = beanDefinition.getPropertyValues();
		System.out.println("PassportInterceptor postProcessBeanFactory BeanClassName:" + beanDefinition.getBeanClassName());
		propertyValues.addPropertyValue("interceptors", new Object[] { this });
		PropertyValue interceptors = propertyValues.getPropertyValue("interceptors");
		Object[] values = (Object[]) interceptors.getValue();
		for (Object value : values) {
			System.out.println("PassportInterceptor postProcessBeanFactory  PropertyValue:" + value);
		}
	}

	@Override
	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		ConfigurableListableBeanFactory factory = ((ConfigurableListableBeanFactory) beanFactory);
		for (String beanName : factory.getBeanDefinitionNames()) {
			BeanDefinition beanDefinition = factory.getBeanDefinition(beanName);
			if (isHandlerMapping(beanDefinition)) {
				System.out.println("PassportInterceptor setBeanFactory source:" + beanDefinition.getSource());
				this.registerInterceptors(beanDefinition);
			}
		}
	}

	@Override
	public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
		// if (bean instanceof RequestMappingHandlerMapping) {
		// System.out.println("PassportInterceptor postProcessBeforeInitialization:" + bean);
		// // RequestMappingHandlerMapping handlerMapping = (RequestMappingHandlerMapping) bean;
		// // handlerMapping.setInterceptors(new Object[] { this });
		// }
		return bean;
	}

	@Override
	public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
		return bean;
	}

}
