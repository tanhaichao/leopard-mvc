package io.leopard.web.passport.controller;

import io.leopard.web.passport.PassportValidate;
import io.leopard.web.servlet.CookieUtil;

import java.io.IOException;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;

@Component
public class PassportValidateImpl implements PassportValidate {

	@Override
	public Object validate(HttpServletRequest request, HttpServletResponse response) {
		String uid = CookieUtil.getCookie("uid", request);
		System.err.println("getCookie:" + uid);
		try {
			return Long.parseLong(uid);
		}
		catch (NumberFormatException e) {
			return null;
		}
	}

	@Override
	public boolean showLoginBox(HttpServletRequest request, HttpServletResponse response) {
		return false;
	}

	@Override
	public boolean login(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String uid = request.getParameter("uid");
		uid = "1";
		// CookieUtil.setCookie("username", username, request, response);
		{
			Cookie cookie = new Cookie("uid", uid);
			cookie.setPath("/");
			response.addCookie(cookie);
		}
		System.err.println("setCookie:" + uid);
		String url = request.getParameter("url");
		if (url != null) {
			response.sendRedirect(url);
		}
		return true;
	}

}
