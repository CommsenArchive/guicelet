package com.commsen.guicelet.test;

import com.commsen.guicelet.GuiceletModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceServletContextListener;

public class GuiceletTestContextListener extends GuiceServletContextListener {

	@Override
	protected Injector getInjector() {
//		return Guice.createInjector(new GuiceletModule() {
//			@SuppressWarnings("rawtypes")
//			@Override
//			protected void configureGuicelets() {
//				for (String root : TestContext.getRoots()) {
//					bindGuiceletsTo(root);
//				}
//				for (Class guicelet : TestContext.getGuicelets()) {
//					addGuicelet(guicelet);
//				}
//			}
//		});
		return Guice.createInjector(new GuiceletTestModule());
	}

}
