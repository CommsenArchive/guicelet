package com.commsen.guicelet.test.util;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;

public class JettyServer {
	
	private static Server server;
	private static int PORT = 9090;

	public static String getHost() {
		return "http://localhost:" + PORT ;
	}

	public static void start() throws Exception {
		server = new Server(PORT);

		WebAppContext context = new WebAppContext();
		context.setDescriptor("./src/test/resources/WEB-INF/web.xml");
		context.setResourceBase("./src/test/resources/webapp");
		context.setContextPath("/");
		context.setParentLoaderPriority(true);

		server.setHandler(context);
		server.start();
	}

	public static void stop() throws Exception {
		server.stop();
	}

}
