package io.leopard.convert;

import io.leopard.json.Json;

public class DefaultBeanConvert<S, T> implements BeanConvert<S, T> {

	private S source;
	private T target;

	public DefaultBeanConvert(S source, Class<T> clazz) {
		if (source != null) {
			String json = Json.toJson(source);
			this.target = Json.toObject(json, clazz, true);
		}
	}

	@Override
	public T convert() {
		if (this.target == null) {
			return null;
		}
		return this.target;
	}

}
