package io.leopard.convert;


public class BeanConvert<S, T> {

	private S source;
	private T target;

	private Class<T> clazz;

	public BeanConvert(S source) {
		this(source, null);
	}

	public BeanConvert(S source, Class<T> clazz) {

		if (source != null) {
			String json = ConvertJson.toJson(source);
			this.target = ConvertJson.toObject(json, clazz);
		}
	}

	public T get() {
		return this.target;
	}

	public T convert() {
		return this.target;
	}

}
