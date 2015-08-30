package io.leopard.web.captcha;

import javax.servlet.http.HttpServletRequest;

public class CaptchaUtil {

	private static final String SESSION_KEY = "sessCaptcha";

	private static String getSessionKey(HttpServletRequest request) {
		String captchaGroupId = (String) request.getAttribute("captchaGroupId");
		return getSessionKey(captchaGroupId);
	}

	private static String getSessionKey(String captchaGroupId) {
		if (captchaGroupId == null || captchaGroupId.length() == 0) {
			return SESSION_KEY;
		}
		else {
			return SESSION_KEY + ":" + captchaGroupId;
		}
	}

	public static void saveSession(HttpServletRequest request, String code) {
		String sessionKey = getSessionKey(request);
		request.getSession().setAttribute(sessionKey, code);
	}

	public static String getCode(HttpServletRequest request) {
		String sessionKey = getSessionKey(request);
		return (String) request.getSession().getAttribute(sessionKey);
	}

	public static String getCode(HttpServletRequest request, String captchaGroupId) {
		String sessionKey = getSessionKey(captchaGroupId);
		return (String) request.getSession().getAttribute(sessionKey);
	}
}
