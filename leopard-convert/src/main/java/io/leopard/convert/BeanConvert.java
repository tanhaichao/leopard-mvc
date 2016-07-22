package io.leopard.convert;

import io.leopard.json.Json;

public class BeanConvert<S, T> {

	private S source;
	private T target;

	private Class<T> clazz;

	public BeanConvert(S source) {
		this(source, null);
	}

	public BeanConvert(S source, Class<T> clazz) {

		if (source != null) {
			String json = Json.toJson(source);
			this.target = Json.toObject(json, null, true);
		}
	}

	public T get() {
		return this.target;
	}

	public T convert() {
		return this.target;
	}

}
