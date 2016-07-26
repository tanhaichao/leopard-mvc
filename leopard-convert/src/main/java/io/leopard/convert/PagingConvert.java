package io.leopard.convert;

import io.leopard.lang.Paging;
import io.leopard.lang.PagingImpl;

public class PagingConvert<S, T> {

	private Paging<S> paging;

	private Paging<T> result;

	private Class<T> clazz;

	public PagingConvert(Paging<S> paging) {
		this(paging, null);
	}

	public PagingConvert(Paging<S> paging, Class<T> clazz) {
		this.paging = paging;
		if (paging != null) {
			Paging<T> result = new PagingImpl<T>(paging);
			for (S source : paging.getList()) {
				String json = ConvertJson.toJson(source);
				System.out.println("source:" + source + " json:" + json);
				T target = ConvertJson.toObject(json, clazz);
				result.add(target);
			}
		}
	}

	public Paging<T> get() {
		return this.result;
	}

	public Paging<T> convert() {
		if (this.result == null || result.getList().isEmpty()) {
			return this.result;
		}
		int index = 0;
		for (T bean : result.getList()) {
			S source = this.paging.get(index);
			ConverterContext.convert(bean, source);
			index++;
		}
		return this.result;
	}

}
