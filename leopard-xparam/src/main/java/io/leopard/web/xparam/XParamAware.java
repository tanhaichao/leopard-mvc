package io.leopard.web.xparam;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.stereotype.Component;

@Component
public class XParamAware implements BeanFactoryAware {

	@Override
	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		System.out.println("XParamAware setBeanFactory:" + beanFactory);
		// {
		// ConfigurableListableBeanFactory factory = (ConfigurableListableBeanFactory) beanFactory;
		// Collection<HandlerAdapter> adapters = factory.getBeansOfType(HandlerAdapter.class).values();
		// System.out.println("XParamAware adapters:" + adapters);
		// }
		// RequestMappingHandlerAdapter adapter = (RequestMappingHandlerAdapter) beanFactory.getBean(XParamRequestMappingHandlerAdapter.class);
		// List<HandlerMethodArgumentResolver> customArgumentResolvers = adapter.getCustomArgumentResolvers();
		// if (customArgumentResolvers == null) {
		// customArgumentResolvers = new ArrayList<HandlerMethodArgumentResolver>();
		// adapter.setCustomArgumentResolvers(customArgumentResolvers);
		// }
		//
		// HandlerMethodArgumentResolver resolver = beanFactory.getBean(XParamHandlerMethodArgumentResolver.class);
		// System.out.println("XParamAware resolver:" + resolver);
		// customArgumentResolvers.add(resolver);
		// System.out.println("XParamAware adapter:" + adapter);
	}

}
