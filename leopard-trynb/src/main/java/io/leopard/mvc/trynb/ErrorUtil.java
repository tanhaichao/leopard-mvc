package io.leopard.mvc.trynb;

import java.lang.reflect.GenericSignatureFormatError;
import java.sql.SQLException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ErrorUtil {
	protected static final Log logger = LogFactory.getLog(ErrorUtil.class);

	/**
	 * 获取异常信息.
	 * 
	 * @param e
	 * @return
	 */
	public static String parseMessage(Throwable e) {
		if (e == null) {
			throw new IllegalArgumentException("exception不能为空?");
		}
		if (e instanceof SQLException) {
			return "操作数据库出错，请稍后重试.";
		}
		String className = e.getClass().getName();

		if ("org.springframework.dao.DataAccessException".equals(className)) {
			return "操作数据库出错，请稍后重试.";
		}
		if ("redis.clients.jedis.exceptions.JedisConnectionException".equals(className)) {
			return "操作数据库出错，请稍后重试.";
		}
		// if (e instanceof OutSideException) {
		if ("io.leopard.core.exception.other.OutSideException".equals(className)) {
			return "访问外部接口出错，请稍后重试.";
		}
		if (e instanceof GenericSignatureFormatError) {
			return "更新程序后，还没有重启服务.";
		}

		String message = e.getMessage();
		if (message == null) {
			return null;
		}
		return message.replaceAll("\\[.*?\\]", "");
	}
}
