package io.leopard.web.passport;

import freemarker.cache.ClassTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;

import java.io.IOException;
import java.util.Locale;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfig;
import org.springframework.web.servlet.view.freemarker.FreeMarkerView;

public class FtlView extends FreeMarkerView {

	private String folder;

	public FtlView() {
	}

	public FtlView(String viewName) {
		this("/", viewName);
	}

	public FtlView(String folder, String viewName) {
		try {
			ApplicationContext applicationContext = ContextLoader.getCurrentWebApplicationContext();
			this.setApplicationContext(applicationContext);
		}
		catch (Exception e) {
			// 兼容普通java环境
		}
		this.setUrl(viewName + ".ftl");
		this.folder = folder;
	}

	public void addObject(String name, Object value) {
		this.addStaticAttribute(name, value);
	}

	@Override
	protected FreeMarkerConfig autodetectConfiguration() throws BeansException {
		return FreeMarkerUtil.getFreeMarkerConfig(super.getApplicationContext());
	}

	@Override
	public String getContentType() {
		return "text/html; charset=UTF-8";
	}

	@Override
	protected Template getTemplate(String name, Locale locale) throws IOException {
		Configuration config = getConfiguration();
		config.setTemplateLoader(new ClassTemplateLoader(this.getClass(), folder));
		Template tmp = (getEncoding() != null ? config.getTemplate(name, locale, getEncoding()) : config.getTemplate(name, locale));

		return tmp;
	}
}
