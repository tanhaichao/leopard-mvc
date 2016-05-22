package io.leopard.web.xparam;

import javax.servlet.http.HttpServletRequest;

import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Service;

/**
 * 获取session中的uid.
 * 
 * @author 阿海
 * 
 */
@Service
public class SessUidXParam implements XParam {

	@Override
	public Object getValue(HttpServletRequest request, MethodParameter parameter) {
		Number sessUid = (Number) request.getSession().getAttribute("passport");
		if (sessUid == null) {
			String ip = XParamUtil.getProxyIp(request);
			throw new NotLoginException("您[" + ip + "]未登录.");
		}
		if (sessUid instanceof Integer) {
			return new Long((Integer) sessUid);
		}
		if (sessUid instanceof Long) {
			return sessUid;
		}
		throw new UnsupportedOperationException("未知类型[" + sessUid.getClass().getName() + "].");
	}

	@Override
	public String getKey() {
		return "sessUid";
	}

	@Override
	public void override(XParam xparam) {

	}
}
