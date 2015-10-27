package io.leopard.web.xparam;

import javax.servlet.http.HttpServletRequest;

import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Service;

/**
 * 获取当前页码.
 * 
 * @author 阿海
 * 
 */
@Service
public class SizeXParam implements XParam {

	@Override
	public Object getValue(HttpServletRequest request, MethodParameter parameter) {
		int size = XParamUtil.toInt(request.getParameter("size"));
		if (size <= 0) {
			None none = parameter.getParameterAnnotation(None.class);
			if (none != null) {
				size = Integer.parseInt(none.value());
			}
		}
		if (size <= 0) {
			size = 10;
		}
		return size;
	}

	@Override
	public String getKey() {
		return "size";
	}

}
