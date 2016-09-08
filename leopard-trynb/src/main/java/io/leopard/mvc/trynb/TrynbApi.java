package io.leopard.mvc.trynb;

import javax.servlet.http.HttpServletRequest;

import io.leopard.mvc.trynb.model.TrynbInfo;

public interface TrynbApi {
	TrynbInfo parse(HttpServletRequest request, String uri, Exception exception);

}
