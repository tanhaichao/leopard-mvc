package io.leopard.web.session;

import io.leopard.httpnb.Httpnb;
import io.leopard.jetty.JettyServer;

import java.io.IOException;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet(name = "userServlet", urlPatterns = "/index")
public class UserServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("service:" + request);
		HttpSession session = request.getSession();

		session.setAttribute("key", new Date());

		Date now = (Date) session.getAttribute("key");
		ServletOutputStream out = response.getOutputStream();
		out.println("now:" + now.toString());
	}

	public static void main(String[] args) throws Exception {
		JettyServer.start("src/test/webapp");
		System.out.println("ok");
		String str = Httpnb.doGet("http://localhost/index");
		System.out.println("str:" + str);
		System.exit(0);
	}
}
