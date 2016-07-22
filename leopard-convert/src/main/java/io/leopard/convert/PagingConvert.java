package io.leopard.convert;

import io.leopard.json.Json;
import io.leopard.lang.Paging;
import io.leopard.lang.PagingImpl;

public class PagingConvert<S, T> {

	private Paging<S> paging;

	private Paging<T> result;

	public PagingConvert(Paging<S> paging) {
		this.paging = paging;
		if (paging != null) {
			Paging<T> result = new PagingImpl<T>(paging);
			for (S source : paging.getList()) {
				String json = Json.toJson(source);
				T target = Json.toObject(json, null, true);
				result.add(target);
			}
		}
	}

	public Paging<T> get() {
		return this.result;
	}

	public Paging<T> convert() {
		return this.result;
	}

}
