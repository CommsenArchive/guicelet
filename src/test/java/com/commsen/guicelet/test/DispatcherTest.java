package com.commsen.guicelet.test;


import javax.servlet.http.HttpServletResponse;

import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.client.methods.HttpOptions;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpTrace;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.commsen.guicelet.test.guicelets.SimpleGuicelet;
import com.commsen.guicelet.test.util.JettyServer;
import com.commsen.guicelet.test.util.TestContext;

public class DispatcherTest extends BaseGuiceletTest {


	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		TestContext.addRoot("/g/");
		TestContext.addGuicelet(SimpleGuicelet.class);
		JettyServer.start();

	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		JettyServer.stop();
	}

	@Test
	public void test() throws Exception {

		// test all methods
		String url = JettyServer.getHost() + "/g/echo";
		testRequest(new HttpDelete(url), HttpServletResponse.SC_OK);
		testRequest(new HttpGet(url), HttpServletResponse.SC_OK, SimpleGuicelet.ECHO);
		testRequest(new HttpHead(url), HttpServletResponse.SC_OK);
		testRequest(new HttpOptions(url), HttpServletResponse.SC_OK);
		testRequest(new HttpPost(url), HttpServletResponse.SC_OK, SimpleGuicelet.ECHO);
		testRequest(new HttpPut(url), HttpServletResponse.SC_OK);
		testRequest(new HttpTrace(url), HttpServletResponse.SC_OK);

		// test trailing slash
		testRequest(new HttpGet(JettyServer.getHost() + "/g/test"), HttpServletResponse.SC_OK, SimpleGuicelet.TEST);
		testRequest(new HttpGet(JettyServer.getHost() + "/g/test/"), HttpServletResponse.SC_NOT_FOUND);

		// test multi-level mapping
		testRequest(new HttpGet(JettyServer.getHost() + "/g/test/test2"), HttpServletResponse.SC_OK, SimpleGuicelet.TEST2);
		testRequest(new HttpGet(JettyServer.getHost() + "/g/test/test/test3"), HttpServletResponse.SC_OK, SimpleGuicelet.TEST3);
		testRequest(new HttpGet(JettyServer.getHost() + "/g/test/test"), HttpServletResponse.SC_NOT_FOUND);
		
		// make sure there is 404 for methods other than GET
		url = JettyServer.getHost() + "/g/test";
		testRequest(new HttpDelete(url), HttpServletResponse.SC_NOT_FOUND);
		testRequest(new HttpHead(url), HttpServletResponse.SC_NOT_FOUND);
		testRequest(new HttpOptions(url), HttpServletResponse.SC_NOT_FOUND);
		testRequest(new HttpPost(url), HttpServletResponse.SC_NOT_FOUND);
		testRequest(new HttpPut(url), HttpServletResponse.SC_NOT_FOUND);
		testRequest(new HttpTrace(url), HttpServletResponse.SC_NOT_FOUND);
	}	
}
