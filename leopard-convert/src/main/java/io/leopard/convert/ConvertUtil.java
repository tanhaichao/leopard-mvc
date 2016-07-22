package io.leopard.convert;

import io.leopard.lang.Paging;

import java.util.List;

public class ConvertUtil {

	public static <S, T> List<T> list(List<S> list, Class<T> clazz) {
		return new ListConvert<S, T>(list).convert();
	}
	
	public static <S, T> Paging<T> list(Paging<S> list, Class<T> clazz) {
		return new PagingConvert<S, T>(list).convert();
	}
}
