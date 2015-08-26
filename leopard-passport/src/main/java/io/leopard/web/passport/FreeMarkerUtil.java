package io.leopard.web.passport;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.springframework.context.ApplicationContext;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfig;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import freemarker.template.TemplateException;

public class FreeMarkerUtil {

	private static FreeMarkerConfigurer configurer;

	protected static FreeMarkerConfigurer getFreeMarkerConfigurer(ApplicationContext applicationContext) {
		Map<String, Object> freemarkerVariables = new HashMap<String, Object>();
		freemarkerVariables.put("xml_escape", "fmXmlEscape");
		// freemarkerVariables.put("replaceParam", new ReplaceParamMethod());
		// freemarkerVariables.putAll(listTemplateMethod(applicationContext));

		Properties freemarkerSettings = new Properties();
		freemarkerSettings.put("template_update_delay", "1");
		freemarkerSettings.put("defaultEncoding", "UTF-8");

		FreeMarkerConfigurer configurer = new FreeMarkerConfigurer();
		configurer.setTemplateLoaderPath("/WEB-INF/ftl/");
		configurer.setFreemarkerVariables(freemarkerVariables);
		configurer.setFreemarkerSettings(freemarkerSettings);

		try {
			configurer.afterPropertiesSet();
		}
		catch (IOException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
		catch (TemplateException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
		return configurer;
	}

	public static FreeMarkerConfig getFreeMarkerConfig(ApplicationContext applicationContext) {
		if (configurer != null) {
			return configurer;
		}
		configurer = getFreeMarkerConfigurer(applicationContext);
		return configurer;
	}
}
