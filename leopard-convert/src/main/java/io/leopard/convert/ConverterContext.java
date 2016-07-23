package io.leopard.convert;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
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
				if (value != null) {
					field.set(bean, value);
				}
			}
		}
	}

	protected static Converter findConverter(Class<?> type, Object source) {
		for (Converter converter : converterList) {
			Class<?> api = converter.getClass().getInterfaces()[0];
			ParameterizedType type2 = (ParameterizedType) converter.getClass().getGenericInterfaces()[0];
			Type[] args = type2.getActualTypeArguments();
			Class<?> clazz1 = (Class<?>) args[0];
			Class<?> clazz2 = (Class<?>) args[1];
			if (!type.equals(clazz2)) {
				continue;
			}
			if (clazz1.equals(source.getClass())) {
				return converter;
			}
			System.err.println("converter:" + converter + " type:" + type.getName() + " type1:" + clazz1.getName() + " type12:" + clazz2.getName());
		}
		return null;
	}

	protected static Object parse(Class<?> type, Object source) throws Exception {
		Converter converter = findConverter(type, source);
		if (converter == null) {
			return null;
		}
		System.out.println("converter111:" + converter);
		return converter.get(source);
	}
}
