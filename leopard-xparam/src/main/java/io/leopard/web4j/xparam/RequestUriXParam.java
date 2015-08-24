package io.leopard.web4j.xparam;

import javax.servlet.http.HttpServletRequest;

import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Service;

@Service
public class RequestUriXParam implements XParam {

	@Override
	public Object getValue(HttpServletRequest request, MethodParameter parameter) {
		// 包含contextPath
		String requestUri = request.getRequestURI();
		return requestUri;
	}

	@Override
	public String getKey() {
		return "requestUri";
	}

}
