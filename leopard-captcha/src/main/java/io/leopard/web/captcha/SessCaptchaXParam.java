package io.leopard.web.captcha;

import io.leopard.web.xparam.XParam;

import javax.servlet.http.HttpServletRequest;

import org.springframework.core.MethodParameter;

/**
 * 获取session中的验证码.
 * 
 * @author 阿海
 * 
 */
public class SessCaptchaXParam implements XParam {

	@Override
	public String getKey() {
		// new Exception("SessCaptchaXParam getKey").printStackTrace();
		return "sessCaptcha";
	}

	@Override
	public Object getValue(HttpServletRequest request, MethodParameter parameter) {
		String captchaGroupId = this.getGroupId(parameter);
		return CaptchaUtil.getCodeAndRemove(request, captchaGroupId);
	}

	private String getGroupId(MethodParameter parameter) {
		CaptchaGroup captchaGroup = parameter.getMethod().getAnnotation(CaptchaGroup.class);
		if (captchaGroup != null) {
			return captchaGroup.value();
		}
		return null;
	}

}
