package io.leopard.mvc.trynb.translate;

import io.leopard.jdbc.LeopardBeanFactoryAware;

public class TranslaterImpl implements Translater {

	private static final Translater instance = new TranslaterImpl();

	public static Translater getInstance() {
		return instance;
	}

	private TranslaterImpl() {

	}

	protected Translater translater;

	@Override
	public String translate(String message) {
		if (translater == null) {
			translater = LeopardBeanFactoryAware.getBeanFactory().getBean(Translater.class);
		}
		return translater.translate(message);
	}

}
