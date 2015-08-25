package io.leopard.web4j.view;

import java.util.List;

public class ViewJson implements IViewJson {

	private static final IViewJson instance = new ViewJson();

	public static IViewJson getInstance() {
		return instance;
	}

	@Override
	public String toJson(Object obj) {
		throw new UnsupportedOperationException("Not Impl.");
	}

	@Override
	public String toFormatJson(Object obj) {
		throw new UnsupportedOperationException("Not Impl.");
	}

	@Override
	public <T> List<T> toListObject(String content, Class<T> valueType) {
		throw new UnsupportedOperationException("Not Impl.");
	}

	@Override
	public <T> T toObject(String json, Class<T> clazz) {
		throw new UnsupportedOperationException("Not Impl.");
	}

}
