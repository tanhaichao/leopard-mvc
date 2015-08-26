package io.leopard.web.passport.xparam;

import io.leopard.web.passport.NotLoginException;
import io.leopard.web.passport.RequestUtil;
import io.leopard.web.passport.SessionUtil;
import io.leopard.web4j.xparam.XParam;

import javax.servlet.http.HttpServletRequest;

import org.springframework.core.MethodParameter;

/**
 * 获取session中的uid.
 * 
 * @author 阿海
 * 
 */

public class SessUidXParam implements XParam {

	@Override
	public Object getValue(HttpServletRequest request, MethodParameter parameter) {
		Long sessUid = SessionUtil.getUid(request.getSession());
		if (sessUid == null) {
			String ip = RequestUtil.getProxyIp(request);
			throw new NotLoginException("您[" + ip + "]未登录.");
		}
		return sessUid;
		// throw new UnsupportedOperationException("Not Impl.");
	}

	@Override
	public String getKey() {
		return "sessUid";
	}
}
