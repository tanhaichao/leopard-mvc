package io.leopard.mvc.trynb.web.controller;

import io.leopard.httpnb.Httpnb;
import io.leopard.jetty.JettyServer;
import io.leopard.web.view.JsonView;

import org.junit.Test;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/xparam/")
public class XParamController {

	@RequestMapping("/welcome.do")
	public JsonView welcome(Long sessUid) {
		return new JsonView(sessUid);
	}

	@Test
	public void testWelcome() throws Exception {
		JettyServer.start("src/test/webapp");
		String result = Httpnb.doGet("http://localhost/xparam/welcome.do");
		System.out.println("result:" + result);
		// Assert.assertEquals("error1.", result);
	}

}
