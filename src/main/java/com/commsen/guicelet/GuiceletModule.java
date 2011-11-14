package com.commsen.guicelet;

import com.commsen.guicelet.servlet.GuiceletServlet;
import com.google.inject.multibindings.Multibinder;
import com.google.inject.servlet.ServletModule;

public class GuiceletModule extends ServletModule {
	private Multibinder<Object> guicelets;

	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected void addGuicelet(Class clazz) {
		if (clazz == null) throw new IllegalArgumentException("Can not add null guicelet!");
		guicelets.addBinding().to(clazz);
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
	}
}
