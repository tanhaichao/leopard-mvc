package io.leopard.convert;

import java.util.List;

import io.leopard.lang.Paging;
import io.leopard.lang.PagingImpl;

public class PagingConvert<S, T> {

	private Paging<S> paging;

	private Paging<T> result;

	private Class<T> clazz;

	// public PagingConvert(Paging<S> paging) {
	// this(paging, null);
	// }

	public PagingConvert(Paging<S> paging, Class<T> clazz) {
		this.paging = paging;
		if (paging != null) {
			result = new PagingImpl<T>(paging);
			// String json = ConvertJson.toJson(paging.getList());
			// List<T> list = ConvertJson.toListObject(json, clazz);
			// for (T target : list) {
			// FillerContext.fill(source, target);
			// result.add(target);
			// }
			for (S source : paging.getList()) {
				// String json2 = Json.toJson(source);
				// System.out.println("json2:" + json2);
				//
				String json = ConvertJson.toJson(source);
				// System.out.println("json:" + json);
				//
				// System.out.println("source:" + source + " json:" + json);
				T target = ConvertJson.toObject(json, clazz);
				FillerContext.fill(source, target);
				// T target = Json.toObject(json, clazz);
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
