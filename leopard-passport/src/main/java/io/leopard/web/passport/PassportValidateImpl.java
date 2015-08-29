package io.leopard.web.passport;

import io.leopard.web.servlet.RequestUtil;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class PassportValidateImpl implements PassportValidate {

	private static final String SESSION_KEY = "passport";

	protected Log logger = LogFactory.getLog(this.getClass());

	private PassportValidate passportValidate = new PassportValidateLoaderImpl();

	private final static PassportValidate instance = new PassportValidateImpl();

	public static PassportValidate getInstance() {
		return instance;
	}

	private PassportValidateImpl() {

	}

	@Override
	public Object validate(HttpServletRequest request, HttpServletResponse response) {
		Object passport = request.getSession().getAttribute(SESSION_KEY);
		if (passport != null) {
			return passport;
		}
		passport = passportValidate.validate(request, response);
		if (passport != null) {
			request.getSession().setAttribute(SESSION_KEY, passport);
		}
		return null;
	}

	@Override
	public boolean showLoginBox(HttpServletRequest request, HttpServletResponse response) {
		String ip = RequestUtil.getProxyIp(request);
		String message = "您[" + ip + "]未登录,uri:" + request.getRequestURI();
		logger.info(message);
		if (passportValidate.showLoginBox(request, response)) {
			return true;
		}

		FtlView view = new FtlView("/passport/ftl", "login");
		String url = RequestUtil.getRequestURL(request);
		String queryString = request.getQueryString();
		if (queryString != null && queryString.length() > 0) {
			url += "?" + queryString;
		}
		Map<String, Object> model = new HashMap<String, Object>();

		model.put("url", url);
		try {
			view.render(model, request, response);
			return true;
		}
		catch (Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	@Override
	public boolean login(HttpServletRequest request, HttpServletResponse response) throws IOException {
		return passportValidate.login(request, response);
	}

}
