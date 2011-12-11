package com.commsen.guicelet.util;

import java.util.Set;

import com.google.inject.servlet.RequestScoped;

@RequestScoped
public class BaseDTO {

	private Set<String> errorCodes;
	private Set<String> successCodes;

	public Set<String> getErrorCodes() {
		return errorCodes;
	}

	public BaseDTO setErrorCodes(Set<String> errorCodes) {
		this.errorCodes = errorCodes;
		return this;
	}

	public Set<String> getSuccessCodes() {
		return successCodes;
	}

	public BaseDTO setSuccessCodes(Set<String> successCodes) {
		this.successCodes = successCodes;
		return this;
	}

}
