package io.leopard.web.passport;

import io.leopard.web.servlet.RequestUtil;
import io.leopard.web.servlet.UriListChecker;

import javax.servlet.http.HttpServletRequest;

public class PassportCheckerImpl implements PassportChecker {

	protected UriListChecker uriListChecker = new UriListChecker();// 需要做登录验证的URL列表

	private PassportChecker passportCheckerHandlerMethodImpl = new PassportCheckerHandlerMethodImpl();

	@Override
	public boolean isNeedCheckLogin(HttpServletRequest request, Object handler) {
		// System.out.println("PassportCheckerImpl:" + handler.getClass());
		if (passportCheckerHandlerMethodImpl.isNeedCheckLogin(request, handler)) {
			return true;
		}
		if (uriListChecker.exists(RequestUtil.getRequestContextUri(request))) {
			return true;
		}
		return false;
	}
}
