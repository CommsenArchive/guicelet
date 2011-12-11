package com.commsen.guicelet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.commsen.guicelet.servlet.GuiceletServlet;
import com.commsen.guicelet.servlet.TemplateServlet;
import com.commsen.guicelet.template.Constants;
import com.google.inject.multibindings.Multibinder;
import com.google.inject.servlet.ServletModule;

public class GuiceletModule extends ServletModule {
	private Multibinder<Object> guicelets;
	private List<TemplateView> templates = new ArrayList<GuiceletModule.TemplateView>();

	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected void addGuicelet(Class clazz) {
		if (clazz == null) throw new IllegalArgumentException("Can not add null guicelet!");
		guicelets.addBinding().to(clazz);
	}

	protected TemplateView render(String path) {
		TemplateView templateView = new TemplateView(path);
		templates.add(templateView);
		return templateView;
	}
	
	
	protected void bindGuiceletsTo(String basePath) {
		if (basePath == null) throw new IllegalArgumentException("Guicelets base path can not be null!");
		String path = basePath.trim();
		if (path.endsWith("/")) {
			path = path + "*";
		} else {
			path = path + "/*";
		}
		serve(path).with(GuiceletServlet.class);
	}

	protected void configureGuicelets() {
	}
	
	@Override
	protected void configureServlets() {
		guicelets = Multibinder.newSetBinder(binder(), Object.class);
		configureGuicelets();
		for (TemplateView template : templates) {
			serve(template.getPath()).with(TemplateServlet.class, template.getParams());
		}
	}
	
	
	public static class TemplateView  {
		private String path;
		public String getPath() {
			return path;
		}

		public Map<String, String> getParams() {
			return params;
		}

		private Map<String, String> params = new HashMap<String, String>();

		public TemplateView(String path) {
			if (path == null) throw new IllegalArgumentException("Template path can not be null!");
			this.path = path;
		}
		
		public TemplateView usingTemplate (String template) {
			if (template == null) throw new IllegalArgumentException("Template can not be null!");
			params.put(Constants.PARAM_TEMPLATE, template);
			return this;
		}

		public TemplateView havingPagesInFolder (String pagesFolder) {
			if (pagesFolder == null) throw new IllegalArgumentException("pagesFolder can not be null!");
			params.put(Constants.PARAM_JSP_FOLDER, pagesFolder);
			return this;
		}

		public TemplateView mappingKeyToPage (String key, String page) {
			if (key == null) throw new IllegalArgumentException("key can not be null!");
			if (page == null) throw new IllegalArgumentException("page can not be null!");
			params.put(Constants.PARAM_TEMPLATE_PREFIX + key, page);
			return this;
		}
		
	}
}
