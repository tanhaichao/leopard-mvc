package io.leopard.web.captcha.controller;

import io.leopard.httpnb.Httpnb;
import io.leopard.jetty.JettyServer;
import io.leopard.web.captcha.CaptchaGroup;
import io.leopard.web.captcha.CaptchaView;
import io.leopard.web.view.JsonView;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class CaptchaController {

	@RequestMapping("/captcha.do")
	public CaptchaView captcha() {
		return new CaptchaView();
	}

	@RequestMapping("/check.do")
	public JsonView check(String sessCaptcha) {
		return new JsonView("sessCaptcha:" + sessCaptcha);
	}

	@RequestMapping("/captcha2.do")
	@CaptchaGroup("captcha2")
	public CaptchaView captcha2() {
		return new CaptchaView();
	}

	@RequestMapping("/check2.do")
	@CaptchaGroup("captcha2")
	public JsonView check2(String sessCaptcha) {
		return new JsonView("sessCaptcha:" + sessCaptcha);
	}

	@Test
	public void testCaptcha() throws Exception {
		JettyServer.start("src/test/webapp");
		{
			Httpnb.doGet("http://localhost/passport/login.leo?uid=1");
		}
		String result = Httpnb.doGet("http://localhost/welcome.do");
		System.out.println("result:" + result);
		Assert.assertEquals("{\"status\":\"RuntimeException\",\"message\":\"ok\",\"data\":null}", result);
	}

	public static void main(String[] args) throws Exception {
		JettyServer.start("src/test/webapp");
	}
}
