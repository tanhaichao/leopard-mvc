package io.leopard.web.seccode;

import io.leopard.web.captcha.FrequencyException;

public interface SeccodeService {

	// String add(String account, SeccodeType type, String target, String seccode);
	//
	// Seccode last(String account, SeccodeType type, String target);

	boolean updateUsed(String seccodeId, boolean used);

	Seccode check(String account, SeccodeType type, String target, String seccode) throws SeccodeWrongException;

	/**
	 * 发送验证码
	 * 
	 * @param account 账号，如手机号码、邮箱地址
	 * @param type 类型:mobile:手机，email:邮件
	 * @param target 目标，即在哪里使用
	 * @param content 消息模板
	 * @return
	 */
	String send(String account, SeccodeType type, String target, String content) throws FrequencyException;

	boolean updateUsed(Seccode seccode);

}
