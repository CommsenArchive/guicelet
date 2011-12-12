package com.commsen.guicelet.test.guicelets;

import static com.commsen.guicelet.HttpMethod.GET;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.commsen.guicelet.Guicelet;
import com.commsen.guicelet.GuiceletRequest;
import com.commsen.guicelet.util.RequestBeanProvider;
import com.google.inject.Inject;

@Guicelet
public class GuiceletWithModel {

	@Inject RequestBeanProvider requestBeanProvider;
	
	@GuiceletRequest(path="/model", methods={GET})
	public void echo(HttpServletRequest request, HttpServletResponse response) throws IOException {
		TestBean testBean = requestBeanProvider.get(TestBean.class);
		String result;
		if (testBean.hasErrors()) {
			result = testBean.getErrors().toString();
		} else {
			result = testBean.getL() + "," + testBean.getS();
		}
		response.getWriter().print(result);
	}
	
	
}
