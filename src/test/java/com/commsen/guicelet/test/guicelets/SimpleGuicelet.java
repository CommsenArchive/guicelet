package com.commsen.guicelet.test.guicelets;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.commsen.guicelet.Guicelet;
import com.commsen.guicelet.GuiceletRequest;
import static com.commsen.guicelet.HttpMethod.*;

@Guicelet
public class SimpleGuicelet {
	
	public static final String ECHO = "ECHO";
	public static final String TEST = "TEST";
	public static final String TEST2 = "TEST2";
	public static final String TEST3 = "TEST3";

	@GuiceletRequest(path="/echo", methods={DELETE, GET, HEAD, OPTIONS, POST, PUT, TRACE})
	public void echo(HttpServletRequest request, HttpServletResponse response) throws IOException {
		response.getWriter().print(ECHO);
	}

	@GuiceletRequest(path="/test", methods={GET})
	public void sayHelloGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		response.getWriter().print(TEST);
	}

	@GuiceletRequest(path="/test/test2", methods={GET})
	public void secondLevel(HttpServletRequest request, HttpServletResponse response) throws IOException {
		response.getWriter().print(TEST2);
	}

	@GuiceletRequest(path="/test/test/test3", methods={GET})
	public void thirdLevel(HttpServletRequest request, HttpServletResponse response) throws IOException {
		response.getWriter().print(TEST3);
	}

}
