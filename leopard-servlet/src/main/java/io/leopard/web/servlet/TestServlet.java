package io.leopard.web.servlet;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;

@WebServlet(name = "testServlet", urlPatterns = "/passport/test.leo")
public class TestServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

}
