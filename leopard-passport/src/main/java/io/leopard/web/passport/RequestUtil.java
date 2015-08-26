package io.leopard.web.passport;

//import io.leopard.burrow.util.StringUtil;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

/**
 * 
 * 
 * @author 阿海
 */
public class RequestUtil {

	/**
	 * 获取代理服务器IP. .
	 * 
	 * @param request
	 * @return
	 */
	public static String getProxyIp(HttpServletRequest request) {
		String proxyIp = request.getHeader("X-Real-IP");
		if (proxyIp == null) {
			proxyIp = request.getHeader("RealIP");
		}
		if (proxyIp == null) {
			proxyIp = request.getRemoteAddr();
		}
		return proxyIp;
	}

	public static String getRequestContextUri(HttpServletRequest request) {
		String contextPath = request.getContextPath();
		String requestURI;
		if ("/".equals(contextPath)) {
			requestURI = request.getRequestURI();
		}
		else {
			String uri = request.getRequestURI();
			requestURI = uri.substring(contextPath.length());
		}
		if (requestURI.indexOf("//") != -1) {
			requestURI = requestURI.replaceAll("/+", "/");
		}
		return requestURI;
	}

	public static HttpServletRequest getCurrentRequest() {
		ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		if (attr == null) {
			return null;
		}
		return attr.getRequest();
	}

	/**
	 * 获取上传的文件.
	 * 
	 * @param request
	 *            请求
	 * @param name
	 *            文件名
	 * @return 文件
	 */
	public static MultipartFile getFile(HttpServletRequest request, String name) {
		// try {
		MultipartHttpServletRequest mrequest = (MultipartHttpServletRequest) request;
		MultipartFile file = mrequest.getFile(name); // 发送对象
		if (file == null || file.isEmpty()) {
			return null;
		}
		return file;
		// }
		// catch (ClassCastException e) {
		// logger.warn("ClassCastException " + e.getMessage());
		// return null;
		// }
	}

	/**
	 * 获取域名.
	 * 
	 * @param request
	 * @return
	 */
	public static String getDomain(HttpServletRequest request) {
		String domain = "http://" + request.getServerName();
		return domain;
	}

	/**
	 * 获取user-agent.
	 */
	public static String getUserAgent(HttpServletRequest request) {
		String userAgent = request.getHeader("user-agent");
		return userAgent;
	}

	/**
	 * 把null或无效的页码转成1.
	 * 
	 * @param pageid
	 * @return
	 */
	public static int getPageid(Integer pageid) {
		if (pageid == null || pageid <= 0) {
			return 1;
		}
		return pageid;
	}

	/**
	 * 获取上次访问的地址.
	 * 
	 * @param request
	 * @return
	 */
	public static String getReferer(HttpServletRequest request) {
		return request.getHeader("referer");
	}

	public static String getRequestURL(HttpServletRequest request) {
		boolean isHttps = "true".equals(request.getHeader("isHttps"));
		StringBuilder sb = new StringBuilder(48);
		int port = request.getServerPort();

		String scheme;
		if (isHttps) {
			scheme = "https";
			if (port == 80) {
				port = 443;
			}
		}
		else {
			scheme = "http";
		}

		sb.append(scheme);
		sb.append("://");
		sb.append(request.getServerName());
		if (port == 80 && "http".equals(scheme)) {
			//
		}
		else if (port == 443 && "https".equals(scheme)) {
			//
		}
		else {
			sb.append(':');
			sb.append(port);
		}
		sb.append(request.getRequestURI());
		return sb.toString();
	}

	// /**
	// * 从cookie获取用户通行证.
	// *
	// * @param request
	// * @return
	// */
	// public static String getCookieUsername(HttpServletRequest request) {
	// String username = CookieUtil.getCookie("username", request);
	// try {
	// username = URLDecoder.decode(username, "UTF-8");
	// }
	// catch (UnsupportedEncodingException e) {
	// throw new RuntimeException(e.getMessage(), e);
	// }
	// return username;
	// }

	// public static void test() {
	// logger.info("test");
	// }

	// public static void main(String[] args) {
	// String str = "192.168.0.1,";
	// int index = str.lastIndexOf(',');
	// String sss = str.substring(index + 1).trim();
	// System.out.println(index + "::" + sss);
	// }

}
