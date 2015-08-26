package io.leopard.web.passport.xparam;

import io.leopard.web.passport.NotLoginException;
import io.leopard.web.passport.RequestUtil;
import io.leopard.web.passport.SessionUtil;
import io.leopard.web4j.xparam.XParam;

import javax.servlet.http.HttpServletRequest;

import org.springframework.core.MethodParameter;

/**
 * 获取存在session中的用户名.
 * 
 * @author 阿海
 * 
 */

public class SessUsernameXParam implements XParam {

	@Override
	public Object getValue(HttpServletRequest request, MethodParameter parameter) {
		String sessUsername = SessionUtil.getUsername(request.getSession());
		if (sessUsername == null) {
			String ip = RequestUtil.getProxyIp(request);
			throw new NotLoginException("您[" + ip + "]未登录.");

		}
		return sessUsername;
		// throw new UnsupportedOperationException("Not Impl.");
	}

	@Override
	public String getKey() {
		return "sessUsername";
	}
}
