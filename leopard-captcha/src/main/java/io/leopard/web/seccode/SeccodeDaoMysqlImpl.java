package io.leopard.web.seccode;

import org.springframework.stereotype.Repository;

import io.leopard.jdbc.Jdbc;
import io.leopard.jdbc.builder.InsertBuilder;

@Repository
public class SeccodeDaoMysqlImpl implements SeccodeDao {

	private Jdbc jdbc;

	public Jdbc getJdbc() {
		return jdbc;
	}

	public void setJdbc(Jdbc jdbc) {
		this.jdbc = jdbc;
	}

	@Override
	public boolean add(Seccode seccode) {
		InsertBuilder builder = new InsertBuilder("seccode");
		builder.setString("seccodeId", seccode.getSeccodeId());
		builder.setString("type", seccode.getType());
		builder.setString("target", seccode.getTarget());
		builder.setString("account", seccode.getAccount());
		builder.setString("seccode", seccode.getSeccode());
		builder.setBool("used", seccode.isUsed());
		builder.setDate("posttime", seccode.getPosttime());
		return jdbc.insertForBoolean(builder);
	}

	@Override
	public Seccode last(String account, String type, String target) {
		String sql = "select * from seccode where account=? and type=? and target=? and used=0 order by posttime desc limit 1";
		return this.jdbc.query(sql, Seccode.class, account, type, target);
	}

	@Override
	public boolean updateUsed(String seccodeId, boolean used) {
		String sql = "update seccode set used=? where seccodeId=?";
		return this.jdbc.updateForBoolean(sql, used, seccodeId);
	}

}
