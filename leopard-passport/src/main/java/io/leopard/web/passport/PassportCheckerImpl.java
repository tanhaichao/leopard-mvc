package io.leopard.web.passport;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import io.leopard.web.servlet.RequestUtil;
import io.leopard.web.servlet.UriListChecker;

public class PassportCheckerImpl implements PassportChecker {

	protected UriListChecker uriListChecker = new UriListChecker();// 需要做登录验证的URL列表

	// private PassportChecker passportCheckerHandlerMethodImpl = new PassportCheckerHandlerMethodImpl();

	private static List<PassportChecker> passportCheckerList = new ArrayList<PassportChecker>();

	static {
		passportCheckerList.add(new PassportCheckerHandlerMethodImpl());
	}

	public static void addPassportChecker(PassportChecker checker) {
		new Exception("addPassportChecker").printStackTrace();
		passportCheckerList.add(checker);
	}

	@Override
	public boolean isNeedCheckLogin(HttpServletRequest request, Object handler) {
		for (PassportChecker checker : passportCheckerList) {
			boolean isNeedCheckLogin = checker.isNeedCheckLogin(request, handler);
			if (isNeedCheckLogin) {
				return true;
			}
		}
		if (uriListChecker.exists(RequestUtil.getRequestContextUri(request))) {
			return true;
		}
		return false;
	}
}
