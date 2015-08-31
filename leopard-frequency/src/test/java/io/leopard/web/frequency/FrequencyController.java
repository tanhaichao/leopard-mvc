package io.leopard.web.frequency;

import io.leopard.jetty.JettyServer;
import io.leopard.web.view.JsonView;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class FrequencyController {

	@RequestMapping("/welcome.do")
	@Frequency
	public JsonView welcome(Long uid) {
		return new JsonView(uid);
	}

	public static void main(String[] args) throws Exception {
		JettyServer.start("src/test/webapp");
	}
}
