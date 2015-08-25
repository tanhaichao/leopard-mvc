package io.leopard.trynb.web.controller;

import io.leopard.httpnb.Httpnb;
import io.leopard.jetty.JettyServer;

import org.junit.Test;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/")
public class IndexController {

	
	@RequestMapping("/index.do")
	public ModelAndView index() {
		throw new RuntimeException("ok");
	}

	@RequestMapping("/welcome.do")
	public ModelAndView welcome() {
		throw new RuntimeException("ok");
	}

	// @RequestMapping("/user.do")
	// public JsonView user() {
	// throw new RuntimeException("ok");
	// }

	@Test
	public void test() throws Exception {
		JettyServer.start("src/test/webapp");
		// System.out.println("ok");
		String result = Httpnb.doGet("http://localhost/index.do");
		System.out.println("result:" + result);
	}
}
