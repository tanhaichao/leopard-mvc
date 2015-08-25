package io.leopard.web.session;

public class Stroe implements IStroe {

	private static final IStroe instance = new Stroe();

	public static IStroe getInstance() {
		return instance;
	}

	@Override
	public String get(String key) {
		throw new UnsupportedOperationException("Not Impl.");
	}

	@Override
	public String set(String key, String value, int seconds) {
		throw new UnsupportedOperationException("Not Impl.");
	}

	@Override
	public Long del(String key) {
		throw new UnsupportedOperationException("Not Impl.");
	}

	@Override
	public boolean isEnable() {
		throw new UnsupportedOperationException("Not Impl.");
	}

}
