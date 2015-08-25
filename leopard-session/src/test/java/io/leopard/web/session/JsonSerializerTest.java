package io.leopard.web.session;

import io.leopard.web.session.JsonSerializer;

import java.util.Date;

import org.junit.Test;

public class JsonSerializerTest {

	@Test
	public void toJson() {
		Date now = new Date();
		String json = JsonSerializer.toJson(now);
		System.out.println("json:" + json);
	}

}