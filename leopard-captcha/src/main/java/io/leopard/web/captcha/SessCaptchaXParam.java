package io.leopard.web.captcha;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.MethodParameter;

import io.leopard.web.xparam.XParam;

/**
 * 获取session中的验证码.
 * 
 * @author 阿海
 * 
 */
public class SessCaptchaXParam implements XParam {

	protected Log logger = LogFactory.getLog(this.getClass());

	@Override
	public String getKey() {
		return "sessCaptcha";
	}

	@Override
	public Object getValue(HttpServletRequest request, MethodParameter parameter) {
		String captchaGroupId = this.getGroupId(parameter);
		Object captcha = CaptchaUtil.getCodeAndRemove(request, captchaGroupId);
		// logger.info("getValue captcha:" + captcha + " captchaGroupId:" + captchaGroupId);
		return captcha;
	}

	private String getGroupId(MethodParameter parameter) {
		CaptchaGroup captchaGroup = parameter.getMethod().getAnnotation(CaptchaGroup.class);
		if (captchaGroup != null) {
			return captchaGroup.value();
		}
		return null;
	}

	// @Override
	// public void override(XParam xparam) {
	//
	// }

}
