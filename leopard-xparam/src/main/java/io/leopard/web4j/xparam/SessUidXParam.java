package io.leopard.web4j.xparam;

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
		Long sessUid = (Long) request.getSession().getAttribute("uid");
		if (sessUid == null) {
			String ip = XParamUtil.getProxyIp(request);
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
