package com.commsen.guicelet.test;

import com.commsen.guicelet.GuiceletModule;
import com.commsen.guicelet.test.guicelets.SimpleGuicelet;

public class GuiceletTestModule extends GuiceletModule {

	@Override
	protected void configureGuicelets() {
		bindGuiceletsTo("/g/");
		bindGuiceletsTo("/g1");
		addGuicelet(SimpleGuicelet.class);
	}
}
