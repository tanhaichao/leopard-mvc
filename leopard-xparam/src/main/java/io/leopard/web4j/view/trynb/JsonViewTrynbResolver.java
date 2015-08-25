package io.leopard.web4j.view.trynb;

import io.leopard.mvc.trynb.model.TrynbInfo;
import io.leopard.mvc.trynb.resolver.TrynbResolver;
import io.leopard.web4j.view.JsonView;
import io.leopard.web4j.view.StatusCodeException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;

public class JsonViewTrynbResolver implements TrynbResolver {

	// public JsonViewTrynbResolver() {
	// new Exception("JsonViewTrynbResolver").printStackTrace();
	// }

	@Override
	public ModelAndView resolveView(HttpServletRequest request, HttpServletResponse response, HandlerMethod handler, Exception exception, TrynbInfo trynbInfo) {
		Class<?> returnType = handler.getMethod().getReturnType();
		if (!JsonView.class.isAssignableFrom(returnType)) {
			return null;
		}

		JsonView jsonView = new JsonView();
		if (exception instanceof StatusCodeException) {
			StatusCodeException e = (StatusCodeException) exception;
			jsonView.setStatus(e.getStatus());
			jsonView.setMessage(e.getMessage());
		}
		else {
			jsonView.setStatus(trynbInfo.getStatusCode());
			jsonView.setMessage(trynbInfo.getMessage());
		}
		return jsonView;
	}

}
