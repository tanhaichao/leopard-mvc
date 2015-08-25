package io.leopard.trynb.web.controller;

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
}
