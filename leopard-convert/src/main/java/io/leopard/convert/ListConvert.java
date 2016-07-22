package io.leopard.convert;

import io.leopard.json.Json;

import java.util.ArrayList;
import java.util.List;

public class ListConvert<S, T> {

	private List<S> list;

	private List<T> result;

	private Class<T> clazz;

	public ListConvert(List<S> list) {
		this(list,null);
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
			String json = Json.toJson(list);
			this.result = Json.toListObject(json, clazz);
		}
	}

	public List<T> get() {
		return this.result;
	}

	public List<T> convert() {
		return this.result;
	}

}
