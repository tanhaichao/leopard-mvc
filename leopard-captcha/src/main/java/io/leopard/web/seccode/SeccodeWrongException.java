package io.leopard.web.seccode;

import io.leopard.core.exception.ForbiddenException;

/**
 * 错误的验证码.
 * 
 * @author 谭海潮
 *
 */
public class SeccodeWrongException extends ForbiddenException {

	private static final long serialVersionUID = 1L;

	public SeccodeWrongException(String seccode) {
		super("错误的验证码[" + seccode + "].");
	}

}
