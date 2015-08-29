package io.leopard.web.passport;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
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
public class PassportInterceptor implements HandlerInterceptor, BeanPostProcessor, BeanFactoryAware, ApplicationContextAware {
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
	public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
		// System.out.println("postProcessBeforeInitialization:" + bean);
		if (bean instanceof RequestMappingHandlerMapping) {
			System.out.println("PassportBeanPostProcessor bean:" + bean);
			// this.registerInterceptor((RequestMappingHandlerMapping) bean);
		}
		return bean;
	}

	private void registerInterceptor(RequestMappingHandlerMapping handlerMapping) {
		handlerMapping.setInterceptors(new Object[] { this });
	}

	@Override
	public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
		return bean;
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		System.out.println("PassportInterceptor setApplicationContext:" + applicationContext);

	}

	//
	@Override
	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		// PassportBeanPostProcessor bean:org.springframework.web.servlet.handler.BeanNameUrlHandlerMapping@9b2dc56
		// PassportBeanPostProcessor bean:org.springframework.web.servlet.mvc.annotation.DefaultAnnotationHandlerMapping@6d6cd1e0
		System.out.println("PassportInterceptor setBeanFactory:" + beanFactory);

		RequestMappingHandlerMapping handlerMapping = beanFactory.getBean(RequestMappingHandlerMapping.class);
		handlerMapping.setInterceptors(new Object[] { this });

	}
}
