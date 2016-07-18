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
		// 分布式session还不够好，Long类型存进去再拿出来会变成Integer，这里做兼容
		Number sessUid = (Number) request.getSession().getAttribute("sessUid");
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

	// @Override
	// public void override(XParam xparam) {
	//
	// }
}
