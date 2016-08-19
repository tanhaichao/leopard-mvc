package io.leopard.mvc.trynb;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.util.StringUtils;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import io.leopard.mvc.trynb.model.ErrorConfig;
import io.leopard.mvc.trynb.model.ExceptionConfig;
import io.leopard.mvc.trynb.model.TrynbInfo;

public class TrynbServiceImpl implements TrynbService {

	private final TrynbDao trynbDao = new TrynbDaoImpl();
	private final TrynbLogger trynbLogger = new TrynbLoggerImpl();

	protected ExceptionConfig find(ErrorConfig errorConfig, Exception exception) {

		List<ExceptionConfig> exceptionConfigList = errorConfig.getExceptionConfigList();
		String exceptionClassName = exception.getClass().getName();

		for (ExceptionConfig exceptionConfig : exceptionConfigList) {
			boolean match = match(exceptionConfig.getType(), exceptionClassName);
			if (match) {
				return exceptionConfig;
			}
		}
		return null;
	}

	@Override
	public TrynbInfo parse(HttpServletRequest request, String uri, Exception exception) {
		ErrorConfig errorConfig = trynbDao.find(uri);

		if (exception instanceof MethodArgumentTypeMismatchException) {
			MethodArgumentTypeMismatchException e2 = (MethodArgumentTypeMismatchException) exception;
			Exception e = (Exception) e2.getCause().getCause();
			if (e != null) {
				exception = e;
			}
			else {
				exception = (Exception) e2.getCause();
			}
		}

		ExceptionConfig exceptionConfig = this.find(errorConfig, exception);

		String message;
		if (exceptionConfig == null || StringUtils.isEmpty(exceptionConfig.getMessage())) {
			message = ErrorUtil.parseMessage(exception);
		}
		else {
			message = exceptionConfig.getMessage();
		}

		String statusCode = this.parseStatusCode(exceptionConfig, request, uri, exception);

		TrynbInfo trynbInfo = new TrynbInfo();
		trynbInfo.setPage(errorConfig.getPage());
		trynbInfo.setMessage(message);
		trynbInfo.setException(exception);
		trynbInfo.setStatusCode(statusCode);
		return trynbInfo;
	}

	protected String parseStatusCode(ExceptionConfig exceptionConfig, HttpServletRequest request, String uri, Exception exception) {
		if (exceptionConfig == null) {
			// logger.error("匹配[" + uri + "." + exception.getClass().getName() +
			// "]不到exception配置");
			// this.error(request, uri, exception);
			trynbLogger.error(request, uri, exception);
			return exception.getClass().getSimpleName();
		}
		String logType = ExceptionConfig.getType(exceptionConfig.getLog());
		if ("error".equals(logType)) {
			// this.error(request, uri, exception);
			trynbLogger.error(request, uri, exception);
		}
		else if ("warn".equals(logType)) {
			// logger.warn("uri:" + uri + " message:" + exception.getMessage());
			trynbLogger.warn(request, uri, exception);
		}
		else if ("info".equals(logType)) {
			// logger.info("uri:" + uri + " message:" + exception.getMessage());
			trynbLogger.info(request, uri, exception);
		}
		else if ("debug".equals(logType)) {
			// logger.debug("uri:" + uri + " message:" + exception.getMessage());
			trynbLogger.debug(request, uri, exception);
		}
		if (StringUtils.isEmpty(exceptionConfig.getStatusCode())) {
			return exception.getClass().getSimpleName();
		}
		return exceptionConfig.getStatusCode();
	}

	private static boolean match(String type, String exceptionClassName) {
		if (type.indexOf(".") == -1) {
			if (exceptionClassName.endsWith(type)) {
				return true;
			}
		}
		if (exceptionClassName.equals(type)) {
			return true;
		}
		return false;
	}

}
