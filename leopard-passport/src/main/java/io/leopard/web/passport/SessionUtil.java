package io.leopard.web.passport;

import io.leopard.json.Json;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class SessionUtil {

	public static final String SESSIONID_COOKIE_NAME = "SESSIONID";

	public static final String USERINFO_SESSION_NAME = "userinfo";

	/**
	 * 从Session获取uid.
	 * 
	 * @param session
	 *            Session
	 * @return
	 */
	public static Long getUid(HttpSession session) {
		PassportUser user = getUserinfo(session);
		if (user == null) {
			return null;
		}
		else {
			return user.getUid();
		}
	}

	public static String getUsername(HttpSession session) {
		PassportUser user = getUserinfo(session);
		if (user == null) {
			return null;
		}
		else {
			return user.getUsername();
		}
	}

	public static void setUserinfo(HttpServletRequest request, PassportUser user) {
		String json = Json.toJson(user);
		request.getSession().setAttribute(USERINFO_SESSION_NAME, json);
	}

	public static PassportUser getUserinfo(HttpSession session) {
		String json = (String) session.getAttribute(USERINFO_SESSION_NAME);
		return Json.toObject(json, PassportUser.class);
	}

}
