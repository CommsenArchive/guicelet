package com.commsen.guicelet;

public enum HttpMethod {
	DELETE,
	GET,
	HEAD,
	OPTIONS,
	POST, 
	PUT,
	TRACE;
	
	public static HttpMethod[] BROWSER_SUPPORTED = new HttpMethod[] {GET, POST};
	
	public static HttpMethod[] ALL = values();
}