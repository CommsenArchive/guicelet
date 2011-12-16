package com.commsen.guicelet.servlet;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.commsen.guicelet.Guicelet;
import com.commsen.guicelet.GuiceletRequest;
import com.commsen.guicelet.HttpMethod;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;

@SuppressWarnings("serial")
@Singleton
public class GuiceletServlet extends HttpServlet {

	@Inject
	private Logger log;

	private static final String GUICELET = Guicelet.class.getSimpleName();

	private final Set<Object> guicelets;
	private final Injector injector;

	private Map<String, Method> deleteMethods = new HashMap<String, Method>();
	private Map<String, Method> getMethods = new HashMap<String, Method>();
	private Map<String, Method> headMethods = new HashMap<String, Method>();
	private Map<String, Method> optionsMethods = new HashMap<String, Method>();
	private Map<String, Method> postMethods = new HashMap<String, Method>();
	private Map<String, Method> putMethods = new HashMap<String, Method>();
	private Map<String, Method> traceMethods = new HashMap<String, Method>();

	@Inject
	public GuiceletServlet(Set<Object> guicelets, Injector injector) {
		super();
		this.guicelets = guicelets;
		this.injector = injector;
	}

	@Override
	public void init() throws ServletException {
		super.init();
		for (Object guicelet : guicelets) {
			String className = guicelet.getClass().getName();
			if (log.isLoggable(Level.FINE)) {
				log.fine("Processing " + className + " guicelet!");
			}
			Guicelet guiceletAnnotation = guicelet.getClass().getAnnotation(
					Guicelet.class);
			if (guiceletAnnotation == null) {
				log.warning("Skipping " + className
						+ " type! It is not annotated with @" + GUICELET
						+ " annotation!");
				continue;
			}

			extractMethods(guicelet);
		}
	}

	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String method = req.getMethod();
		if (method.equals("DELETE")) {
			processRequest(deleteMethods, req, resp);
		} else if (method.equals("GET")) {
			processRequest(getMethods, req, resp);
		} else if (method.equals("HEAD")) {
			processRequest(headMethods, req, resp);
		} else if (method.equals("OPTIONS")) {
			processRequest(optionsMethods, req, resp);
		} else if (method.equals("POST")) {
			processRequest(postMethods, req, resp);
		} else if (method.equals("PUT")) {
			processRequest(putMethods, req, resp);
		} else if (method.equals("TRACE")) {
			processRequest(traceMethods, req, resp);
		} else {
		    resp.sendError(HttpServletResponse.SC_NOT_IMPLEMENTED);
		}
	}
	
	private void processRequest(Map<String, Method> methodMap, HttpServletRequest req,
			HttpServletResponse resp) throws IOException {

		Method m = methodMap.get(req.getPathInfo());
		if (log.isLoggable(Level.FINE)) {
			log.fine("Invoking " + m + " guicelet!");
		}
		
		if (m == null) {
			if (log.isLoggable(Level.FINE)) {
				log.fine("No guicelet registered for '" + req.getPathInfo() + "' path!");
			}
			resp.sendError(HttpServletResponse.SC_NOT_FOUND);
			return;
		}
		
		try {
			m.invoke(injector.getInstance(m.getDeclaringClass()), req, resp);
		} catch (Exception e) {
			log.log(Level.SEVERE, "Invocation of " + m + " failed", e);
			resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
	}

	private void extractMethods(Object guicelet) {
		List<Method> methods = Arrays.asList(guicelet.getClass().getDeclaredMethods());
		for (Method method : methods) {
			GuiceletRequest requestAnnotation = method
					.getAnnotation(GuiceletRequest.class);
			if (requestAnnotation == null) {
				continue;
			}
			
			// TODO check and skip if method is private or protected 
			
			@SuppressWarnings("rawtypes")
			Class[] params = method.getParameterTypes();
			if (params == null || params.length != 2
					|| params[0] != HttpServletRequest.class
					|| params[1] != HttpServletResponse.class) {
				log.warning("Method " + method.getName() + " in "
						+ guicelet.getClass().getName() + " annotated with "
						+ GuiceletRequest.class.getSimpleName()
						+ " has invaild signatue!");
				continue;
			}
			List<HttpMethod> httpMethods = Arrays.asList(requestAnnotation
					.methods());
			String path = requestAnnotation.path();
			for (HttpMethod httpMethod : httpMethods) {
				updateMethodMap(httpMethod, path, method);
			}
		}
	}

	private void updateMethodMap(HttpMethod httpMethod, String path,
			Method method) {
		switch (httpMethod) {
		case DELETE:
			deleteMethods.put(path, method);
			break;
		case GET:
			getMethods.put(path, method);
			break;
		case HEAD:
			headMethods.put(path, method);
			break;
		case OPTIONS:
			optionsMethods.put(path, method);
			break;
		case POST:
			postMethods.put(path, method);
			break;
		case PUT:
			putMethods.put(path, method);
			break;
		case TRACE:
			traceMethods.put(path, method);
			break;
		default:
			break;
		}
	}

}
