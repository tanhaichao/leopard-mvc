package io.leopard.upload;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

public class LeopardMultipartResolver extends CommonsMultipartResolver {

	protected MultipartParsingResult parseRequest(HttpServletRequest request) throws MultipartException {
		return super.parseRequest(request);
	}
}
