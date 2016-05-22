package io.leopard.web.passport;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpServletRequest;

import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.MethodParameter;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.web.method.HandlerMethod;

public class PassportCheckerHandlerMethodImpl implements PassportChecker {
	private static final Set<String> parameterNameSet = new HashSet<String>();
	static {
		parameterNameSet.add("sessUid");
		parameterNameSet.add("sessUsername");
	}

	private Map<String, Boolean> handlerCacheMap = new ConcurrentHashMap<String, Boolean>();
	private final ParameterNameDiscoverer parameterNameDiscoverer = new LocalVariableTableParameterNameDiscoverer();

	public boolean hasPassportParameter(HandlerMethod handlerMethod) {
		MethodParameter[] parameters = handlerMethod.getMethodParameters();
		if (parameters != null) {
			for (MethodParameter parameter : parameters) {
				parameter.initParameterNameDiscovery(parameterNameDiscoverer);
				String parameterName = parameter.getParameterName();
				if (parameterNameSet.contains(parameterName)) {
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public Boolean isNeedCheckLogin(HttpServletRequest request, Object handler) {
		if (!(handler instanceof HandlerMethod)) {
			return false;
		}
		String key = handler.toString();
		Boolean contain = handlerCacheMap.get(key);
		if (contain != null) {
			return contain;
		}
		boolean hasPassportParameter = this.hasPassportParameter((HandlerMethod) handler);
		// System.out.println("hasPassportParameter:" + hasPassportParameter);
		if (hasPassportParameter) {
			handlerCacheMap.put(key, hasPassportParameter);
			return true;
		}
		return null;
	}

}
