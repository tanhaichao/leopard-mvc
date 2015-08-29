package io.leopard.web.passport;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 性能监控数据
 * 
 * @author 阿海
 */
@WebServlet(name = "passportLoginServlet", urlPatterns = "/passport/login.leo")
public class PassportLoginServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		boolean flag = PassportValidateImpl.getInstance().login(request, response);
		if (flag) {
			return;
		}

		String html = "未实现PassportValidate.login接口";
		byte[] bytes = html.getBytes();
		response.setContentType("text/html");
		response.setContentLength(bytes.length);

		OutputStream out = response.getOutputStream();
		out.write(bytes);
		out.flush();
	}

}