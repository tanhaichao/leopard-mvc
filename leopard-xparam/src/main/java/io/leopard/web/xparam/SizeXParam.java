package io.leopard.web.xparam;

import java.lang.reflect.Parameter;

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
			size = this.getSize(parameter);
		}
		if (size <= 0) {
			size = 10;
		}
		request.setAttribute("paging_size", size);// TODO ahai 耦合
		return size;
	}

	protected int getSize(MethodParameter parameter) {
		if (this.getKey().equalsIgnoreCase(parameter.getParameterName())) {
			None none = parameter.getParameterAnnotation(None.class);
			if (none == null) {
				return 0;
			}
			else {
				return Integer.parseInt(none.value());
			}
		}

		String[] names = XParamUtil.getParameterNames(parameter);
		Parameter[] parameters = parameter.getMethod().getParameters();

		for (int i = 0; i < names.length; i++) {
			if (this.getKey().equalsIgnoreCase(names[i])) {
				None none = parameters[i].getAnnotation(None.class);
				if (none == null) {
					return 0;
				}
				else {
					return Integer.parseInt(none.value());
				}
			}
		}
		return 0;
	}

	@Override
	public String getKey() {
		return "size";
	}

}
