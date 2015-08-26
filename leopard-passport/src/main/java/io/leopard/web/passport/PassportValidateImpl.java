package io.leopard.web.passport;

import io.leopard.web.servlet.RequestUtil;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.ServiceLoader;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class PassportValidateImpl implements PassportValidate {

	private static final String SESSION_KEY = "passport";

	protected Log logger = LogFactory.getLog(this.getClass());

	private PassportValidate passportValidate = null;

	public PassportValidateImpl() {
		Iterator<PassportValidate> iterator = ServiceLoader.load(PassportValidate.class).iterator();
		if (iterator.hasNext()) {
			this.passportValidate = iterator.next();
		}
	}

	private PassportValidate getPassportValidate() {
		if (passportValidate == null) {
			throw new NullPointerException("PassportValidate接口未实现.");
		}
		return passportValidate;
	}

	@Override
	public Object validate(HttpServletRequest request, HttpServletResponse response) {
		Object passport = request.getSession().getAttribute(SESSION_KEY);
		if (passport != null) {
			return passport;
		}
		passport = getPassportValidate().validate(request, response);
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
		if (getPassportValidate().showLoginBox(request, response)) {
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

}
