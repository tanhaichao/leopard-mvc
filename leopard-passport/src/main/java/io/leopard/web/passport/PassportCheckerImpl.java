package io.leopard.web.passport;

import java.util.HashSet;
import java.util.Set;

import io.leopard.web.servlet.RequestUtil;
import io.leopard.web.servlet.UriListChecker;

import javax.servlet.http.HttpServletRequest;

import org.springframework.core.MethodParameter;
import org.springframework.web.method.HandlerMethod;

public class PassportCheckerImpl implements PassportChecker {

	protected UriListChecker uriListChecker = new UriListChecker();// 需要做登录验证的URL列表
	private Set<String> handlerSet = new HashSet<String>();
	private static Set<String> parameterNameSet = new HashSet<String>();

	static {
		parameterNameSet.add("sessUid");
		parameterNameSet.add("sessUsername");
	}

	public void registerHandlerMethod(HandlerMethod handlerMethod) {
		MethodParameter[] parameters = handlerMethod.getMethodParameters();
		if (parameters != null) {
			for (MethodParameter parameter : parameters) {
				String parameterName = parameter.getParameterName();
				if (parameterNameSet.contains(parameterName)) {
					handlerSet.add(handlerMethod.toString());
				}
			}
		}
	}

	@Override
	public boolean isNeedCheckLogin(HttpServletRequest request, Object handler) {
		System.err.println("PassportInterceptor handler:" + handler);
		if (handlerSet.contains(handler.toString())) {
			return true;
		}
		if (uriListChecker.exists(RequestUtil.getRequestContextUri(request))) {
			return true;
		}
		return false;
	}
}
