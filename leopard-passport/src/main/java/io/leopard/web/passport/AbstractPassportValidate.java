package io.leopard.web.passport;

import io.leopard.web.servlet.RequestUtil;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 通行证登陆验证抽象实现.
 * 
 * @author 阿海
 *
 */
public abstract class AbstractPassportValidate implements PassportValidate {

	@Override
	public void showLoginBox(HttpServletRequest request, HttpServletResponse response) {
		FtlView view = this.getView();
		String url = RequestUtil.getRequestURL(request);
		String queryString = request.getQueryString();
		if (queryString != null && queryString.length() > 0) {
			url += "?" + queryString;
		}
		Map<String, Object> model = new HashMap<String, Object>();

		model.put("url", url);
		try {
			view.render(model, request, response);
		}
		catch (Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	protected FtlView getView() {
		return new FtlView("/passport/ftl", "login");
	}
}
