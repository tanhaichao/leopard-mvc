package io.leopard.convert;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;

public class ConverterContext implements BeanFactoryAware {

	@SuppressWarnings("rawtypes")
	private static List<Converter> converterList = new ArrayList<Converter>();

	@SuppressWarnings("rawtypes")
	@Override
	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		Map<String, Converter> beanMap = ((DefaultListableBeanFactory) beanFactory).getBeansOfType(Converter.class);
		for (Entry<String, Converter> entry : beanMap.entrySet()) {
			converterList.add(entry.getValue());
		}
		// new Exception("ConverterContext").printStackTrace();
	}

	public static void convert(Object bean, Object source) {
		try {
			convert2(bean, source);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected static void convert2(Object bean, Object source) throws Exception {
		for (Field field : bean.getClass().getDeclaredFields()) {
			field.setAccessible(true);
			Object value = field.get(bean);
			if (value == null) {
				value = parse(field.getType(), source);
			}
		}
	}

	protected static Object parse(Class<?> type, Object source) throws Exception {
		return null;
	}
}
