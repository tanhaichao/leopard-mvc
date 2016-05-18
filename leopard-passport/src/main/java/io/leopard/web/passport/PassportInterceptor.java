package io.leopard.web.passport;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import io.leopard.web.servlet.RegisterHandlerInterceptor;

/**
 * 检查是否已登录.
 * 
 * @author 阿海
 * 
 */
@Component
// TODO ahai 在site项目必须实现BeanPostProcessor接口才能成功配置拦截器.
@Order(9)
public class PassportInterceptor extends RegisterHandlerInterceptor {

	protected Log logger = LogFactory.getLog(this.getClass());

	private PassportCheckerImpl passportChecker = new PassportCheckerImpl();
	private PassportValidate passportValidateLei = PassportValidateImpl.getInstance();

	@Override
	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		super.setBeanFactory(beanFactory);
		// System.err.println("setBeanFactory setBeanFactory:" + beanFactory);
		passportChecker.setBeanFactory(beanFactory);
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		Boolean isNeedCheckLogin = passportChecker.isNeedCheckLogin(request, handler);
		if (isNeedCheckLogin == null) {
			return true;
		}
		// System.out.println("PassportInterceptor preHandle:" + isNeedCheckLogin + " " + handler);
		if (!isNeedCheckLogin) {
			return true;
		}

		Object account = passportValidateLei.validate(request, response);
		if (account == null) {
			passportValidateLei.showLoginBox(request, response);
			return false;
		}
		return true;
	}

}
