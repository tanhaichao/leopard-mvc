package io.leopard.web4j.view;

import java.util.List;

public interface IViewJson {

	String toJson(Object obj);

	String toFormatJson(Object obj);

	<T> T toObject(String json, Class<T> clazz);

	<T> List<T> toListObject(String content, Class<T> valueType);
}
