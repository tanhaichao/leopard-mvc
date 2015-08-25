package io.leopard.web.session;

import java.util.Iterator;
import java.util.ServiceLoader;

public class Stroe implements IStroe {

	private static final IStroe instance = new Stroe();

	public static IStroe getInstance() {
		return instance;
	}

	private IStroe stroe;

	public Stroe() {
		Iterator<IStroe> iterator = ServiceLoader.load(IStroe.class).iterator();
		while (iterator.hasNext()) {
			IStroe stroe = iterator.next();
			if (stroe.isEnable()) {
				this.stroe = stroe;
				break;
			}
		}
	}

	@Override
	public String get(String key) {
		if (stroe != null) {
			return stroe.get(key);
		}
		throw new UnsupportedOperationException("Not Impl.");
	}

	@Override
	public String set(String key, String value, int seconds) {
		if (stroe != null) {
			return stroe.set(key, value, seconds);
		}
		throw new UnsupportedOperationException("Not Impl.");
	}

	@Override
	public Long del(String key) {
		if (stroe != null) {
			return stroe.del(key);
		}
		throw new UnsupportedOperationException("Not Impl.");
	}

	@Override
	public boolean isEnable() {
		if (stroe != null) {
			return stroe.isEnable();
		}
		throw new UnsupportedOperationException("Not Impl.");
	}

}
