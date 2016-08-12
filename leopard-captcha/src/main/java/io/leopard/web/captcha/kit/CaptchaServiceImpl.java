package io.leopard.web.captcha.kit;

import java.util.Date;
import java.util.UUID;

import javax.annotation.PostConstruct;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import io.leopard.core.exception.forbidden.CaptchaWrongException;
import io.leopard.jdbc.Jdbc;
import io.leopard.redis.Redis;
import io.leopard.web.captcha.CaptchaInvalidException;
import io.leopard.web.captcha.FrequencyException;

//@Service
public class CaptchaServiceImpl implements CaptchaService {

	/**
	 * 类别，如：captcha:图片验证码,seccode:短信验证码
	 * 
	 */
	protected Log logger = LogFactory.getLog(this.getClass());

	protected Jdbc jdbc;
	protected Redis redis;

	public void setJdbc(Jdbc jdbc) {
		this.jdbc = jdbc;
	}

	public void setRedis(Redis redis) {
		this.redis = redis;
	}

	protected CaptchaDao captchaDao;

	@PostConstruct
	public void init() {
		CaptchaDaoMysqlImpl captchaDaoMysqlImpl = new CaptchaDaoMysqlImpl();
		captchaDaoMysqlImpl.setJdbc(jdbc);
		this.captchaDao = captchaDaoMysqlImpl;
	}

	@Override
	public void checkSessCaptcha(String captcha, String sessCaptcha) throws CaptchaWrongException {
		if (StringUtils.isEmpty(captcha)) {
			throw new CaptchaInvalidException("验证码不能为空.");
		}
		if (StringUtils.isEmpty(sessCaptcha)) {
			throw new CaptchaInvalidException("验证码未生成，验证码使用");
		}
		if (!captcha.equals(sessCaptcha)) {
			logger.warn("错误验证码 sessCaptcha:" + sessCaptcha + " captcha:" + captcha);
			throw new CaptchaWrongException(sessCaptcha + " " + captcha);
		}
	}

	@Override
	public Captcha checkCaptcha(String account, String target, String captcha) throws CaptchaWrongException {
		return this.check(account, CaptchaCategory.CAPTCHA, target, captcha);
	}

	@Override
	public Captcha checkSeccode(String account, String target, String captcha) throws CaptchaWrongException {
		return this.check(account, CaptchaCategory.SECCODE, target, captcha);
	}

	@Override
	public Captcha check(String account, String category, String target, String captcha) throws CaptchaWrongException {
		Assert.hasText(account, "参数account不能为空");
		Assert.hasText(category, "参数category不能为空");
		Assert.hasText(target, "参数target不能为空");
		Assert.hasText(captcha, "参数captcha不能为空");

		// String securityCode2 = lastSecurityCode(mobile, type);
		Captcha bean = this.last(account, category, target);
		if (bean == null) {
			throw new CaptchaWrongException("获取不到验证码记录[" + account + " " + category + " " + target + "]");
		}
		if (!bean.getCaptcha().equals(captcha)) {
			throw new CaptchaWrongException(captcha);
		}
		return bean;
	}

	@Override
	public String add(String account, String category, String type, String target, String captcha) {
		Assert.hasText(account, "参数account不能为空");
		Assert.hasText(category, "参数category不能为空");
		Assert.hasText(type, "参数type不能为空");
		Assert.hasText(target, "参数target不能为空");
		Assert.hasText(captcha, "参数captcha不能为空");

		String captchaId = UUID.randomUUID().toString().replaceAll("-", "").toLowerCase();

		Captcha bean = new Captcha();
		bean.setCaptchaId(captchaId);
		bean.setAccount(account);
		bean.setCategory(category);
		bean.setType(type);
		bean.setTarget(target);
		bean.setPosttime(new Date());
		bean.setCaptcha(captcha);
		bean.setUsed(false);
		captchaDao.add(bean);

		return captchaId;
	}

	protected String lastCaptcha(String account, String category, String target) {
		Captcha captcha = this.last(account, category, target);
		if (captcha == null) {
			return null;
		}
		return captcha.getCaptcha();
	}

	@Override
	public Captcha last(String account, String category, String target) {
		Jdbc jdbc = ((CaptchaDaoMysqlImpl) captchaDao).getJdbc();
		if (jdbc == null) {
			throw new NullPointerException("jdbc还没有初始化.");
		}

		Assert.hasText(account, "参数account不能为空");
		Assert.hasText(category, "参数category不能为空");
		Assert.hasText(target, "参数target不能为空");
		return this.captchaDao.last(account, category, target);
	}

	@Override
	public boolean updateUsed(String captchaId, boolean used) {
		Assert.hasText(captchaId, "参数captchaId不能为空");
		return captchaDao.updateUsed(captchaId, used);
	}

	@Override
	public String send(String account, String category, String type, String target, String content) {
		Assert.hasText(account, "参数account不能为空");
		Assert.hasText(category, "参数category不能为空");
		Assert.hasText(type, "参数type不能为空");
		Assert.hasText(target, "参数target不能为空");

		String captcha = lastCaptcha(account, category, target);
		if (captcha == null) {
			String str = Long.toString(System.nanoTime());
			captcha = str.substring(str.length() - 4);
			this.add(account, category, type, target, captcha);
		}
		content = content.replace("{captcha}", captcha);

		CaptchaDebugger.debug(captcha, content);

		return captcha;
	}

	@Override
	public String sendCaptcha(String account, String type, String target) {
		return this.send(account, CaptchaCategory.CAPTCHA, type, target, "您的验证码:{captcha}");
	}

	public String sendSeccode(String account, String target) throws FrequencyException {
		return this.sendSeccode(account, CaptchaCategory.SECCODE, target);
	}

	@Override
	public String sendSeccode(String account, String type, String target) throws FrequencyException {
		return this.sendSeccode(account, type, target, "您的验证码:{captcha}");
	}

	@Override
	public String sendCaptcha(String account, String type, String target, String content) {
		return this.send(account, CaptchaCategory.CAPTCHA, type, target, content);
	}

	/**
	 * 检查访问频率
	 * 
	 * @param account
	 * @param category
	 * @param type
	 * @param target
	 */
	protected void checkFrequency(String account, String category, String type, String target, int seconds) throws FrequencyException {
		String member = type + ":" + target + ":" + account;
		String key = "captcha:" + category;
		Double score = redis.zscore(key, member);
		if (score != null) {
			long time = score.longValue();
			if ((time + 1000L * seconds) > System.currentTimeMillis()) {
				throw new FrequencyException("您[" + account + " " + category + " " + type + " " + target + "]访问太频繁了，歇一会儿吧.");
			}
		}
		redis.zadd(key, System.currentTimeMillis(), member);
	}

	@Override
	public String sendSeccode(String account, String type, String target, String content) throws FrequencyException {
		this.checkFrequency(account, CaptchaCategory.SECCODE, type, target, 60);
		return this.send(account, CaptchaCategory.SECCODE, type, target, content);
	}

}
