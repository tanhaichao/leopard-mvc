package io.leopard.web.passport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ServiceLoader;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;
import org.springframework.web.servlet.FrameworkServlet;

public class PassportValidateLoaderImpl implements PassportValidate {

	private PassportValidate passportValidate = null;

	private PassportValidate getPassportValidate(HttpServletRequest request) {
		if (passportValidate != null) {
			return passportValidate;
		}
		this.passportValidate = this.getPassportValidateByApplicationContext(request);
		// System.out.println("getPassportValidateByApplicationContext:" + passportValidate);
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
		Map<String, PassportValidate> beans = BeanFactoryUtils.beansOfTypeIncludingAncestors(context, PassportValidate.class);
		if (beans == null || beans.isEmpty()) {
			return null;
		}
		List<PassportValidate> beanList = new ArrayList<PassportValidate>(beans.values());
		AnnotationAwareOrderComparator.sort(beanList);
		// for (PassportValidate bean : beanList) {
		// System.err.println("PassportValidate:" + bean);
		// }
		return beanList.get(0);
		//
		// try {
		// return context.getBean(PassportValidate.class);
		// }
		// catch (NoUniqueBeanDefinitionException e) {
		// throw e;
		// }
		// catch (NoSuchBeanDefinitionException e) {
		// // TODO ahai
		// return null;
		// }
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
