package io.leopard.web.passport;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

/**
 * 检查是否已登录.
 * 
 * @author 阿海
 * 
 */

public class PassportInterceptor implements HandlerInterceptor {
	protected Log logger = LogFactory.getLog(this.getClass());

	private PassportChecker passportChecker = new PassportCheckerImpl();
	private PassportValidate passportValidateLei = new PassportValidateImpl();

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		if (!passportChecker.isNeedCheckLogin(request, handler)) {
			return true;
		}
		Object account = passportValidateLei.validate(request, response);
		if (account == null) {
			passportValidateLei.showLoginBox(request, response);
			return false;
		}
		// FrequencyInterceptor.setAccount(request, account);
		request.setAttribute("account", account);
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

	}

}
