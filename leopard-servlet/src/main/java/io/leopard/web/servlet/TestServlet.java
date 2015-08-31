package io.leopard.web.servlet;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;

@WebServlet(name = "testServlet", urlPatterns = "/passport/test.leo")
@SuppressWarnings("serial")
public class TestServlet extends HttpServlet {

	public TestServlet() {
		System.err.println("new TestServlet.");
	}
}
