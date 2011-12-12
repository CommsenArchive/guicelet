package com.commsen.guicelet.util;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.inject.Inject;
import com.google.inject.servlet.RequestScoped;

@RequestScoped
public class RequestBean {
	
	public static final String MISSING_REQUIRED_FIELD = "MISSING_REQUIRED_FIELD";
	public static final String ERROR_CONVERTING_VALUE = "ERROR_CONVERTING_VALUE";
	
	@Inject protected HttpServletRequest request;
	@Inject protected HttpServletResponse response;
	
	
	private Map<String, Set<String>> _errors = new HashMap<String, Set<String>> ();


	public void addError(String field, String error) {
		Set<String> s = _errors.get(field);
		if (s == null) {
			s = new HashSet<String>();
			_errors.put(field, s);
		}
		s.add(error);
	}

	public Map<String, Set<String>> getErrors() {
		return _errors;
	}

	public boolean hasErrors() {
		return !_errors.isEmpty();
	}
}
