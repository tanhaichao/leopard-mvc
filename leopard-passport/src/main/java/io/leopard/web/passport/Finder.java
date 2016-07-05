package io.leopard.web.passport;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.util.StringUtils;

public class Finder {

	private PassportCheckerImpl passportChecker = new PassportCheckerImpl();

	private static Finder instance = new Finder();

	public static Finder getInstance() {
		return instance;
	}

	private Finder() {
	}

	// private List<PassportValidator> list = Collections.synchronizedList(new ArrayList<PassportValidator>());
	private List<PassportValidator> list = new ArrayList<PassportValidator>();

	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		DefaultListableBeanFactory factory = (DefaultListableBeanFactory) beanFactory;
		Map<String, PassportValidator> map = factory.getBeansOfType(PassportValidator.class);
		for (Entry<String, PassportValidator> entry : map.entrySet()) {
			System.err.println("entry.getValue():" + entry.getValue());
			list.add(new PassportValidatorWrapper(entry.getValue()));
		}
		// System.err.println("list:" + list);
		passportChecker.setBeanFactory(beanFactory);
	}

	public PassportValidator find(String type) {
		for (PassportValidator validator : list) {
			if (type.equals(getSessionKey(validator))) {
				System.err.println("find:" + validator + " sessionKey:" + getSessionKey(validator));
				return validator;
			}
		}
		throw new IllegalArgumentException("找不到通行证验证器[" + type + "]的实现.");
	}

	public static String getSessionKey(PassportValidator validator) {
		if (validator instanceof PassportValidatorWrapper) {
			validator = ((PassportValidatorWrapper) validator).getValidator();
		}

		PassportGroup anno = validator.getClass().getAnnotation(PassportGroup.class);
		if (anno == null || StringUtils.isEmpty(anno.value())) {
			return "sessUid";
		}
		return anno.value();
	}

	public List<PassportValidator> find(HttpServletRequest request, Object handler) {
		List<PassportValidator> list = new ArrayList<PassportValidator>();
		for (PassportValidator validator : this.list) {
			Boolean isNeedCheckLogin = validator.isNeedCheckLogin(request, handler);
			// System.err.println("validator:" + validator + " isNeedCheckLogin:" + isNeedCheckLogin + " request:" + request.getRequestURI());

			if (isNeedCheckLogin == null) {
				isNeedCheckLogin = passportChecker.isNeedCheckLogin(request, handler);
				// System.err.println("default:" + validator + " isNeedCheckLogin:" + isNeedCheckLogin + " request:" + request.getRequestURI());

			}
			if (isNeedCheckLogin != null && isNeedCheckLogin) {
				// System.err.println("validator:" + Finder.getSessionKey(validator) + " isNeedCheckLogin:" + isNeedCheckLogin + " request:" + request.getRequestURI());
				list.add(validator);
			}
		}

		return list;
	}
}
