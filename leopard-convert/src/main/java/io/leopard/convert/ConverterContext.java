package io.leopard.convert;

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

	public static void convert(Object bean) {

	}

}
