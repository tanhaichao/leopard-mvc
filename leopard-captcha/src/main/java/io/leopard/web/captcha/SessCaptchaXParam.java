package io.leopard.web.captcha;

import io.leopard.web.xparam.XParam;

import javax.servlet.http.HttpServletRequest;

import org.springframework.core.MethodParameter;

public class SessCaptchaXParam implements XParam {

	@Override
	public String getKey() {
		return "sessCaptcha";
	}

	@Override
	public Object getValue(HttpServletRequest request, MethodParameter parameter) {
		String captchaGroupId = this.getGroupId(parameter);
		return CaptchaUtil.getCode(request, captchaGroupId);
	}

	private String getGroupId(MethodParameter parameter) {
		CaptchaGroup captchaGroup = parameter.getMethod().getAnnotation(CaptchaGroup.class);
		if (captchaGroup != null) {
			return captchaGroup.value();
		}
		return null;
	}

}
