package io.leopard.web.captcha.kit;

public interface CaptchaDao {

	boolean add(Captcha captcha);

	Captcha last(String account, String type, String target);

	boolean updateUsed(String captchaId, boolean used);
}
