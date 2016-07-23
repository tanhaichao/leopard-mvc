package io.leopard.convert;

import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;

public class ConverterContext implements BeanFactoryAware {

	@SuppressWarnings("rawtypes")
	@Override
	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		Map<String, Converter> beanMap = ((DefaultListableBeanFactory) beanFactory).getBeansOfType(Converter.class);
		for (Entry<String, Converter> entry : beanMap.entrySet()) {
			System.out.println("entry:" + entry.getKey());
		}
	}

}
