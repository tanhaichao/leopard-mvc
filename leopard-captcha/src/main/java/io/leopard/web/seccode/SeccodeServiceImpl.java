package io.leopard.web.seccode;

import java.util.Date;
import java.util.UUID;

import javax.annotation.PostConstruct;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.Assert;

import io.leopard.core.exception.forbidden.CaptchaWrongException;
import io.leopard.jdbc.Jdbc;
import io.leopard.redis.Redis;
import io.leopard.web.captcha.FrequencyException;
import io.leopard.web.captcha.kit.Captcha;
import io.leopard.web.captcha.kit.CaptchaDebugger;

//@Service
public class SeccodeServiceImpl implements SeccodeService {

	protected Log logger = LogFactory.getLog(this.getClass());

	protected Jdbc jdbc;
	protected Redis redis;

	public void setJdbc(Jdbc jdbc) {
		this.jdbc = jdbc;
	}

	public void setRedis(Redis redis) {
		this.redis = redis;
	}

	protected SeccodeDao seccodeDao;

	@PostConstruct
	public void init() {
		SeccodeDaoMysqlImpl seccodeDaoMysqlImpl = new SeccodeDaoMysqlImpl();
		seccodeDaoMysqlImpl.setJdbc(jdbc);
		this.seccodeDao = seccodeDaoMysqlImpl;
	}

	protected String add(String account, SeccodeType type, String target, String seccode) {
		Assert.hasText(account, "参数account不能为空");
		Assert.notNull(type, "参数type不能为空");
		Assert.hasText(target, "参数target不能为空");
		Assert.hasText(seccode, "参数seccode不能为空");
		String seccodeId = UUID.randomUUID().toString().replaceAll("-", "").toLowerCase();
		Seccode bean = new Seccode();
		bean.setSeccodeId(seccodeId);
		bean.setAccount(account);
		bean.setType(type.getKey());
		bean.setTarget(target);
		bean.setPosttime(new Date());
		bean.setSeccode(seccode);
		bean.setUsed(false);
		seccodeDao.add(bean);
		return seccodeId;
	}

	protected Seccode last(String account, SeccodeType type, String target) {
		Jdbc jdbc = ((SeccodeDaoMysqlImpl) seccodeDao).getJdbc();
		if (jdbc == null) {
			throw new NullPointerException("jdbc还没有初始化.");
		}

		Assert.hasText(account, "参数account不能为空");
		Assert.notNull(type, "参数type不能为空");
		Assert.hasText(target, "参数target不能为空");
		return this.seccodeDao.last(account, type.getKey(), target);
	}

	@Override
	public boolean updateUsed(Seccode seccode) {
		Assert.notNull(seccode, "参数seccode不能为空");
		return this.updateUsed(seccode.getSeccodeId(), true);
	}

	@Override
	public boolean updateUsed(String seccodeId, boolean used) {
		Assert.hasText(seccodeId, "参数seccodeId不能为空");
		return seccodeDao.updateUsed(seccodeId, used);
	}

	@Override
	public Seccode check(String account, SeccodeType type, String target, String seccode) throws SeccodeWrongException {
		Assert.hasText(account, "参数account不能为空");
		Assert.notNull(type, "参数type不能为空");
		Assert.hasText(target, "参数target不能为空");
		Assert.hasText(seccode, "参数seccode不能为空");

		// String securityCode2 = lastSecurityCode(mobile, type);
		Seccode bean = this.last(account, type, target);
		if (bean == null) {
			throw new SeccodeWrongException("获取不到验证码记录[" + account + " " + type.getKey() + " " + target + "]");
		}
		if (!bean.getSeccode().equals(seccode)) {
			throw new SeccodeWrongException(seccode);
		}
		return bean;
	}

	@Override
	public String send(String account, SeccodeType type, String target, String content) throws FrequencyException {
		Assert.hasText(account, "参数account不能为空");
		Assert.notNull(type, "参数type不能为空");
		Assert.hasText(target, "参数target不能为空");

		Seccode bean = this.last(account, type, target);
		String seccode;
		if (bean == null) {
			String str = Long.toString(System.nanoTime());
			seccode = str.substring(str.length() - 4);
			this.add(account, type, target, seccode);
		}
		else {
			seccode = bean.getSeccode();
		}
		content = content.replace("{seccode}", seccode);
		CaptchaDebugger.debug(seccode, content);
		return seccode;
	}

}
