package io.leopard.mvc.trynb.web.controller;

import io.leopard.httpnb.Httpnb;
import io.leopard.jetty.JettyServer;
import io.leopard.web.view.JsonView;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/")
public class TrynbController {

	@RequestMapping("/index.do")
	public ModelAndView index() {
		throw new RuntimeException("error1.");
	}

	@RequestMapping("/welcome.do")
	public ModelAndView welcome() {
		throw new RuntimeException("error2.");
	}

	@RequestMapping("/user.do")
	public JsonView user() {
		throw new RuntimeException("ok");
	}

	@Test
	public void testIndex() throws Exception {
		JettyServer.start("src/test/webapp");
		String result = Httpnb.doGet("http://localhost/index.do");
		System.out.println("result:" + result);
		Assert.assertEquals("error1.", result);
	}

	@Test
	public void testWelcome() throws Exception {
		JettyServer.start("src/test/webapp");
		String result = Httpnb.doGet("http://localhost/welcome.do");
		System.out.println("result:" + result);
		Assert.assertEquals("error2.", result);
	}

	@Test
	public void testUser() throws Exception {
		JettyServer.start("src/test/webapp");
		String result = Httpnb.doGet("http://localhost/user.do");
		System.out.println("result:" + result);
		Assert.assertEquals("{\"status\":\"RuntimeException\",\"message\":\"ok\",\"data\":null}", result);
	}
}
