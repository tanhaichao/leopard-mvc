package io.leopard.web.captcha;

import org.springframework.core.MethodParameter;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.method.support.ModelAndViewContainer;

//@Component
public class CaptchaHandlerMethodReturnValueHandler implements HandlerMethodReturnValueHandler {

	@Override
	public boolean supportsReturnType(MethodParameter returnType) {
		boolean supportsReturnType = CaptchaView.class.isAssignableFrom(returnType.getParameterType());
		System.out.println("CaptchaHandlerMethodReturnValueHandler supportsReturnType:" + supportsReturnType);
		return supportsReturnType;
	}

	@Override
	public void handleReturnValue(Object returnValue, MethodParameter returnType, ModelAndViewContainer mavContainer, NativeWebRequest webRequest) throws Exception {

	}

}
