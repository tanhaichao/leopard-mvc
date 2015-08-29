package io.leopard.web.passport;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.ServiceLoader;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.web.servlet.FrameworkServlet;
import org.springframework.web.servlet.support.RequestContextUtils;

public class PassportValidateLoaderImpl implements PassportValidate {

	private PassportValidate passportValidate = null;

	private PassportValidate getPassportValidate(HttpServletRequest request) {
		if (passportValidate != null) {
			return passportValidate;
		}
		this.passportValidate = this.getPassportValidateByApplicationContext(request);
		System.out.println("getPassportValidateByApplicationContext:" + passportValidate);
		if (passportValidate == null) {
			this.passportValidate = this.getPassportValidateByServiceLoader();
		}
		return passportValidate;
	}

	private PassportValidate getPassportValidateByApplicationContext(HttpServletRequest request) {
		String attributeName = null;
		{
			Enumeration<String> e = request.getServletContext().getAttributeNames();
			while (e.hasMoreElements()) {
				String name = e.nextElement();
				if (name.startsWith(FrameworkServlet.SERVLET_CONTEXT_PREFIX)) {
					attributeName = name;
					break;
				}
			}
			if (attributeName == null) {
				return null;
			}
		}
		ApplicationContext context = (ApplicationContext) request.getServletContext().getAttribute(attributeName);
		// ApplicationContext context = ContextLoader.getCurrentWebApplicationContext();
		if (context == null) {
			return null;
		}
		try {
			return context.getBean(PassportValidate.class);
		}
		catch (BeansException e) {
			// TODO ahai
			return null;
		}
	}

	private PassportValidate getPassportValidateByServiceLoader() {
		Iterator<PassportValidate> iterator = ServiceLoader.load(PassportValidate.class).iterator();
		if (iterator.hasNext()) {
			return iterator.next();
		}
		return null;
	}

	@Override
	public Object validate(HttpServletRequest request, HttpServletResponse response) {
		PassportValidate passportValidate = this.getPassportValidate(request);
		if (passportValidate == null) {
			throw new UnsupportedOperationException("PassportValidate接口未实现.");
		}
		return passportValidate.validate(request, response);
	}

	@Override
	public boolean showLoginBox(HttpServletRequest request, HttpServletResponse response) {
		PassportValidate passportValidate = this.getPassportValidate(request);
		if (passportValidate == null) {
			throw new UnsupportedOperationException("PassportValidate接口未实现.");
		}
		return passportValidate.showLoginBox(request, response);
	}

	@Override
	public boolean login(HttpServletRequest request, HttpServletResponse response) throws IOException {
		PassportValidate passportValidate = this.getPassportValidate(request);
		if (passportValidate == null) {
			return false;
		}
		return passportValidate.login(request, response);
	}

}
