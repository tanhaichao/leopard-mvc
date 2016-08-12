package io.leopard.web.seccode;

import java.util.Date;

public class Seccode {

	private String seccodeId;

	private String account;

	/**
	 * 类别:图片、文字
	 */
	private String category;

	/**
	 * 类型:手机、邮件
	 */
	private String type;

	/**
	 * 在哪里使用
	 */
	private String target;

	private String seccode;
	private Date posttime;

	private boolean used;

	public String getSeccodeId() {
		return seccodeId;
	}

	public void setSeccodeId(String seccodeId) {
		this.seccodeId = seccodeId;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getSeccode() {
		return seccode;
	}

	public void setSeccode(String seccode) {
		this.seccode = seccode;
	}

	public Date getPosttime() {
		return posttime;
	}

	public void setPosttime(Date posttime) {
		this.posttime = posttime;
	}

	public boolean isUsed() {
		return used;
	}

	public void setUsed(boolean used) {
		this.used = used;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

}