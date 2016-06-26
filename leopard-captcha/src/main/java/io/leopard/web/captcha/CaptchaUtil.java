package io.leopard.web.captcha;

import javax.servlet.http.HttpServletRequest;

import org.springframework.util.StringUtils;

public class CaptchaUtil {

	private static final String SESSION_KEY = "sessCaptcha";

	// private static String getSessionKey(HttpServletRequest request) {
	// String captchaGroupId = (String) request.getAttribute("captchaGroupId");
	// return getSessionKey(captchaGroupId);
	// }

	public static void checkCaptcha(String captcha, String sessCaptcha) {
		if (StringUtils.isEmpty(captcha)) {
			throw new IllegalArgumentException("验证码不能为空.");
		}
		if (!captcha.equals(sessCaptcha)) {
			throw new IllegalArgumentException("验证码不正确.");
		}
	}

	private static String getSessionKey(String captchaGroupId) {
		if (captchaGroupId == null || captchaGroupId.length() == 0) {
			return SESSION_KEY;
		}
		else {
			return SESSION_KEY + ":" + captchaGroupId;
		}
	}

	public static String getCaptchaGroupId(HttpServletRequest request) {
		return (String) request.getAttribute("captchaGroupId");
	}

	public static void saveSession(HttpServletRequest request, String captchaGroupId, String code) {
		String sessionKey = getSessionKey(captchaGroupId);
		System.out.println("saveSession:" + sessionKey + " captchaGroupId:" + captchaGroupId + " code:" + code);
		request.getSession().setAttribute(sessionKey, code);
	}

	public static String getCode(HttpServletRequest request) {
		String sessionKey = getSessionKey(getCaptchaGroupId(request));
		return (String) request.getSession().getAttribute(sessionKey);
	}

	public static String getCode(HttpServletRequest request, String captchaGroupId) {
		String sessionKey = getSessionKey(captchaGroupId);
		System.out.println("getCode:" + sessionKey + " captchaGroupId:" + captchaGroupId);
		return (String) request.getSession().getAttribute(sessionKey);
	}

	public static String getCodeAndRemove(HttpServletRequest request, String captchaGroupId) {
		String sessionKey = getSessionKey(captchaGroupId);
		System.out.println("getCode:" + sessionKey + " captchaGroupId:" + captchaGroupId);
		String code = (String) request.getSession().getAttribute(sessionKey);
		if (code != null) {
			request.getSession().removeAttribute(sessionKey);
		}
		return code;
	}
}
