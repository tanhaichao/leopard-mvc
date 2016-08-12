package io.leopard.web.seccode;

import org.junit.Test;

import io.leopard.jdbc.Jdbc;
import io.leopard.jdbc.JdbcFactory;
import io.leopard.web.captcha.FrequencyException;

public class SeccodeServiceImplTest {

	private SeccodeServiceImpl seccodeService = new SeccodeServiceImpl();

	public SeccodeServiceImplTest() {
		Jdbc jdbc = JdbcFactory.creaeJdbcMysqlImpl("112.126.75.27", "example", "example", "leopard");
		seccodeService.setJdbc(jdbc);
		seccodeService.init();
	}

	@Test
	public void send() throws FrequencyException, SeccodeWrongException {
		String account = "13924718422";
		String seccode = seccodeService.send(account, SeccodeType.MOBILE, "test", "content");
		seccode = seccodeService.send(account, SeccodeType.MOBILE, "test", "content");
		Seccode bean = seccodeService.check(account, SeccodeType.MOBILE, "test", seccode);
		seccodeService.updateUsed(bean);
	}

}