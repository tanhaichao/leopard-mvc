package io.leopard.web.xparam;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.stereotype.Component;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

@Component
public class XParamAware implements BeanFactoryAware {

	@Override
	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		System.out.println("XParamAware setBeanFactory:" + beanFactory);
		// {
		// ConfigurableListableBeanFactory factory = (ConfigurableListableBeanFactory) beanFactory;
		// Collection<HandlerAdapter> adapters = factory.getBeansOfType(HandlerAdapter.class).values();
		// for (HandlerAdapter adapter : adapters) {
		// System.out.println("XParamAware adapters:" + adapter);
		// }
		// }
		{
			RequestMappingHandlerAdapter adapter = (RequestMappingHandlerAdapter) beanFactory.getBean(RequestMappingHandlerAdapter.class);
			if (adapter != null) {
				List<HandlerMethodArgumentResolver> customArgumentResolvers = adapter.getCustomArgumentResolvers();
				if (customArgumentResolvers == null) {
					customArgumentResolvers = new ArrayList<HandlerMethodArgumentResolver>();
					adapter.setCustomArgumentResolvers(customArgumentResolvers);
				}
				System.out.println("XParamAware adapter:" + adapter);
			}
		}

		//
		// HandlerMethodArgumentResolver resolver = beanFactory.getBean(XParamHandlerMethodArgumentResolver.class);
		// System.out.println("XParamAware resolver:" + resolver);
		// customArgumentResolvers.add(resolver);
		// System.out.println("XParamAware adapter:" + adapter);
	}

}
