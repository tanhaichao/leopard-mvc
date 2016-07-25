package io.leopard.convert;

public interface PropertyResolver {

	boolean supports(Class<?> clazz, String fieldName);
}
