package io.leopard.web4j.view;

import java.util.List;

public class ViewJson implements IViewJson {

	private static final IViewJson instance = null;

	public static IViewJson getInstance() {
		return instance;
	}

	@Override
	public String toJson(Object obj) {
		return null;
	}

	@Override
	public String toFormatJson(Object obj) {
		return null;
	}

	@Override
	public <T> List<T> toListObject(String content, Class<T> valueType) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> T toObject(String json, Class<T> clazz) {
		// TODO Auto-generated method stub
		return null;
	}

}
