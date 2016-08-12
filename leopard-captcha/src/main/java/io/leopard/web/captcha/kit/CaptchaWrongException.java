package io.leopard.web.captcha.kit;

import io.leopard.core.exception.ForbiddenException;

/**
 * 错误的验证码.
 * 
 * @author 谭海潮
 *
 */
public class CaptchaWrongException extends ForbiddenException {

	private static final long serialVersionUID = 1L;

	public CaptchaWrongException(String seccode) {
		super("错误的验证码[" + seccode + "].");
	}

}
