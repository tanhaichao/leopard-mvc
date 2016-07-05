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

	private List<PassportValidator> list = new ArrayList<PassportValidator>();

	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		DefaultListableBeanFactory factory = (DefaultListableBeanFactory) beanFactory;
		Map<String, PassportValidator> map = factory.getBeansOfType(PassportValidator.class);
		for (Entry<String, PassportValidator> entry : map.entrySet()) {
			list.add(new PassportValidatorWrapper(entry.getValue()));
		}
		passportChecker.setBeanFactory(beanFactory);
	}

	public static String getSessionKey(PassportValidator validator) {
		PassportGroup anno = validator.getClass().getAnnotation(PassportGroup.class);
		if (anno == null || StringUtils.isEmpty(anno.value())) {
			return "sessUid";
		}
		return anno.value();
	}

	public PassportValidator find(String type) {
		for (PassportValidator validator : list) {
			if (type.equals(getSessionKey(validator))) {
				return validator;
			}
		}
		throw new IllegalArgumentException("找不到通行证验证器[" + type + "]的实现.");
	}

	public List<PassportValidator> find(HttpServletRequest request, Object handler) {
		List<PassportValidator> list = new ArrayList<PassportValidator>();
		for (PassportValidator validator : this.list) {
			Boolean isNeedCheckLogin = validator.isNeedCheckLogin(request, handler);
			if (isNeedCheckLogin == null) {
				isNeedCheckLogin = passportChecker.isNeedCheckLogin(request, handler);
			}
			if (isNeedCheckLogin != null && isNeedCheckLogin) {
				list.add(validator);
			}
		}
		return list;
	}
}
