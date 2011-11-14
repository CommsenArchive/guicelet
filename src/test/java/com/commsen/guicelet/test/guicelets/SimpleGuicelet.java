package com.commsen.guicelet.test.guicelets;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.commsen.guicelet.Guicelet;
import com.commsen.guicelet.GuiceletRequest;
import com.commsen.guicelet.HttpMethod;
import com.google.inject.Singleton;

@Guicelet
@Singleton
public class SimpleGuicelet {

	@GuiceletRequest(path="/echo", methods=HttpMethod.ALL)
	public void echo(HttpServletRequest request, HttpServletResponse response) throws IOException {
		response.getWriter().println("ECHO");
	}

	@GuiceletRequest(path="/test", methods={HttpMethod.GET})
	public void sayHelloGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		response.getWriter().println("GET");
	}

	@GuiceletRequest(path="/test", methods={HttpMethod.POST})
	public void sayHelloPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
		response.getWriter().println("POST");
	}

	@GuiceletRequest(path="/test", methods={HttpMethod.POST})
	public void a
	(HttpServletRequest request, HttpServletResponse response) throws IOException {
		response.getWriter().println("POST");
	}
}
