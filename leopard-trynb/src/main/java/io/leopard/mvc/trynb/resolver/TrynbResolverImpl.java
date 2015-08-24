package io.leopard.mvc.trynb.resolver;

import io.leopard.mvc.trynb.model.TrynbInfo;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ServiceLoader;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;

public class TrynbResolverImpl implements TrynbResolver {
	private List<TrynbResolver> trynbResolverList = new ArrayList<TrynbResolver>();

	public TrynbResolverImpl() {
		ServiceLoader<TrynbResolver> loader = ServiceLoader.load(TrynbResolver.class);
		Iterator<TrynbResolver> driversIterator = loader.iterator();
		while (driversIterator.hasNext()) {
			TrynbResolver resolver = driversIterator.next();
			trynbResolverList.add(resolver);
		}
	}

	@Override
	public ModelAndView resolveView(HttpServletRequest request, HttpServletResponse response, HandlerMethod handler, Exception exception, TrynbInfo trynbInfo) {
		for (TrynbResolver trynbResolver : trynbResolverList) {
			ModelAndView view = trynbResolver.resolveView(request, response, handler, exception, trynbInfo);
			if (view != null) {
				return view;
			}
		}
		return null;
	}

}
