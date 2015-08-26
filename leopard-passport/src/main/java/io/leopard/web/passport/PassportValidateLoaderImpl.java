package io.leopard.web.passport;

import java.util.Iterator;
import java.util.ServiceLoader;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.ContextLoader;

public class PassportValidateLoaderImpl implements PassportValidate {

	private PassportValidate passportValidate = null;

	private PassportValidate getPassportValidate() {
		if (passportValidate != null) {
			return null;
		}
		this.passportValidate = this.getPassportValidateByApplicationContext();
		if (passportValidate == null) {
			this.passportValidate = this.getPassportValidateByServiceLoader();
			if (passportValidate == null) {
				throw new NullPointerException("PassportValidate接口未实现.");
			}
		}
		return passportValidate;
	}

	private PassportValidate getPassportValidateByApplicationContext() {
		ApplicationContext context = ContextLoader.getCurrentWebApplicationContext();
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
		return getPassportValidate().validate(request, response);
	}

	@Override
	public boolean showLoginBox(HttpServletRequest request, HttpServletResponse response) {
		return getPassportValidate().showLoginBox(request, response);
	}

	@Override
	public boolean login(HttpServletRequest request, HttpServletResponse response) {
		return getPassportValidate().login(request, response);
	}

}
