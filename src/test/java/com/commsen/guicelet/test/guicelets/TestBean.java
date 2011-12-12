package com.commsen.guicelet.test.guicelets;

import com.commsen.guicelet.RequestParameter;
import com.commsen.guicelet.util.RequestBean;
import com.google.inject.servlet.RequestScoped;

@RequestScoped
public class TestBean extends RequestBean{
	
	@RequestParameter(name="string", required=true)
	private String s;
	
	@RequestParameter(name="long")
	private Long l;
	
	public String getTest() {
		return "new" + request.getParameter("test");
	}
	
	public String getS() {
		return s;
	}

	public Long getL() {
		return l;
	}

}
