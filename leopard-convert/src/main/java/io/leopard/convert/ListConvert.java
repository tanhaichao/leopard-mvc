package io.leopard.convert;

import java.util.ArrayList;
import java.util.List;

public class ListConvert<S, T> {

	private List<S> list;

	private List<T> result;

	private Class<T> clazz;

	public ListConvert(List<S> list) {
		this(list, null);
	}

	public ListConvert(List<S> list, Class<T> clazz) {
		this.clazz = clazz;
		this.list = list;
		if (list == null) {
		}
		else if (list.isEmpty()) {
			this.result = new ArrayList<T>();
		}
		else {
			for (S source : list) {
				String json = ConvertJson.toJson(source);
				T target = ConvertJson.toObject(json, clazz);
				FillerContext.fill(source, target);
				this.result.add(target);

			}
		}
	}

	public List<T> get() {
		return this.result;
	}

	public List<T> convert() {
		if (this.result == null || result.isEmpty()) {
			return this.result;
		}
		int index = 0;
		for (T bean : result) {
			S source = this.list.get(index);
			ConverterContext.convert(bean, source);
			index++;
		}
		return this.result;
	}

}
