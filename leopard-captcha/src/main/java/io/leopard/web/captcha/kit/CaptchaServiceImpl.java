package io.leopard.web.captcha.kit;

import java.util.Date;
import java.util.UUID;

import javax.annotation.PostConstruct;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.Assert;

import io.leopard.jdbc.Jdbc;
import io.leopard.redis.Redis;
import io.leopard.web.captcha.FrequencyException;

//@Service
public class CaptchaServiceImpl implements CaptchaService {

	protected Log logger = LogFactory.getLog(this.getClass());

	protected Jdbc jdbc;
	protected Redis redis;
	private String tableName;

	public void setJdbc(Jdbc jdbc) {
		this.jdbc = jdbc;
	}

	public void setRedis(Redis redis) {
		this.redis = redis;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	protected CaptchaDao captchaDao;

	@PostConstruct
	public void init() {
		CaptchaDaoMysqlImpl captchaDaoMysqlImpl = new CaptchaDaoMysqlImpl();
		captchaDaoMysqlImpl.setJdbc(jdbc);
		captchaDaoMysqlImpl.setTableName(tableName);
		this.captchaDao = captchaDaoMysqlImpl;
	}

	protected String add(String account, CaptchaType type, String target, String captcha) {
		Assert.hasText(account, "参数account不能为空");
		Assert.notNull(type, "参数type不能为空");
		Assert.hasText(target, "参数target不能为空");
		Assert.hasText(captcha, "参数captcha不能为空");
		String seccodeId = UUID.randomUUID().toString().replaceAll("-", "").toLowerCase();
		Captcha bean = new Captcha();
		bean.setSeccodeId(seccodeId);
		bean.setAccount(account);
		bean.setType(type.getKey());
		bean.setTarget(target);
		bean.setPosttime(new Date());
		bean.setSeccode(captcha);
		bean.setUsed(false);
		captchaDao.add(bean);
		return seccodeId;
	}

	protected Captcha last(String account, CaptchaType type, String target) {
		Jdbc jdbc = ((CaptchaDaoMysqlImpl) captchaDao).getJdbc();
		if (jdbc == null) {
			throw new NullPointerException("jdbc还没有初始化.");
		}

		Assert.hasText(account, "参数account不能为空");
		Assert.notNull(type, "参数type不能为空");
		Assert.hasText(target, "参数target不能为空");
		return this.captchaDao.last(account, type.getKey(), target);
	}

	@Override
	public boolean updateUsed(Captcha captcha) {
		Assert.notNull(captcha, "参数captcha不能为空");
		return this.updateUsed(captcha.getCaptchaId(), true);
	}

	@Override
	public boolean updateUsed(String captchaId, boolean used) {
		Assert.hasText(captchaId, "参数captchaId不能为空");
		return captchaDao.updateUsed(captchaId, used);
	}

	@Override
	public Captcha check(String account, CaptchaType type, String target, String seccode) throws CaptchaWrongException {
		Assert.hasText(account, "参数account不能为空");
		Assert.notNull(type, "参数type不能为空");
		Assert.hasText(target, "参数target不能为空");
		Assert.hasText(seccode, "参数seccode不能为空");

		// String securityCode2 = lastSecurityCode(mobile, type);
		Captcha bean = this.last(account, type, target);
		if (bean == null) {
			throw new CaptchaWrongException("获取不到验证码记录[" + account + " " + type.getKey() + " " + target + "]");
		}
		if (!bean.getCaptcha().equals(seccode)) {
			throw new CaptchaWrongException(seccode);
		}
		return bean;
	}

	@Override
	public String send(String account, CaptchaType type, String target, String content) throws FrequencyException {
		Assert.hasText(account, "参数account不能为空");
		Assert.notNull(type, "参数type不能为空");
		Assert.hasText(target, "参数target不能为空");

		Captcha bean = this.last(account, type, target);
		String seccode;
		if (bean == null) {
			String str = Long.toString(System.nanoTime());
			seccode = str.substring(str.length() - 4);
			this.add(account, type, target, seccode);
		}
		else {
			seccode = bean.getCaptcha();
		}
		content = content.replace("{seccode}", seccode);
		CaptchaDebugger.debug(seccode, content);
		return seccode;
	}

}
