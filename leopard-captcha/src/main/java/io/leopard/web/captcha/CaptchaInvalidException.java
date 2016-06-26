package io.leopard.web.captcha;

import io.leopard.core.exception.InvalidException;

/**
 * 非法 校验码.
 * 
 * @author 谭海潮
 *
 */
public class CaptchaInvalidException extends InvalidException {

	private static final long serialVersionUID = 1L;

	public CaptchaInvalidException(String captcha) {
		super("非法校验码[" + captcha + "].");
	}

}
