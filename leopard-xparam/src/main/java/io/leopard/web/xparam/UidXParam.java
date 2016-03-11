package io.leopard.web.xparam;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Service;

/**
 * 根据username解析uid.
 * 
 * @author 阿海
 * 
 */
@Service
public class UidXParam implements XParam {

	@Override
	public Object getValue(HttpServletRequest request, MethodParameter parameter) {
		String uid = request.getParameter("uid");
		if (StringUtils.isNotEmpty(uid)) {
			return NumberUtils.toLong(uid);
		}
		String username = request.getParameter("username");
		
		return 0L;
	}

	@Override
	public String getKey() {
		return "uid";
	}
}
