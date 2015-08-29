package io.leopard.web.passport;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.config.BeanDefinition;
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
public class PassportInterceptor implements HandlerInterceptor, BeanFactoryAware {
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

	@Override
	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		ConfigurableListableBeanFactory factory = ((ConfigurableListableBeanFactory) beanFactory);
		// BeanDefinition beanDefinition = ((ConfigurableListableBeanFactory) beanFactory).getBeanDefinition("io.leopard.web.passport.RequestMappingHandlerMapping#0");
		String[] names = factory.getBeanDefinitionNames();
		for (String beanName : names) {
			BeanDefinition beanDefinition = factory.getBeanDefinition(beanName);
			this.registerInterceptors(beanDefinition);
		}
	}

	private void registerInterceptors(BeanDefinition beanDefinition) {
		String beanClassName = beanDefinition.getBeanClassName();
		Class<?> clazz;
		try {
			clazz = Class.forName(beanClassName);
		}
		catch (ClassNotFoundException e) {
			return;
		}
		if (!RequestMappingHandlerMapping.class.isAssignableFrom(clazz)) {
			return;
		}
		MutablePropertyValues propertyValues = beanDefinition.getPropertyValues();
		// System.out.println("postProcessBeanFactory:" + beanClassName);
		propertyValues.addPropertyValue("interceptors", new Object[] { this });
	}
}
