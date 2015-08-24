package io.leopard.web4j.xparam;

import javax.servlet.http.HttpServletRequest;

import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Service;

/**
 * 获取session中的验证码.
 * 
 * @author 阿海
 * 
 */
@Service
public class SessCaptchaXParam implements XParam {

	@Override
	public Object getValue(HttpServletRequest request, MethodParameter parameter) {
		// HttpSession session = request.getSession();
		//
		// String captchaGroupId = (String) request.getAttribute("captchaGroupId");
		// String sessionKey = CaptchaView.getSessionKey(captchaGroupId);
		// String code = (String) session.getAttribute(sessionKey);
		// if (StringUtils.isEmpty(code)) {
		// throw new IllegalArgumentException("验证码未生成.");
		// }
		// session.removeAttribute(sessionKey);
		// return code;
		throw new UnsupportedOperationException("Not Impl.");
	}

	@Override
	public String getKey() {
		return "sessCaptcha";
	}

}
