package io.leopard.web.seccode;

public interface SeccodeDao {

	boolean add(Seccode seccode);

	Seccode last(String account, String type, String target);

	boolean updateUsed(String seccodeId, boolean used);
}
